package xyz.slienceme.tuyun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.slienceme.tuyun.common.entity.Result;
import xyz.slienceme.tuyun.mapper.FileMapper;
import xyz.slienceme.tuyun.mapper.UserMapper;
import xyz.slienceme.tuyun.pojos.File;
import xyz.slienceme.tuyun.pojos.User;
import xyz.slienceme.tuyun.service.FileService;
import xyz.slienceme.tuyun.utils.DateUtil;
import xyz.slienceme.tuyun.utils.JWT;
import xyz.slienceme.tuyun.utils.StringUtil;
import xyz.slienceme.tuyun.vo.TokenVO;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author slience_me
 * @since 2023-03-26
 */
@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {


    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private UserMapper userMapper;
    @Value("${file.fileupload}")
    private String fileupload;
    @Value("${image.imagesupload}")
    private String imagesupload;
    @Value("${spring.path.url}")
    private String baseUrl;

    private static final Integer MAXSIZE = 419430400;
    private static final Integer MAXSIZE1 = 251658240;

    /**
     * 上传接口
     *
     * @param multipartFiles
     * @return
     * @throws Exception
     */
    @Override
    public Result<?> uploadImage(String accessToken, MultipartFile[] multipartFiles, Integer type) throws IOException {
        if (Objects.isNull(multipartFiles)) {
            return Result.createBySuccessMessage("图片未上传!!");
        }
        long userId;
        String describe;
        if (Objects.isNull(accessToken) || "".equals(accessToken)) {
            describe = "Image-public"; //token为空默认共享
            userId = 1;
        } else {
            if (type.equals(1)) {
                describe = "Image-public"; //标识为1默认共享
            } else { //标识为2默认私有
                describe = "Image"; //标识为1默认共享
            }
            userId = JWT.unsign(accessToken, TokenVO.class).getUserId();
        }

        StringBuilder Msg = new StringBuilder();
        List<HashMap<String, Object>> list = new ArrayList<>();
        int i = 0;
        for (MultipartFile multipartFile : multipartFiles) {
            // 文件大小fileSize
            long fileSize = multipartFile.getSize();
            if (fileSize > MAXSIZE1) {
                Msg.append(i).append(". ").append(multipartFile.getName()).append("图片不能上传超过30MB!!").append("--->");
            }
            // 文件类型
            String contentType = multipartFile.getContentType();
            if (!contentType.split("/")[0].equals("image")) {
                Msg.append(i).append(". ").append(multipartFile.getName()).append("请上传图片类型!!!").append("--->");
            }
            ApplicationHome application = new ApplicationHome(getClass());
            java.io.File parentPathStringLinux = application.getSource();
            String date = DateUtil.getDate("yyyyMMdd");
            // 目录路径 dirPath
            String dirPath = parentPathStringLinux.getParentFile().toString().replace("\\", "/") + imagesupload + date + "/";
            java.io.File filePath = new java.io.File(dirPath);
            if (!filePath.exists()) {
                boolean mkdirs = filePath.mkdirs();
                if (!mkdirs) {
                    System.out.println("文件创建失败!!");
                }
            }
            // 获取原始文件名(含后缀)
            String originalName = multipartFile.getOriginalFilename();
            int length = originalName.length();
            if (length >= 8){
                originalName = originalName.substring(length-8);
            }
            // 获取文件文件名(不含后缀)
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String fileName = Objects.isNull(originalName) ? uuid : originalName.substring(originalName.lastIndexOf("."));
            // 文件格式  日期+uuid+文件名
            fileName = date + uuid + fileName;
            BufferedImage imageTemplate = ImageIO.read(new ClassPathResource("static/watermark.png").getInputStream());
            java.io.File file = new java.io.File(dirPath, fileName);
            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String filename_zip = "zip" + fileName.split("\\.")[0] + ".jpg";
            String path = dirPath + filename_zip;
            path = path.split("\\.")[0] + ".jpg";
            try {
                Thumbnails.of(file)
                        .size(160, 100)
                        .watermark(Positions.BOTTOM_RIGHT, imageTemplate, 0.5f)
                        .outputFormat("jpg")
                        .toFile(path);
            } catch (Exception e){
                log.error("图片{}压缩异常, 按照原图作为压缩图处理", multipartFile.getName());
                multipartFile.transferTo(new java.io.File(path));
                Msg.append(i).append(". ").append(multipartFile.getName()).append("图片压缩失败!!").append("--->");
                e.printStackTrace();
            }
            String url = baseUrl + "/media" + imagesupload + date + "/" + fileName;
            String url_zip = baseUrl + "/media" + imagesupload + date + "/" + filename_zip;
            fileMapper.insert(new File(originalName, fileName, contentType, url_zip, url, StringUtil.readableFileSize(fileSize), describe, userId));
            HashMap<String, Object> data = new HashMap<>();
            data.put("url", url);
            data.put("urlZip", url_zip);
            data.put("originName", originalName);
            data.put("size", StringUtil.readableFileSize(fileSize));
            list.add(data);
        }
        if (Msg.length() == 0) {
            return Result.createBySuccess(list);
        } else {
            return Result.createByErrorMessage(Msg.toString());
        }
    }

    @Override
    public PageInfo<File> seletAllImage(String username, Integer p) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (!"未登录".equals(username) && !Objects.isNull(username)) {
            if (user.getType() != 1){
                return null;
            }
            if (Objects.isNull(p)) {
                p = 1;
            }
            PageHelper.startPage(p, 12);
            // 共有的ID 1
            List<File> files = fileMapper.selectList(new LambdaQueryWrapper<File>()
                    .eq(File::getIsDeleted, 0)
                    .eq(File::getFileDescribe, "Image-public")
                    .orderByDesc(File::getCreatedTime));
            return new PageInfo<>(files);
        }
        return null;
    }

    @Override
    public PageInfo<File> seletAllPublicImage(Integer p) {
        // 共有的ID 1
        if (Objects.isNull(p)) {
            p = 1;
        }
        PageHelper.startPage(p, 20);
        List<File> files = fileMapper.selectList(new LambdaQueryWrapper<File>().eq(File::getFileDescribe, "Image-public").eq(File::getIsDeleted, 0).orderByDesc(File::getCreatedTime));
        return new PageInfo<>(files);
    }

    @Override
    public PageInfo<File> seletAllPrivateImage(String username, Integer p) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (!"未登录".equals(username) && !Objects.isNull(username)) {
            if (Objects.isNull(p)) {
                p = 1;
            }
            PageHelper.startPage(p, 12);
            // 共有的ID 1
            List<File> files = fileMapper.selectList(new LambdaQueryWrapper<File>()
                    .eq(File::getCreatedBy, user.getId())
                    .eq(File::getIsDeleted, 0)
                    .ne(File::getFileDescribe, "File")
                    .orderByDesc(File::getCreatedTime));
            return new PageInfo<>(files);
        }
        return null;
    }

    @Override
    public PageInfo<File> seletAllFiles(String username, Integer p) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (!"未登录".equals(username) && !Objects.isNull(username)) {
            if (Objects.isNull(p)) {
                p = 1;
            }
            PageHelper.startPage(p, 12);
            // 共有的ID 1
            List<File> files = fileMapper.selectListByXml(user.getId(), "File");
            //System.out.println(files);
            return new PageInfo<>(files);
        }
        return null;
    }

    @Override
    public Result<?> delFile(String accessToken, Integer id) {
        File file = fileMapper.selectById(id);
        if (Objects.isNull(file)){
            return Result.createByErrorMessage("文件不存在");
        }
        file.setIsDeleted(1); //删除
        ApplicationHome application = new ApplicationHome(getClass());
        java.io.File parentPathStringLinux = application.getSource();
        // 目录路径 dirPath
        String dirPath;
        boolean delflag = false;
        if("File".equals(file.getFileDescribe())){
            dirPath = new ApplicationHome(getClass()).getSource().getParentFile().toString().replace("\\", "/") + file.getFilePath();
            java.io.File file1 = new java.io.File(dirPath);
            delflag = file1.delete();
        } else {
            dirPath = new ApplicationHome(getClass()).getSource().getParentFile().toString().replace("\\", "/") + file.getFileUrl().split(baseUrl + "/media")[1];
            boolean flag1 = new java.io.File(dirPath).delete();
            dirPath = new ApplicationHome(getClass()).getSource().getParentFile().toString().replace("\\", "/") + file.getFilePath().split(baseUrl + "/media")[1];
            boolean flag2 = new java.io.File(dirPath).delete();
            if (flag1 && flag2){
                delflag = true;
            }
        }
        fileMapper.updateById(file);
        if (delflag){
            return Result.createBySuccessMessage("删除成功!");
        } else {
            return Result.createBySuccessMessage("删除失败!");
        }
    }

    @Override
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, Integer id) {
        /*if (Objects.isNull(id)){
            return Result.createByErrorMessage("id异常,必须传输");
        }*/
        File file = fileMapper.selectById(id);
        String fileName = file.getFileName();
        String dirPath;
        if("File".equals(file.getFileDescribe())){
            dirPath = new ApplicationHome(getClass()).getSource().getParentFile().toString().replace("\\", "/") + file.getFilePath();
        } else {
            dirPath = new ApplicationHome(getClass()).getSource().getParentFile().toString().replace("\\", "/") + file.getFileUrl().split(baseUrl + "/media")[1];
        }
        java.io.File ioFile = new java.io.File(dirPath);
        try {
            FileInputStream fileInputStream = new FileInputStream(ioFile);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            response.setCharacterEncoding("UTF-8");
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载 inline表示在线打开 "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            // 告知浏览器文件的大小
            response.addHeader("Content-Length", "" + ioFile.length());
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            outputStream.write(buffer);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Result<?> uploadFile(String accessToken, MultipartFile[] multipartFiles) {
        if (Objects.isNull(multipartFiles)) {
            return Result.createByErrorMessage("文件未上传!!");
        }
        long userId;
        String describe = "File"; //token为空默认共享
        if (Objects.isNull(accessToken) || "".equals(accessToken)) {
            return Result.createByErrorMessage("请登录!!!!");

        } else {
            userId = JWT.unsign(accessToken, TokenVO.class).getUserId();
            User user = userMapper.selectById(userId);
            if (user.getType() != 1) { //管理员1 用户 2
                return Result.createByErrorMessage("您没有权限!!");
            }
        }
        StringBuilder Msg = new StringBuilder();
        List<HashMap<String, Object>> list = new ArrayList<>();
        int i = 0;
        for (MultipartFile multipartFile : multipartFiles) {
            // 文件大小fileSize
            long fileSize = multipartFile.getSize();
            if (fileSize > MAXSIZE1) {
                Msg.append(i).append(". ").append(multipartFile.getName()).append("文件不能上传超过30MB!!").append("--->");
            }
            // 文件类型
            String contentType = multipartFile.getContentType();
            System.out.println("contentType = " + contentType);
            /*if (!contentType.split("/")[0].equals("image")) {
                Msg.append(i).append(". ").append(multipartFile.getName()).append("请上传图片类型!!!").append("--->");
            }*/
            ApplicationHome application = new ApplicationHome(getClass());
            java.io.File parentPathStringLinux = application.getSource();
            String date = DateUtil.getDate("yyyyMMdd");
            // 目录路径 dirPath
            String dirPath = parentPathStringLinux.getParentFile().toString().replace("\\", "/") + fileupload + date + "/";
            java.io.File filePath = new java.io.File(dirPath);
            if (!filePath.exists()) {
                boolean mkdirs = filePath.mkdirs();
                if (!mkdirs) {
                    System.out.println("文件创建失败!!");
                }
            }
            // 获取原始文件名(含后缀)
            String originalName = multipartFile.getOriginalFilename();
            int length = originalName.length();
            if (length >= 8){
                originalName = originalName.substring(length-8);
            }
            // 获取文件文件名(不含后缀)
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String fileName = Objects.isNull(originalName) ? uuid : originalName.substring(originalName.lastIndexOf("."));
            // 文件格式  日期+uuid+文件名
            fileName = date + uuid + fileName;
            java.io.File file = new java.io.File(dirPath, fileName);
            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String path = fileupload + date + "/" + fileName;
            System.out.println("path = " + path);
            String url = baseUrl + "/media" + fileupload + date + "/" + fileName;
            System.out.println("url = " + url);
            fileMapper.insert(new File(originalName, fileName, contentType, path, url, StringUtil.readableFileSize(fileSize), describe, userId));
            HashMap<String, Object> data = new HashMap<>();
            data.put("fileName", originalName);
            data.put("size", StringUtil.readableFileSize(fileSize));
            list.add(data);
        }
        if (Msg.length() == 0) {
            return Result.createBySuccess(list);
        } else {
            return Result.createByErrorMessage(Msg.toString());
        }
    }
}

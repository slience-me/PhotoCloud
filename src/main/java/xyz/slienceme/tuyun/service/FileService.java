package xyz.slienceme.tuyun.service;

import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;
import org.springframework.web.multipart.MultipartFile;
import xyz.slienceme.tuyun.common.entity.Result;
import xyz.slienceme.tuyun.pojos.File;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.slienceme.tuyun.pojos.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author slience_me
 * @since 2023-03-26
 */
public interface FileService extends IService<File> {

    void downloadFile(HttpServletRequest request, HttpServletResponse response, Integer id);

    Result<?> uploadFile(String accessToken, MultipartFile[] multipartFiles) throws IOException;

    Result<?> uploadImage(String accessToken, MultipartFile[] multipartFiles, Integer type) throws IOException;

    PageInfo<File> seletAllImage(String username, Integer p);

    PageInfo<File> seletAllPublicImage(Integer p);

    PageInfo<File> seletAllPrivateImage(String username, Integer p);

    PageInfo<File> seletAllFiles(String username, Integer p);

    Result<?> delFile(String accessToken, Integer id);

}

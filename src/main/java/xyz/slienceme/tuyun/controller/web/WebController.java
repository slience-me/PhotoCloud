/**
 * @title WebController
 * @description tuyun
 * @author slience_me
 * @version 1.0.0
 * @since 2023/3/30 20:00
 */
package xyz.slienceme.tuyun.controller.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import xyz.slienceme.tuyun.controller.api.BaseController;
import xyz.slienceme.tuyun.pojos.File;
import xyz.slienceme.tuyun.service.FileService;
import xyz.slienceme.tuyun.utils.JWT;

import javax.jws.WebParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
@RequestMapping
//@Api(tags = "【WEB】操作接口")
public class WebController extends BaseController {

    public static final String UPLOADFILE = "web/upload-file";
    public static final String UPLOAD = "web/upload-img";
    public static final String FILELIST = "web/filelist";
    public static final String FILELIST1 = "web/filelist1";
    public static final String IMAGELIST = "web/imagelist";
    public static final String IMAGELIST1 = "web/imagelist1";
    public static final String INDEX = "web/index";
    public static final String PRIVATE = "web/private";
    public static final String PRIVATE1 = "web/private1";
    public static final String DETAIL = "web/detail";

    @Autowired
    private FileService fileService;


    @GetMapping("/upload-file.html")
    public String uploadFileWeb(HttpServletRequest request, ModelMap model) {
        return UPLOADFILE;
    }

    @GetMapping("/upload-img.html")
    public String uploadWeb(HttpServletRequest request, ModelMap model) {
        return UPLOAD;
    }

    @GetMapping("/filelist.html")
    public String fileListWeb(ModelMap model, HttpServletRequest request, @RequestParam(required = false) Integer p) {
        String username = getUser(request);
        if (Objects.isNull(username) || "未登录".equals(username)){
            return FILELIST1;
        }
        PageInfo<File> filePageInfo = fileService.seletAllFiles(username, p);
        model.addAttribute("page", filePageInfo);
        if (Objects.nonNull(filePageInfo)){
            return FILELIST;
        } else {
            return FILELIST1;
        }
    }

    @GetMapping("/imagelist.html")
    public String imageListWeb(ModelMap model, HttpServletRequest request, @RequestParam(required = false) Integer p) {
        String username = getUser(request);
        if (Objects.isNull(username) || "未登录".equals(username)){
            return IMAGELIST1;
        }
        PageInfo<File> filePageInfo = fileService.seletAllPrivateImage(username, p);
        model.addAttribute("page", filePageInfo);
        if (Objects.nonNull(filePageInfo)){
            return IMAGELIST;
        } else {
            return IMAGELIST1;
        }

    }

    @GetMapping("/index.html")
    public String indexWeb(ModelMap model, @RequestParam(required = false) Integer p) {
        model.addAttribute("page", fileService.seletAllPublicImage(p));
        return INDEX;
    }

    @GetMapping("")
    public String indexWeb1(ModelMap model, @RequestParam(required = false) Integer p) {
        model.addAttribute("page", fileService.seletAllPublicImage(p));
        return INDEX;
    }

    @GetMapping("/about.html")
    public String aboutWeb() {
        return "web/about";
    }

    private static String getUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String username = "未登录";
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if ("secret".equals(cookie.getName())) {
                    username = JWT.unsign(cookie.getValue(), String.class);
                    break;
                }
            }
        }
        //System.out.println("user = " + username);
        return username;
    }

    @GetMapping("/private.html")
    public String privateWeb(ModelMap model, HttpServletRequest request, @RequestParam(required = false) Integer p) {
        String username = getUser(request);
        if (Objects.isNull(username) || "未登录".equals(username)){
            return PRIVATE1;
        }
        PageInfo<File> filePageInfo = fileService.seletAllPrivateImage(username, p);
        model.addAttribute("page", filePageInfo);
        if (Objects.nonNull(filePageInfo)){
            return PRIVATE;
        } else {
            return PRIVATE1;
        }

    }

    @GetMapping("/detail.html")
    public String detailWeb(ModelMap model, String imgUrl, String fileName) {
        model.addAttribute("imgUrl", imgUrl);
        model.addAttribute("fileName", fileName);
        return DETAIL;
    }



}

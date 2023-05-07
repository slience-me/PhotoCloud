/**
 * @title WebUserController
 * @description tuyun
 * @author slience_me
 * @version 1.0.0
 * @since 2023/3/27 10:46
 */
package xyz.slienceme.tuyun.controller.web;


import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.slienceme.tuyun.pojos.File;
import xyz.slienceme.tuyun.pojos.User;
import xyz.slienceme.tuyun.service.FileService;
import xyz.slienceme.tuyun.service.UserService;
import xyz.slienceme.tuyun.utils.JWT;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;


@Controller
@Slf4j
@RequestMapping("/ad")
//@Api(tags = "【WEB】用户操作接口")
public class WebAdminController {


    public static final String USERLIST = "admin/userlist";
    public static final String USERLIST1 = "admin/userlist1";

    public static final String IMAGELIST = "admin/imagelist";
    public static final String IMAGELIST1 = "admin/imagelist1";

    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;

    /**
     * 登录界面
     */
    @GetMapping("/lo")
    public String login(){
        return "admin/login";
    }


    @GetMapping("/ul")
    public String userList(ModelMap model, HttpServletRequest request, @RequestParam(value = "p", required = false) Integer p){
        String username = getUser(request);
        if (Objects.isNull(username) || "未登录".equals(username)){
            return USERLIST1;
        }
        PageInfo<User> filePageInfo = userService.seletAllUsers(username, p);
        model.addAttribute("page", filePageInfo);
        if (Objects.nonNull(filePageInfo)){
            return USERLIST;
        } else {
            return USERLIST1;
        }
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

    @GetMapping("/il")
    public String imageListWeb(ModelMap model, HttpServletRequest request, @RequestParam(required = false) Integer p) {
        String username = getUser(request);
        if (Objects.isNull(username) || "未登录".equals(username)){
            return IMAGELIST1;
        }
        PageInfo<File> filePageInfo = fileService.seletAllImage(username, p);
        model.addAttribute("page", filePageInfo);
        if (Objects.nonNull(filePageInfo)){
            return IMAGELIST;
        } else {
            return IMAGELIST1;
        }

    }

}

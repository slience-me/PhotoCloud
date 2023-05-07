/**
 * @title WebUserController
 * @description tuyun
 * @author slience_me
 * @version 1.0.0
 * @since 2023/3/27 10:46
 */
package xyz.slienceme.tuyun.controller.web;


import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import xyz.slienceme.tuyun.service.UserService;

import java.util.List;


@Controller
@Slf4j
@RequestMapping("/user")
//@Api(tags = "【WEB】用户操作接口")
public class WebUserController {


    public static final String INDEX = "/index.html";
    public static final String LOGIN = "/user/login.html";
    public static final String REGISTER = "/user/register.html";

    @Autowired
    private UserService userService;

    @GetMapping("/confirm")
    public String confirm(ModelMap model,
                          @RequestParam String code,
                          @RequestParam String u) {
        //log.info("[用户激活接口调用-----</confirm>:  " + code +"]");
        List<Object> result = userService.confirm(u, code);
        Integer flag = (Integer) result.get(0);
        if (flag.equals(1)){
            model.addAttribute("msg", result.get(1));
            model.addAttribute("link", REGISTER);
            model.addAttribute("label", "点击重新注册");
        } else if (flag.equals(2)){
            model.addAttribute("msg", result.get(1));
        } else if (flag.equals(3)){
            model.addAttribute("msg", result.get(1));
            model.addAttribute("link", INDEX);
            model.addAttribute("login", LOGIN);
            model.addAttribute("label", "点击进入主页");
            model.addAttribute("label1", "请登录");
        }
        return "user/confirm";
    }

    /**
     * 登录界面
     */
    @GetMapping("/login.html")
    public String login(){
        return "user/login";
    }

    /**
     * 注册界面
     */
    @GetMapping("/register.html")
    public String register(){
        return "user/register";
    }

    /**
     * 协议界面
     */
    @GetMapping("/agreement.html")
    public String agreement(){
        return "user/agreement";
    }
}

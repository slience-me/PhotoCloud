package xyz.slienceme.tuyun.controller.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import xyz.slienceme.tuyun.common.entity.Result;
import xyz.slienceme.tuyun.pojos.User;
import xyz.slienceme.tuyun.service.UserService;
import xyz.slienceme.tuyun.vo.LoginVO;
import xyz.slienceme.tuyun.vo.RegisterVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author slience_me
 * @since 2023-03-26
 */
@Slf4j
@RestController
@RequestMapping("/user")
//@Api(tags = "【用户】操作接口")
public class UserController {

    @Autowired
    private UserService userService;

//    @ApiOperation("用户登陆")
    @CrossOrigin
    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginVO loginVO,
                           HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, JsonProcessingException {
        //LoginVO loginVO = new LoginVO(username, password, verifyCode);
        log.info("[用户登陆接口调用-----</login>:  " + loginVO +"]");
        return userService.login(loginVO, request, response);
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody RegisterVO registerVO, HttpServletRequest request) throws IOException {
        log.info("[用户注册接口调用-----</login>:  " + registerVO +"]");
        return userService.register(registerVO, request);
    }

    @GetMapping("/logout")
    public Result<?> logOut(@RequestHeader("x-access-token") String accessToken) throws Exception {
        log.info("[用户退出登陆接口调用---get--</out>]");
        return userService.logOut(accessToken);
    }

}

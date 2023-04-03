package xyz.slienceme.tuyun.controller.api;


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

import javax.servlet.http.HttpServletRequest;

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
@Api(tags = "【用户】操作接口")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("用户登陆")
    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginVO loginVO, HttpServletRequest request) {
        log.info("[用户登陆接口调用-----</login>:  " + loginVO +"]");
        return userService.login(loginVO, request);
    }

//    @ApiOperation("查询用户列表")
//    @GetMapping("/userList")
//    public Result userList(@RequestHeader("x-access-token") String accessToken,
//                            @ApiParam(value = "第几页", required = true) @RequestParam(value = "pageNo") Integer pageNo,
//                            @ApiParam(value = "每页条数", required = true) @RequestParam(value = "pageSize") Integer pageSize,
//                            @ApiParam(value = "Enable标识 0启用 1停用") @RequestParam(value = "enabled", required = false) Integer enabled,
//                            @ApiParam(value = "用户名称、描述") @RequestParam(value = "keyword", required = false) String keyword) throws Exception {
//        //log.info("[查询用户列表接口调用--get---</userList>:  pageNo=" + pageNo + ",pageSize=" + pageSize + ",keyword=" + keyword + "]");
//        return userService.userList(accessToken, pageNo, pageSize, enabled, keyword);
//    }
//
//
//    @ApiOperation("添加用户")
//    @PostMapping("/userList")
//    public Result userAdd(@RequestHeader("x-access-token") String accessToken,
//                           @RequestBody User user) throws Exception {
//        log.info("[添加用户接口调用---post--</userList>:  userAddVO=" + user+ "]");
//        return userService.userAdd(accessToken, user);
//    }
//
//
//    @ApiOperation("通过id删除用户账号")
//    @DeleteMapping("/userList")
//    public Result userDel(@RequestHeader("x-access-token") String accessToken,
//                           @ApiParam(value = "用户id") @RequestParam(value = "userId") Integer userId) throws Exception {
//        log.info("[通过id删除用户账号接口调用---delete--</userList>:  userId=" + userId+ "]");
//        return userService.userDel(accessToken, userId);
//    }
//
//
//    @ApiOperation("修改用户")
//    @PutMapping("/userList")
//    public Result userPut(@RequestHeader("x-access-token") String accessToken,
//                           @RequestBody User user) throws Exception {
//        log.info("[修改用户接口调用---put--</userList>:  user=" + user+ "]");
//        return userService.userPut(accessToken, user);
//    }
//
//
//    @ApiOperation("修改密码")
//    @PutMapping("/userPwd")
//    public Result userPwdPut(@RequestHeader("x-access-token") String accessToken,
//                              @RequestBody PwdVO pwdVO) throws Exception {
//        log.info("[修改密码接口调用---put--</userPwd>:  PwdVO=" + pwdVO+ "]");
//        return userService.userPwdPut(accessToken, pwdVO);
//    }
//
//    @ApiOperation("重置密码")
//    @GetMapping("/userPwd")
//    public Result userPwd(@RequestHeader("x-access-token") String accessToken,
//                           @ApiParam(value = "用户id") @RequestParam(value = "userId") Integer userId) throws Exception {
//        log.info("[重置密码接口调用---put--</userPwd>:  userId=" + userId+ "]");
//        return userService.userPwd(accessToken, userId);
//    }
//
//    @ApiOperation("user退出登陆")
//    @GetMapping("/out")
//    public Result userOut(@RequestHeader("x-access-token") String accessToken) throws Exception {
//        log.info("[user退出登陆接口调用---put--</out>]");
//        return userService.userOut(accessToken);
//    }

}

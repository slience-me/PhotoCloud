/**
 * @title VerifyController
 * @description tuyun
 * @author slience_me
 * @version 1.0.0
 * @since 2023/3/26 20:03
 */
package xyz.slienceme.tuyun.controller.web;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.slienceme.tuyun.common.entity.Result;
import xyz.slienceme.tuyun.service.VerifyService;
import xyz.slienceme.tuyun.utils.VerifyUtil;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
//测试Controller
@RestController
@RequestMapping("verify")
@Api(tags = "【验证码】操作接口")
public class VerifyController {

    @Autowired
    private VerifyService verifyService;

    /**
     * 生成验证码的接口
     *
     * @param response Response对象
     * @param request  Request对象
     * @throws Exception
     */
    @ApiOperation("生成验证码")
    @GetMapping("/getcode")
    public void getCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        verifyService.getCode(request, response);
    }

//    /**
//     * 业务接口包含了验证码的验证
//     *
//     * @param code    前端传入的验证码
//     * @param request Request对象
//     * @return
//     */
//    @ApiOperation("业务接口包含了验证码的验证")
//    @GetMapping("/checkcode")
//    public Result<?> checkCode(String code, HttpServletRequest request) {
//        String result = verifyService.checkCode(code, request);
//        return Result.createBySuccessMessage(result);
//    }
}
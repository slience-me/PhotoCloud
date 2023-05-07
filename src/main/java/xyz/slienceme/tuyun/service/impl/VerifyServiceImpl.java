package xyz.slienceme.tuyun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import xyz.slienceme.tuyun.service.VerifyService;
import xyz.slienceme.tuyun.utils.VerifyUtil;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class VerifyServiceImpl implements VerifyService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void getCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取到session
        HttpSession session = request.getSession();
        // 取到sessionid
        String id = session.getId();
        //System.out.println("sessionId = " + id);
        // 利用图片工具生成图片
        // 返回的数组第一个参数是生成的验证码，第二个参数是生成的图片
        Object[] objs = VerifyUtil.newBuilder()
                .setWidth(80)   //设置图片的宽度
                .setHeight(35)   //设置图片的高度
                .setSize(4)      //设置字符的个数
                .setLines(10)    //设置干扰线的条数
                .setFontSize(30) //设置字体的大小
                .setTilt(false)   //设置是否需要倾斜
                .setBackgroundColor(Color.LIGHT_GRAY) //设置验证码的背景颜色
                .build()         //构建VerifyUtil项目
                .createImage();  //生成图片
        // 将验证码存入Session
        //session.setAttribute("SESSION_VERIFY_CODE_" + id, objs[0]);
        String codeKey = "SESSION_VERIFY_CODE:" + id;
        session.setAttribute(codeKey, objs[0]);
        // 打印验证码
        //System.out.println(objs[0]);
        /*System.out.println(objs[0]);
        String uuid = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("CaptchaCode",uuid);
        response.addCookie(cookie);*/

        // 设置redis值的序列化方式
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        // 在redis中保存一个验证码最多尝试次数
        // 这里采用的是先预设一个上限次数，再以reidis decrement(递减)的方式来进行验证
        // 这样有个缺点，就是用户只申请验证码，不验证就走了的话，这里就会白白占用5分钟的空间，造成浪费了
        // 为了避免以上的缺点，也可以采用redis的increment（自增）方法，只有用户开始在做验证的时候设置值，
        //    超过多少次错误，就失效；避免空间浪费
        try {
            redisTemplate.opsForValue().set(("VERIFY_CODE:" + id), "3", 5 * 60, TimeUnit.SECONDS);
        } catch (Exception e){
            System.out.println("Read timed out, Redis链接超时！");
            return;
        }
        // 将图片输出给浏览器
        BufferedImage image = (BufferedImage) objs[1];
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "png", os);
    }
}

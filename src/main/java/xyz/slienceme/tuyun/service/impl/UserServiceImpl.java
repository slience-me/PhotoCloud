package xyz.slienceme.tuyun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import xyz.slienceme.tuyun.common.entity.Result;
import xyz.slienceme.tuyun.mapper.LogMapper;
import xyz.slienceme.tuyun.pojos.File;
import xyz.slienceme.tuyun.pojos.Log;
import xyz.slienceme.tuyun.pojos.User;
import xyz.slienceme.tuyun.mapper.UserMapper;
import xyz.slienceme.tuyun.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.slienceme.tuyun.utils.*;
import xyz.slienceme.tuyun.vo.LoginVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import xyz.slienceme.tuyun.vo.RegisterVO;
import xyz.slienceme.tuyun.vo.TokenVO;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author slience_me
 * @since 2023-03-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${redis.admin.login.token}")
    private String redisAdminLoginKey;
    @Value("${email.env.active}")
    private String env;

    @Value("${user.login.long.time}")
    private Long maxAge;

    @Value("${spring.path.url}")
    private String url;

    @Autowired
    private JavaMailSender javaMailSender;
    private static final String from = "slience_me@foxmail.com";
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LogMapper logMapper;

    /**
     * 用户登陆
     *
     * @param loginVO 登录信息对象
     */
    @Override
    public Result<?> login(LoginVO loginVO, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, JsonProcessingException {
        if (Objects.isNull(loginVO.getUsername()) || "".equals(loginVO.getUsername()) ||
                Objects.isNull(loginVO.getPwd()) || "".equals(loginVO.getPwd()) ||
                Objects.isNull(loginVO.getVerifyCode()) || "".equals(loginVO.getVerifyCode())){
            return Result.createByErrorMessage("填写内容不全");
        }
        Integer verifyResult = checkCode(loginVO.getVerifyCode(), request);
        if (verifyResult.equals(2)) {
            return Result.createByVerifyErrorMessage("验证码错误!");
        } else if (verifyResult.equals(1)) {
            return Result.createByVerifyInvalidMessage("验证码失效!");
        } else if (verifyResult.equals(3)) {
            return Result.createByErrorMessage("当前网络较慢,请重试!");
        }
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, loginVO.getUsername()).eq(User::getPassword, loginVO.getPwd()).eq(User::getIsDeleted, 0));
        String countKey = "errorCount:" + loginVO.getUsername();
        if (Objects.isNull(user)) {
            System.out.println("用户名或密码错误！" + loginVO);
            User user1 = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, loginVO.getUsername()).eq(User::getIsDeleted, 0));
            if (Objects.isNull(user1)) {
                return Result.createByErrorMessage("用户不存在!请重试!");
            } else {
                String errorCount = redisTemplate.opsForValue().get(countKey);
                if (("").equals(errorCount) || Objects.isNull(errorCount)) {
                    redisTemplate.opsForValue().set(countKey, "1", 10 * 60, TimeUnit.SECONDS);
                    return Result.createByErrorMessage("账号或密码错误1次, 密码错误∞次账户将被封禁");
                } else {
                    /*if (Integer.parseInt(errorCount) >= 3) {
                        user1.setEnabled(1);
                        userMapper.updateById(user1);
                        return Result.createByErrorMessage("账户已被封禁,请联系管理员");
                    }*/
                    redisTemplate.opsForValue().increment(countKey);
                    errorCount = redisTemplate.opsForValue().get(countKey);
                    return Result.createByErrorMessage("账号或密码错误" + errorCount + "次, 密码错误∞次账户将被封禁");
                }
            }
        } else {
            System.out.println("用户存在！" + loginVO + "{ 姓名：'" + user.getName() + "'}");
            if (user.getEnabled() == UserEnabledEnum.DEACTIVATE.getCode()) {
                return Result.createByErrorMessage("该账户未激活，请及时激活!");
            }
            if (user.getEnabled() == UserEnabledEnum.FORBIDDEN.getCode()) {
                return Result.createByErrorMessage("该账户已被封禁，请联系管理员解除");
            }
            if (user.getEnabled() == UserEnabledEnum.MAINTAINED.getCode()) {
                return Result.createByErrorMessage("系统维护中!!!!!!!!");
            }
            redisTemplate.delete(countKey);
            String IP = IpUtil.getIpAddr(request);
            String device = DeviceUtil.getdevice(DeviceUtils.getCurrentDevice(request));
            String userAgent = StringUtil.userAgent(request.getHeader("user-agent"));
            logMapper.insert(new Log(user.getId(), user.getName(), IP, device + " " + userAgent, "管理员 【" + user.getName() + "】 登录了"));
            TokenVO tokenVo = new TokenVO(user.getId(), user.getType(), IP, device + " " + userAgent);
            HashMap<String, Object> hashMap = new HashMap<>();
            String token = JWT.sign(tokenVo, maxAge);
            hashMap.put("x-access-token", token);
            hashMap.put("userInfo", user);
            hashMap.put("IP", IP);
            hashMap.put("deviceUserAgent", device + userAgent);
            HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
            hashOperations.put(redisAdminLoginKey, user.getId().toString(), token);
            //byte[] encrypt = DesUtil.encrypt(user.getUsername().getBytes(), DesUtil.secret);
            //String secret =  Objects.nonNull(encrypt) ? new String(encrypt) : "";
            Cookie cookie = new Cookie("secret", JWT.sign(user.getUsername(), 604800000));
            cookie.setPath("/");
            cookie.setMaxAge(604800);  //秒 7天
            response.addCookie(cookie);
            return Result.createBySuccess(hashMap);
        }
    }

    @Override
    public Result<?> register(RegisterVO registerVO, HttpServletRequest request) throws IOException {
        if ((Objects.isNull(registerVO.getUsername()) || "".equals(registerVO.getUsername())) ||
                (Objects.isNull(registerVO.getPassword()) || "".equals(registerVO.getPassword())) ||
                (Objects.isNull(registerVO.getName()) || "".equals(registerVO.getName())) ||
                (Objects.isNull(registerVO.getEmail()) || "".equals(registerVO.getEmail()))) {
            return Result.createByErrorMessage("请完整填写数据!");
        }
        Integer verifyResult = checkCode(registerVO.getVerifyCode(), request);
        if (verifyResult.equals(2)) {
            return Result.createByErrorMessage("验证码错误!");
        } else if (verifyResult.equals(1)) {
            return Result.createByErrorMessage("验证码失效!");
        }
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, registerVO.getUsername()).eq(User::getIsDeleted, 0));
        if (Objects.nonNull(user)) {
            return Result.createByErrorMessage("该账户已存在, 请直接登录!");
        }
        if (!EmailValidator.isValid(registerVO.getEmail())) {
            return Result.createByErrorMessage("邮箱格式不正确!");
        }
        int flag = userMapper.insert(
                new User(registerVO.getUsername(),
                        registerVO.getPassword(),
                        registerVO.getName(),
                        1L,
                        registerVO.getEmail(),
                        RoleEnum.USER.getType(),
                        registerVO.getName()));
        if (flag > 0) {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            String uuid = UUID.randomUUID().toString().replace("-", "");
            System.out.println("uuid = " + uuid);
            String htmlEmail = EmaildataUtil.getHtmlEmailRegister(
                    registerVO.getUsername(),
                    url,
                    uuid,
                    1,
                    env
            );
            String confirmKey = "confirm:" + registerVO.getUsername();
            redisTemplate.opsForValue().set(confirmKey, uuid, 60 * 60, TimeUnit.SECONDS);
            try {
                helper.setFrom(from + "(注册成功邮件)");
                helper.setTo(registerVO.getEmail());
                helper.setSubject("图云官方提醒您,注册成功!请及时激活账户!");
                helper.setText(htmlEmail, true);
                javaMailSender.send(message);
            } catch (Exception e) {
                e.printStackTrace();
                userMapper.delete(new LambdaQueryWrapper<User>().eq(User::getUsername, registerVO.getUsername()));
                return Result.createBySuccessMessage("注册失败！请重试!");
            }
            return Result.createBySuccessMessage("注册成功！");
        } else {
            return Result.createBySuccessMessage("注册失败！请重试!");
        }
    }

    @Override
    public List<Object> confirm(String username, String code) {
        String confirmKey = "confirm:" + username;
        String redisCode = redisTemplate.opsForValue().get(confirmKey);
        List<Object> result = new ArrayList<>(2);
        if (Objects.isNull(redisCode) || "".equals(redisCode)) {
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
            if (user.getType() == 1){ // 未激活就删除
                userMapper.delete(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
            }
            result.add(1);
            result.add("激活码已经失效, 请重新注册!");
            return result;
        }
        if (redisCode.equals(code)) {
            redisTemplate.delete(confirmKey);
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username).eq(User::getIsDeleted, 0));
            if (Objects.isNull(user)) {
                result.add(2);
                result.add("激活失败!");
            }
            user.setEnabled(UserEnabledEnum.ACTIVATE.getCode());
            userMapper.updateById(user);
            result.add(3);
            result.add("恭喜您！"+user.getName()+" 您的图云客户端激活成功!");
        } else {
            result.add(2);
            result.add("激活失败!");
        }
        return result;
    }

    @Override
    public Result<?> logOut(String accessToken) {
        if (Objects.isNull(accessToken) || "".equals(accessToken) || "null".equals(accessToken)){
            return Result.createByErrorMessage("当前未登录!!");
        } else {
            Long userId = JWT.unsign(accessToken, TokenVO.class).getUserId();
            //User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, userId));
            redisTemplate.opsForHash().delete(redisAdminLoginKey, userId.toString());
            return Result.createBySuccessMessage("成功");
        }

    }

    @Override
    public PageInfo<User> seletAllUsers(String username, Integer p) {
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
            List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>()
                    .eq(User::getIsDeleted, 0)
                    .orderByDesc(User::getCreatedTime));
            return new PageInfo<>(users);
        }
        return null;
    }


    private Integer checkCode(String code, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String id = session.getId();
        /*Cookie[] cookies = request.getCookies();
        System.out.println("cookie = " + cookies);*/
        // 将redis中的尝试次数减一
        String verifyCodeKey = "VERIFY_CODE:" + id;
        long num;
        try {
            num = redisTemplate.opsForValue().decrement(verifyCodeKey);
        } catch (Exception e){
            System.out.println("Read timed out, Redis链接超时！");
            return 3;
        }
        // 如果次数次数小于0 说明验证码已经失效
        if (num < 0) {
            //return "验证码失效!";
            return 1;
        }
        // 将session中的取出对应session id生成的验证码
        String serverCode = (String) session.getAttribute("SESSION_VERIFY_CODE:" + id);
        // 校验验证码
        if (null == serverCode || null == code || !serverCode.toUpperCase().equals(code.toUpperCase())) {
            //return "验证码错误!";
            return 2;
        }
        // 验证通过之后手动将验证码失效
        redisTemplate.delete(verifyCodeKey);
        // 这里做具体业务相关
        return 0;
        //return "验证码正确!";
    }
}

package xyz.slienceme.tuyun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import xyz.slienceme.tuyun.common.entity.Result;
import xyz.slienceme.tuyun.mapper.LogMapper;
import xyz.slienceme.tuyun.pojos.User;
import xyz.slienceme.tuyun.mapper.UserMapper;
import xyz.slienceme.tuyun.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.slienceme.tuyun.vo.LoginVO;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


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
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LogMapper logMapper;
    @Autowired
    private HttpServletRequest request;

    /**
     * 用户登陆
     *
     * @param loginVO 登录信息对象
     */
    @Override
    public Result<?> login(LoginVO loginVO, HttpServletRequest request) {
        Integer verifyResult = checkCode(loginVO.getVerifyCode(), request);
        if (verifyResult.equals(2)) {
            return Result.createByErrorMessage("验证码错误!");
        } else if (verifyResult.equals(1)) {
            return Result.createByErrorMessage("验证码失效!");
        }
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, loginVO.getUsername())
                        .eq(User::getPassword, loginVO.getPwd())
                        .eq(User::getIsDeleted, 0));
        String countKey = "errorCount:" + loginVO.getUsername();
        if (Objects.isNull(user)) {
            System.out.println("用户名或密码错误！" + loginVO);
            User user1 = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, loginVO.getUsername()));
            if (Objects.isNull(user1)) {
                return Result.createByErrorMessage("用户不存在!请重试!");
            } else {
                String errorCount = redisTemplate.opsForValue().get(countKey);
                if (("").equals(errorCount) || Objects.isNull(errorCount)) {
                    redisTemplate.opsForValue().set(countKey, "1", 10 * 60, TimeUnit.SECONDS);
                } else {
                    if (Integer.parseInt(errorCount) >= 3) {
                        user1.setEnabled(1);
                        userMapper.updateById(user1);
                        return Result.createByErrorMessage("账户已被封禁,请联系管理员");
                    }
                    redisTemplate.opsForValue().increment(countKey);
                    return Result.createByErrorMessage("账号或密码错误" + errorCount + "次, 密码错误三次账户将被封禁");
                }
            }
        } else {
            System.out.println("用户存在！" + loginVO + "{ 姓名：'" + user.getName() + "'}");
            if (user.getEnabled() == 1) {
                return Result.createByErrorMessage("该账户已被封禁，请联系管理员解除");
            }
            if (user.getEnabled() == 2) {
                return Result.createByErrorMessage("系统维护中!!!!!!!!");
            }
            redisTemplate.delete(countKey);
            String IP = IpUtil.getIpAddr(request);
            String device = DeviceUtil.getdevice(DeviceUtils.getCurrentDevice(request));
            String userAgent = StringUtil.userAgent(request.getHeader("user-agent"));
            logMapper.insert(new Log(admin.getAdminId(), "【管理员模块】", IP, device + " " + userAgent, "管理员 【" + admin.getAdminName() + "】 登录了"));
            TokenVO tokenVo = new TokenVO(admin.getAdminId(), null,1,  IP, device + " " + userAgent);
            HashMap<String, Object> hashMap = new HashMap<>();
            String token = JWT.sign(tokenVo, Long.parseLong(PropertyUtil.getValue("application.properties", "user.login.long.time")));
            hashMap.put("x-access-token", token);
            hashMap.put("userInfo", admin);
            hashMap.put("IP", IP);
            hashMap.put("deviceUserAgent", device + userAgent);
            HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
            hashOperations.put(redisAdminLoginKey, admin.getAdminId().toString(), token);
            return Result.createBySuccess(hashMap);
        }
    }

    private Integer checkCode(String code, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String id = session.getId();
        // 将redis中的尝试次数减一
        String verifyCodeKey = "VERIFY_CODE:" + id;
        long num = redisTemplate.opsForValue().decrement(verifyCodeKey);
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

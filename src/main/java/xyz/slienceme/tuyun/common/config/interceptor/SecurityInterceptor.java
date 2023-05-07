package xyz.slienceme.tuyun.common.config.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.slienceme.tuyun.utils.Delay;
import xyz.slienceme.tuyun.utils.JWT;
import xyz.slienceme.tuyun.vo.TokenVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;

@Component
public class SecurityInterceptor implements HandlerInterceptor {
    /**
     * logger instance
     */
    static Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

    private long lastTime = 0;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Value("${redis.admin.login.token}")
    private String redisAdminLoginKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("x-access-token");
        if (StringUtil.isEmpty(token)) {
            getMsg("请登录后访问!", 2, request, response);
            return false;
        }

        TokenVO tokenVo;
        try {
            tokenVo = JWT.unsign(token, TokenVO.class);
            if (Objects.isNull(tokenVo)) {
                getMsg("请登录后访问!", 2, request, response);
                return false;
            }
        } catch (Exception e) {
            getMsg("您还未登录，或者Token已过期!", 3, request, response);
            return false;
        }

        //判断token是否一致
        try {
            HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
            if (!hashOperations.hasKey(redisAdminLoginKey, tokenVo.getUserId().toString())) {
                getMsg("请登录后访问!", 2, request, response);
                return false;
            } else {
                String s = Objects.requireNonNull(hashOperations.get(redisAdminLoginKey, tokenVo.getUserId().toString())).toString();
                if (!s.equals(token)) {
                    getMsg("请登录后访问!", 2, request, response);
                    return false;
                }
            }
        } catch (Exception e) {
            getMsg("请登录后访问!", 2, request, response);
            return false;
        }

        //开始进入请求地址拦截
        HandlerMethod hm = (HandlerMethod) handler;
        Delay delay = hm.getMethodAnnotation(Delay.class);
        if (delay != null) {
            boolean b = startDelay(delay.time());
            if (!b) {
                JSONObject res = new JSONObject();
                res.put("data", new ArrayList<>());
                res.put("message", "请勿重复请求");
                res.put("code", 0);
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                PrintWriter out = null;
                out = response.getWriter();
                out.append(res.toString());
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //处理请求完成后视图渲染之前的处理操作
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //视图渲染之后的操作
    }

    private boolean startDelay(int time) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTime > time) {
            lastTime = currentTime;
            return true;
        }
        return false;
    }

    //封装了返回前端的消息体
    private void getMsg(String msg, Integer code, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject res = new JSONObject();
        res.put("msg", msg);
        res.put("status", code);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        out = response.getWriter();
        out.append(res.toString());
    }
}


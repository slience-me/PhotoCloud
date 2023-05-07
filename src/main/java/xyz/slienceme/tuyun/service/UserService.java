package xyz.slienceme.tuyun.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import xyz.slienceme.tuyun.common.entity.Result;
import xyz.slienceme.tuyun.pojos.User;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.slienceme.tuyun.vo.LoginVO;
import xyz.slienceme.tuyun.vo.RegisterVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author slience_me
 * @since 2023-03-26
 */
public interface UserService extends IService<User> {

    Result<?> login(LoginVO loginVO, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, JsonProcessingException;

    Result<?> register(RegisterVO registerVO, HttpServletRequest request) throws IOException;

    List<Object> confirm(String username, String code);

    Result<?> logOut(String accessToken);

    PageInfo<User> seletAllUsers(String username, Integer p);
}

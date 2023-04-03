package xyz.slienceme.tuyun.service;

import xyz.slienceme.tuyun.common.entity.Result;
import xyz.slienceme.tuyun.pojos.User;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.slienceme.tuyun.vo.LoginVO;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author slience_me
 * @since 2023-03-26
 */
public interface UserService extends IService<User> {

    Result<?> login(LoginVO loginVO, HttpServletRequest request);
}

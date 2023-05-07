package xyz.slienceme.tuyun.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.slienceme.tuyun.pojos.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author slience_me
 * @since 2023-03-26
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {


    /**
     * 登录操作
     * @param username
     * @param password
     * @return
     */
    User selectByUserNameAndPassword(@Param("username") String username,
                                     @Param("password") String password);

    User selectByUserName(@Param("username") String username);
}

package xyz.slienceme.tuyun.mapper;

import org.apache.ibatis.annotations.Mapper;
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

}

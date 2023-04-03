package xyz.slienceme.tuyun.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.slienceme.tuyun.pojos.Log;
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
public interface LogMapper extends BaseMapper<Log> {

}

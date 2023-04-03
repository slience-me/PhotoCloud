package xyz.slienceme.tuyun.service.impl;

import xyz.slienceme.tuyun.pojos.Log;
import xyz.slienceme.tuyun.mapper.LogMapper;
import xyz.slienceme.tuyun.service.LogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author slience_me
 * @since 2023-03-26
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

}

package xyz.slienceme.tuyun.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.slienceme.tuyun.pojos.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.slienceme.tuyun.pojos.Log;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author slience_me
 * @since 2023-03-26
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {

    Page<File> findPage();

    List<File> selectListByXml(@Param("userId") Long userId,
                               @Param("fileDescribe") String fileDescribe);
}

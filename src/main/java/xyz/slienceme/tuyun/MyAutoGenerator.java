/**
 * @title MyAutoGenerator
 * @description tuyun
 * @author slience_me
 * @version 1.0.0
 * @since 2023/3/26 15:18
 */
package xyz.slienceme.tuyun;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;

import java.util.List;

/**
 * 替换数据中空格，换行
 */
public class MyAutoGenerator extends AutoGenerator {

    @Override
    protected List<TableInfo> getAllTableInfoList(ConfigBuilder config) {
        List<TableInfo> tableInfos =  super.getAllTableInfoList(config);
        tableInfos.forEach(t->{
            t.getFields().forEach(f->{
                if(StringUtils.isNotEmpty(f.getComment())) {
                    String comment = f.getComment();
                    if(config.getGlobalConfig().isSwagger2()){
                        comment = comment.replaceAll("\r\n\\s*" , "");//注意，替换换行符
                    }
                    f.setComment(comment.trim());
                }
            });
        });
        return tableInfos;
    }
}

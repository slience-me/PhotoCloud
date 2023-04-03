package xyz.slienceme.tuyun.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *      日志表
 * </p>
 *
 * @author slience_me
 * @since 2023-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作用户ID
     */
    private Long userId;

    /**
     * 操作用户
     */
    private String name;

    /**
     * 操作IP
     */
    private String operateIp;

    /**
     * 操作设备
     */
    private String operateDevice;

    /**
     * 操作内容
     */
    private String operateContent;

    /**
     * 状态
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createdTime;


}

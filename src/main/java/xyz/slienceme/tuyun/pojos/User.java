package xyz.slienceme.tuyun.pojos;

import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 *      用户表
 * </p>
 *
 * @author slience_me
 * @since 2023-03-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账户
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 邮件
     */
    private String email;

    /**
     * 标识(管理员1 用户2)
     */
    private Integer type;

    /**
     * 启用状态
     */
    private Integer enabled;

    /**
     * 状态
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 创建人
     */
    private String createdBy;

    public User(String username, String password, String name, Long roleId, String email, Integer type, String createdBy) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.roleId = roleId;
        this.email = email;
        this.type = type;
        this.createdBy = createdBy;
    }
}

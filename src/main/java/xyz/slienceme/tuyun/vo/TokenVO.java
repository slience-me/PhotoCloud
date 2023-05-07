package xyz.slienceme.tuyun.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author slience_me
 * @Time : 2021/7/16  8:25
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenVO {
    private Long userId;//用户id
    private Integer status;//用户状态 0用户，1管理员
    private String IP;//用户IP
    private String device;//设备

}

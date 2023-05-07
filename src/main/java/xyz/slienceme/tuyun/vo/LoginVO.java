/**
 * @title LoginVO
 * @description tuyun
 * @author slience_me
 * @version 1.0.0
 * @since 2023/3/26 15:34
 */
package xyz.slienceme.tuyun.vo;

import lombok.Data;

@Data
public class LoginVO {
    private String username;//用户名
    private String pwd;//密码
    private String verifyCode; //验证码

    public LoginVO() {
    }

    public LoginVO(String username, String pwd, String verifyCode) {
        this.username = username;
        this.pwd = pwd;
        this.verifyCode = verifyCode;
    }
}

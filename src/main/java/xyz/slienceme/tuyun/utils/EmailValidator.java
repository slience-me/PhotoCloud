/**
 * @title EmailValidator
 * @description tuyun
 * @author slience_me
 * @version 1.0.0
 * @since 2023/3/27 10:00
 */
package xyz.slienceme.tuyun.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\." +
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * 判断邮箱格式是否正确
     *
     * @param email 邮箱字符串
     * @return true：邮箱格式正确；false：邮箱格式错误
     */
    public static boolean isValid(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public static void main(String[] args) {

        boolean isValidEmail = EmailValidator.isValid("example@example.com");
        if (isValidEmail) {
            System.out.println("邮箱格式正确");
        } else {
            System.out.println("邮箱格式错误");
        }

    }
}

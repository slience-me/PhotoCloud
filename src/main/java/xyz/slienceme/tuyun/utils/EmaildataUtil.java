package xyz.slienceme.tuyun.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class EmaildataUtil {

    public static String getHtmlEmail(String data) {
        Date nowTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTimeDateFormat = sdf.format(nowTime);
        String format = null;
        String html = "</head>\n" +
                "<body>\n" +
                "<div style=\"background-color:#ECECEC; padding: 35px;\">\n" +
                "    <table cellpadding=\"0\" align=\"center\"\n" +
                "           style=\"width: 600px; margin: 0px auto; text-align: left; position: relative; border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 5px; border-bottom-left-radius: 5px; font-size: 14px; font-family:微软雅黑, 黑体; line-height: 1.5; box-shadow: rgb(153, 153, 153) 0px 0px 5px; border-collapse: collapse; background-position: initial; background-repeat: initial;background:#fff;\">\n" +
                "        <tbody>\n" +
                "        <tr>\n" +
                "            <th valign=\"middle\"\n" +
                "                style=\"height: 25px; line-height: 25px; padding: 15px 35px; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: #EA743B; background-color: #EA743B; border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 0px; border-bottom-left-radius: 0px;\">\n" +
                "                <font face=\"微软雅黑\" size=\"5\" style=\"color: rgb(255, 255, 255); \">仓库系统异常通知</font>\n" +
                "            </th>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td>\n" +
                "                <div style=\"padding:25px 35px 40px; background-color:#fff;\">\n" +
                "                    <h2 style=\"margin: 5px 0px; \">\n" +
                "                        <font color=\"#333333\" style=\"line-height: 20px; \">\n" +
                "                            <font style=\"line-height: 22px; \" size=\"4\">\n" +
                "                                相关异常信息:</font>\n" +
                "                        </font>\n" +
                "                    </h2>\n" +
                "                    <p><b>{0}</b><br></p>\n" +
                "                    <p align=\"right\">{1}</p>\n" +
                "                    <div style=\"width:700px;margin:0 auto;\">\n" +
                "                        <div style=\"padding:10px 10px 0;border-top:1px solid #ccc;color:#747474;margin-bottom:20px;line-height:1.3em;font-size:12px;\">\n" +
                "                            <p>\n" +
                "                                <span>此为系统邮件，请勿回复</span><br>\n" +
                "                                <span>请保管好您的邮箱，避免账号被他人盗用</span><br>\n" +
                "\n" +
                "                            </p>\n" +
                "                            <p><span>Copyright © 2021 Slience_me</span><br>\n" +
                "                                <span id=\"poweredby\">Powered by <a href=\"https://www.slienceme.xyz/\" target=\"_blank\"\n" +
                "                                                                   title=\"Slience_me\">Slience_me</a></span></p>\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        </tbody>\n" +
                "    </table>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>\n";
        format = MessageFormat.format(html, data, nowTimeDateFormat);
        return format;
    }

    /**
     * 注册邮件
     * @param env  环境
     * @return
     * @throws IOException
     */
    public static String getHtmlEmailRegister(String username, String url, String code, Integer time, String env) throws IOException {
        Date nowTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTimeDateFormat = sdf.format(nowTime);
        String format = null;
        String html = new EmaildataUtil().getHtmlByPageName("register", env);
        format = MessageFormat.format(html, username, url, code, time, nowTimeDateFormat, "https://blog.csdn.net/Slience_me");
        return format;
    }

    /**
     * 获取对应pageName的html内容（本地eclipse里直接run）
     */
    private String getHtmlByPageName1(String pageName) {
        URL url = this.getClass().getClassLoader().getResource("templates/email/" + pageName + ".html");
        log.info("pageName : {} - {}", pageName, url);
        StringBuffer sb = new StringBuffer();
        BufferedInputStream bis = null;
        try {
            File f = new File(url.toURI());
            FileInputStream fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis);
            int len = 0;
            byte[] temp = new byte[1024];
            while ((len = bis.read(temp)) != -1) {
                sb.append(new String(temp, 0, len));
            }
            log.debug("page content:\n{}...", sb.toString().substring(0, 200));
        } catch (Exception e) {
            log.error("Error occurred, cause by: ", e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    log.error("Error occurred, cause by: ", e);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取对应pageName的html内容（生产环境java -jar直接run）
     */
    private String getHtmlByPageName2(String pageName) throws IOException {
        // /BOOT-INF/classes/templates/dashboard.html
        String path = "/BOOT-INF/classes/templates/email/" + pageName + ".html";
        // 返回读取指定资源的输入流
        InputStream is = this.getClass().getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s = "";
        StringBuffer sb = new StringBuffer();
        while ((s = br.readLine()) != null) {
            sb.append(s).append("\n");
        }
        return sb.toString();
    }

    /**
     * 获取对应pageName的html内容（本地eclipse里直接run）
     */
    private String getHtmlByPageName(String pageName, String env) throws IOException {
        if ("dev".equals(env)) {
            return getHtmlByPageName1(pageName);
        } else {
            return getHtmlByPageName2(pageName);
        }
    }

    public static void main(String[] args) throws IOException {
        String htmlEmailRegister = EmaildataUtil.getHtmlEmailRegister("135633693002", "http://127.0.0.1:7080", "fuhdfjahfjdgfjdsgfh", 1, "dev");
        System.out.println("htmlEmailRegister = " + htmlEmailRegister);

    }

}

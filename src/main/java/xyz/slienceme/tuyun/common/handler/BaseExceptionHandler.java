package xyz.slienceme.tuyun.common.handler;//package xyz.slienceme.common.handler;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import xyz.slienceme.common.entity.Result;
//import xyz.slienceme.common.exception.CommonException;
//import xyz.slienceme.utils.EmaildataUtil;
//
//import javax.mail.internet.MimeMessage;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//
///**
// * 自定义的公告异常处理类
// * 1. 声明异常处理类
// * 2. 对异常统一处理
// */
//
//@RestControllerAdvice
//public class BaseExceptionHandler {
//    //拦截所有的异常信息
//    @Autowired
//    private JavaMailSender javaMailSender;
//    private static final String from = "slience_me@foxmail.com";
//    private static final String to = "slience_me@foxmail.com";
//    private static final String subject = "异常邮件";
//
//    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
//    public Result doException(HttpServletRequest request, HttpServletResponse response, Exception ex){
////        System.out.println("ex.getMessage() = " + ex.getMessage());
//        MimeMessage message = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper  = new MimeMessageHelper(message);
//        String htmlEmail = EmaildataUtil.getHtmlEmail(ex.getMessage());
//        try {
////            helper.setFrom(from+"(仓库后台异常邮件)");
////            helper.setTo(to);
////            helper.setSubject(subject);
////            helper.setText(htmlEmail, true);
////            javaMailSender.send(message);
//            if (ex.getClass() == CommonException.class) {
//                CommonException ce = (CommonException) ex;
//                return Result.createByErrorMessage(ce.getMessage());
//            } else {
//                return Result.createByError();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return Result.createByError();
//    }
//
//    public Result BusinessException(String message){
//        return Result.createByErrorMessage(message);
//    }
//}

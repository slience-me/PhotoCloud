package xyz.slienceme.tuyun.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtil {


    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 大陆号码或香港号码均可
     */
    public static boolean isPhoneLegal(String str) throws PatternSyntaxException {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str);
    }

    /**
     * 生成uuid 无- uuid
     *
     * @return String
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 验证手机号码
     *
     * @param phone
     * @return
     */
    public static boolean verifyingPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        return Pattern.matches(regex, phone);
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    public static boolean isHKPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^(5|6|8|9)\\d{7}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isEmpty(List<?> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 产生随机的六位数
     *
     * @return
     */
    public static String getCode() {
        Random rad = new Random();

        String result = rad.nextInt(1000000) + "";

        if (result.length() != 6) {
            return getCode();
        }
        return result;
    }

    /**
     * 产生随机的六位数
     *
     * @return
     */
    public static String getCode11() {
        Random rad = new Random();

        String result = rad.nextInt(1000000000) + "" + rad.nextInt(100);

        if (result.length() != 11) {
            return getCode11();
        }
        return result;
    }

    //保留一位小数
    public static Double getMoneyZhengShu(Double bookMoneyCount) {
        return (double) Math.round(bookMoneyCount * 10) / 10;
    }

    /**
     * 短信生成
     *
     * @return
     */
    public static String getText(String code) {
        return "【草药后台测试】您的验证码是" + code + ",在5分钟内有效。如非本人操作请忽略本短信。";
    }

    public static String getSerialIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if (hashCodeV < 0) {//有可能是负数
            hashCodeV = -hashCodeV;
        }
        // 0 代表前面补充0
        // % 12 代表长度为12
        // d 代表参数为正数型
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + machineId + String.format("%012d", hashCodeV);
    }

    /**
     * 去除字符串的空格回车换行
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static String[] getList(String str) {
        String regex = ",|，";
        String[] list = str.split(regex);
        return list;
    }

    public static String getQRcode(Integer id) {
        DecimalFormat decFrt = new DecimalFormat("YC00000000");
        String szResult = decFrt.format(id);
        return szResult;
    }

    /**
     * 流水号生成
     */
    private final static String FORMAT_CODE = "000000";
    private final static String PRE_CODE = "SP";

    public static String serialNumber(Integer num) {
        DecimalFormat dft = new DecimalFormat(FORMAT_CODE);
        String code = dft.format(num);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(new Date()); // 格式化日期 date: 20200724
        /*System.out.println(codeEnd);*/
        return PRE_CODE + date + code;
    }

    /**
     * 获取流水号
     */
    private final static String FORMAT_CODE_GOODS = "00000000000";
    private final static String PRE_CODE_SY = "GOOD";
    private final static String PRE_CODE_MT = "RAW";
    private final static String PRE_CODE_SG = "SEMI";
    private final static String PRE_CODE_WORKER = "WORKER";
    private final static String PRE_CODE_GENERAL = "GENERAL";
    private final static String PRE_CODE_IN = "IN";
    private final static String PRE_CODE_OUT = "OUT";
    private final static String PRE_CODE_OTHER = "OTHER";

    public static String getserialNumber(Integer num, Integer type){
        DecimalFormat dft = new DecimalFormat(FORMAT_CODE_GOODS);
        String code = dft.format(num);
        if (type==1){
            return PRE_CODE_SY + code;
        } else if (type==2) {
            return PRE_CODE_MT + code;
        } else if (type==3) {
            return PRE_CODE_SG + code;
        } else if (type==4) {
            return PRE_CODE_WORKER + code;
        }else {
            return PRE_CODE_GENERAL + code;
        }
    }

    public static String getInOrOutNumber(Integer num, Integer type){
        DecimalFormat dft = new DecimalFormat(FORMAT_CODE_GOODS);
        String code = dft.format(num);
        if (type==1){
            return PRE_CODE_IN + code; //入库
        } else if (type==2) {
            return PRE_CODE_OUT + code; //出库
        } else {
            return PRE_CODE_OTHER + code; //其他
        }
    }

    public static String userAgent(String agent) {
        String[] split = agent.split("\\(")[1].split("\\)");
        return split[0];
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + units[digitGroups];
    }


    public static void main(String[] args) {
        String str = "12345678901234567890.jpg";
//        System.out.println(str.substring(str.lastIndexOf('.')));
        System.out.println(str.substring(str.length()-8));
    }
}

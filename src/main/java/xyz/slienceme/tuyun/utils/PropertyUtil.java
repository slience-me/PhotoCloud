package xyz.slienceme.tuyun.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @Author slience_me
 * @Time : 2021/7/12  16:01
 */

/**
 * 读取classpath下 指定的properties配置文件
 */
public class PropertyUtil {

    /**
     * 读取 classpath 下 指定的properties配置文件，加载到Properties并返回Properties
     *
     * @param name 配置文件名，如：mongo.properties
     */
    private static Properties getConfig(String name) {
        Properties props = null;
        try {
            props = new Properties();
            InputStream in = PropertyUtil.class.getClassLoader().getResourceAsStream(name);
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            props.load(bf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }

    //获取配置文件的某个参数
    private static String getPropValue(Properties prop, String key) {
        if (key == null || "".equals(key.trim())) {
            return null;
        }
        String value = prop.getProperty(key);
        if (value == null) {
            return null;
        }
        value = value.trim();
        //判断是否是环境变量配置属性,例如 server.env=${serverEnv:local}
        if (value.startsWith("${") && value.endsWith("}") && value.contains(":")) {
            //获取冒号的下标 ${serverEnv:local}
            int indexOfColon = value.indexOf(":");
            //${   serverEnv:local} 去除前两个符号
            String envName = value.substring(2, indexOfColon);
            //获取系统环境变量 envName 的内容，如果没有找到，则返回defaultValue
            String envValue = System.getenv(envName);
            if (envValue == null) {
                //配置的默认值 冒号到最后
                return value.substring(indexOfColon + 1, value.length() - 1);
            }
            return envValue;

        }
        return value;
    }

    /**
     * 静态方法类方法，获取配置文件的某个参数
     *
     * @param propertiesName 配置类对象
     * @param valueName      属性key值
     * @return 配置文件value值
     */
    public static String getValue(String propertiesName, String valueName) {
        Properties prop = PropertyUtil.getConfig(propertiesName);
        return PropertyUtil.getPropValue(prop, valueName);
    }
}

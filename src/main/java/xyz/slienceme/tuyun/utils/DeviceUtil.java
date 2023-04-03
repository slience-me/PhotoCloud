/**
 * @title DeviceUtil
 * @description wms-parent
 * @author slience_me
 * @version 1.0.0
 * @since 2023/1/4 17:54
 */
package xyz.slienceme.tuyun.utils;

import org.springframework.mobile.device.Device;

public class DeviceUtil {
    public static String getdevice(Device device){

        if (device.isMobile()) {
            //System.out.println("========请求来源设备是手机！========");
            return "手机登录";
        } else if (device.isTablet()) {
            //System.out.println("========请求来源设备是平板！========");
            return "平板登录";
        } else if(device.isNormal()){
            //System.out.println("========请求来源设备是PC！========");
            return "电脑登录";
        }else {
            //System.out.println("========请求来源设备是其它！========");
            return "其他登录";
        }
    }
}

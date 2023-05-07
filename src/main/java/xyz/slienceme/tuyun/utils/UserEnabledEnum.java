package xyz.slienceme.tuyun.utils;

public enum UserEnabledEnum {

    //设置常量
    DEACTIVATE(1, "未激活"),
    ACTIVATE(2, "已激活"),
    FORBIDDEN(3, "已封禁"),
    MAINTAINED(4, "维护中");

    private int code;//响应码
    private String desc;//描述description、内容

    //枚举的构造方法。
    UserEnabledEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    //遍历所有常量，返回与index对应的常量
    public static UserEnabledEnum stateOf(int index){
        for(UserEnabledEnum state : values()){
            if (state.getCode() == index){
                return state;
            }
        }
        return null;
    }
}

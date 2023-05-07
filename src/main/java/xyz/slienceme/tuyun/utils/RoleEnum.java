package xyz.slienceme.tuyun.utils;

public enum RoleEnum {

    //设置常量
    ADMIN(1, "管理员"),
    USER(2, "用户");

    private int type;//响应码
    private String desc;//描述description、内容

    //枚举的构造方法。
    RoleEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    //遍历所有常量，返回与index对应的常量
    public static RoleEnum stateOf(int index){
        for(RoleEnum state : values()){
            if (state.getType() == index){
                return state;
            }
        }
        return null;
    }
}

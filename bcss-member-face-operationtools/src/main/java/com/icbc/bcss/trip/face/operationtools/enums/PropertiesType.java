package com.icbc.bcss.trip.face.operationtools.enums;

public enum PropertiesType {

    PROPERTIES_VALUE_SOURCE_DEFINE("4001","属性值来源于动态参数文件"),
    PROPERTIES_VALUE_SOURCE_APPLICATION("4002","属性值来源于静态参数文件");

    String code;
    String desc;

    PropertiesType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

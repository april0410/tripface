package com.icbc.bcss.trip.face.operationtools.enums;

@Deprecated
public enum  InitMsgType {
    bcss_default_industry("bcss.default.industry","本地人脸库动态维护的行业场景标识符不存在，请检查"),
    bcss_init_delete_time("bcss.init.delete.time","本地人脸库动态维护的人脸删除信息初始同步时间不存在，请检查");

    String code;
    String desc;

    InitMsgType(String code, String desc) {
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

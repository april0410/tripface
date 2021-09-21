package com.icbc.bcss.trip.face.operationtools.enums;

public enum FaceRegisterType {

    FACE_REGISTER_SERVICE_RETURN_EMPTY("1001","调用人脸注册接口后返回结果为空"),
    FACE_REGISTER_SERVICE_RETURN_EXCEPTION("1002","调用人脸注册接口出错并抛出异常"),

    FACE_Query_SERVICE_RETURN_EMPTY("1003","调用人脸查询接口后返回结果为空"),
    FACE_Query_SERVICE_RETURN_EXCEPTION("1004","调用人脸查询接口出错并抛出异常"),

    FACE_UPDATE_SERVICE_RETURN_EMPTY("1005","调用人脸更新接口后返回结果为空"),
    FACE_UPDATE_SERVICE_RETURN_EXCEPTION("1006","调用人脸更新接口出错并抛出异常"),

    FACE_BIND_GROUP_SERVICE_RETURN_EMPTY("1007","绑定人脸到自定义库接口后返回结果为空"),
    FACE_BIND_GROUP_SERVICE_RETURN_EXCEPTION("1008","绑定人脸到自定义库接口出错并抛出异常");
    String code;
    String desc;

    FaceRegisterType(String code, String desc) {
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

package com.icbc.bcss.trip.face.operationtools.enums;

public enum FaceDeleteType {

    FACE_DELETE_SERVICE_RETURN_EMPTY("2001","调用人脸删除接口后返回结果为空"),
    FACE_DELETE_SERVICE_RETURN_EXCEPTION("2002","调用人脸删除接口出错并抛出异常");

    String code;
    String desc;

    FaceDeleteType(String code, String desc) {
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

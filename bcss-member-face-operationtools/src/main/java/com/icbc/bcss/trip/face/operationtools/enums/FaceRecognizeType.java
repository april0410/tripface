package com.icbc.bcss.trip.face.operationtools.enums;

public enum FaceRecognizeType {

    FACE_RECOGNIZE_SERVICE_RETURN_EMPTY("3001","调用人脸识别接口后返回结果为空"),
    FACE_RECOGNIZE_SERVICE_RETURN_EXCEPTION("3002","调用人脸识别接口出错并抛出异常");

    String code;
    String desc;

    FaceRecognizeType(String code, String desc) {
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

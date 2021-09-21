package com.icbc.bcss.trip.face.operationtools.enums;

public enum FaceOperType {
    OPERTYPE_ONE_FACE_DETAIL("1","获取人脸明细"),//需要考虑后端是否开启当前企业的下载人脸权限，以及默认下载会员人脸，还是全行业人脸
    OPERTYPE_TWO_FACE_COUNT("2","获取人脸数量"),//需要考虑后端是否开启当前企业的下载人脸权限，以及默认下载会员人脸，还是全行业人脸
    OPERTYPE_SEVEN_FACE_DELETE_DETAIL("7","获取删除人脸明细"),//需要考虑后端默认下载会员人脸，还是全行业人脸
    OPERTYPE_EIGHT_FACE_DELETE_COUNT("8","获取删除人脸数量");//需要考虑后端默认下载会员人脸，还是全行业人脸

    String code;
    String desc;

    FaceOperType(String code, String desc) {
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

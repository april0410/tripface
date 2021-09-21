package com.icbc.bcss.trip.face.operationtools.enums;

public enum FailMessage {
    GROUP_INFO_ADD_FAILED_LOCALDATABASE("100001","云从服务器已经建立特征分库，本地新增特征分库信息失败"),
    GROUP_INFO_QUERY_ISLASTPAGE_FALSE("100002","当前分页查询结果并不是最后一页，可以查询下一页"),

    ORDER_UPLOAD_RESULT_FIRST_FAILED("200001","本地离线订单上送云端结果:初次上送的部分订单存在失败情况"),
    ORDER_UPLOAD_RESULT_SECOND_FAILED("200002","本地离线订单上送云端结果:第二次上送的部分订单存在失败情况"),
    ORDER_UPLOAD_RESULT_TWICE_FAILED("200003","本地离线订单上送云端结果:两次上送的部分订单|都|存在失败情况");

    String code;
    String decs;

    FailMessage(String code, String decs) {
        this.code = code;
        this.decs = decs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDecs() {
        return decs;
    }

    public void setDecs(String decs) {
        this.decs = decs;
    }
}

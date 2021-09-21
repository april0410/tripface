package com.icbc.bcss.trip.face.operationtools.enums;

public enum OrderType {
    ORDER_UPLOAD_WAITING("0","订单待上送"),
    ORDER_UPLOAD_SUCC("1","订单上送成功");


    String code;
    String decs;

    OrderType(String code, String decs) {
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

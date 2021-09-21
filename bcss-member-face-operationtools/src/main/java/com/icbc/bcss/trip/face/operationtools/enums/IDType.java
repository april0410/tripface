package com.icbc.bcss.trip.face.operationtools.enums;

public enum IDType {
    IDTYPE_IDCARD("0","居民身份证");
//    GROUP_INFO_QUERY_ISLASTPAGE_FALSE("100002","当前分页查询结果并不是最后一页，可以查询下一页");


    String code;
    String decs;

    IDType(String code, String decs) {
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

package com.icbc.bcss.trip.face.operationtools.enums;

public enum RecordInfoType {
    SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL("1","云端到本地批量操作"),
    SORT_RESULTDESC_LOCAL_TO_LOCAL_FAIL("2","本地到本地单个操作"),

    INFO_TYPE_REGISTER_FACE_API_FAIL("1","注册API调用失败"),
    INFO_TYPE_REGISTER_FACE_DEAL_DETAIL_FAIL("2","注册人脸失败具体名单"),
    INFO_TYPE_DELETE_FACE_API_FAIL("3","删除API调用失败"),
    INFO_TYPE_DELETE_FACE_DEAL_DETAIL_FAIL("4","删除人脸失败具体名单"),
    INFO_TYPE_REGISTER_FACE_RANGE_DETAIL_FAIL("5","单次指定范围注册人脸失败明细"),
    INFO_TYPE_DELETE_FACE_RANGE_DETAIL_FAIL("6","单次指定范围删除人脸失败明细"),

    DEALSTATUS_DESC_WAITING("0","待处理"),
    DEALSTATUS_DESC_SUCC("1","处理成功");

    String code;
    String decs;

    RecordInfoType(String code, String decs) {
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

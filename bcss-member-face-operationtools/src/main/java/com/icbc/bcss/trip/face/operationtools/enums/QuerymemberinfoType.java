package com.icbc.bcss.trip.face.operationtools.enums;

public enum QuerymemberinfoType {
    REQUEST_FACE_DATA_SUCC("0","获取人脸数据成功"),
    PARAM_CHECK_CORP_ID_EMPTY("901","参数企业编号corpId为空"),
    PARAM_CHECK_TIMESTAMP_EMPTY("902","参数时间戳timeStamp为空"),
    PARAM_CHECK_CLIENTTRANSNO_EMPTY("903","参数交易流水号clientTransNo为空"),
    PARAM_CHECK_OPERTYPE_EMPTY("904","参数查询方式operType为空"),
    PARAM_CHECK_FEATURETYPE_EMPTY("905","参数特征类型featureType为空"),
    PARAM_CHECK_STARTDATE_EMPTY("906","参数开始日期startDate为空"),
    PARAM_CHECK_ENDDATE_EMPTY("907","参数结束日期endDate为空"),
    PARAM_CHECK_CHANNEL_EMPTY("908","参数渠道channel为空"),
    PARAM_CHECK_STARTNUM_EMPTY("909","参数开始位置startNum为空"),
    PARAM_CHECK_ENDNUM_EMPTY("910","参数结束位置endNum为空"),
    PARAM_CHECK_OPERINFO_EMPTY("911","参数查询详情operInfo为空"),
    PARAM_CHECK_TIMESTAMP_ILLEGAL("912","参数timeStamp日期格式不合法"),
    PARAM_CHECK_STARTDATE_ILLEGAL("913","参数startDate日期格式不合法"),
    PARAM_CHECK_ENDDATE_ILLEGAL("914","参数endDate日期格式不合法"),
    PARAM_CHECK_STARTNUM_ILLEGAL("915","参数startNum数字格式合法"),
    PARAM_CHECK_ENDNUM_ILLEGAL("916","参数endNum数字格式不合法"),


    EXCEPTION_OCCUR_DATE_FROM_FACE_DETAIL("exception_occur_date","获取人脸明细数据异常发生时间"),
    EXCEPTION_OCCUR_DATE_FROM_FACE_COUNT("exception_occur_date_with_count","获取人脸数量数据异常发生时间"),
    EXCEPTION_OCCUR_DATE_FROM_FACE_DELETE_DETAIL("exception_occur_delete_date","获取人脸删除明细数据异常发生时间"),
    EXCEPTION_OCCUR_DATE_FROM_FACE_DELETE_COUNT("exception_occur_delete_date_with_count","获取人脸删除信息数量数据异常发生时间"),
//    设备关联位置状态：[关闭]，不校验通行权限
//    人员不存在且非会员卡用户
//    PERSON_NOT_EXIST("112","人员不存在"),
//    CORPID_EMPTY("113","企业编号不能为空"),
//    MANUFACTURE_EMPTY("114","厂商标识不能为空"),
//    MANUFACTURE_NOT_EXIST("115","厂商标识不存在"),
//    TIMESTAMP_EMPTY("116","时间戳不能为空"),
//    TIMESTAMP_FORMAT_ERROR("117","时间戳格式错误"),
//    PROTOCOLTYPE_EMPTY("118","报文类型不能为空"),
//    PROTOCOLTYPE_ERROR("119","报文类型错误"),
//    CIENT_TRANSNO_EMPTY("120","交易流水号不能为空"),
//    CIENT_TRANSNO_DUPLICATE("121","交易流水号重复"),
//    CLIENT_NO_EMPTY("122","设备编号不能为空"),
//    INOUTDIRECT_EMPTY("123","通行方向不能为空"),
//    INOUTDIRECT_ERROR("124","通行方向至非法"),
//    ID_INFORMATION_EMPTY("125","身份信息不能为空"),
//    AUTH_BY_PERSONID_NEED_IMAGE("126","通行报文类型为010时，otherData字段需要上送人脸"),
//    ONLINE_QRCODE_REJUST("127","当前为本地权限校验方式，联机二维码无法校验"),
//    OFFLINE_QRCODE_TO_PUBLIC_KEY_NULL("128","当前为本地权限校验方式，离线二维码解密的公钥为空"),
//    OFFLINE_QRCODE_TO_AES_KEY_NULL("129","当前为本地权限校验方式，离线二维码解密的AES为空"),
//    ACCESS_UPDATA_EMPTY("130","上送的通行凭证参数为空"),
//    ENVIRONMENT_PARAM_EMPTY_FOR_PAYPUBLICKEY("131","默认配置文件中缺乏支付公钥的配置"),
//    ENVIRONMENT_PARAM_EMPTY_FOR_AESKEY("132","默认配置文件中缺乏AES密钥的配置"),
    OFFLINE_QRCODE_REJUST("133","当前为联机权限校验方式，离线二维码无法校验");

    String code;
    String decs;

    QuerymemberinfoType(String code, String decs) {
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

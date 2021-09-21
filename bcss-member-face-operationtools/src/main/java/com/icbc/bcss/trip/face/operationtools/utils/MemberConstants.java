package com.icbc.bcss.trip.face.operationtools.utils;

import java.io.File;

public class MemberConstants {
    public static final String COMMON_SEPATATOR="_";
    public static final String DEFAULT_GROUP_SEQNO="1";

    public static final String RETURN_CODE="return_code";
    public static final String RETURN_MSG="return_msg";
    public static final String SERIAL_NO="serial_no";
    public static final String CLIENTTRANSNO="clientTransNo";

    public static final String CLOUDWALK_CLIENTTRANSNO="cloudwalk_clientTransNo";

    public static final String PROPERTIES_VALUE="properties_value";
    public static final String PROPERTIES_VALUE_SOURCE="properties_values_source";

    //普通成功的交易码
    public static final String COMMON_SUCCESS="0";
    //普通失败的交易码
    public static final String COMMON_FAIL="1";
    //普通异常的交易码
    public static final String COMMON_EXCEPTION="-1";
    public static final String COMMON_ICBCAPI_EXCEPTION="-2";
    public static final String COMMON_OPERTYPE_EXCEPTION="-3";
    //每次循环，api请求人脸信息的数理
    public static final long DEFAULT_INCREASE_STEP_FROM_FACE=10;

    //人脸图片大小的最大值最大值限制，单位是：字节(Byte)
    public static final int FACE_IMAGE_SIZE_MAX_VALUE=150*1024;
    //人脸图片大小的最大值最小值限制，单位是：字节(Byte)
    public static final int FACE_IMAGE_SIZE_MIN_VALUE=20*1024;

    public static final String FILEPACKET_NAME="failIMG";
    public static final String DEFAULT_IMAGE_PATH=System.getProperty("user.dir")+File.separator+FILEPACKET_NAME;

    /*调用方标识*/
    public static final String FACE_SINGLE_REGISTER_MODEL_NAME ="faceSingleRegisterModelName";
    public static final String FACE_SINGLE_DELETE_MODEL_NAME ="faceSingleDeleteModelName";

    //商品产品的长度限制
    public static final int MAX_LENGTH_PERSONUNIQUECODE=32;
    public static final int MAX_LENGTH_BCSSPERSONID=20;

    //订单api中主要字段的长度限制
    public static final int MAX_LENGTH_CLIENTTRANSNO=32;
    public static final int MAX_LENGTH_MANUFACTURERID=10;
    public static final int MAX_LENGTH_IMEINO=20;
    public static final int MAX_LENGTH_TERMINALNO=35;
    public static final int MAX_LENGTH_CORPID=10;
    public static final int MAX_LENGTH_CORGNO=12;
    public static final int MAX_LENGTH_MERORDERNO=60;
    public static final int MAX_LENGTH_MERNO=20;
    public static final int MAX_LENGTH_TIMESTAMP=19;
    public static final int MAX_LENGTH_CLIENTTYPE=3;
    public static final int MAX_LENGTH_TRADEMODE=3;
    public static final int MAX_LENGTH_MSGFLAG=3;
    public static final int MAX_LENGTH_OPTIONTYPE=3;
    public static final int MAX_LENGTH_INPUTVALUE=50;
    public static final int MAX_LENGTH_INDUSTRY=3;
    public static final int MAX_LENGTH_FACESIM = 6;
    public static final int MAX_LENGTH_PAYTYPE = 3;
    public static final int MAX_LENGTH_FEATURE_CORGNO=12;
    public static final int MAX_LENGTH_FEATURE_NO=30;
    public static final int MAX_LENGTH_DATASRC=3;
    public static final int MAX_LENGTH_ORDERSRC=3;
    public static final int MAX_LENGTH_DEFINED_AMT_FLAG=3;
    //订单支付凭证的字段长度
    public static final int MAX_LENGTH_FEATURE_TYPE=10;
//    public static final int MAX_LENGTH_FEATURENO=30;
//    public static final int MAX_LENGTH_FEATURECORGNO=12;

    //商品产品的长度限制
    public static final int MAX_LENGTH_GOODSID=20;
    public static final int MAX_LENGTH_GOODSNAME=100;
    public static final int MAX_LENGTH_GOODSDETAIL=100;
    public static final int MAX_LENGTH_GOODSTYPE=20;
    public static final int MAX_LENGTH_GROUPNO=32;
    public static final int MAX_LENGTH_DISTRIBUTEDATE=20;
    //优惠券的长度
    public static final int MAX_LENGTH_COUPONNO=32;
    public static final int MAX_LENGTH_COUPONTYPE=3;
    public static final int MAX_LENGTH_COUPONDETAIL=100;

    /*长度*/
    public static final String PERSONID_FULL_DEFAULT="00000000000000000000";
    public static final int MAX_LENGTH_PERSONID=18;
    public static final int MAX_LENGTH_SEQNO=2;
    public static final int MAX_LENGTH_MEMCARDN0=20;
    public static final int MAX_LENGTH_REG_DATA_SOURCE=3;//
    public static final int MAX_LENGTH_REG_THREE_INDIFIED_INFO=32;//身份证三要素长度
    public static final int MAX_LENGTH_FEATURE_SEQNO=10;
    public static final int MAX_LENGTH_SEX=3;
    public static final int MAX_LENGTH_BIRTHDAY=10;

    //云从注册字段通用长度
    public static final int MAX_LENGTH_COMMON_CLOUDWALK_32=32;
    public static final int MAX_LENGTH_COMMON_CLOUDWALK_64=64;
    public static final int MAX_LENGTH_COMMON_CLOUDWALK_255=255;

    //PersonId和seqNo的分隔符
    public static final String DEFAULT_SEPARATOR_FROM_PERSONID_TO_SEQNO="_";

    //y异常时间的分隔符，开始时间与结束时间用+连接，两个时间则通过,连接
    public static final String DEFAULT_CONNECTION_SYMBOL="+";
    public static final String DEFAULT_CONNECTION_SYMBOL_WITHTIME=",";
    public static final String EMPTY_STRING_SYMBOL="";

    //云从的返回字段
    public static final String RESPCODE="respCode";
    public static final String RESPDESC="respDesc";

    //初始化程序时，当前时间-固定时间=开始索引时间
    public static final long REGISTER_STEP=3*24*60*60*1000;
    public static final long REGISTER_STEP_ONEDAY =1*24*60*60*1000;

    //默认所有数据的最原始的开始日期
    public static final String ORIGINAL_START_DATE_DEFAULT ="2019-01-01";

    //MSG--信息集合
    public static final String RETURN_MSG_DEVELOPING_FUNCTION="功能服务正在开发，请后续使用";

    //实体模型的key
    public static final String MEM_ORDER_INFO_MODEL="memOrderInfoModel";
    public static final String MEM_ORDER_PAY_FACE_LOG_MODEL="memOrderPayFaceLogModel";
    public static final String MEM_ORDER_PROD_INFO_MODEL_LIST ="memOrderProdInfoModelList";
    public static final String MEM_ORDER_COUPON_INFO_MODEL_LIST ="memOrderCouponInfoModelList";


    //记录请求失败的文件前缀名称
    public static final String REGISTER_REQUEST_API_FAILED_PRENAME="registerAPIFailed";
    public static final String DELETE_REQUEST_API_FAILED_PRENAME="deleteAPIFailed";
    public static final String RECORD_FILE_SUFFIXNAME=".txt";

    //记录请求云从处理失败的客户信息的文件前缀名称
    public static final String REGISTER_DETAIL_INFO_FAILED_PRENAME="registerDetailInfoFailed";
    public static final String DELETE_DETAIL_INFO_FAILED_PRENAME="registerDetailInfoFailed";

    //固定属性名
    public static final String KEY_CORP_ID="corpId";
    public static final String KEY_CORP_NAME="corpName";

    /*
    * 云从
    * */
    //云从接口--固定属性名
    public static final String KEY_CLOUDWALK_GROUP_CODE="groupCode";
    public static final String KEY_CLOUDWALK_GROUP_NAME="groupName";
    public static final String KEY_CLOUDWALK_CHANNEL_CODE="channelCode";
    //交易属性名
    public static final String KEY_RESPONSE_CODE="code";
    public static final String KEY_RESPONSE_MESSAGE="message";
    public static final String KEY_RESPONSE_DATA="data";
    public static final String KEY_RESPONSE_FLOWNUM="flowNum";
    //云从接口--固定成功交易代码
    public static final String VALUE_FULL_SUCC="00000000";

    /*对应云从证件类型字典
    * */
    public static final String MEMBER_CLOUDWALK_CERT_TYPE_STR="bcss.default.cloudwalk.certificateType_";

}

package com.icbc.bcss.trip.face.operationtools.api;

import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.enums.FaceOperType;
import com.icbc.bcss.trip.face.operationtools.enums.QuerymemberinfoType;
import com.icbc.bcss.trip.face.operationtools.responsemodel.BcssMemberFeatureQuerymemberinfoResponseV1;
import com.icbc.bcss.trip.face.operationtools.utils.CommonMapReturn;
import com.icbc.bcss.trip.face.operationtools.utils.MemberConstants;
import com.icbc.bcss.trip.face.operationtools.utils.PropertyPlaceholder;
import com.icbc.bcss.trip.face.operationtools.utils.TransNoUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("bcssMemberFeatureService")
public class BcssMemberFeatureServiceImpl implements BcssMemberFeatureService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BcssMemberFeatureServiceImpl.class);

    @Autowired
    private BcssMemberFeatureQuerymemberinfoService bcssMemberFeatureQuerymemberinfoService;

    @Autowired
    private PropertyPlaceholder environment;
    @Override
    public Map<String, Object> execute(Map<String, Object> param) {
        String clientTransNo= (String) param.get("clientTransNo");
        LOGGER.error("-------------"+"BcssMemberFeatureServiceImpl.exectue is start:"+clientTransNo+"-------------------");

        //检测参数
        Map<String,Object> checkRes=checkParam(param);
        String  checkCode= (String) checkRes.get(MemberConstants.RETURN_CODE);
        if (checkCode==null||checkCode.isEmpty()||!checkCode.equals(MemberConstants.COMMON_SUCCESS)){
            LOGGER.error("参数校验失败，返回失败校验结果");
            return checkRes;
        }

        LOGGER.debug("check param and result of BcssMemberFeatureServiceImpl :" + JSONObject.toJSONString(param)+"  ||  "+JSONObject.toJSONString(checkRes));

//        判断 具体的分支
        HashMap<String,Object> returnMap=new HashMap<>();
        String operType= (String) param.get("operType");
        if(FaceOperType.OPERTYPE_ONE_FACE_DETAIL.getCode().equals(operType)){//operType=1
            //调用方法下载人脸明细
            Map<String,Object> request=executeUpdateFaceDetail(param);
            returnMap.putAll(request);

        }else if(FaceOperType.OPERTYPE_TWO_FACE_COUNT.getCode().equals(operType)){//operType=2
            //调用方法下载人脸数量
            Map<String,Object> request=executeUpdateFaceDetail(param);
            returnMap.putAll(request);
            System.out.println("请求人脸返回的数据："+JSONObject.toJSONString(returnMap));
        }else if(FaceOperType.OPERTYPE_SEVEN_FACE_DELETE_DETAIL.getCode().equals(operType)){//operType=7
            //调用方法下载人脸删除明细
            Map<String,Object> request=executeDeleteFaceDetail(param);
            returnMap.putAll(request);

        }else if(FaceOperType.OPERTYPE_EIGHT_FACE_DELETE_COUNT.getCode().equals(operType)){ //operType=8
            //调用方法下载人脸删除数量
            Map<String,Object> request=executeDeleteFaceDetail(param);
            returnMap.putAll(request);
            System.out.println("请求人脸返回的数据："+JSONObject.toJSONString(returnMap));
        }else {
            String msg="当前查询方法operType的值为非合法指定值，请重新上送";
            LOGGER.error(msg);
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL,msg,clientTransNo);
        }

        //最后返回的报文
        LOGGER.error("-------------"+"BcssMemberFeatureServiceImpl.exectue  is end:"+clientTransNo+"-------------------");
        return returnMap;
    }
//////////////////////////////////////////////////////////////////////
    @Override
    /*
    *operType=1和2
    * 处理请求人脸api返回参数
    * 转换returnCode为return_code等参数
    * */
    public Map<String, Object> executeUpdateFaceDetail(Map<String, Object> param) {
        Map<String,Object> res=new HashMap<>();

        String clientTransNo= (String) param.get("clientTransNo");
        BcssMemberFeatureQuerymemberinfoResponseV1 responseResult=bcssMemberFeatureQuerymemberinfoService.requestFaceData(param);//获取注册以及更新人脸信息，
        //转换returnCode为return_code等参数
//        String res=JSONObject.toJSONString(responseResult);
        if(responseResult==null){
            LOGGER.error("请求回来的响应参数体为空，请检查："+JSONObject.toJSONString(responseResult));
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL,"请求回来的响应参数体为空，请检查",clientTransNo);
        }

        if( 0!=responseResult.getReturnCode() ){
            LOGGER.error("请求失败，原因为："+responseResult.getReturnMsg());
            res.put(MemberConstants.RETURN_CODE,String.valueOf(responseResult.getReturnCode() ));
            res.put(MemberConstants.RETURN_MSG,responseResult.getReturnMsg());
            res.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
        }else{
            res.put(MemberConstants.RETURN_CODE,String.valueOf(responseResult.getReturnCode() ));
            res.put(MemberConstants.RETURN_MSG,responseResult.getReturnMsg());
            res.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
            res.put("memCount",responseResult.getMemCount());
            res.put("dataList",responseResult.getDataList());
        }

        return res;
    }

    /*operType=7和8*/
    @Override
    public Map<String, Object> executeDeleteFaceDetail(Map<String, Object> param) {
        String clientTransNo= (String) param.get("clientTransNo");
        BcssMemberFeatureQuerymemberinfoResponseV1 responseResult=bcssMemberFeatureQuerymemberinfoService.requestFaceData(param);//获取注册以及更新人脸信息，
        //转换returnCode为return_code等参数
//        String res=JSONObject.toJSONString(responseResult);
//        if(res==null||res.isEmpty()){
//            LOGGER.error("请求回来的响应参数体为空，请检查："+res);
//            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL,"请求回来的响应参数体为空，请检查",clientTransNo);
//        }

//        HashMap result=JSONObject.parseObject(res,HashMap.class);
//        if( ((String)result.get(MemberConstants.RETURN_CODE))==null ||  ((String)result.get(MemberConstants.RETURN_CODE)).isEmpty() ){
//            result.put(MemberConstants.RETURN_CODE,String.valueOf(result.get("returnCode")));
//        }
//        if( ((String)result.get(MemberConstants.RETURN_MSG))==null ||  ((String)result.get(MemberConstants.RETURN_MSG)).isEmpty() ){
//            result.put(MemberConstants.RETURN_MSG,(String)result.get("returnMsg"));
//        }

        if(responseResult==null){
            LOGGER.error("请求回来的响应参数体为空，请检查："+JSONObject.toJSONString(responseResult));
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL,"请求回来的响应参数体为空，请检查",clientTransNo);
        }

        if( 0!=responseResult.getReturnCode() ){
            LOGGER.error("请求失败，原因为："+responseResult.getReturnMsg());
            return CommonMapReturn.constructMapWithClientTransNo(String.valueOf(responseResult.getReturnCode()),responseResult.getReturnMsg(),clientTransNo);
        }else{
//            return CommonMapReturn.constructMapWithClientTransNo(String.valueOf(responseResult.getReturnCode()),responseResult.getReturnMsg(),clientTransNo);
            Map<String,Object> res=new HashMap<>();
            res.put(MemberConstants.RETURN_CODE,String.valueOf(responseResult.getReturnCode() ));
            res.put(MemberConstants.RETURN_MSG,responseResult.getReturnMsg());
            res.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
            res.put("memCount",responseResult.getMemCount());
            res.put("dataList",responseResult.getDataList());
            return res;
        }
    }


   /* 检测参数*/
    public Map<String, Object> checkParam(Map<String,Object> param){
        //判断参数
        String corpId= (String) param.get("corpId");
        String timeStamp=(String) param.get("timeStamp");
        String operType= (String) param.get("operType");
        String featureType=(String) param.get("featureType");
        String startDate=(String) param.get("startDate");
        String endDate=(String) param.get("endDate");
        String channel=(String) param.get("channel");
        String startNum=(String) param.get("startNum");
        String endNum=(String) param.get("endNum");
        String operInfo=(String) param.get("operInfo");
        String clientTransNo= (String) param.get("clientTransNo");

        Map<String,Object> returnMap=new HashMap<String,Object>();
        if(corpId==null||corpId.equals("")){
            LOGGER.error(QuerymemberinfoType.PARAM_CHECK_CORP_ID_EMPTY.getDecs());
            returnMap.put(MemberConstants.RETURN_CODE, QuerymemberinfoType.PARAM_CHECK_CORP_ID_EMPTY.getCode());
            returnMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
            returnMap.put(MemberConstants.RETURN_MSG,QuerymemberinfoType.PARAM_CHECK_CORP_ID_EMPTY.getDecs());
            return returnMap;
        }

        if(timeStamp==null||timeStamp.equals("")){
            LOGGER.error(QuerymemberinfoType.PARAM_CHECK_TIMESTAMP_EMPTY.getCode());
            return CommonMapReturn.constructMapWithClientTransNo(QuerymemberinfoType.PARAM_CHECK_TIMESTAMP_EMPTY.getCode(),QuerymemberinfoType.PARAM_CHECK_TIMESTAMP_EMPTY.getDecs(),clientTransNo);
        }

        Date timeStampDate=TransNoUtils.getInstance().convertTimeStamp(timeStamp);
        if(timeStampDate==null ){
            LOGGER.error(QuerymemberinfoType.PARAM_CHECK_TIMESTAMP_ILLEGAL.getDecs());
            return CommonMapReturn.constructMapWithClientTransNo(QuerymemberinfoType.PARAM_CHECK_TIMESTAMP_ILLEGAL.getCode(),QuerymemberinfoType.PARAM_CHECK_TIMESTAMP_ILLEGAL.getDecs(),clientTransNo);
        }

        if(operType==null||operType.equals("")){
            LOGGER.error(QuerymemberinfoType.PARAM_CHECK_OPERTYPE_EMPTY.getDecs());
            return CommonMapReturn.constructMapWithClientTransNo(QuerymemberinfoType.PARAM_CHECK_OPERTYPE_EMPTY.getCode(),QuerymemberinfoType.PARAM_CHECK_OPERTYPE_EMPTY.getDecs(),clientTransNo);
        }

        if(featureType==null||operType.equals("")){
            LOGGER.error(QuerymemberinfoType.PARAM_CHECK_FEATURETYPE_EMPTY.getDecs());
            return CommonMapReturn.constructMapWithClientTransNo(QuerymemberinfoType.PARAM_CHECK_FEATURETYPE_EMPTY.getCode(),QuerymemberinfoType.PARAM_CHECK_FEATURETYPE_EMPTY.getDecs(),clientTransNo);
        }

        //校验开始时间和结束时间
        if(startDate==null||startDate.equals("")){
            LOGGER.error(QuerymemberinfoType.PARAM_CHECK_STARTDATE_EMPTY.getDecs());
            return CommonMapReturn.constructMapWithClientTransNo(QuerymemberinfoType.PARAM_CHECK_STARTDATE_EMPTY.getCode(),QuerymemberinfoType.PARAM_CHECK_STARTDATE_EMPTY.getDecs(),clientTransNo);
        }

        Date tempStartDate=TransNoUtils.getInstance().convertDate(startDate);
        if (tempStartDate==null){
            LOGGER.error(QuerymemberinfoType.PARAM_CHECK_STARTDATE_ILLEGAL.getDecs());
            return CommonMapReturn.constructMapWithClientTransNo(QuerymemberinfoType.PARAM_CHECK_STARTDATE_ILLEGAL.getCode(),QuerymemberinfoType.PARAM_CHECK_STARTDATE_ILLEGAL.getDecs(),clientTransNo);
        }

        if(endDate==null||endDate.equals("")){
            LOGGER.error(QuerymemberinfoType.PARAM_CHECK_ENDDATE_EMPTY.getDecs());
            return CommonMapReturn.constructMapWithClientTransNo(QuerymemberinfoType.PARAM_CHECK_ENDDATE_EMPTY.getCode(),QuerymemberinfoType.PARAM_CHECK_ENDDATE_EMPTY.getDecs(),clientTransNo);
        }
        Date tempEndDate=TransNoUtils.getInstance().convertDate(endDate);
        if (tempEndDate==null){
            LOGGER.error(QuerymemberinfoType.PARAM_CHECK_ENDDATE_ILLEGAL.getDecs());
            return CommonMapReturn.constructMapWithClientTransNo(QuerymemberinfoType.PARAM_CHECK_ENDDATE_ILLEGAL.getCode(),QuerymemberinfoType.PARAM_CHECK_ENDDATE_ILLEGAL.getDecs(),clientTransNo);
        }

        //判断时间前后
        if(tempEndDate.getTime()<tempStartDate.getTime()){
            LOGGER.error("开始查询日期 大于 结束查询日期，请重新输入");
            returnMap.put(MemberConstants.RETURN_CODE, MemberConstants.COMMON_EXCEPTION);
            returnMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
            returnMap.put(MemberConstants.RETURN_MSG,"开始查询日期 大于 结束查询日期，请重新输入");
            return returnMap;
        }
//        //开始日期和结束日期都不能大于今天
//        if(tempStartDate.getTime()>System.currentTimeMillis()||tempEndDate.getTime()>System.currentTimeMillis()){
//            LOGGER.error("开始查询日期 或结束查询日期 大于当前日期，不合法，请重新输入");
//            returnMap.put(MemberConstants.RETURN_CODE, MemberConstants.COMMON_EXCEPTION);
//            returnMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
//            returnMap.put(MemberConstants.RETURN_MSG,"开始查询日期 或结束查询日期 大于当前日期，不合法，请重新输入");
//            return returnMap;
//        }

        //校验开始位置和结束位置
        if(startNum==null||startNum.equals("")){
            LOGGER.error(QuerymemberinfoType.PARAM_CHECK_STARTNUM_EMPTY.getDecs());
            return CommonMapReturn.constructMapWithClientTransNo(QuerymemberinfoType.PARAM_CHECK_STARTNUM_EMPTY.getCode(),QuerymemberinfoType.PARAM_CHECK_STARTNUM_EMPTY.getDecs(),clientTransNo);
        }
        if(endNum==null||endNum.equals("")){
            LOGGER.error(QuerymemberinfoType.PARAM_CHECK_ENDNUM_EMPTY.getDecs());
            return CommonMapReturn.constructMapWithClientTransNo(QuerymemberinfoType.PARAM_CHECK_ENDNUM_EMPTY.getCode(),QuerymemberinfoType.PARAM_CHECK_ENDNUM_EMPTY.getDecs(),clientTransNo);
        }

        try{
            BigDecimal tempStartNum=new BigDecimal(startNum);
            BigDecimal tempEndNum=new BigDecimal(endNum);
            if (tempStartNum.longValue()<0){
                LOGGER.error(QuerymemberinfoType.PARAM_CHECK_STARTNUM_ILLEGAL.getDecs());
                return CommonMapReturn.constructMapWithClientTransNo(QuerymemberinfoType.PARAM_CHECK_STARTNUM_ILLEGAL.getCode(),QuerymemberinfoType.PARAM_CHECK_STARTNUM_ILLEGAL.getDecs(),clientTransNo);
            }
            if (tempEndNum.longValue()<0){
                LOGGER.error(QuerymemberinfoType.PARAM_CHECK_ENDNUM_ILLEGAL.getDecs());
                return CommonMapReturn.constructMapWithClientTransNo(QuerymemberinfoType.PARAM_CHECK_ENDNUM_ILLEGAL.getCode(),QuerymemberinfoType.PARAM_CHECK_ENDNUM_ILLEGAL.getDecs(),clientTransNo);

            }

            //判断位置前后
            if(tempStartNum.compareTo(tempEndNum)==1){
                LOGGER.error("开始查询位置 大于 结束查询位置");
                returnMap.put(MemberConstants.RETURN_CODE, MemberConstants.COMMON_EXCEPTION);
                returnMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
                returnMap.put(MemberConstants.RETURN_MSG,"开始查询位置 大于 结束查询位置");
                return returnMap;
            }

        }catch (NumberFormatException e){
            LOGGER.error("startNum或者endNum数字转换操作异常",e);
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION,"startNum或者endNum数字转换操作异常",clientTransNo);
        }

        //channel和operInfo都可以为空，不校验


        //所有参数通过检查
        returnMap.put(MemberConstants.RETURN_CODE, MemberConstants.COMMON_SUCCESS);
        returnMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
        returnMap.put(MemberConstants.RETURN_MSG,"所有参数通过检查");
        return returnMap;
    }
}

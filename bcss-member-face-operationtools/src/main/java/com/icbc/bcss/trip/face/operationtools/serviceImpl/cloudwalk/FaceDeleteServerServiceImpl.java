package com.icbc.bcss.trip.face.operationtools.serviceImpl.cloudwalk;

import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.enums.FaceDeleteType;
import com.icbc.bcss.trip.face.operationtools.exception.BusinessException;
import com.icbc.bcss.trip.face.operationtools.http.HttpOperation;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemCloudwalkGroupPersonBindInfoBaseService;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemCloudwalkPersonRegisterFeatureDetailBaseService;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemCloudwalkPersonRegisterInfoBaseService;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceDeleteServerService;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceRegisterServerService;
import com.icbc.bcss.trip.face.operationtools.utils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

//@Service("faceDeleteServerService")
public class FaceDeleteServerServiceImpl implements FaceDeleteServerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FaceDeleteServerServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PropertyPlaceholder environment;

    @Autowired
    private FaceRegisterServerService faceRegisterServerService;

    @Autowired
    private BcssMemCloudwalkPersonRegisterInfoBaseService bcssMemCloudwalkPersonRegisterInfoBaseService;

    @Autowired
    private BcssMemCloudwalkPersonRegisterFeatureDetailBaseService bcssMemCloudwalkPersonRegisterFeatureDetailBaseService;

    @Autowired
    private BcssMemCloudwalkGroupPersonBindInfoBaseService bcssMemCloudwalkGroupPersonBindInfoBaseService;//绑定人脸信息

    @Override
    @Transactional(rollbackFor = Exception.class)//发生异常，回退数据库操作
    public Map<String, Object> deleteInServer(Map<String, Object> param) {
        //获取url
        String clientTransNo = (String) param.get(MemberConstants.CLIENTTRANSNO);
        String queryPersonUrl = environment.getProperty("bcss.default.face.query_URL");
        String deleteUrl = environment.getProperty("bcss.default.face.delete_URL");
        if (deleteUrl == null || deleteUrl.isEmpty() || queryPersonUrl == null || queryPersonUrl.isEmpty()) {
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "读取属性配置文件中，不存在人员删除、人员查询URL地址", clientTransNo);
        }

        //调用查询
        Map queryResult = faceRegisterServerService.queryDataOnly(param, clientTransNo, queryPersonUrl);

        //判断查询结果
        //如果存在，则删除，否则默认删除成功
        Map<String, Object> resParam = new HashMap<>();
        if (MemberConstants.VALUE_FULL_SUCC.equals(queryResult.get(MemberConstants.KEY_RESPONSE_CODE))) {
            resParam = deleteOnly(param, clientTransNo, deleteUrl, queryResult);
            return resParam;
        } else {
            String netQueryStatus= (String) queryResult.get("netQueryStatus");
            if("1".equals(netQueryStatus)){
                LOGGER.error("删除前查询人员信息结果未知，直接删除失败");
                return queryResult;

            }

            //为空或者为错误码，视为该人员不存在云从服务器，直接删除本地数据库
            LOGGER.error("查询人员信息--返回为空或者为错误码，视为人员不存在服务器，进行本地数据库删除");
            return deleteLocalDatabase(param, clientTransNo);
        }

    }

    /*
     * 函数：请求云从服务器删除人员记录+删除本地数据库的记录
     * */
    private Map<String, Object> deleteOnly(Map<String, Object> param, String clientTransNo, String deleteUrl, Map queryResult) {
        //参数构造
        Map<String, Object> request = new HashMap<>();
        try {
            Map<String, Object> res = getParamForDelete(param);
            if (res != null && res.size() > 0) {
                LOGGER.debug("人脸删除参数成功获取");
                //把传入的参数放入Request
                request.putAll(res);
            } else {
                throw new BusinessException("人脸删除参数获取为空");
            }
        } catch (BusinessException e) {
            LOGGER.error("人脸删除异常：" + e.toString());
            LOGGER.error(e.getMessage(), e);
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION, "人脸删除异常：" + e.getMessage(), clientTransNo);
        }

        //请求服务器
        LOGGER.error("人脸删除请求参数为：" + JSONObject.toJSONString(request));
        Map<String, Object> returnMap = new HashMap<>();
        try {
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(deleteUrl, HttpOperation.generatePostJson(request), String.class);
            String StringResponse = apiResponse.getBody();
            LOGGER.error("人脸删除后返回的请求体为：" + StringResponse);

            if (StringResponse == null || StringResponse.isEmpty()) {
                returnMap.put(MemberConstants.RETURN_CODE, FaceDeleteType.FACE_DELETE_SERVICE_RETURN_EMPTY.getCode());
                returnMap.put(MemberConstants.RETURN_MSG, FaceDeleteType.FACE_DELETE_SERVICE_RETURN_EMPTY.getDesc());
                returnMap.put(MemberConstants.CLIENTTRANSNO, clientTransNo);
                System.out.println("人脸删除调用返回空结果");
                return returnMap;
            } else {
                Map<String, Object> cloudReturnp = JSONObject.parseObject(StringResponse, Map.class);
                returnMap = FaceReturnConvertCommonReturn.converFieldName(cloudReturnp);
            }

        } catch (Exception e) {
            LOGGER.error("人脸删除接口调用抛出异常：" + e.toString());
            LOGGER.error(e.getMessage(), e);
            Map<String, Object> exceptionMap = new HashMap<>();
            exceptionMap.put(MemberConstants.RETURN_CODE, FaceDeleteType.FACE_DELETE_SERVICE_RETURN_EXCEPTION.getCode());
            exceptionMap.put(MemberConstants.RETURN_MSG, FaceDeleteType.FACE_DELETE_SERVICE_RETURN_EXCEPTION.getDesc());
            System.out.println("人脸删除接口调用抛出异常");
            exceptionMap.put(MemberConstants.CLIENTTRANSNO, clientTransNo);
            return exceptionMap;
        }
//        return null;
        //判断删除结果
        if (MemberConstants.VALUE_FULL_SUCC.equals(returnMap.get(MemberConstants.KEY_RESPONSE_CODE))) {
            //删除成功
            LOGGER.warn("当前服务器存在此人脸信息，服务器删除人员信息成功，准备删除本地数据库");
            return deleteLocalDatabase(param, clientTransNo);
        } else {
            //删除失败
            String msg = "前期查询表明：当前服务器存在此人脸信息，但是删除失败，原因：" + returnMap.get(MemberConstants.KEY_RESPONSE_MESSAGE);
            LOGGER.error(msg);
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION, "人脸删除异常：" + msg, clientTransNo);
        }

    }

    /*
     * 函数：单独删除本地数据库的记录
     * 包括：人员绑定自定义特征分库信息表、注册特征客户信息、注册特征明细信息
     * @param personUniqueFaceNo
     * */
    private Map<String, Object> deleteLocalDatabase(Map<String, Object> param, String clientTransNo) {
        String corpId = (String) param.get("corpId");
        String faceNo = (String) param.get("personUniqueFaceNo");
        String groupcode = corpId + MemberConstants.DEFAULT_SEPARATOR_FROM_PERSONID_TO_SEQNO + MemberConstants.DEFAULT_GROUP_SEQNO;

        //注册特征客户信息--操作
        //1.查询是否存在实体信息，2.如果不存在，走更新
        int registerInfoCount = bcssMemCloudwalkPersonRegisterInfoBaseService.deleteByCorpID2FaceID(corpId, faceNo);
        //异常结果必须要threw Exception，用于事务回滚；成功则正常返回
        //如果人员删除结果=1，则是服务器删除成功，本地不存在数据
        if (registerInfoCount >= 0) {
            LOGGER.info("registerInfoModel删除数据库成功");
        } else {
            LOGGER.error("registerInfoModel删除数据库异常,原因：删除记录数负数");
            throw new BusinessException("registerInfoModel删除数据库异常,原因：删除记录数负数");
        }


        //注册特征明细信息表--操作
        //首先删除corpId和faceno下的所有特征记录，不需要判断删除结果，删除多条或者0条都是为成功
        int recCount = bcssMemCloudwalkPersonRegisterFeatureDetailBaseService.deleteByCorpId2FaceId(corpId, faceNo);//1：注册照
        LOGGER.error("删除bcssMemCloudwalkPersonRegisterFeatureDetail记录数量：" + recCount);
        //异常结果必须要threw Exception，用于事务回滚；成功则正常返回
        if (recCount >= 0) {
            LOGGER.info("registerFeatureDetailModelList删除数据库成功");
            //如果人员查询结果为全0，则是服务器查询成功，本地不存在数据
        } else {
            LOGGER.error("registerFeatureDetailModelList删除数据库异常，原因：删除记录数负数");//不管服务器是更新还是注册
            throw new BusinessException("registerFeatureDetailModelList删除数据库异常，原因：删除记录数负数");
        }

        //人员绑定自定义特征分库信息表--操作
        int personBindInfoCount = bcssMemCloudwalkGroupPersonBindInfoBaseService.deleteByCorpId2faceId2Groupcode(corpId, faceNo,groupcode);//1：注册照
        //异常结果必须要threw Exception，用于事务回滚；成功则正常返回
        if (recCount >= 0) {
            LOGGER.info("GroupPersonBindInfo删除数据库成功");
            //如果人员查询结果为全0，则是服务器查询成功，本地不存在数据
        } else {
            LOGGER.error("GroupPersonBindInfo删除数据库异常，原因：删除记录数负数");//不管服务器是更新还是注册
            throw new BusinessException("GroupPersonBindInfo删除数据库异常，原因：删除记录数负数");
        }

        //正常返回
        HashMap<String, Object> operationRes = new HashMap<>();
        operationRes.put(MemberConstants.RETURN_CODE, "0");
        operationRes.put(MemberConstants.RETURN_MSG, "删除人脸场景：本地数据库操作成功");
        operationRes.put(MemberConstants.CLIENTTRANSNO, param.get(MemberConstants.CLIENTTRANSNO));
        LOGGER.info("删除人脸场景："+JSONObject.toJSONString(operationRes));
        return operationRes;

    }


    /*
     * 函数：组装删除报文
     * */
    public Map<String, Object> getParamForDelete(Map<String, Object> param) {
        //先查询（渠道编号+唯一编号）
        //获取channel-渠道号
        Map<String, String> loadParamMap = new HashMap<>();
        String resCode = null;
        loadParamMap = DefinePropertiesFileUtils.getInstance().commonloadParam("bcss.default.cloudwalk.channelCode");
        resCode = loadParamMap.get(MemberConstants.RETURN_CODE);
        String channel = null;
        if (resCode == null || resCode.isEmpty() || !resCode.equals(MemberConstants.COMMON_SUCCESS)) {
//            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL,loadParamMap.get(MemberConstants.RETURN_MSG),clientTransNo);
            throw new BusinessException("bcss.default.cloudwalk.channelCode:" + loadParamMap.get(MemberConstants.RETURN_MSG));
        } else {
            channel = loadParamMap.get(MemberConstants.PROPERTIES_VALUE);
        }
        //人员唯一编号
        String faceNo = (String) param.get("personUniqueFaceNo");
        if (faceNo == null || faceNo.isEmpty()) {
            LOGGER.error("用户人脸注册的唯一编号构造失败，数据异常");
            throw new BusinessException("bcss.default.cloudwalk.channelCode:" + loadParamMap.get(MemberConstants.RETURN_MSG));
//            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION,"用户人脸注册的唯一编号构造失败，数据异常",clientTransNo);
        }

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put(MemberConstants.KEY_CLOUDWALK_CHANNEL_CODE, channel);
        queryMap.put("code", faceNo);
        return queryMap;
    }

    //组装参数
    @Deprecated
    public Map<String, Object> getParamFromFile(Map<String, Object> parm) {
        Map<String, String> loadParamMap = new HashMap<>();
        String resCode = null;
        Map<String, Object> result = new HashMap<>();


        //获取tradingCode-交易吗
        loadParamMap = DefinePropertiesFileUtils.getInstance().commonloadParam("cloudwalk.common.tradingCode");
        resCode = loadParamMap.get(MemberConstants.RETURN_CODE);
        String tradingCode = null;
        if (resCode == null || resCode.isEmpty() || !resCode.equals(MemberConstants.COMMON_SUCCESS)) {
//            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL,loadParamMap.get(MemberConstants.RETURN_MSG),clientTransNo);
            throw new BusinessException("cloudwalk.common.tradingCode:" + loadParamMap.get(MemberConstants.RETURN_MSG));
        } else {
            tradingCode = loadParamMap.get(MemberConstants.PROPERTIES_VALUE);
        }

        //获取orgCode-机构代码
        loadParamMap = DefinePropertiesFileUtils.getInstance().commonloadParam("cloudwalk.common.orgCode");
        resCode = loadParamMap.get(MemberConstants.RETURN_CODE);
        String orgCode = null;
        if (resCode == null || resCode.isEmpty() || !resCode.equals(MemberConstants.COMMON_SUCCESS)) {
            throw new BusinessException("cloudwalk.common.orgCode:" + loadParamMap.get(MemberConstants.RETURN_MSG));
//            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL,loadParamMap.get(MemberConstants.RETURN_MSG),clientTransNo);
        } else {
            orgCode = loadParamMap.get(MemberConstants.PROPERTIES_VALUE);
        }

        //获取channel-渠道号
        loadParamMap = DefinePropertiesFileUtils.getInstance().commonloadParam("cloudwalk.common.channel");
        resCode = loadParamMap.get(MemberConstants.RETURN_CODE);
        String channel = null;
        if (resCode == null || resCode.isEmpty() || !resCode.equals(MemberConstants.COMMON_SUCCESS)) {
//            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL,loadParamMap.get(MemberConstants.RETURN_MSG),clientTransNo);
            throw new BusinessException("cloudwalk.common.channel:" + loadParamMap.get(MemberConstants.RETURN_MSG));
        } else {
            channel = loadParamMap.get(MemberConstants.PROPERTIES_VALUE);
        }

        loadParamMap = DefinePropertiesFileUtils.getInstance().commonloadParam("cloudwalk.reg2del.userType");
        resCode = loadParamMap.get(MemberConstants.RETURN_CODE);
        String userType = null;
        if (resCode == null || resCode.isEmpty() || !resCode.equals(MemberConstants.COMMON_SUCCESS)) {
//            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL,loadParamMap.get(MemberConstants.RETURN_MSG),clientTransNo);
            throw new BusinessException("cloudwalk.reg2del.userType:" + loadParamMap.get(MemberConstants.RETURN_MSG));
        } else {
            userType = loadParamMap.get(MemberConstants.PROPERTIES_VALUE);
        }

        result.put("tradingCode", tradingCode);
        result.put("orgCode", orgCode);
        result.put("channle", channel);
        result.put("userType", userType);
        LOGGER.info("人脸删除固定参数：" + JSONObject.toJSONString(result));

        return result;
    }


}

package com.icbc.bcss.trip.face.operationtools.serviceImpl.cloudwalk;

import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.enums.FaceRegisterType;
import com.icbc.bcss.trip.face.operationtools.exception.BusinessException;
import com.icbc.bcss.trip.face.operationtools.http.HttpBaseService;
import com.icbc.bcss.trip.face.operationtools.http.HttpOperation;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemCloudwalkGroupPersonBindInfoModel;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemCloudwalkPersonRegisterFeatureDetailModel;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemCloudwalkPersonRegisterInfoModel;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemCloudwalkGroupPersonBindInfoBaseService;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemCloudwalkPersonRegisterFeatureDetailBaseService;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemCloudwalkPersonRegisterInfoBaseService;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceRegisterServerService;
import com.icbc.bcss.trip.face.operationtools.service.localFace.FaceLocalConstructParamService;
import com.icbc.bcss.trip.face.operationtools.utils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

//@Service("faceRegisterServerService")
public class FaceRegisterServerServiceImpl implements FaceRegisterServerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FaceRegisterServerServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PropertyPlaceholder environment;

    @Autowired
    private HttpBaseService httpBaseService;

    @Autowired
    private BcssMemCloudwalkPersonRegisterInfoBaseService bcssMemCloudwalkPersonRegisterInfoBaseService;

    @Autowired
    private BcssMemCloudwalkPersonRegisterFeatureDetailBaseService bcssMemCloudwalkPersonRegisterFeatureDetailBaseService;

    @Autowired
    private BcssMemCloudwalkGroupPersonBindInfoBaseService bcssMemCloudwalkGroupPersonBindInfoBaseService;//绑定人脸信息

    @Autowired
    private FaceLocalConstructParamService faceLocalConstructParamService;

    /*
     * 函数：①请求人员查询，根据查询结果决定调用：注册还是更新
     * 备注：查询可以把全部信息查询回来；更新只是单纯告诉是否更新成功
     * */
    @Override
    @Transactional(rollbackFor = Exception.class)//发生异常，回退数据库操作
    public Map<String, Object>  registerInServer(Map<String, Object> param) {
        //获取url
        String clientTransNo = (String) param.get(MemberConstants.CLIENTTRANSNO);
        String registerUrl = environment.getProperty("bcss.default.face.register_URL");
        String queryPersonUrl = environment.getProperty("bcss.default.face.query_URL");
        String updateUrl = environment.getProperty("bcss.default.face.update_URL");
        if (registerUrl == null || registerUrl.isEmpty() || queryPersonUrl == null || queryPersonUrl.isEmpty() ||
                updateUrl == null || updateUrl.isEmpty()) {
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "读取属性配置文件中，不存在人脸注册、人员查询或者人员更新URL地址", clientTransNo);
        }

        //调用查询
        Map queryResult=queryDataOnly(param,clientTransNo,queryPersonUrl);

        //判断查询结果
        Map<String, Object> resParam = new HashMap<>();
        boolean regOrUpdateFlag = false;//false--服务器更新人脸，true--服务器注册人脸
        if (MemberConstants.VALUE_FULL_SUCC.equals(queryResult.get(MemberConstants.KEY_RESPONSE_CODE))) {
            //获取 data
            Map<String, Object> cloudwalkData = (Map<String, Object>) queryResult.get("data");
            if (cloudwalkData == null || cloudwalkData.isEmpty()) {
                LOGGER.error("查询人员信息--返回成功交易码，但是data数据为空，异常");
                return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION, "查询人员信息--返回成功交易码，但是data数据为空，异常", clientTransNo);
            }
            //调用更新接口
            resParam = updateOnly(param, clientTransNo, updateUrl, queryResult);
            regOrUpdateFlag = false;
        } else {
            //为空或者为错误码，视为注册，
            LOGGER.error("查询人员信息--返回为空或者为错误码，视为注册");
            resParam = registerOnly(param, clientTransNo, registerUrl, queryResult);
            regOrUpdateFlag = true;
        }

        //可能存在注册人脸的时候绑定失败，那么更新人脸的时候就要绑定绑定人脸，因此直接全部走绑定接口
        LOGGER.warn("当前人脸前置完成人脸服务器的注册/更新接口调用，准备根据返回结果判断是否调用：人脸绑定分库接口");
        if (MemberConstants.COMMON_SUCCESS.equals((String) resParam.get(MemberConstants.RETURN_CODE))) {
            return bindPersonInfo(param);//绑定人脸
        }else{
            //失败，就直接返回结果
            return resParam;
        }

    }

    /*
    * 函数作用：调用人员查询接口
    * */
    @Override
    public Map<String,Object> queryDataOnly(Map<String,Object> param,String clientTransNo,String queryPersonUrl){
        Map<String, Object> queryMap = new HashMap<>();
        try {
            Map<String, Object> res = getParamForQueryOrUpdate(param);
            if (res != null && res.size() > 0) {
                LOGGER.debug("人脸查询信息参数成功获取");
                queryMap.putAll(res);
            }
        } catch (BusinessException e) {
            LOGGER.error(e.toString());
//            LOGGER.error(e.getMessage());
            LOGGER.error("人脸查询信息参数获取异常：" + e.getMessage(), e);
            Map speicalMap= CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION, e.getMessage(), clientTransNo);
            speicalMap.put("netQueryStatus","1");//标记此为网络异常，认为未知
            return speicalMap;
        }

        //开始查询
        LOGGER.error("请求查询人员的参数：" + JSONObject.toJSONString(queryMap));
        Map<String, Object> queryResult = new HashMap<>();
        try {
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(queryPersonUrl, HttpOperation.generatePostJson(queryMap), String.class);
            String StringResponse = apiResponse.getBody();
            LOGGER.error("人员查询后返回的请求体为：" + StringResponse);

            Map<String, Object> returnMap = new HashMap<>();
            if (StringResponse == null || StringResponse.isEmpty()) {
                returnMap.put(MemberConstants.RETURN_CODE, FaceRegisterType.FACE_Query_SERVICE_RETURN_EMPTY.getCode());
                returnMap.put(MemberConstants.RETURN_MSG, FaceRegisterType.FACE_Query_SERVICE_RETURN_EMPTY.getDesc());
                returnMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
                System.out.println("人员查询调用返回空结果");
                return returnMap;
            }

            queryResult = JSONObject.parseObject(StringResponse, Map.class);
            return queryResult;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("人员信息接口调用抛出异常" + e.toString());
            LOGGER.error("人员信息接口调用抛出异常：" + e.getMessage(), e);
            Map<String, Object> exceptionMap = new HashMap<>();
            exceptionMap.put(MemberConstants.RETURN_CODE, FaceRegisterType.FACE_Query_SERVICE_RETURN_EXCEPTION.getCode());
            exceptionMap.put(MemberConstants.RETURN_MSG, FaceRegisterType.FACE_Query_SERVICE_RETURN_EXCEPTION.getDesc());
            exceptionMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
            exceptionMap.put("netQueryStatus","1");//标记此为网络异常，认为未知
            System.out.println("人脸信息接口调用抛出异常");
            return exceptionMap;
        }
    }

    /*
     * 单纯更新接口
     * */
    public Map<String, Object> updateOnly(Map<String, Object> param, String clientTransNo, String recognizeUrl, Map<String, Object> queryResult) {
        //参数构造
        Map<String, Object> request = new HashMap<>();
        try {
            Map<String, Object> res = getParamForQueryOrUpdate(param);
            if (res != null && res.size() > 0) {
                LOGGER.debug("人脸更新参数成功获取");
                request.putAll(res);
            }
        } catch (BusinessException e) {
            LOGGER.error(e.toString());
//            LOGGER.error(e.getMessage());
            LOGGER.error("人脸更新参数获取异常：" + e.getMessage(), e);
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION, e.getMessage(), clientTransNo);
        }

        //姓名
        String name = (String) param.get("memName");
        if (name == null || name.isEmpty()) {
            LOGGER.error("默认姓名：未知");
            name = "未知";
        }

        String imageBase64 = (String) param.get("memParam");
        List<Map<String, Object>> biologyFeatures = new ArrayList<>();
        Map<String, Object> biology1 = new HashMap<>();
        biology1.put("file", imageBase64);
        biology1.put("type", "1");//1-人脸注册照
        biology1.put("subType", "1");//默认序号为第一顺序
        biologyFeatures.add(biology1);
        String remark = "本地人脸前置服务注册的人脸";

        //把传入的参数放入Request
        request.put("name", name);
        request.put("remark", remark);
        request.put("biologyFeatures", biologyFeatures);

        /*
        * 根据传入模块决定后续的操作
        * */
        String modelName= (String) param.get(MemberConstants.FACE_SINGLE_REGISTER_MODEL_NAME);
        if(modelName!=null){
            LOGGER.warn("更新服务--传入的调用模块名为："+modelName);
            Map<String,Object> temp=faceLocalConstructParamService.constructRegisterCommonParam(param);
//            request.clear();
            request.putAll(temp);
        }

        //请求服务器
        LOGGER.error("人脸更新请求参数为：" + JSONObject.toJSONString(request));
        Map<String, Object> returnMap = new HashMap<>();
        try {
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(recognizeUrl, HttpOperation.generatePostJson(request), String.class);
            String StringResponse = apiResponse.getBody();
            LOGGER.error("人脸更新后返回的请求体为：" + StringResponse);

//            Map<String,Object> cloudReturn=new HashMap<>();
            if (StringResponse == null || StringResponse.isEmpty()) {
                returnMap.put(MemberConstants.RETURN_CODE, FaceRegisterType.FACE_UPDATE_SERVICE_RETURN_EMPTY.getCode());
                returnMap.put(MemberConstants.RETURN_MSG, FaceRegisterType.FACE_UPDATE_SERVICE_RETURN_EMPTY.getDesc());
                System.out.println("人脸更新调用返回空结果");
                return returnMap;
            } else {
                returnMap = JSONObject.parseObject(StringResponse, Map.class);
                returnMap = FaceReturnConvertCommonReturn.converFieldName(returnMap);
                //
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("人脸更新接口调用抛出异常" + e.toString());
            LOGGER.error("人脸更新接口调用抛出异常：" + e.getMessage(), e);
            Map<String, Object> exceptionMap = new HashMap<>();
            exceptionMap.put(MemberConstants.RETURN_CODE, FaceRegisterType.FACE_UPDATE_SERVICE_RETURN_EXCEPTION.getCode());
            exceptionMap.put(MemberConstants.RETURN_MSG, FaceRegisterType.FACE_UPDATE_SERVICE_RETURN_EXCEPTION.getDesc());
            System.out.println("人脸更新接口调用抛出异常");
            return exceptionMap;
        }

        //更新本地数据库
        if (MemberConstants.VALUE_FULL_SUCC.equals(returnMap.get(MemberConstants.KEY_RESPONSE_CODE))) {
            //还需要查询一次，获取更新后 全量数据
            Map secondQuery=queryDataOnly(param,clientTransNo,environment.getProperty("bcss.default.face.query_URL"));//第二次查询
            //判断查询结果
            if (MemberConstants.VALUE_FULL_SUCC.equals(secondQuery.get(MemberConstants.KEY_RESPONSE_CODE))) {
                //获取 data
                Map<String, Object> secondQueryData = (Map<String, Object>) secondQuery.get("data");
                if (secondQueryData == null || secondQueryData.isEmpty()) {
                    LOGGER.error("（2）查询人员信息--返回成功交易码，但是data数据为空，异常");
                    return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION, "（2）查询人员信息--返回成功交易码，但是data数据为空，异常", clientTransNo);
                }
                //成功获取data数据

            } else {
                //为空或者为错误码，视为注册，
                LOGGER.error("（2）查询人员信息--返回为空或者为错误码");
                return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION, "（2）查询人员信息--返回为空或者为错误码", clientTransNo);
            }

            //调用更新接口--把请求参数放在结果中
//            returnMap.putAll(request);
            returnMap.put("updateParam",request);
            return insertOrUpdateLocalDatabase(param, returnMap, secondQuery, false);
        } else {
            String msg="更新人员信息--返回为空或者为错误码，原因："+returnMap.get(MemberConstants.KEY_RESPONSE_MESSAGE);
            LOGGER.error(msg);
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION, msg, clientTransNo);
        }
//        return null;
    }


    /*
     * 单纯注册接口
     * */
    public Map<String, Object> registerOnly(Map<String, Object> param, String clientTransNo, String recognizeUrl, Map<String, Object> queryResult) {
        //参数构造
        Map<String, Object> request = new HashMap<>();
        try {
            Map<String, Object> res = getParamFromFile(param);
            if (res != null && res.size() > 0) {
                LOGGER.debug("人脸注册参数成功获取");
                request.putAll(res);
            }
        } catch (BusinessException e) {
            LOGGER.error(e.toString());
//            LOGGER.error(e.getMessage());
            LOGGER.error("人脸注册参数获取异常：" + e.getMessage(), e);
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION, e.getMessage(), clientTransNo);
        }

        //获取姓名的默认值
        String name = (String) param.get("memName");
        if (name == null || name.isEmpty()) {
            LOGGER.error("默认姓名：未知");
            name = "未知";
        }

        //性别送3-为止
        int sex = 3;//默认未知

        //获取人脸唯一编号：code
        String faceNo = (String) param.get("personUniqueFaceNo");
        if (faceNo == null || faceNo.isEmpty()) {
            LOGGER.error("用户人脸注册的唯一编号构造失败，数据异常");
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION, "用户人脸注册的唯一编号构造失败，数据异常", clientTransNo);
        }

//
//        //personId单独加密，用于餐饮场景
//        Map tempRes=DefinePropertiesFileUtils.getInstance().commonloadParam("guard.my_aeskey");
//        String aeskey= (String) tempRes.get(MemberConstants.PROPERTIES_VALUE);
//        if(aeskey==null||aeskey.isEmpty()){
//            LOGGER.error("获取AES密钥失败，参数配置异常，请检查");
//            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION,"获取AES密钥失败，参数配置异常，请检查",clientTransNo);
//        }
//        //加密
//        String encryptText=null;
//        try {
//            encryptText = IcbcEncrypt.encryptContent(personId,"AES",aeskey,"UTF-8");
//        } catch (IcbcApiException e) {
////            e.printStackTrace();
//            LOGGER.error(e.toString());
//            LOGGER.error("使用AES密钥加密BCSS用户编号失败，请检查："+e.getMessage(),e);
//            LOGGER.error("使用AES密钥加密BCSS用户编号失败，请检查");
//            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION,"使用AES密钥加密BCSS用户编号失败，请检查",clientTransNo);
//        }


        String imageBase64 = (String) param.get("memParam");
        List<Map<String, Object>> biologyFeatures = new ArrayList<>();
        Map<String, Object> biology1 = new HashMap<>();
        biology1.put("file", imageBase64);
        biology1.put("type", "1");//1-人脸注册照
        biology1.put("subType", "1");//默认序号为第一顺序
        biologyFeatures.add(biology1);
        String remark = "本地人脸前置服务注册的人脸";

        //把传入的参数放入Request
        request.put("code", faceNo);
        request.put("name", name);
        request.put("sex", sex);
        request.put("remark", remark);
        request.put("biologyFeatures", biologyFeatures);

        /*
        * 上述是最基础参数，此处需要判断是否其他模块调用，进行特殊
        * */
        String modelName= (String) param.get(MemberConstants.FACE_SINGLE_REGISTER_MODEL_NAME);
        if(modelName!=null){
            LOGGER.warn("新增--传入的调用模块名为："+modelName);
            Map<String,Object> temp=faceLocalConstructParamService.constructRegisterCommonParam(param);
//            request.clear();
            request.putAll(temp);
        }

        //请求服务器
        LOGGER.error("人脸注册请求参数为：" + JSONObject.toJSONString(request));
        Map<String, Object> returnMap = new HashMap<>();
        try {
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(recognizeUrl, HttpOperation.generatePostJson(request), String.class);
            String StringResponse = apiResponse.getBody();
            LOGGER.error("人脸注册后返回的请求体为：" + StringResponse);

//            Map<String,Object> returnMap=new HashMap<>();
            if (StringResponse == null || StringResponse.isEmpty()) {
//                returnMap.put("respCode", FaceRegisterType.FACE_REGISTER_SERVICE_RETURN_EMPTY.getCode());
//                returnMap.put("respDesc",FaceRegisterType.FACE_REGISTER_SERVICE_RETURN_EMPTY.getDesc());
//                returnMap.put("data",null);
                returnMap.put(MemberConstants.RETURN_CODE, FaceRegisterType.FACE_REGISTER_SERVICE_RETURN_EMPTY.getCode());
                returnMap.put(MemberConstants.RETURN_MSG, FaceRegisterType.FACE_REGISTER_SERVICE_RETURN_EMPTY.getDesc());
                System.out.println("人脸注册调用返回空结果");
                return returnMap;
            } else {
                returnMap = JSONObject.parseObject(StringResponse, Map.class);
                returnMap = FaceReturnConvertCommonReturn.converFieldName(returnMap);
            }
//            return returnMap;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("人脸注册接口调用抛出异常" + e.toString());
            LOGGER.error("人脸注册接口调用抛出异常：" + e.getMessage(), e);
            Map<String, Object> exceptionMap = new HashMap<>();
//            exceptionMap.put("respCode", FaceRegisterType.FACE_REGISTER_SERVICE_RETURN_EXCEPTION.getCode());
//            exceptionMap.put("respDesc",FaceRegisterType.FACE_REGISTER_SERVICE_RETURN_EXCEPTION.getDesc());
//            exceptionMap.put("data",null);
            exceptionMap.put(MemberConstants.RETURN_CODE, FaceRegisterType.FACE_REGISTER_SERVICE_RETURN_EXCEPTION.getCode());
            exceptionMap.put(MemberConstants.RETURN_MSG, FaceRegisterType.FACE_REGISTER_SERVICE_RETURN_EXCEPTION.getDesc());
            System.out.println("人脸注册接口调用抛出异常");
            return exceptionMap;
        }

        //更新本地数据库
        if (MemberConstants.VALUE_FULL_SUCC.equals(returnMap.get(MemberConstants.KEY_RESPONSE_CODE))) {
            //获取 data
            Map<String, Object> cloudwalkData = (Map<String, Object>) returnMap.get("data");
            if (cloudwalkData == null || cloudwalkData.isEmpty()) {
                LOGGER.error("人脸注册--返回成功交易码，但是data数据为空，异常");
                return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION, "人脸注册--返回成功交易码，但是data数据为空，异常", clientTransNo);
            }
            //调用注册接口
            return insertOrUpdateLocalDatabase(param, returnMap, queryResult, true);
        } else {
            //为空或者为错误码,直接返回，
            String msg="人脸注册--注册失败，原因："+returnMap.get(MemberConstants.KEY_RESPONSE_MESSAGE);
            LOGGER.error(msg);
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION,msg, clientTransNo);
        }
//        return null;
    }

    /*
     * 绑定人员信息接口
     * */
    public Map<String, Object> bindPersonInfo(Map<String, Object> bindparam) {
        String bindUrl = environment.getProperty("bcss.default.face.bind_ownGroup_URL");
        String bindQueryUrl=environment.getProperty("bcss.default.face.bing_query_ownGroup_URL");
        String clientTransNo = (String) bindparam.get(MemberConstants.CLIENTTRANSNO);
        if (bindUrl == null || bindUrl.isEmpty()||bindQueryUrl==null||bindQueryUrl.isEmpty()) {
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "读取属性配置文件中，不存在绑定人脸信息到自定义库URL地址或查询绑定信息的URL地址", clientTransNo);
        }

        String faceNo = (String) bindparam.get("personUniqueFaceNo");
        if (faceNo == null || faceNo.isEmpty()) {
            LOGGER.error("用户人脸注册的唯一编号构造失败，数据异常");
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION, "用户人脸注册的唯一编号构造失败，数据异常", clientTransNo);
        }

        String channelCode = environment.getProperty("bcss.default.cloudwalk.channelCode");
        String corpId = (String) bindparam.get("corpId");
        String groupCode = corpId + MemberConstants.DEFAULT_SEPARATOR_FROM_PERSONID_TO_SEQNO + MemberConstants.DEFAULT_GROUP_SEQNO;

        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("channelCode", channelCode);
        queryMap.put("personCode", faceNo);
        queryMap.put("groupCode", groupCode);

        //请求服务器--查询
        Map queryBingRes=httpBaseService.execute(queryMap,bindQueryUrl);
        if( !(MemberConstants.COMMON_SUCCESS.equals(queryBingRes.get(MemberConstants.RETURN_CODE))) ) {
            LOGGER.error("查询人脸绑定信息到自定义分库失败，原因："+JSONObject.toJSONString(queryBingRes));
            return CommonMapReturn.constructMapWithClientTransNo((String)queryBingRes.get(MemberConstants.RETURN_CODE),(String)queryBingRes.get(MemberConstants.RETURN_MSG),clientTransNo);
        }

        Map<String,Object> bindStatusRes= (Map<String, Object>) queryBingRes.get("httpBody");
        if(! (MemberConstants.VALUE_FULL_SUCC.equals(bindStatusRes.get(MemberConstants.KEY_RESPONSE_CODE))) ){
            LOGGER.error("查询人脸绑定信息到自定义分库失败，原因："+bindStatusRes.get(MemberConstants.KEY_RESPONSE_MESSAGE));
            return CommonMapReturn.constructMapWithClientTransNo((String)bindStatusRes.get(MemberConstants.KEY_RESPONSE_CODE),(String) bindStatusRes.get(MemberConstants.KEY_RESPONSE_MESSAGE),clientTransNo);
        }
        Map<String,Object> dataMap= (Map<String, Object>) bindStatusRes.get(MemberConstants.KEY_RESPONSE_DATA);
        Integer bindStatus= (Integer) dataMap.get("bindStatus");
        //1--绑定，0-没有绑定
        boolean bingfalg=false;
        if(1==bindStatus.intValue()){
            LOGGER.info("人脸已经绑定");
            bingfalg=true;
        }else if(0==bindStatus.intValue()){
            LOGGER.error("人脸没有绑定分库");
            bingfalg=false;
        }else{
            LOGGER.error("绑定状态非合法字典值，默认未绑定");
            bingfalg=false;
        }

        //构造信息
        bindparam.put("groupCode",groupCode);
        if(bingfalg==true){
            //查询本地
            BcssMemCloudwalkGroupPersonBindInfoModel bcssMemCloudwalkGroupPersonBindInfoModel1=constructGroupPersonBindInfoModelByInsertOrUpdate(bindparam,bindStatusRes);
            return bindLocalDatabase(bcssMemCloudwalkGroupPersonBindInfoModel1,bindparam);
        }else{
            LOGGER.error("绑定人脸到自定义库-请求参数为：" + JSONObject.toJSONString(queryMap));
            Map<String, Object> returnMap = new HashMap<>();
            try {
                ResponseEntity<String> apiResponse = restTemplate.postForEntity(bindUrl, HttpOperation.generatePostJson(queryMap), String.class);
                String StringResponse = apiResponse.getBody();
                LOGGER.error("绑定人脸到自定义库-后返回的请求体为：" + StringResponse);

//            Map<String,Object> returnMap=new HashMap<>();
                if (StringResponse == null || StringResponse.isEmpty()) {
                    returnMap.put(MemberConstants.RETURN_CODE, FaceRegisterType.FACE_BIND_GROUP_SERVICE_RETURN_EMPTY.getCode());
                    returnMap.put(MemberConstants.RETURN_MSG, FaceRegisterType.FACE_BIND_GROUP_SERVICE_RETURN_EMPTY.getDesc());
                    System.out.println("绑定人脸到自定义库返回空结果");
                    return returnMap;
                } else {
                    returnMap = JSONObject.parseObject(StringResponse, Map.class);
                    returnMap = FaceReturnConvertCommonReturn.converFieldName(returnMap);
                }

            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("绑定人脸到自定义库接口调用抛出异常" + e.toString());
                LOGGER.error("绑定人脸到自定义库接口调用抛出异常：" + e.getMessage(), e);
                Map<String, Object> exceptionMap = new HashMap<>();
                exceptionMap.put(MemberConstants.RETURN_CODE, FaceRegisterType.FACE_BIND_GROUP_SERVICE_RETURN_EXCEPTION.getCode());
                exceptionMap.put(MemberConstants.RETURN_MSG, FaceRegisterType.FACE_BIND_GROUP_SERVICE_RETURN_EXCEPTION.getDesc());
                System.out.println("绑定人脸到自定义库接口调用抛出异常");
                return exceptionMap;
            }

            BcssMemCloudwalkGroupPersonBindInfoModel groupPersonBindInfoModel=constructGroupPersonBindInfoModelByInsertOrUpdate(bindparam,bindStatusRes);
            //插入本地数据库
            if (MemberConstants.VALUE_FULL_SUCC.equals(returnMap.get(MemberConstants.RETURN_CODE))) {
                return bindLocalDatabase(groupPersonBindInfoModel,bindparam);
            } else if ("53060069".equals(returnMap.get(MemberConstants.RETURN_CODE))) {
                LOGGER.error("绑定人脸的http返回信息为：人员已在库中，所以查询或者更新");
                return bindLocalDatabase(groupPersonBindInfoModel,bindparam);
            } else {
                LOGGER.error("绑定人脸的http请求失败");
                return returnMap;
            }
        }

    }

    //绑定人脸的本地数据库操作
    private Map<String, Object> bindLocalDatabase(BcssMemCloudwalkGroupPersonBindInfoModel groupPersonBindInfoModel,Map<String,Object> parentMap) {
        BcssMemCloudwalkGroupPersonBindInfoModel bindInfoModel=bcssMemCloudwalkGroupPersonBindInfoBaseService.
                selectByCorpId2FaceId2GroupCode(groupPersonBindInfoModel.getCorpId(),groupPersonBindInfoModel.getFaceId(),groupPersonBindInfoModel.getGroupcode());
        if(bindInfoModel==null){
            LOGGER.error("查询人脸绑定分库(bcssMemCloudwalkGroupPersonBindInfoBaseService)结果为空");
            groupPersonBindInfoModel.setSetdate(new Date());
            groupPersonBindInfoModel.setLstmdate(new Date());
            int res=bcssMemCloudwalkGroupPersonBindInfoBaseService.insert(groupPersonBindInfoModel);
            if (res == 1) {
                LOGGER.info("groupPersonBindInfoModel插入数据库正常");
            } else {
                LOGGER.error("groupPersonBindInfoModel插入数据库异常");
                throw new BusinessException("groupPersonBindInfoModel插入数据库异常");
            }
        }else{
            groupPersonBindInfoModel.setFlownum(null);
            groupPersonBindInfoModel.setLstmdate(new Date());
            groupPersonBindInfoModel.setSeqId(bindInfoModel.getSeqId());
            int res=bcssMemCloudwalkGroupPersonBindInfoBaseService.updateByPrimaryKeySelective(groupPersonBindInfoModel);
            if (res >= 0) {
                LOGGER.info("groupPersonBindInfoModel插入数据库正常");
            } else {
                LOGGER.error("groupPersonBindInfoModel插入数据库异常");
                throw new BusinessException("groupPersonBindInfoModel插入数据库异常");
            }
        }
        //正常返回
        HashMap<String, Object> operationRes = new HashMap<>();
        operationRes.put(MemberConstants.RETURN_CODE, "0");
        operationRes.put(MemberConstants.RETURN_MSG, "本地数据库操作(bindLocalDatabase)成功");
        operationRes.put(MemberConstants.CLIENTTRANSNO, parentMap.get(MemberConstants.CLIENTTRANSNO));
        LOGGER.info(JSONObject.toJSONString(operationRes));
        return operationRes;
    }

    //绑定信息实体
    private BcssMemCloudwalkGroupPersonBindInfoModel constructGroupPersonBindInfoModelByInsertOrUpdate(Map<String, Object> parentMap,Map<String,Object> resultMap) {
        String flowNum= (String) resultMap.get("flowNum");
        BcssMemCloudwalkGroupPersonBindInfoModel groupPersonBindInfoModel = new BcssMemCloudwalkGroupPersonBindInfoModel();
        groupPersonBindInfoModel.setCorpId((String) parentMap.get("corpId"));
        groupPersonBindInfoModel.setFaceId((String) parentMap.get("personUniqueFaceNo"));
        //更新服务器人脸情况下，返回参数不存在：应用编号	APPLICATION_ID；业务编号	BUSINESS_ID
        groupPersonBindInfoModel.setGroupcode((String) parentMap.get("groupCode"));
        groupPersonBindInfoModel.setFlownum(flowNum);
        return groupPersonBindInfoModel;
    }

    /*
     * 更新本地数据库
     * 注备：以注册接口，或者更新接口（查询接口）返回的值为准
     * @param param：注册或者更新接口返回参数
     * @param  queryResult ：查询接口返回的值
     * insertOrUpdateFlag  true:插入，false--更新
     * */
    private Map<String, Object> insertOrUpdateLocalDatabase(Map<String, Object> parentMap, Map<String, Object> param, Map<String, Object> queryResult, boolean insertOrUpdateFlag) {
        BcssMemCloudwalkPersonRegisterInfoModel bcssMemCloudwalkPersonRegisterInfoModel = new BcssMemCloudwalkPersonRegisterInfoModel();
        List<BcssMemCloudwalkPersonRegisterFeatureDetailModel> bcssMemCloudwalkPersonRegisterFeatureDetailModelList = new ArrayList<>();
        if (insertOrUpdateFlag == false) {
            //更新服务器信息的情况下
            bcssMemCloudwalkPersonRegisterInfoModel = constructRegisterInfoModelByUpdate(parentMap, param, queryResult);
            bcssMemCloudwalkPersonRegisterFeatureDetailModelList = constructRegisterFeatureDetailModelByUpdate(parentMap, param, queryResult);
        } else {
            //注册
            bcssMemCloudwalkPersonRegisterInfoModel = constructRegisterInfoModelByInsert(parentMap, param, queryResult);
            bcssMemCloudwalkPersonRegisterFeatureDetailModelList = constructRegisterFeatureDetailModelByInsert(parentMap, param, queryResult);
        }
        String corpId = (String) parentMap.get("corpId");
        String faceNo = (String) parentMap.get("personUniqueFaceNo");

        //注册特征客户信息--操作
        //1.查询是否存在实体信息，2.如果不存在，走更新
        BcssMemCloudwalkPersonRegisterInfoModel registerInfoModel = bcssMemCloudwalkPersonRegisterInfoBaseService.selectByCorpID2FaceID(corpId, faceNo);
        //异常结果必须要threw Exception，用于事务回滚；成功则正常返回
        if (registerInfoModel == null) {
            LOGGER.error("本地数据库操作-registerInfoModel-查询结果为空，插入数据");
            //如果人员查询结果为全0，则是服务器查询成功，本地不存在数据
            bcssMemCloudwalkPersonRegisterInfoModel.setSetdate(new Date());
            bcssMemCloudwalkPersonRegisterInfoModel.setLstmdate(new Date());
            int res = bcssMemCloudwalkPersonRegisterInfoBaseService.insert(bcssMemCloudwalkPersonRegisterInfoModel);
            if (res == 1) {
                LOGGER.info("registerInfoModel插入数据库正常");
            } else {
                LOGGER.error("registerInfoModel插入数据库异常");
                throw new BusinessException("registerInfoModel插入数据库异常");
            }
            //否则，则是服务器查询失败，本地不存在数据
        } else {
            LOGGER.error("本地数据库操作-registerInfoModel-查询结果存在，更新数据");//不管服务器是更新还是注册
            bcssMemCloudwalkPersonRegisterInfoModel.setLstmdate(new Date());
            bcssMemCloudwalkPersonRegisterInfoModel.setSeqId(registerInfoModel.getSeqId());
            int res = bcssMemCloudwalkPersonRegisterInfoBaseService.updateByPrimaryKeySelective(bcssMemCloudwalkPersonRegisterInfoModel);
            if (res >= 0) {
                LOGGER.info("registerInfoModel更新数据库正常");
            } else {
                LOGGER.error("registerInfoModel更新数据库异常");
                throw new BusinessException("registerInfoModel更新数据库异常");
            }
        }


        //注册特征明细信息表--操作
        //首先删除corpId和faceno下的所有特征记录，不需要判断删除结果，删除多条或者0条都是为成功
        int recCount=bcssMemCloudwalkPersonRegisterFeatureDetailBaseService.deleteByCorpId2FaceId2Type(corpId,faceNo,"1");//1：注册照
        LOGGER.error("删除bcssMemCloudwalkPersonRegisterFeatureDetail记录数量："+recCount);
        //1.查询是否存在实体信息，2.如果不存在，走更新；3存在抛出异常
        List<BcssMemCloudwalkPersonRegisterFeatureDetailModel> registerFeatureDetailModelList = bcssMemCloudwalkPersonRegisterFeatureDetailBaseService.selectByCorpID2FaceID2PersonFeatureType(corpId, faceNo,"1");
        //异常结果必须要threw Exception，用于事务回滚；成功则正常返回
        if (registerFeatureDetailModelList == null || registerFeatureDetailModelList.isEmpty()) {
            LOGGER.error("本地数据库操作-registerFeatureDetailModelList-查询结果为空，插入数据");
            //如果人员查询结果为全0，则是服务器查询成功，本地不存在数据
            for (BcssMemCloudwalkPersonRegisterFeatureDetailModel detailModel : bcssMemCloudwalkPersonRegisterFeatureDetailModelList) {
                detailModel.setSetdate(new Date());
                detailModel.setLstmdate(new Date());
                int res = bcssMemCloudwalkPersonRegisterFeatureDetailBaseService.insert(detailModel);
                if (res == 1) {
                    LOGGER.info("registerFeatureDetailModelList插入数据库正常");
                } else {
                    LOGGER.error("registerFeatureDetailModelList插入数据库异常");
                    throw new BusinessException("registerFeatureDetailModelList插入数据库异常");
                }
            }
            //否则，则是服务器查询失败，本地不存在数据
        } else {
            LOGGER.error("本地数据库操作-registerFeatureDetailModelList-查询结果存在，即异常，原因：由于前面已经把当前corpid和faceId的记录全部删除");//不管服务器是更新还是注册
            throw new BusinessException("本地数据库操作-registerFeatureDetailModelList-查询结果存在，即异常，原因：由于前面已经把当前corpid和faceId的记录全部删除");
        }
        //正常返回
        HashMap<String, Object> operationRes = new HashMap<>();
        operationRes.put(MemberConstants.RETURN_CODE, "0");
        operationRes.put(MemberConstants.RETURN_MSG, "注册人脸场景：+本地数据库操作成功");
        operationRes.put(MemberConstants.CLIENTTRANSNO, parentMap.get(MemberConstants.CLIENTTRANSNO));
        operationRes.put("server_register_info_code",bcssMemCloudwalkPersonRegisterInfoModel.getServerRegisterInfoCode());
        LOGGER.info("注册人脸场景："+JSONObject.toJSONString(operationRes));
        return operationRes;
    }


    //更新服务器情况下，构造RegisterFeatureDetailModel
    private List<BcssMemCloudwalkPersonRegisterFeatureDetailModel> constructRegisterFeatureDetailModelByUpdate(Map<String, Object> parentMap, Map<String, Object> param, Map<String, Object> queryResult) {
        //先取更新的值，然后再取旧查询的值
        Map<String, Object> queryDataMap = (Map<String, Object>) queryResult.get(MemberConstants.KEY_RESPONSE_DATA);
//        Map<String, Object> queryDataMap = JSONObject.parseObject(queryDataStr, Map.class);//人员查询接口回来的data数据
        List<Map<String, Object>> biologyFeaturesData = (List<Map<String, Object>>) queryDataMap.get("biologyFeatures");

        List<BcssMemCloudwalkPersonRegisterFeatureDetailModel> bcssMemCloudwalkPersonRegisterFeatureDetailModelList = new ArrayList<>();
        for (Map singleMap : biologyFeaturesData) {
            LOGGER.info("循环读取特征信息:" + JSONObject.toJSONString(singleMap));
            BcssMemCloudwalkPersonRegisterFeatureDetailModel registerFeatureDetailModel = new BcssMemCloudwalkPersonRegisterFeatureDetailModel();
            String filePathName = (String) singleMap.get("file");
            String fileType = String.valueOf(singleMap.get("type"));
            String subType = String.valueOf(singleMap.get("subType"));

            registerFeatureDetailModel.setCorpId((String) parentMap.get("corpId"));
            registerFeatureDetailModel.setFaceId((String) parentMap.get("personUniqueFaceNo"));
            registerFeatureDetailModel.setFlownum((String) param.get("flowNum"));
            registerFeatureDetailModel.setServerRegisterInfoCode(null);
            registerFeatureDetailModel.setPersonFeatureType(fileType);
            registerFeatureDetailModel.setPersonFeatureSubType(subType);
            registerFeatureDetailModel.setPersonFeatureSavePath(filePathName);
            registerFeatureDetailModel.setPersonFeatureId(null);

            bcssMemCloudwalkPersonRegisterFeatureDetailModelList.add(registerFeatureDetailModel);
        }
        return bcssMemCloudwalkPersonRegisterFeatureDetailModelList;
    }

    //更新服务器情况下，构造RegisterInfoModel
    private BcssMemCloudwalkPersonRegisterInfoModel constructRegisterInfoModelByUpdate(Map<String, Object> parentMap, Map<String, Object> param, Map<String, Object> queryResult) {
        Map<String, Object> queryDataMap = (Map<String, Object>) queryResult.get(MemberConstants.KEY_RESPONSE_DATA);
//        Map<String, Object> queryDataMap = JSONObject.parseObject(queryDataStr, Map.class);//人员查询接口回来的data数据

        BcssMemCloudwalkPersonRegisterInfoModel registerInfoModel = new BcssMemCloudwalkPersonRegisterInfoModel();
        registerInfoModel.setCorpId((String) parentMap.get("corpId"));
        registerInfoModel.setFaceId((String) parentMap.get("personUniqueFaceNo"));
        //更新服务器人脸情况下，返回参数不存在：应用编号	APPLICATION_ID；业务编号	BUSINESS_ID
        registerInfoModel.setApplicationId(null);
        registerInfoModel.setBusinessId(null);
        registerInfoModel.setFlownum((String) param.get("flowNum"));
//        registerInfoModel.setServerRegisterInfoCode(null);//人员信息查询接口，无法返回人脸底库的id，所以服务器更新情况下，无法获取该id
        registerInfoModel.setChannelCode(environment.getProperty("bcss.default.cloudwalk.channelCode"));
        registerInfoModel.setServiceModelVersion(environment.getProperty("bcss.default.cloudwalk.serviceModelVersion"));
        registerInfoModel.setOrgcode((String) queryDataMap.get("orgCode"));
        registerInfoModel.setPeopletype((String) queryDataMap.get("type"));
        registerInfoModel.setViplevel((String) queryDataMap.get("vipLevel"));
        registerInfoModel.setCerttype((String) queryDataMap.get("certType"));
        registerInfoModel.setCertno((String) queryDataMap.get("certNo"));
        registerInfoModel.setName((String) queryDataMap.get("name"));
        registerInfoModel.setSex(String.valueOf(queryDataMap.get("sex")));//查询接口返回的是int类型
        registerInfoModel.setBirthday((String) queryDataMap.get("birthday"));
        registerInfoModel.setDuty((String) queryDataMap.get("duty"));
        registerInfoModel.setPosition((String) queryDataMap.get("position"));
        registerInfoModel.setTelephone((String) queryDataMap.get("telephone"));
        registerInfoModel.setEmail((String) queryDataMap.get("email"));
        registerInfoModel.setAddress((String) queryDataMap.get("address"));
        registerInfoModel.setRemark((String) queryDataMap.get("remark"));
        registerInfoModel.setProvince((String) queryDataMap.get("province"));
        registerInfoModel.setCity((String) queryDataMap.get("city"));
        registerInfoModel.setArea((String) queryDataMap.get("area"));
        return registerInfoModel;
    }

    //服务器注册返回
    private BcssMemCloudwalkPersonRegisterInfoModel constructRegisterInfoModelByInsert(Map<String, Object> parentMap, Map<String, Object> param, Map<String, Object> queryResult) {
        Map<String, Object> insertDataMap = (Map<String, Object>) param.get(MemberConstants.KEY_RESPONSE_DATA);
//         = JSONObject.parseObject(insertDataStr, Map.class);//人员查询接口回来的data数据

        BcssMemCloudwalkPersonRegisterInfoModel registerInfoModel = new BcssMemCloudwalkPersonRegisterInfoModel();
        registerInfoModel.setCorpId((String) parentMap.get("corpId"));
        registerInfoModel.setFaceId((String) parentMap.get("personUniqueFaceNo"));
        //更新服务器人脸情况下，返回参数不存在：应用编号	APPLICATION_ID；业务编号	BUSINESS_ID
        registerInfoModel.setApplicationId((String) insertDataMap.get("applicationId"));
        registerInfoModel.setBusinessId((String) insertDataMap.get("businessId"));
        registerInfoModel.setFlownum((String) param.get("flowNum"));
        registerInfoModel.setServerRegisterInfoCode((String) insertDataMap.get("id"));//服务器注册，返回字段
        registerInfoModel.setChannelCode(environment.getProperty("bcss.default.cloudwalk.channelCode"));
        registerInfoModel.setServiceModelVersion(environment.getProperty("bcss.default.cloudwalk.serviceModelVersion"));
        registerInfoModel.setOrgcode(environment.getProperty("bcss.default.cloudwalk.orgCode"));
        registerInfoModel.setPeopletype((String) insertDataMap.get("type"));
        registerInfoModel.setViplevel((String) insertDataMap.get("vipLevel"));
        registerInfoModel.setCerttype((String) insertDataMap.get("certType"));
        registerInfoModel.setCertno((String) insertDataMap.get("certNo"));
        registerInfoModel.setName((String) insertDataMap.get("name"));
        registerInfoModel.setSex(String.valueOf(insertDataMap.get("sex")));//查询接口返回的是int类型
        registerInfoModel.setBirthday((String) insertDataMap.get("birthday"));
        registerInfoModel.setDuty((String) insertDataMap.get("duty"));
        registerInfoModel.setPosition((String) insertDataMap.get("position"));
        registerInfoModel.setTelephone((String) insertDataMap.get("telephone"));
        registerInfoModel.setEmail((String) insertDataMap.get("email"));
        registerInfoModel.setAddress((String) insertDataMap.get("address"));
        registerInfoModel.setRemark((String) insertDataMap.get("remark"));
        registerInfoModel.setProvince((String) insertDataMap.get("province"));
        registerInfoModel.setCity((String) insertDataMap.get("city"));
        registerInfoModel.setArea((String) insertDataMap.get("area"));
        return registerInfoModel;
    }

    //服务器注册返回
    private List<BcssMemCloudwalkPersonRegisterFeatureDetailModel> constructRegisterFeatureDetailModelByInsert(Map<String, Object> parentMap, Map<String, Object> param, Map<String, Object> queryResult) {
        Map<String, Object> queryDataMap = (Map<String, Object>) param.get(MemberConstants.KEY_RESPONSE_DATA);
//        Map<String, Object> queryDataMap = JSONObject.parseObject(queryDataStr, Map.class);//人员查询接口回来的data数据

        List<Map<String, Object>> personPics = (List<Map<String, Object>>) queryDataMap.get("personPics");

        List<BcssMemCloudwalkPersonRegisterFeatureDetailModel> bcssMemCloudwalkPersonRegisterFeatureDetailModelList = new ArrayList<>();
        for (Map singleMap : personPics) {
            LOGGER.info("循环读取特征信息:" + JSONObject.toJSONString(singleMap));
            BcssMemCloudwalkPersonRegisterFeatureDetailModel registerFeatureDetailModel = new BcssMemCloudwalkPersonRegisterFeatureDetailModel();
            String filePathName = (String) singleMap.get("file");
            String fileType = String.valueOf(singleMap.get("type"));
            String subType = String.valueOf(singleMap.get("subType"));
            String fileId = (String) singleMap.get("id");

            registerFeatureDetailModel.setCorpId((String) parentMap.get("corpId"));
            registerFeatureDetailModel.setFaceId((String) parentMap.get("personUniqueFaceNo"));
            registerFeatureDetailModel.setFlownum((String) param.get("flowNum"));
            registerFeatureDetailModel.setServerRegisterInfoCode((String) queryDataMap.get("id"));
            registerFeatureDetailModel.setPersonFeatureType(fileType);
            registerFeatureDetailModel.setPersonFeatureSubType(subType);
            registerFeatureDetailModel.setPersonFeatureSavePath(filePathName);
            registerFeatureDetailModel.setPersonFeatureId(fileId);

            bcssMemCloudwalkPersonRegisterFeatureDetailModelList.add(registerFeatureDetailModel);
        }
        return bcssMemCloudwalkPersonRegisterFeatureDetailModelList;
    }


    //获取固定参数--注册
    public Map<String, Object> getParamFromFile(Map<String, Object> parm) {
        Map<String, Object> result = new HashMap<>();
        //渠道编号
        String channel = environment.getProperty("bcss.default.cloudwalk.channelCode");
        if (channel == null || channel.isEmpty()) {
            throw new BusinessException("bcss.default.cloudwalk.channelCode:" + "在application.properties配置文件中为空！");
        }

        //获取orgCode-机构代码
        String orgCode = environment.getProperty("bcss.default.cloudwalk.orgCode");
        if (orgCode == null || orgCode.isEmpty()) {
            throw new BusinessException("bcss.default.cloudwalk.orgCode:" + "在application.properties配置文件中为空！");
        }

        //获取peopleType-人员类型
        String peopleType = environment.getProperty("bcss.default.cloudwalk.peopleType");
        if (orgCode == null || orgCode.isEmpty()) {
            throw new BusinessException("bcss.default.cloudwalk.peopleType:" + "在application.properties配置文件中为空！");
        }

        //获取peopleType-人员类型
        String vipLevel = environment.getProperty("bcss.default.cloudwalk.vipLevel");
        if (orgCode == null || orgCode.isEmpty()) {
            throw new BusinessException("bcss.default.cloudwalk.vipLevel:" + "在application.properties配置文件中为空！");
        }

        result.put("channelCode", channel);
        result.put("orgCode", orgCode);
        result.put("type", peopleType);
        result.put("vipLevel", vipLevel);
        LOGGER.info("人脸注册固定参数：" + JSONObject.toJSONString(result));

        return result;
    }

    //调用人员查询或者人脸更新的参数组合
    public Map<String, Object> getParamForQueryOrUpdate(Map<String, Object> param) {
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

}

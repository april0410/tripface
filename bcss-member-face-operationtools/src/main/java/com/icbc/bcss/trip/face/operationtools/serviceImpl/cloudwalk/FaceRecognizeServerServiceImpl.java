package com.icbc.bcss.trip.face.operationtools.serviceImpl.cloudwalk;

import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.enums.FaceRecognizeType;
import com.icbc.bcss.trip.face.operationtools.exception.BusinessException;
import com.icbc.bcss.trip.face.operationtools.http.HttpOperation;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceRecognizeServerService;
import com.icbc.bcss.trip.face.operationtools.utils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

//@Service("faceRecognizeServerService")
public class FaceRecognizeServerServiceImpl implements FaceRecognizeServerService {
    private static final String recognizeUrlDefault = "bcss.default.face.recognition_URL";

    private static final Logger LOGGER = LoggerFactory.getLogger(FaceRecognizeServerServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PropertyPlaceholder environment;


    /*
    * 函数：1:1识别
    * */
    @Override
    public Map<String,Object> recognizeInServerInOne(Map<String,Object> param) {
        //获取url
        String clientTransNo= (String) param.get(MemberConstants.CLIENTTRANSNO);
        Map<String,String> loadParamMap=DefinePropertiesFileUtils.getInstance().commonloadParam("bcss.default.face.recognize_One_URL");
        String recognizeUrl=null;
        if(! (MemberConstants.COMMON_SUCCESS.equals(loadParamMap.get(MemberConstants.RETURN_CODE))) ){
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL,loadParamMap.get(MemberConstants.RETURN_MSG),clientTransNo);
        }else{
            recognizeUrl=loadParamMap.get(MemberConstants.PROPERTIES_VALUE);
        }

        //参数构造
        Map<String, Object> request = new HashMap<>();
        try{
            Map<String,Object> res=getParamFromFile(param);
            if(res!=null&&res.size()>0){
                LOGGER.debug("人脸识别(1:1)参数成功获取");
                request.putAll(res);
            }else{
                throw  new BusinessException("人脸识别(1:1)参数为空");
            }
        }catch (BusinessException e){
            LOGGER.error("人脸识别(1:1)参数为空，异常："+e.getMessage());
            LOGGER.error(e.toString(),e);
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION,e.getMessage(),clientTransNo);
        }

        String faceDataA= (String) param.get("imageA");
        String faceDataB= (String) param.get("imageB");

        //把传入的参数放入Request
        request.put("imgA",faceDataA);
        request.put("imgB",faceDataB);

        //请求服务器
        LOGGER.debug("人脸识别(1:1)的请求参数为："+JSONObject.toJSONString(request));
        try {
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(recognizeUrl, HttpOperation.generatePostJson(request), String.class);
            String StringResponse = apiResponse.getBody();
            LOGGER.warn("人脸识别(1:1)后返回的请求体为："+StringResponse);

            Map<String,Object> returnMap=new HashMap<>();
            if (StringResponse==null||StringResponse.isEmpty()) {
                returnMap.put(MemberConstants.RETURN_CODE,FaceRecognizeType.FACE_RECOGNIZE_SERVICE_RETURN_EMPTY.getCode());
                returnMap.put(MemberConstants.RETURN_MSG,FaceRecognizeType.FACE_RECOGNIZE_SERVICE_RETURN_EMPTY.getDesc());
                System.out.println("人脸识别调用返回空结果");
                returnMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
            }else{
                Map<String,Object> cloudReturnp= JSONObject.parseObject(StringResponse,Map.class);
                returnMap = FaceReturnConvertCommonReturn.converFieldName(cloudReturnp);
                if(MemberConstants.VALUE_FULL_SUCC.equals(cloudReturnp.get(MemberConstants.KEY_RESPONSE_CODE))){
                    returnMap.put(MemberConstants.RETURN_CODE,"0");
                }
                returnMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
            }
            return returnMap;

        } catch (Exception e) {
            LOGGER.error("请求云从服务器1:1服务异常："+e.getMessage());
            LOGGER.error(e.toString(),e);
            Map<String,Object> exceptionMap=new HashMap<>();
            exceptionMap.put(MemberConstants.RETURN_CODE, FaceRecognizeType.FACE_RECOGNIZE_SERVICE_RETURN_EXCEPTION.getCode());
            exceptionMap.put(MemberConstants.RETURN_MSG,FaceRecognizeType.FACE_RECOGNIZE_SERVICE_RETURN_EXCEPTION.getDesc());
            System.out.println("人脸识别(1:1)调用抛出异常");
            exceptionMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
            return exceptionMap;
        }
//        return null;
    }

    /*
    * 函数：1：N人脸识别
    * */
    @Override
    public Map<String, Object> recognizeInServerInN(Map<String, Object> param) {
        //获取url
        String clientTransNo= (String) param.get(MemberConstants.CLIENTTRANSNO);
        Map<String,String> loadParamMap=DefinePropertiesFileUtils.getInstance().commonloadParam("bcss.default.face.recognize_N_URL");
        String recognizeUrl=null;
        if(! (MemberConstants.COMMON_SUCCESS.equals(loadParamMap.get(MemberConstants.RETURN_CODE))) ){
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL,loadParamMap.get(MemberConstants.RETURN_MSG),clientTransNo);
        }else{
            recognizeUrl=loadParamMap.get(MemberConstants.PROPERTIES_VALUE);
        }

        //参数构造
        Map<String, Object> request = new HashMap<>();
        try{
            Map<String,Object> res=getParamFromFile(param);
            if(res!=null&&res.size()>0){
                LOGGER.debug("人脸识别(1:N)参数成功获取");
                request.putAll(res);
            }else{
                throw  new BusinessException("人脸识别(1:N)参数为空");
            }
        }catch (BusinessException e){
            LOGGER.error("人脸识别(1:N)参数为空，异常："+e.getMessage());
            LOGGER.error(e.toString(),e);
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION,e.getMessage(),clientTransNo);
        }

        //从集合获取参数
        String faceData= (String) param.get("image");
        String corpId= (String) param.get("corpId");
        //默认库编码的构成规则
        String groupCode=corpId+MemberConstants.DEFAULT_SEPARATOR_FROM_PERSONID_TO_SEQNO+MemberConstants.DEFAULT_GROUP_SEQNO;
        String topN= (String) param.get("topN");

        //把传入的参数放入Request
        request.put("img",faceData);
        request.put("groupCode",groupCode);
        request.put("topN",topN);

        //请求服务器
        LOGGER.warn("人脸识别(1:N)的请求参数为："+JSONObject.toJSONString(request));
        try {
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(recognizeUrl, HttpOperation.generatePostJson(request), String.class);
            String StringResponse = apiResponse.getBody();
            LOGGER.warn("人脸识别(1:N)后返回的请求体为："+StringResponse);

            Map<String,Object> returnMap=new HashMap<>();
            if (StringResponse==null||StringResponse.isEmpty()) {
                returnMap.put(MemberConstants.RETURN_CODE,FaceRecognizeType.FACE_RECOGNIZE_SERVICE_RETURN_EMPTY.getCode());
                returnMap.put(MemberConstants.RETURN_MSG,FaceRecognizeType.FACE_RECOGNIZE_SERVICE_RETURN_EMPTY.getDesc());
                System.out.println("人脸识别1:N调用返回空结果");
                returnMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
            }else{
                Map<String,Object> cloudReturnp= JSONObject.parseObject(StringResponse,Map.class);
                returnMap = FaceReturnConvertCommonReturn.converFieldName(cloudReturnp);
                if(MemberConstants.VALUE_FULL_SUCC.equals(cloudReturnp.get(MemberConstants.KEY_RESPONSE_CODE))){
                    returnMap.put(MemberConstants.RETURN_CODE,"0");
                }
                returnMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
            }
            return returnMap;

        } catch (Exception e) {
            LOGGER.error("请求云从服务器1:N服务异常："+e.getMessage());
            LOGGER.error(e.toString(),e);
            Map<String,Object> exceptionMap=new HashMap<>();
            exceptionMap.put(MemberConstants.RETURN_CODE, FaceRecognizeType.FACE_RECOGNIZE_SERVICE_RETURN_EXCEPTION.getCode());
            exceptionMap.put(MemberConstants.RETURN_MSG,FaceRecognizeType.FACE_RECOGNIZE_SERVICE_RETURN_EXCEPTION.getDesc());
            System.out.println("人脸识别(1:N)调用抛出异常");
            exceptionMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
            return exceptionMap;
        }
//        return null;
    }

    public Map<String,Object> getParamFromFile(Map<String, Object> parm){
        Map<String,Object> result=new HashMap<>();

        //渠道编号
        String channel = environment.getProperty("bcss.default.cloudwalk.channelCode");
        if (channel == null || channel.isEmpty()) {
            throw new BusinessException("bcss.default.cloudwalk.channelCode:" + "在application.properties配置文件中为空！");
        }

        result.put("channelCode", channel);
        LOGGER.info("人脸识别固定参数："+ JSONObject.toJSONString(result));

        return result;
    }


    @Deprecated
    public Map<String,Object> recognizeInServer(String faceData) {
        String recognizeUrl = environment.getProperty("bcss.default.face.recognize_URL");
        if (recognizeUrl == null) {
            recognizeUrl = recognizeUrlDefault;
        }
//        JSONObject jo = new JSONObject();
//        jo.put("type", "2");
//        jo.put("tradingCode", "0640");
//        jo.put("channel", "0340");
//        jo.put("modelVersion", "0814");
//        jo.put("faceData", faceData);

        Map<String, Object> request = new HashMap<>();
        request.put("type", "2");
        request.put("tradingCode", "0640");
        request.put("orgCode","0000");
        request.put("channel", "0340");
        request.put("modelVersion", "0814");
        request.put("faceData", faceData);
//        request.put("timestamp", new Date().toString());

        try {
//            String resp = HttpUtils.doHttpPost("recognizeUrl", jo.toJSONString());
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(recognizeUrl, HttpOperation.generatePostJson(request), String.class);
            String StringResponse = apiResponse.getBody();
            LOGGER.debug("人脸识别后返回的请求体为："+StringResponse);

            Map<String,Object> returnMap=new HashMap<>();
            if (StringResponse==null||StringResponse.isEmpty()) {
                returnMap.put("respCode","-1");
                returnMap.put("respDesc","");
                returnMap.put("data",null);
            }else{
                returnMap= JSONObject.parseObject(StringResponse,Map.class);
            }
            return returnMap;

        } catch (Exception e) {
            e.printStackTrace();
//            LOGGER.error(e.toString());
            LOGGER.error(e.getMessage(),e);
            Map<String,Object> exceptionMap=new HashMap<>();
            exceptionMap.put("respCode", FaceRecognizeType.FACE_RECOGNIZE_SERVICE_RETURN_EXCEPTION.getCode());
            exceptionMap.put("respDesc",FaceRecognizeType.FACE_RECOGNIZE_SERVICE_RETURN_EXCEPTION.getDesc());
            exceptionMap.put("data",null);
//            System.out.println("人脸识别调用出错");
            return exceptionMap;

        }
//        return 0.0D;
    }


}
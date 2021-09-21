package com.icbc.bcss.trip.face.operationtools.http;

import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.utils.MemberConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service("httpBaseService")
public class HttpBaseServiceImpl implements HttpBaseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpBaseServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Map<String, Object> execute(Map<String, Object> param, String httpurl) {
        LOGGER.warn("*************************************"+"当前请求url为："+httpurl+"*************************************");
        Map<String,Object> result=new HashMap<>();
        if(param==null||param.isEmpty()){
            LOGGER.error("参数集合为空，无法正常执行网络请求");
            result.put(MemberConstants.RETURN_CODE,"1");
            result.put(MemberConstants.RETURN_MSG,"参数集合为空，无法正常执行网络请求");
            return result;
        }

        //请求服务器
        LOGGER.error(httpurl+":请求参数为："+ JSONObject.toJSONString(param));
        ResponseEntity<String> apiResponse=null;
        try {
            apiResponse= restTemplate.postForEntity(httpurl, HttpOperation.generatePostJson(param), String.class);
        }catch (Exception e){
            LOGGER.error("网络请求过程中发生异常,请求URL为:"+httpurl+" ,具体异常为:"+e.getMessage());
            LOGGER.error(e.toString()+"",e);
            result.put(MemberConstants.RETURN_CODE,"-1");
            result.put(MemberConstants.RETURN_MSG,"网络请求过程中发生异常,请求URL为:"+httpurl+" ,具体异常为:"+e.toString());
            return result;
        }
        //请求成功
        LOGGER.error(httpurl+":返回结果为："+ apiResponse);

        //如果为空
        if(apiResponse==null){
            LOGGER.error("网络请求返回结果为空" );
            result.put(MemberConstants.RETURN_CODE,"4");
            result.put(MemberConstants.RETURN_MSG,"网络请求返回结果为空");
            return result;
        }

        //返回有httpstatus
        if (apiResponse.getStatusCodeValue()!=200){
            //非请求成功
            LOGGER.error("网络请求返回的httpStatus为："+apiResponse.getStatusCodeValue()+"，非成功请求");
            result.put(MemberConstants.RETURN_CODE,"5");
            result.put(MemberConstants.RETURN_MSG,"网络请求返回的httpStatus为："+apiResponse.getStatusCodeValue()+"，非成功请求");
            result.put("httpStatusCodeValue",String.valueOf(apiResponse.getStatusCodeValue()));
            return result;
        }else{
            result.put(MemberConstants.RETURN_CODE,"0");
            result.put(MemberConstants.RETURN_MSG,"网络请求完全成功");
            result.put("httpStatusCodeValue",String.valueOf(apiResponse.getStatusCodeValue()));
            result.put("httpBody",JSONObject.parseObject(apiResponse.getBody(),Map.class));
            return result;
        }
    }

    @Override
    /*
    * 总入口
    * */
    public String  executeAll(Map<String, Object> param, String httpurl) {
        //返回结果
        Map httpresult=execute(param,httpurl);
        String returnCode= (String) httpresult.get(MemberConstants.RETURN_CODE);
        //判断
        if(returnCode==null||returnCode.isEmpty()){
            LOGGER.error("http请求返回有异常："+ JSONObject.toJSONString(httpresult));
            return null;
        }

        if(!"0".equals(returnCode)){
            LOGGER.error("http请求返回失败："+ JSONObject.toJSONString(httpresult));
            return null;
        } else {
            String httpStatusValue= (String) httpresult.get("httpStatusCodeValue");
            Map<String,Object> bodyString= (Map<String, Object>) httpresult.get("httpBody");
            if(bodyString==null||bodyString.isEmpty()){
                String msg="当前网络请求状态为："+httpStatusValue+"，返回响应体为空："+bodyString+"！冲突异常，请检查日志";
                LOGGER.error(msg);
                return null;
            }

            //判断是否json格式
//            try{
//                JSONObject.parseObject(bodyString,Map.class);
//            }catch (Exception e){
//                LOGGER.error("网络请求的响应结果转换为key-value的json格式异常，"+e.getMessage());
//                LOGGER.error(e.toString(),e);
//                return null;
//            }

            return JSONObject.toJSONString(bodyString);
        }

    }


    @Deprecated
    public static String executeWithoutProxy(){

        return null;
    }
}

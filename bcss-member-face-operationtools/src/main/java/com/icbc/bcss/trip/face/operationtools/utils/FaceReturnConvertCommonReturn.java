package com.icbc.bcss.trip.face.operationtools.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class FaceReturnConvertCommonReturn {
    private static final Logger LOGGER = LoggerFactory.getLogger(FaceReturnConvertCommonReturn.class);
    public static Map<String,Object> converFieldName(Map<String, Object> param){
        LOGGER.warn("------***"+"转换开始"+"***------");
        Map<String,Object> returnMap=new HashMap<>();
        returnMap.putAll(param);
        returnMap.put(MemberConstants.RETURN_CODE,param.get(MemberConstants.KEY_RESPONSE_CODE));
        returnMap.put(MemberConstants.RETURN_MSG,param.get(MemberConstants.KEY_RESPONSE_MESSAGE));
        return returnMap;
    }
}

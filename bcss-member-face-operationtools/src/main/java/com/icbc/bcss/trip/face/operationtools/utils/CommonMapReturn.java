package com.icbc.bcss.trip.face.operationtools.utils;

import java.util.HashMap;
import java.util.Map;

public class CommonMapReturn {

    public static Map<String,Object> constructMapWithClientTransNo(String code,String msg,String clientTransNo){
        HashMap<String,Object> result=new HashMap<>();
        result.put(MemberConstants.RETURN_CODE,code);
        result.put(MemberConstants.RETURN_MSG,msg);
        result.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
        return result;
    }

    public static Map<String,Object> constructMapWithSerialNo(String code,String msg,String serialNo){
        HashMap<String,Object> result=new HashMap<>();
        result.put(MemberConstants.RETURN_CODE,code);
        result.put(MemberConstants.RETURN_MSG,msg);
        result.put(MemberConstants.CLIENTTRANSNO,serialNo);
        return result;
    }
}

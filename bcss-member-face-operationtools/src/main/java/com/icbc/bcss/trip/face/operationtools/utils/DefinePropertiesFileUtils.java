package com.icbc.bcss.trip.face.operationtools.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.enums.PropertiesType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DefinePropertiesFileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefinePropertiesFileUtils.class);

    private static DefinePropertiesFileUtils singleton=null;
    private DefinePropertiesFileUtils(){};

    public static DefinePropertiesFileUtils getInstance(){
        synchronized (DefinePropertiesFileUtils.class){
            if(singleton == null){
                singleton = new DefinePropertiesFileUtils();
            }
        }
        return singleton;
    }

    @Autowired
    private PropertyPlaceholder environment;

    public Map<String,String>  commonSaveParam(String key , String Value) {
        HashMap<String,String> res=new HashMap<>();

        PropertisUtils.initProperties();
        String setRes=PropertisUtils.setPropertiesValue(key,Value);
        if (setRes==null){
            LOGGER.error("动态参数初始化key-value存储异常！！！！");
            res.put(MemberConstants.RETURN_CODE,MemberConstants.COMMON_FAIL);
            res.put(MemberConstants.RETURN_MSG,"动态参数初始化key-value存储异常");
            return res;
        }else{
            LOGGER.info("动态参数初始化key-value存储成功");
        }

        String dateTime=TransNoUtils.getInstance().getTimeStamp();
        String storeRes=PropertisUtils.storeFile(dateTime);
        if(storeRes.equals("0")){
            LOGGER.info("动态参数存储到文件成功！！！！");
        }else {
            LOGGER.error("动态参数存储到文件异常失败！！！！");
            res.put(MemberConstants.RETURN_CODE,MemberConstants.COMMON_FAIL);
            res.put(MemberConstants.RETURN_MSG,"动态参数存储到文件异常失败");
            return res;
        }

        //构造返回参数
        res.put(MemberConstants.RETURN_CODE,MemberConstants.COMMON_SUCCESS);
        res.put(MemberConstants.RETURN_MSG,"成功存储参数到动态文件");
        return res;
    }



    public Map<String,String> commonloadParam(String key){
        PropertisUtils.initProperties();
        String value=PropertisUtils.getPropertiValue(key);
        HashMap<String,String> res=new HashMap<>();

        if (value==null||value.isEmpty()){
            LOGGER.warn("动态参数文件读取的key为："+key+"，value为："+value+"。需要读取application.properties的默认值"+environment.getProperty(key));
            value=environment.getProperty(key);
            if(value==null||value.isEmpty()){
                LOGGER.error("application.properties文件读取的key为："+key+"，value为："+value+"。即该属性值为空");
                res.put(MemberConstants.RETURN_CODE,MemberConstants.COMMON_FAIL);
                res.put(MemberConstants.RETURN_MSG,"读取动态参数文件和静态参数文件失败，请检查");
            }
            else{
                LOGGER.warn("动态参数文件读取的key为："+key+"，value为："+value+"，另外application.properties的默认值"+environment.getProperty(key));
                res.put(MemberConstants.RETURN_CODE,MemberConstants.COMMON_SUCCESS);
                res.put(MemberConstants.RETURN_MSG,"获取属性值成功");
                res.put(MemberConstants.PROPERTIES_VALUE,value);
                res.put(MemberConstants.PROPERTIES_VALUE_SOURCE, PropertiesType.PROPERTIES_VALUE_SOURCE_APPLICATION.getCode());
            }
        }else {
            LOGGER.debug("动态参数文件读取的key为："+key+"，value为："+value+"。不用读取application.properties的默认值"+environment.getProperty(key));//application.properties可能不在此值
            res.put(MemberConstants.RETURN_CODE,MemberConstants.COMMON_SUCCESS);
            res.put(MemberConstants.RETURN_MSG,"获取属性值成功");
            res.put(MemberConstants.PROPERTIES_VALUE,value);
            res.put(MemberConstants.PROPERTIES_VALUE_SOURCE, PropertiesType.PROPERTIES_VALUE_SOURCE_DEFINE.getCode());
        }
        return res;
    }
}

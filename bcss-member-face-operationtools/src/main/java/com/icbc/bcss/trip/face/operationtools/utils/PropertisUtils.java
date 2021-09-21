package com.icbc.bcss.trip.face.operationtools.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertisUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertisUtils.class);

    private static Properties propertis = new Properties();

    public static final String propertyFileName = "define.properties";

    //初始化
    public static void initProperties() {
        String absolutePath = System.getProperty("user.dir");
        String osName = System.getProperties().getProperty("os.name");
        LOGGER.info("osname:" + osName);

        //通过当前的绝对路径+分隔符+文件名
        String fileName = absolutePath + File.separator + propertyFileName;
        try {
            File file = new File(fileName);
            if (!file.exists())
                file.createNewFile();
        } catch (IOException e) {
//            e.printStackTrace();
            LOGGER.error("---动态参数文件--初始化过程（创建文件）异常，具体为：",e);
            LOGGER.error(e.toString());
            LOGGER.error(e.getMessage(),e);
        }

        //尝试获取自定义文件
        try {
            propertis.load(new FileInputStream(fileName));

        } catch (Exception e) {
//            e.printStackTrace();
            LOGGER.error("---动态参数文件--初始化（读入文件）异常，具体为：",e);
            LOGGER.error(e.toString());
            LOGGER.error(e.getMessage(),e);
        }

//        return propertis;
    }

    //获取文件的key-value
    public static String getPropertiValue(String key) {
        if (propertis != null) {
            return propertis.getProperty(key);
        }
        return null;
    }


    public static String setPropertiesValue(String key, String value) {
        if (key != null && value != null && propertis!=null) {
            LOGGER.info("setPropertiesValue--key:"+key+",value:"+value+",propertis:"+propertis.toString());
            Object res = propertis.setProperty(key, value);
//            LOGGER.info("存储动态参数结果："+res.toString());
            if (res == null) {
//                return "success_put_for_new_key";
            } else {
//                return "success_put_for_old_key";
                LOGGER.info(res.toString());
            }
            return "0";
        }
        return null;
    }

    public static String storeFile(String comment) {
        String absolutePath = System.getProperty("user.dir");
        String fileName = absolutePath + File.separator + propertyFileName;
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(fileName);

            //存储键值
            propertis.store(fileOutputStream, comment);
        } catch (Exception e) {
//            e.printStackTrace();
            LOGGER.error("---动态参数文件--保存键值到文件异常，具体为：",e);
            LOGGER.error(e.toString());
            LOGGER.error(e.getMessage(),e);
        }

        if (fileOutputStream == null) {
            LOGGER.error("fileOutputStream is null!保存键值到文件失败");
            return "1";
        }else{

            LOGGER.error("fileOutputStream is not null!保存键值到文件成功");
            return "0";
        }

    }

}
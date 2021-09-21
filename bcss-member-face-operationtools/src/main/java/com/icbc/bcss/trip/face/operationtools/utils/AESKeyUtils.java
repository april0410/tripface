package com.icbc.bcss.trip.face.operationtools.utils;


import com.icbc.api.utils.IcbcEncrypt;
import com.icbc.bcss.trip.face.operationtools.configuration.DataKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AESKeyUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(AESKeyUtils.class);

    public static String decrypt(String cipher){

        //对密文进行解密--商户密钥
        String plainKey=null;
        try {
            plainKey = IcbcEncrypt.decryptContent(cipher, "AES", DataKey.AES_KEY, "UTF-8");
            if (plainKey == null || plainKey.isEmpty()) {
                LOGGER.error("密文解密失败，请看日志！密文为："+cipher);
                return null;
            } else {
                LOGGER.info("密文解密成功");
            }
        }catch (Exception e){
            LOGGER.error("解密密文异常，打印异常，返回空值!密文为："+cipher);
            LOGGER.error(e.getMessage());
            LOGGER.error(e.toString(),e);
            return null;
        }
        return plainKey;
    }
}

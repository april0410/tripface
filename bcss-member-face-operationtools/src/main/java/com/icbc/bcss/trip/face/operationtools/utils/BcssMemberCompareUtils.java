package com.icbc.bcss.trip.face.operationtools.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BcssMemberCompareUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(BcssMemberCompareUtils.class);

    /*
     * 子函数--判断拉取人脸的行业类型是否命中本地指定允许注册人脸的行业类型，命中一个就可以
     * */
    public static boolean judgeCloudIndustryHintInLocal(String localIndustry, String cloudIndustry) {
        //localdustry保证有值，
        //如果cloudindustry没有值，但是有人脸（脏数据），则视为没有命中本地行业，不允许注册
        //用英文逗号分割
        String[] localArr = localIndustry.split(",");
        String[] cloudArr = cloudIndustry.split(",");

        boolean breakFlag = false;
        for (int i = 0; i < cloudArr.length; i++) {
            String tempCloud = cloudArr[i];
            if (tempCloud == null || tempCloud.isEmpty()) {
                LOGGER.debug("当前云端的行业标识为：" + tempCloud + ",没有命中");
                continue;
            }
            for (int j = 0; j < localArr.length; j++) {
                if (tempCloud.equals(localArr[j])) {
                    LOGGER.debug("当前云端的行业标识为：" + tempCloud + ",本地标识为：" + localArr[j] + ",命中本地设置的标识");
                    breakFlag = true;
                    break;
                }
            }

            if (breakFlag == true) {
                break;
            }
            LOGGER.debug("当前云端的行业标识为：" + tempCloud + ",已经判断完，没有命中");
        }

        if (breakFlag == true) {
            return true;
        } else {
            return false;
        }
    }
}

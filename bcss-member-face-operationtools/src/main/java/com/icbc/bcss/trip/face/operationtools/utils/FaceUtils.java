package com.icbc.bcss.trip.face.operationtools.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;


public class FaceUtils {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(FaceUtils.class);

    public static boolean compareScoreINSenceN(String factScore, String defaultScore) {
        if (StringUtils.isBlank(factScore)||StringUtils.isBlank(defaultScore)){
            LOGGER.error("传入的人脸分数和人脸阈值有存在空值情况，默认返回false");
            return false;
        }

        try {
            //获取实际分数
            BigDecimal scoreBigDecimal = new BigDecimal(factScore);
            //获取标准
            BigDecimal defaultBigDecimal = new BigDecimal(defaultScore);


//        比较大小：
//        int a = bigdemical.compareTo(bigdemical2)
//        a == -1,表示bigdemical小于bigdemical2；
//        a == 0,表示bigdemical等于bigdemical2；
//        a == 1,表示bigdemical大于bigdemical2；
            boolean flag = false;
            int compareRes = scoreBigDecimal.compareTo(defaultBigDecimal);
            if (compareRes == 0 || compareRes == 1) {
                LOGGER.warn("实际人脸识别分数为：" + factScore + ",默认人脸阈值为：" + defaultScore + "! 人脸成功验证");
                flag=true;
            } else {
                LOGGER.warn("实际人脸识别分数为：" + factScore + ",默认人脸阈值为：" + defaultScore + "! 人脸校验失败");
                flag=false;
            }
            //返回结果
            return flag;
        } catch (Exception e) {
            LOGGER.error("传入的人脸分数非标准数字，默认人脸阈值校验不通过，请重新上送");
            return false;
        }

    }
}

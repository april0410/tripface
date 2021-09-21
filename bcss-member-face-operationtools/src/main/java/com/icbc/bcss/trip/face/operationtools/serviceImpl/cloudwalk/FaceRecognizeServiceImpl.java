package com.icbc.bcss.trip.face.operationtools.serviceImpl.cloudwalk;

import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemCustBaseInfoModel;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemCustBaseInfoBaseService;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceRecognizeDesktopServer;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceRecognizeServerService;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceRecognizeService;
import com.icbc.bcss.trip.face.operationtools.utils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Service("faceRecognizeService")
public class FaceRecognizeServiceImpl implements FaceRecognizeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FaceRecognizeServiceImpl.class);

    @Autowired
    private FaceRecognizeServerService faceRecognizeServerService;

    @Autowired
    private FaceRecognizeDesktopServer faceRecognizeDesktopServer;

    @Autowired
    private PropertyPlaceholder environment;

    @Autowired
    private BcssMemCustBaseInfoBaseService bcssMemCustBaseInfoBaseService;

    /*
     * 函数：根据版本设置，进入服务器接口或者pc接口；同时只实现1:1识别对比
     * */
    @Override
    public Map<String, Object> recognizeServiceInOne(Map<String, Object> param) {
        String clientTransNo = (String) (param.get(MemberConstants.CLIENTTRANSNO));
        LOGGER.warn("------***" + "开始人脸识别(1:1)流程,流水号为：" + clientTransNo + "***------");
        String versionnew = environment.getProperty("bcss.default.face.hardwareVersion");
        Map<String, Object> result = new HashMap<>();
        if (versionnew.equals("2")) {
            LOGGER.error("当前本地人脸服务平台的版本设置为Server Level");
            result = faceRecognizeServerService.recognizeInServerInOne(param);//有失败，有成功
//            return faceDeleteServerService.deleteInServer(param);
            //拆分结果，重新组织
            if (MemberConstants.COMMON_SUCCESS.equals(result.get(MemberConstants.RETURN_CODE))) {
                Map finalRes = dealResultWithOneByOne(result);
                return finalRes;
            }
            return result;

        } else if (versionnew.equals("1")) {
            LOGGER.error("当前本地人脸服务平台的版本设置为PC Level，但功能尚在规划，请确认！！！");
//            faceRecognizeDesktopServer.recognizeInDesktop(param);
            result.put(MemberConstants.RETURN_CODE, MemberConstants.COMMON_FAIL);
            result.put(MemberConstants.RETURN_MSG, "当前本地人脸服务平台的版本设置为PC Level，但功能尚在规划，请确认");
            result.put(MemberConstants.CLIENTTRANSNO, clientTransNo);
//            return result;
        } else {
            LOGGER.error("当前获取本地人脸服务平台的版本不存在功能接口，请关闭程序检测！！！");
//            Map<String,Object> result=new HashMap<>();
            result.put(MemberConstants.RETURN_CODE, MemberConstants.COMMON_FAIL);
            result.put(MemberConstants.RETURN_MSG, "当前获取本地人脸服务平台的版本不存在功能接口，请关闭程序检测！！！");
//            return result;
            result.put(MemberConstants.CLIENTTRANSNO, clientTransNo);
        }
        LOGGER.warn("------***" + "结束人脸识别(1:1)流程,流水号为：" + clientTransNo + "***------");
        return result;
    }

    /*
     * 函数：拆解http的请求，按我们的返回组装
     * */
    private Map dealResultWithOneByOne(Map<String, Object> result) {
        Map<String, Object> data = (Map<String, Object>) result.get(MemberConstants.KEY_RESPONSE_DATA);
        String clientTransNo= (String) result.get(MemberConstants.CLIENTTRANSNO);
        String factScore=String.valueOf(data.get("score"));
        //判断默认阈值
        String compareScore = environment.getProperty("bcss.face.oneBYone.threshold");
        if (compareScore == null || "".equals(compareScore)) {
            compareScore = "0.80";
            LOGGER.warn("人脸1：1功能配置的默认人脸阈值为空，因此采用代码默认阈值：" + compareScore);
        }
        //格式异常
        boolean numberFomatCheck = MemberUtil.judgeBigDecimalFormat(compareScore);
        if (numberFomatCheck == false) {
            LOGGER.error("人脸1：1功能配置的默认人脸阈值为格式异常，请重新配置并重启程序：" + compareScore);
            return CommonMapReturn.constructMapWithClientTransNo("-1", "人脸1：1功能配置的默认人脸阈值为格式异常，请重新配置并重启程序：" + compareScore, clientTransNo);
        }
        //判断阈值
        boolean passFlag= FaceUtils.compareScoreINSenceN(factScore,compareScore);
        if(passFlag==false){
            LOGGER.error("1:1人脸比对，当前客户的人脸分数无法通过默认阈值校验，人脸识别对比失败");
            return CommonMapReturn.constructMapWithClientTransNo("-1", "1:1人脸比对，当前客户的人脸分数无法通过默认阈值校验，人脸识别对比失败", clientTransNo);
        }

        Map returnRes = new HashMap<String, Object>();
        returnRes.put(MemberConstants.RETURN_CODE, result.get(MemberConstants.RETURN_CODE));
        returnRes.put(MemberConstants.RETURN_MSG, result.get(MemberConstants.RETURN_MSG));
        returnRes.put(MemberConstants.CLIENTTRANSNO, clientTransNo);
        Map newData = new HashMap<String, Object>();
        newData.put("score", factScore);

        returnRes.put("data", newData);
        return returnRes;
    }


    /*
     * 函数：根据版本设置，进入服务器接口或者pc接口；同时只实现1:N识别对比
     * 同时需要查询数据库，组装报文
     * */
    @Override
    public Map<String, Object> recognizeServiceInN(Map<String, Object> param) {
        String clientTransNo = (String) (param.get(MemberConstants.CLIENTTRANSNO));
        LOGGER.warn("------***" + "开始人脸识别(1:N)流程,流水号为：" + clientTransNo + "***------");
        String versionnew = environment.getProperty("bcss.default.face.hardwareVersion");
        Map<String, Object> result = new HashMap<>();
        if (versionnew.equals("2")) {
            LOGGER.error("当前本地人脸服务平台的版本设置为Server Level");
            result = faceRecognizeServerService.recognizeInServerInN(param);//有失败，有成功
//            return faceDeleteServerService.deleteInServer(param);
            //拆分结果，重新组织
            if (MemberConstants.COMMON_SUCCESS.equals(result.get(MemberConstants.RETURN_CODE))) {
                Map finalRes = dealResultWithOneByN(result);
                return finalRes;
            }
            return result;

        } else if (versionnew.equals("1")) {
            LOGGER.error("当前本地人脸服务平台的版本设置为PC Level，但功能尚在规划，请确认！！！");
//            faceRecognizeDesktopServer.recognizeInDesktop(param);
            result.put(MemberConstants.RETURN_CODE, MemberConstants.COMMON_FAIL);
            result.put(MemberConstants.RETURN_MSG, "当前本地人脸服务平台的版本设置为PC Level，但功能尚在规划，请确认");
            result.put(MemberConstants.CLIENTTRANSNO, clientTransNo);
//            return result;
        } else {
            LOGGER.error("当前获取本地人脸服务平台的版本不存在功能接口，请关闭程序检测！！！");
//            Map<String,Object> result=new HashMap<>();
            result.put(MemberConstants.RETURN_CODE, MemberConstants.COMMON_FAIL);
            result.put(MemberConstants.RETURN_MSG, "当前获取本地人脸服务平台的版本不存在功能接口，请关闭程序检测！！！");
//            return result;
            result.put(MemberConstants.CLIENTTRANSNO, clientTransNo);
        }
        LOGGER.warn("------***" + "结束人脸识别(1:N)流程,流水号为：" + clientTransNo + "***------");
        return result;
//        return null;
    }

    /*
     * 函数：拆解http的请求，按我们的返回组装
     * 场景：1：N
     * */
    private Map dealResultWithOneByN(Map<String, Object> result) {
        List<Map<String, Object>> data = (List<Map<String, Object>>) result.get(MemberConstants.KEY_RESPONSE_DATA);
        String clientTransNo = (String) result.get(MemberConstants.CLIENTTRANSNO);

        //data为空，转换失败
        if (data == null || data.isEmpty()) {
            LOGGER.error("1:N人脸识别结果：data为空，作为识别“失败”结果返回");
            Map returnRes = new HashMap<String, Object>();
            returnRes.put(MemberConstants.RETURN_CODE, "-1");
            returnRes.put(MemberConstants.RETURN_MSG, "1:N人脸识别结果：data为空，作为识别“失败”结果返回");
            returnRes.put(MemberConstants.CLIENTTRANSNO, result.get(MemberConstants.CLIENTTRANSNO));
            return returnRes;
        }

        //成功
        Map returnRes = new HashMap<String, Object>();
        returnRes.put(MemberConstants.RETURN_CODE, "0");
        returnRes.put(MemberConstants.RETURN_MSG, result.get(MemberConstants.RETURN_MSG));
        returnRes.put(MemberConstants.CLIENTTRANSNO, result.get(MemberConstants.CLIENTTRANSNO));

        //判断默认阈值
        String compareScore = environment.getProperty("bcss.face.ONEbyN.threshold");
        if (compareScore == null || "".equals(compareScore)) {
            compareScore = "0.80";
            LOGGER.warn("人脸1：N功能配置的默认人脸阈值为空，因此采用代码默认阈值：" + compareScore);
        }
        //格式异常
        boolean numberFomatCheck = MemberUtil.judgeBigDecimalFormat(compareScore);
        if (numberFomatCheck == false) {
            LOGGER.error("人脸1：N功能配置的默认人脸阈值为格式异常，请重新配置并重启程序：" + compareScore);
            return CommonMapReturn.constructMapWithClientTransNo("-1", "人脸1：N功能配置的默认人脸阈值为格式异常，请重新配置并重启程序：" + compareScore, clientTransNo);
        }

        //循环一下，获取结果
        List<Map<String, Object>> newList = new ArrayList<>();
        for (Map singleMap : data) {
            String personCode = (String) singleMap.get("code");
            String imageId = (String) singleMap.get("imageId");
            String imagePath = (String) singleMap.get("imagePath");
            String ServerPseronCode = (String) singleMap.get("personId");
//            BigDecimal simliarSorce= (BigDecimal) singleMap.get("score");
            BigDecimal simliarSorce = new BigDecimal(singleMap.get("score").toString());

            //判断阈值
            boolean passFlag= FaceUtils.compareScoreINSenceN(singleMap.get("score").toString(),compareScore);
            if(passFlag==false){
                LOGGER.error("客户："+personCode+"，其人脸分数无法通过默认阈值校验，人脸识别失败!分数为："+simliarSorce);
                return CommonMapReturn.constructMapWithClientTransNo("-1", "客户："+personCode+"，其人脸分数无法通过默认阈值校验，人脸识别失败!分数为："+simliarSorce, clientTransNo);
            }

            //查询数据库
            BcssMemCustBaseInfoModel custBaseInfoModel = bcssMemCustBaseInfoBaseService.selectByFaceId(personCode);
            if (custBaseInfoModel == null) {
                LOGGER.error("人脸1：N识别后，查询数据库客户信息为空，异常情况，默认识别失败，当前唯一编号：" + personCode);
                return CommonMapReturn.constructMapWithClientTransNo("-1", "人脸1：N识别后，查询数据库客户信息为空，异常情况，默认识别失败，当前唯一编号：" + personCode, clientTransNo);
            }
            LOGGER.debug("dealResultWithOneByN--查询的信息为：" + JSONObject.toJSONString(custBaseInfoModel));

            Map newData = new HashMap<String, Object>();
            newData.put("personUniqueCode", personCode);
            newData.put("score", String.valueOf(simliarSorce));
            newData.put("personId", custBaseInfoModel.getPersonId());
            newData.put("memName", custBaseInfoModel.getMemname());
            newData.put("memCardNo", custBaseInfoModel.getMemcardno());
            //增加方法默认过滤分数

            newList.add(newData);
        }
        returnRes.put("data", newList);

        return returnRes;
    }
}

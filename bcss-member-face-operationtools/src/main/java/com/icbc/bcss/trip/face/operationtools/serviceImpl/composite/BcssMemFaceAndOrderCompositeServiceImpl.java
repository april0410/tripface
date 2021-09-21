package com.icbc.bcss.trip.face.operationtools.serviceImpl.composite;

import com.alibaba.fastjson.JSONObject;
import com.icbc.api.crypt.AES;
import com.icbc.bcss.member.qrcode.dto.ResultDto;
import com.icbc.bcss.member.qrcode.util.QrCodeAnalysisUtils;
import com.icbc.bcss.trip.face.operationtools.exception.BusinessException;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemOrderPayFaceLogModel;
import com.icbc.bcss.trip.face.operationtools.service.FaceRecognizeAllService;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemOrderPayFaceLogBaseService;
import com.icbc.bcss.trip.face.operationtools.service.composite.BcssMemFaceAndOrderCompositeService;
import com.icbc.bcss.trip.face.operationtools.utils.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Service("bcssMemFaceAndOrderCompositeService")
public class BcssMemFaceAndOrderCompositeServiceImpl implements BcssMemFaceAndOrderCompositeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BcssMemFaceAndOrderCompositeServiceImpl.class);

    @Autowired
    private PropertyPlaceholder environment;

    @Autowired
    private FaceRecognizeAllService faceRecognizeAllService;

    @Autowired
    private BcssMemOrderPayFaceLogBaseService bcssMemOrderPayFaceLogBaseService;

    @Override
    /*
    * 人脸和离线二维码的识别入口
    * */
    public Map<String, Object> execute(Map<String, Object> parentParam) {
        String featureType= (String) parentParam.get("featureType");
        try{
            if("002".equals(featureType)){
                //如果是人脸，调用方法
                return  executeFaceRecognizeService(parentParam);
            }else {
                return executeQrCodeRecognizeService(parentParam);
            }
        }catch (Exception e){
            LOGGER.error("登记订单信息过程中，识别支付消费凭证异常，原因："+e.getMessage());
            LOGGER.error(e.toString(),e);
            return CommonMapReturn.constructMapWithClientTransNo("-2","登记订单信息过程中，识别支付消费凭证异常，原因："+e.getMessage(),
                    (String) parentParam.get(MemberConstants.CLIENTTRANSNO));
        }

    }

    /*
     * 函数：离线二维码识别和相关数据操作
     * */
    public Map<String, Object> executeQrCodeRecognizeService(Map<String, Object> parentParam) {
        String clientTransNo= (String) parentParam.get(MemberConstants.CLIENTTRANSNO);
        String publicKey = environment.getProperty("bcss.member.api.my_public_key");
        String aeskey = environment.getProperty("bcss.member.api.my_aeskey");
        String featureContent= (String) parentParam.get("feature");

        //获取密钥
        String plainKey= AESKeyUtils.decrypt(aeskey);
        if(plainKey==null||plainKey.isEmpty()){
            LOGGER.error("登记订单过程中人脸识别的时候，获取商户AES密钥失败，请检查配置文件的正确性");
            return CommonMapReturn.constructMapWithClientTransNo("2","登记订单过程中人脸识别的时候，获取商户AES密钥失败，请检查配置文件的正确性",
                    (String) parentParam.get(MemberConstants.CLIENTTRANSNO));
        }
        String plainPublicKey= AESKeyUtils.decrypt(publicKey);
        if(plainPublicKey==null||plainPublicKey.isEmpty()){
            LOGGER.error("登记订单过程中人脸识别的时候，获取商户RSA公钥密钥失败，请检查配置文件的正确性");
            return CommonMapReturn.constructMapWithClientTransNo("2","登记订单过程中人脸识别的时候，获取商户AES密钥失败，请检查配置文件的正确性",
                    (String) parentParam.get(MemberConstants.CLIENTTRANSNO));
        }

        String corpId= (String) parentParam.get("corpId");
        String featureType= (String) parentParam.get("featureType");
        //开始解析
        ResultDto resultDto = QrCodeAnalysisUtils.analysisData(featureContent, plainPublicKey, plainKey);
        if (resultDto!=null && MemberConstants.COMMON_SUCCESS.equals(resultDto.getRetCode())) {
            String data = resultDto.getData();
            JSONObject jsonObject = JSONObject.parseObject(data);
            if (jsonObject.size() < 1) {
                LOGGER.error("离线二维码解密出的数据为空：" + resultDto.getRetMsg());
                Map<String, Object> resResult = CommonMapReturn.constructMapWithClientTransNo(resultDto.getRetCode(),"离线二维码解密出的数据为空：" + resultDto.getRetMsg(),clientTransNo);
                return resResult;
            }

            String qrcodeNo = jsonObject.getString("p");
            if (qrcodeNo == null || qrcodeNo.equals("")) {
                LOGGER.error("离线二维码解密出的二维码编号为空：" + resultDto.getRetMsg());
                Map<String, Object> resResult = CommonMapReturn.constructMapWithClientTransNo(resultDto.getRetCode(),"离线二维码解密出的二维码编号为空：" + resultDto.getRetMsg(),clientTransNo);
                return resResult;
            }

            String personId = jsonObject.getString("ps");
            if (personId == null || personId.equals("")) {
                LOGGER.error("离线二维码解密出的BCSS用户编号为空：" + resultDto.getRetMsg());
                Map<String, Object> resResult = CommonMapReturn.constructMapWithClientTransNo(resultDto.getRetCode(),"离线二维码解密出的BCSS用户编号为空：" + resultDto.getRetMsg(),clientTransNo);
                return resResult;
            }

            String memCardNo = jsonObject.getString("m");
            if (StringUtils.isBlank(memCardNo)) {
                LOGGER.error("离线二维码解析的会员卡号不能为空，" + resultDto.getRetMsg());
                Map<String, Object> resResult = CommonMapReturn.constructMapWithClientTransNo(resultDto.getRetCode(),"离线二维码解析的会员卡号不能为空，" + resultDto.getRetMsg(),clientTransNo);
                return resResult;
            }

            //校验一下二维码是否已经使用了
            String start = TransNoUtils.getInstance().getDate();
//                String end=TransNoUtils.getInstance().getDate();
            List<BcssMemOrderPayFaceLogModel> memOrderPayFaceLogModelList = bcssMemOrderPayFaceLogBaseService.selectByCorpId2FeatureType2RecordDate(corpId, featureType, start);
            if (memOrderPayFaceLogModelList != null && memOrderPayFaceLogModelList.size() > 0) {
                boolean qrflag = false;
                for (BcssMemOrderPayFaceLogModel payFaceLogModel : memOrderPayFaceLogModelList) {
                    if (payFaceLogModel.getFeature().equals(featureContent)) {
                        LOGGER.error("本地数据库中支付流水表存在相同的离线二维码，请查看数据库，支付凭证校验失败");
                        qrflag = true;
                    }
                }

                if (qrflag == false) {
                    LOGGER.warn("本地数据库中支付流水表不存在该离线二维码，二维码有效");
                } else {
                    Map<String, Object> resResult = CommonMapReturn.constructMapWithClientTransNo("1", "离线二维码已经被使用了，请刷新", clientTransNo);
                    return resResult;
                }
            }

            HashMap<String, Object> returnMap = new HashMap<>();
            LOGGER.warn("离线二维码识别和校验成功");
            returnMap.put(MemberConstants.RETURN_CODE, "0");
            returnMap.put(MemberConstants.RETURN_MSG, "离线二维码识别和校验成功");
            returnMap.put(MemberConstants.CLIENTTRANSNO, clientTransNo);
            returnMap.put("bcssPersonId", personId);

            try {
                String memAccno=constructMemAccNo(plainKey,"5",clientTransNo, personId);
                returnMap.put("memAccno",memAccno);

                return returnMap;
            } catch (Exception e) {
                LOGGER.error("登记订单过程中二维码离线识别的时候，加密用户信息异常，请检查配置文件的正确性："+e.getMessage());
                LOGGER.error(e.toString(),e);
                return CommonMapReturn.constructMapWithClientTransNo("2","登记订单过程中人脸识别的时候，加密用户信息异常，请检查配置文件的正确性："+e.getMessage(),
                        (String) parentParam.get(MemberConstants.CLIENTTRANSNO));
            }
//            return returnMap;

        } else {
            Map<String, Object> resResult = CommonMapReturn.constructMapWithClientTransNo(resultDto.getRetCode(),"离线二维码解密失败，原因为：" + JSONObject.toJSONString(resultDto),
                    clientTransNo);
            return resResult;
        }
    }

    /*
    * 函数：人脸识别和相关数据操作
    * */
    private Map<String, Object> executeFaceRecognizeService(Map<String, Object> parentParam) {
        //获取参数
        String corpId= (String) parentParam.get("corpId");
        String image= (String) parentParam.get("feature");
        String topN="1";//默认一个
        String timeStamp= (String) parentParam.get("timeStamp");
        String clientTransNo= (String) parentParam.get("clientTransNo");

        //获取密钥
        String key=environment.getProperty("bcss.member.api.my_aeskey");
        String plainKey= AESKeyUtils.decrypt(key);
        if(plainKey==null||plainKey.isEmpty()){
            LOGGER.error("登记订单过程中人脸识别的时候，获取商户AES密钥失败，请检查配置文件的正确性");
            return CommonMapReturn.constructMapWithClientTransNo("2","登记订单过程中人脸识别的时候，获取商户AES密钥失败，请检查配置文件的正确性",
                    (String) parentParam.get(MemberConstants.CLIENTTRANSNO));
        }

        Map<String,Object> upParam=new HashMap<>();
        upParam.put("corpId",corpId);
        upParam.put("image",image);
        upParam.put("topN",topN);
        upParam.put("timeStamp",timeStamp);
        upParam.put("clientTransNo",clientTransNo);


        Map<String,Object> faceRes= faceRecognizeAllService.recognizeFaceWithOneByN(upParam);
        if(faceRes==null|| !(MemberConstants.COMMON_SUCCESS.equals(faceRes.get(MemberConstants.RETURN_CODE))) ){
            LOGGER.error("登记订单过程中，人脸识别失败："+ JSONObject.toJSONString(faceRes));
        }
        //获取实际分数
        List<Map<String,Object>> recognizeDataList= (List<Map<String, Object>>) faceRes.get("data");
        Map recognizeData=recognizeDataList.get(0);
        String score= (String) recognizeData.get("score");
//        float scoreFloat=Float.parseFloat(score);
        BigDecimal scoreBigDecimal=new BigDecimal(score);

        //获取标准
        String faceSim= (String) parentParam.get("faceSim");
//        float faceSimfloat=Float.parseFloat(faceSim);
        BigDecimal faceSimBigDecimal=new BigDecimal(faceSim);

//        //判断分数
//        if(scoreFloat<faceSimfloat){
//            LOGGER.error("耦合订单登记的人脸识别分数小于标准值");
//            return CommonMapReturn.constructMapWithClientTransNo("1","耦合订单登记的人脸识别分数小于标准值",clientTransNo);
//        }

//        比较大小：
//        int a = bigdemical.compareTo(bigdemical2)
//        a == -1,表示bigdemical小于bigdemical2；
//        a == 0,表示bigdemical等于bigdemical2；
//        a == 1,表示bigdemical大于bigdemical2；
        int compareRes=scoreBigDecimal.compareTo(faceSimBigDecimal);
        if(compareRes==0||compareRes==1){
            LOGGER.warn("订单登记过程中的人脸识别分数为："+score+",设备端要求通过验证的人脸阈值为："+faceSim+"! 人脸成功验证");

        }else {
            LOGGER.error("订单登记过程中的人脸识别分数为："+score+",设备端要求通过验证的人脸阈值为："+faceSim+"! 人脸校验失败了");
            return CommonMapReturn.constructMapWithClientTransNo("2","订单登记过程中的人脸识别分数为："+score+",设备端要求通过验证的人脸阈值为："+faceSim+"! 人脸校验失败了",
                    (String) parentParam.get(MemberConstants.CLIENTTRANSNO));
        }

        //成功
        Map succ=CommonMapReturn.constructMapWithClientTransNo("0","订单登记过程中的人脸识别分数为："+score+",设备端要求通过验证的人脸阈值为："+faceSim+"! 人脸成功验证",
                (String) parentParam.get(MemberConstants.CLIENTTRANSNO));
        succ.put("sroce",score);
        succ.put("personId",recognizeData.get("personId"));
        succ.put("personUniqueCode",recognizeData.get("personUniqueCode"));

        try {
            String memAccno=constructMemAccNo(plainKey,"5",clientTransNo, (String) recognizeData.get("personId"));
            succ.put("memAccno",memAccno);

            return succ;
        } catch (Exception e) {
            LOGGER.error("登记订单过程中人脸识别的时候，加密用户信息异常，请检查配置文件的正确性："+e.getMessage());
            LOGGER.error(e.toString(),e);
            return CommonMapReturn.constructMapWithClientTransNo("2","登记订单过程中人脸识别的时候，加密用户信息异常，请检查配置文件的正确性："+e.getMessage(),
                    (String) parentParam.get(MemberConstants.CLIENTTRANSNO));
        }

    }

    /*组装用户信息*/
    public  String constructMemAccNo(String key,String dt,String clientTransNo,String context) throws Exception {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("dt", dt);
        jsonObject.put("clientTransNo", clientTransNo);
        if("5".equals(dt)){
            jsonObject.put("mobile", "");
            jsonObject.put("staffno", "");
            jsonObject.put("name", "");
            jsonObject.put("custcode", "");
            jsonObject.put("custsort", "");
            jsonObject.put("personId", context);//312112185478410240
            jsonObject.put("memCardNo", "");
        }else if("1".equals(dt)){
            jsonObject.put("mobile", "");
            jsonObject.put("staffno", "");
            jsonObject.put("name", "");
            jsonObject.put("custcode", "");
            jsonObject.put("custsort", "");
            jsonObject.put("personId", "");//312112185478410240
            jsonObject.put("memCardNo", context);
        }else {
            LOGGER.error("加密信息的顺序属性不正确，请检查");
            throw new BusinessException("加密信息的顺序属性不正确，请检查");
        }

        String encrypt= AES.aesEncrypt(jsonObject.toJSONString(),key);
        return encrypt;
    }


}

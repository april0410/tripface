package com.icbc.bcss.trip.face.operationtools.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.service.FaceRecognizeAllService;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceRecognizeService;
import com.icbc.bcss.trip.face.operationtools.utils.CommonMapReturn;
import com.icbc.bcss.trip.face.operationtools.utils.MemberConstants;
import com.icbc.bcss.trip.face.operationtools.utils.TransNoUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


//@Service("faceRecognizeAllService")
public class FaceRecognizeAllServiceImpl implements FaceRecognizeAllService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FaceRecognizeAllServiceImpl.class);

    @Autowired
    private FaceRecognizeService faceRecognizeService;

    /*
     * 调用人脸识别(1:1)
     * */
    @Override
    public Map<String, Object> recognizeFaceWithOneByOne(Map<String, Object> param) {
        Map checkRes=checkFaceRecognizeParamByOne(param);
        if( !(MemberConstants.COMMON_SUCCESS.equals(checkRes.get(MemberConstants.RETURN_CODE))) ){
            //校验失败
            return checkRes;
        }
        String clientTransNo= (String) param.get("clientTransNo");
//        LOGGER.warn("------------"+"准备进行1:1人脸比对，流水号为："+clientTransNo+"------------");
        Map recognize=faceRecognizeService.recognizeServiceInOne(param);
        LOGGER.debug("------------"+"结束1:1人脸比对，流水号为："+clientTransNo+"------------");
        LOGGER.debug("------------"+"结果："+ JSONObject.toJSONString(recognize) +"------------");
        return recognize;
    }

    @Override
    public Map<String, Object> recognizeFaceWithOneByN(Map<String, Object> param) {
        Map checkRes=checkFaceRecognizeParamByN(param);
        if( !(MemberConstants.COMMON_SUCCESS.equals(checkRes.get(MemberConstants.RETURN_CODE))) ){
            //校验失败
            return checkRes;
        }
        String clientTransNo= (String) param.get("clientTransNo");
//        LOGGER.warn("------------"+"准备进行1:1人脸比对，流水号为："+clientTransNo+"------------");
        Map recognize=faceRecognizeService.recognizeServiceInN(param);
        LOGGER.debug("------------"+"结束1:N人脸比对，流水号为："+clientTransNo+"------------");
        LOGGER.debug("------------"+"结果："+ JSONObject.toJSONString(recognize) +"------------");
        return recognize;
//        return null;
    }

     /*
     * 检测上参数 1:1场景
     * */
    private Map<String, Object> checkFaceRecognizeParamByOne(Map<String, Object> param) {
        String clientTransNo= (String) param.get("clientTransNo");
        if (clientTransNo==null||clientTransNo.isEmpty()){
            LOGGER.error("人脸1:1识别参数：交易流水号为空");
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "人脸1:1识别参数：交易流水号为空", TransNoUtils.getInstance().getTransNoWithRandom());
        }

        String faceDataA = (String)(param.get("imageA"));
        String faceDataB= (String) param.get("imageB");
        if (faceDataA == null || faceDataA.isEmpty()||faceDataB==null||faceDataB.isEmpty()) {
            LOGGER.error("人脸1:1识别参数：人脸数据为空");
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "人脸1:1识别参数：人脸数据为空", clientTransNo);
        }
        //判断图片大小
        try {
            ByteArrayInputStream fileInputStreamA = new ByteArrayInputStream(faceDataA.getBytes("UTF-8"));
            int countA = fileInputStreamA.available();
            if (countA > MemberConstants.FACE_IMAGE_SIZE_MAX_VALUE || countA<MemberConstants.FACE_IMAGE_SIZE_MIN_VALUE) {
                LOGGER.error("判断图片大小为：" + countA + "字节，人脸图片不符合规范");
                return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "判断图片大小为：" + countA + "字节，人脸图片不符合规范", clientTransNo);
            }

            ByteArrayInputStream fileInputStreamB = new ByteArrayInputStream(faceDataB.getBytes("UTF-8"));
            int countB = fileInputStreamB.available();
            if (countB > MemberConstants.FACE_IMAGE_SIZE_MAX_VALUE||countB<MemberConstants.FACE_IMAGE_SIZE_MIN_VALUE) {
                LOGGER.error("判断图片大小为：" + countB + "字节，人脸图片不符合规范");
                return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "判断图片大小为：" + countB + "字节，人脸图片不符合规范", clientTransNo);
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("判断图片大小失败，继续进行后续识别:"+e.getMessage());
            LOGGER.error(e.toString(),e);
        }

        LOGGER.debug("人脸数据(A)的字符数为：" + faceDataA.length());
        LOGGER.debug("人脸数据(B)的字符数为：" + faceDataB.length());

        //判断类型时间戳
        String timeStamp = (String) param.get("timeStamp");
        Date test=TransNoUtils.getInstance().convertTimeStamp(timeStamp);
        if (test == null ) {
            LOGGER.error("人脸1:1识别参数：时间戳为空");
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "人脸1:1识别参数：时间戳为空", clientTransNo);
        }


        HashMap<String, Object> res = new HashMap<>();
        res.put(MemberConstants.RETURN_CODE, MemberConstants.COMMON_SUCCESS);
        res.put(MemberConstants.RETURN_MSG, "参数校验成功");
        res.put(MemberConstants.CLIENTTRANSNO, clientTransNo);
        return res;
    }

    /*
     * 检测上参数 1:N场景
     * */
    private Map checkFaceRecognizeParamByN(Map<String, Object> param) {
        String corpId= (String) param.get("corpId");
        if (corpId==null||corpId.isEmpty()){
            LOGGER.error("人脸1:N识别参数：企业编号为空");
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "人脸1:N识别参数：企业编号为空", TransNoUtils.getInstance().getTransNoWithRandom());
        }

        String clientTransNo= (String) param.get("clientTransNo");
        if (clientTransNo==null||clientTransNo.isEmpty()){
            LOGGER.error("人脸1:N识别参数：交易流水号为空");
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "人脸1:N识别参数：交易流水号为空", TransNoUtils.getInstance().getTransNoWithRandom());
        }

        String faceDataA = (String)(param.get("image"));
        if (faceDataA == null || faceDataA.isEmpty()) {
            LOGGER.error("人脸1:N识别参数：人脸数据为空");
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "人脸1:N识别参数：人脸数据为空", clientTransNo);
        }
        //判断图片大小
        try {
            ByteArrayInputStream fileInputStreamA = new ByteArrayInputStream(faceDataA.getBytes("UTF-8"));
            int countA = fileInputStreamA.available();
            if (countA > MemberConstants.FACE_IMAGE_SIZE_MAX_VALUE||countA<MemberConstants.FACE_IMAGE_SIZE_MIN_VALUE) {
                LOGGER.error("判断图片大小为：" + countA + "字节，人脸图片大小不符合规范");
                return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "判断图片大小为：" + countA + "字节，人脸图片大小不符合规范", clientTransNo);
            }

        } catch (UnsupportedEncodingException e) {
            LOGGER.error("判断图片大小失败，继续进行后续识别:"+e.getMessage());
            LOGGER.error(e.toString(),e);
        }

        LOGGER.debug("人脸数据的字符数为：" + faceDataA.length());

        //判断类型时间戳
        String timeStamp = (String) param.get("timeStamp");
        Date test=TransNoUtils.getInstance().convertTimeStamp(timeStamp);
        if (test == null ) {
            LOGGER.error("人脸1:N识别参数：时间戳为空");
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "人脸1:N识别参数：时间戳为空", clientTransNo);
        }

        //判断topN
        String topN = (String) param.get("topN");
        if (topN == null ||topN.isEmpty()) {
            LOGGER.error("人脸1:N识别参数：相似度信息个数为空");
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "人脸1:N识别参数：相似度信息个数为空", clientTransNo);
        }
        if(! ("1".equals(topN)) ){
            LOGGER.error("人脸1:N识别参数：相似度信息个数不为1，值非法");
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "人脸1:N识别参数：相似度信息个数不为1，值非法", clientTransNo);

        }


        HashMap<String, Object> res = new HashMap<>();
        res.put(MemberConstants.RETURN_CODE, MemberConstants.COMMON_SUCCESS);
        res.put(MemberConstants.RETURN_MSG, "参数校验成功");
        res.put(MemberConstants.CLIENTTRANSNO, clientTransNo);
        return res;
    }




    /*
     * 调用人脸识别
     * */
    @Deprecated
    public Map<String, Object> recognizeFaceAllService(Map<String, Object> param) {
        String clientTransNo = (String) param.get(MemberConstants.CLIENTTRANSNO);
        if (clientTransNo == null || clientTransNo.equals("")) {
            clientTransNo = TransNoUtils.getInstance().getTransNoIn20();
            LOGGER.warn("调用方没有上送交易流水号，因此此次请求系统自行生成流水号：" + clientTransNo);
            param.put(MemberConstants.CLIENTTRANSNO, clientTransNo);
        } else {
            LOGGER.info("默认采用调用方上送的流水号，即为：" + clientTransNo);
        }

        Map<String, Object> checkResult = checkFaceRecognizeParamByOne(param);
        String code = (String) checkResult.get(MemberConstants.RETURN_CODE);
        if (code == null || code.isEmpty()) {
            LOGGER.error("参数校验失败，请重试");
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION, "参数校验失败，请重试", TransNoUtils.getInstance().getTransNoIn20());
        }
        if (!code.equals(MemberConstants.COMMON_SUCCESS)) {
            LOGGER.error("直接返回参数校验结果");
            return checkResult;
        }

//        return faceRecognizeService.recognizeServiceInOne(param);
        return null;
    }
}

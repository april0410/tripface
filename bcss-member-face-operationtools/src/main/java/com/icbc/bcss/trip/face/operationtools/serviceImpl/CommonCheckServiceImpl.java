package com.icbc.bcss.trip.face.operationtools.serviceImpl;

import com.icbc.bcss.trip.face.operationtools.service.CommonCheckService;
import com.icbc.bcss.trip.face.operationtools.utils.CommonMapReturn;
import com.icbc.bcss.trip.face.operationtools.utils.MemberConstants;
import com.icbc.bcss.trip.face.operationtools.utils.PropertyPlaceholder;
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

@Service("commonCheckService")
public class CommonCheckServiceImpl implements CommonCheckService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonCheckServiceImpl.class);

    @Autowired
    private PropertyPlaceholder environment;
    /*
     * 检测上参数 1:N场景
     * */
    public  Map checkFaceRecognizeParamByN(Map<String, Object> param) {
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

//        //判断topN
//        String topN = (String) param.get("topN");
//        if (topN == null ||topN.isEmpty()) {
//            LOGGER.error("人脸1:N识别参数：相似度信息个数为空");
//            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "人脸1:N识别参数：相似度信息个数为空", clientTransNo);
//        }
//        if(! ("1".equals(topN)) ){
//            LOGGER.error("人脸1:N识别参数：相似度信息个数不为1，值非法");
//            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "人脸1:N识别参数：相似度信息个数不为1，值非法", clientTransNo);
//
//        }


        HashMap<String, Object> res = new HashMap<>();
        res.put(MemberConstants.RETURN_CODE, MemberConstants.COMMON_SUCCESS);
        res.put(MemberConstants.RETURN_MSG, "参数校验成功");
        res.put(MemberConstants.CLIENTTRANSNO, clientTransNo);
        return res;
    }
}

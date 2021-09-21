package com.icbc.bcss.trip.face.operationtools.serviceImpl.localFace;

import com.icbc.bcss.trip.face.operationtools.service.localFace.FaceLocalConstructParamService;
import com.icbc.bcss.trip.face.operationtools.serviceImpl.cloudwalk.FaceRegisterServerServiceImpl;
import com.icbc.bcss.trip.face.operationtools.utils.CommonMapReturn;
import com.icbc.bcss.trip.face.operationtools.utils.MemberConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("faceLocalConstructParamService")
public class FaceLocalConstructParamServiceImpl implements FaceLocalConstructParamService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FaceLocalConstructParamServiceImpl.class);

    @Override
    public Map<String, Object> constructRegisterCommonParam(Map<String, Object> param) {
        //唯一编号
        String personUniqueFaceNo = (String) param.get("personUniqueFaceNo");
        //获取姓名的默认值
        String name = (String) param.get("memName");
        //性别送3-为止
        int sex = 3;//默认未知
        String sexCheck= (String) param.get("sex");
        if ("1".equals(sexCheck)){
            sex=1;
        }else if("2".equals(sexCheck)){
            sex=2;
        }else {
            sex=3;
        }

        //人脸
        String imageBase64 = (String) param.get("featureContent");
        String featureType= (String) param.get("featureType");
        String featureSeqNo= (String) param.get("featureSeqNo");
        List<Map<String, Object>> biologyFeatures = new ArrayList<>();
        Map<String, Object> biology1 = new HashMap<>();
        biology1.put("file", imageBase64);
        biology1.put("type", featureType);//1-人脸注册照--固定人脸
        biology1.put("subType", featureSeqNo);//默认序号为第一顺序
        biologyFeatures.add(biology1);

        //备注
        String remark = "标准--本地人脸前置服务注册的人脸";

        //非必填
        String certType= (String) param.get("certType");
        String certNo= (String) param.get("certNo");

        String birthday= (String) param.get("birthday");
        String duty= (String) param.get("duty");
        String position= (String) param.get("position");
        String telephone= (String) param.get("telephone");
        String email= (String) param.get("email");
        String address= (String) param.get("address");
        String province= (String) param.get("province");
        String city= (String) param.get("city");
        String area= (String) param.get("area");

        //把传入的参数放入Request
        Map<String,Object> requestParam=new HashMap<>();
        requestParam.put("code", personUniqueFaceNo);
        requestParam.put("name", name);
        requestParam.put("sex", sex);
        requestParam.put("remark", remark);
        requestParam.put("biologyFeatures", biologyFeatures);
        requestParam.put("certType",certType);
        requestParam.put("certNo",certNo);
        requestParam.put("birthday",birthday);
        requestParam.put("duty",duty);
        requestParam.put("position",position);
        requestParam.put("telephone",telephone);
        requestParam.put("email",email);
        requestParam.put("address",address);
        requestParam.put("remark",remark);
        requestParam.put("province",province);
        requestParam.put("city",city);
        requestParam.put("area",area);

        return requestParam;
    }

    @Override
    public Map<String, Object> constructDeleteCommonParam(Map<String, Object> parentParams) {
        return null;
    }
}

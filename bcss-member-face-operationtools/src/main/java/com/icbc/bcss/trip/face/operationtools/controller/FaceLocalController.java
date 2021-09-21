package com.icbc.bcss.trip.face.operationtools.controller;

import com.icbc.bcss.trip.face.operationtools.service.FaceAllService;
import com.icbc.bcss.trip.face.operationtools.service.FaceRecognizeAllService;
import com.icbc.bcss.trip.face.operationtools.service.RequestAsyncService;
import com.icbc.bcss.trip.face.operationtools.service.localFace.FaceLocalAllService;
import com.icbc.bcss.trip.face.operationtools.task.StaticScheduleTask;
import com.icbc.bcss.trip.face.operationtools.utils.FaceDataRegAndDelSingleton;
import com.icbc.bcss.trip.face.operationtools.utils.MemberConstants;
import com.icbc.bcss.trip.face.operationtools.utils.PropertyPlaceholder;
import com.icbc.bcss.trip.face.operationtools.utils.TransNoUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


//@RequestMapping(value = "/local")
//@RestController
public class FaceLocalController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FaceLocalController.class);

    @Autowired
    private FaceAllService faceAllService;

    @Autowired
    private FaceRecognizeAllService faceRecognizeAllService;

    @Autowired
    private FaceLocalAllService faceLocalAllService;


    //人脸注册服务，外部人脸注册
    @RequestMapping(value="/person/register")
    public Map register(@RequestBody Map params){
        long startTimeCur=System.currentTimeMillis();
        //调用查询接口
//
//        //判断自定义锁
//        if("0".equals(FaceDataRegAndDelSingleton.getSingleton().getRegisterFlag())){
//            LOGGER.error("当前已经线程进行人脸注册信息拉取，此次请求不进行后续操作");
//            HashMap errorMap=new HashMap();
//            errorMap.put(MemberConstants.RETURN_CODE,"1");
//            errorMap.put(MemberConstants.RETURN_MSG,"当前已经线程进行人脸注册信息拉取，此次请求不进行后续操作");
//            errorMap.put(MemberConstants.CLIENTTRANSNO,TransNoUtils.getInstance().getTransNoWithRandom());
//            return errorMap;
//        }

        //单个人脸注册--同步
        Map<String,Object> response= faceLocalAllService.execute(params);

        //耗时计算
        response.put("startTime", TransNoUtils.getInstance().getTimeStamp(new Date(startTimeCur)));
        long endTimeCur=System.currentTimeMillis();
        response.put("endTime", TransNoUtils.getInstance().getTimeStamp(new Date(endTimeCur)));
        response.put("consumeTime", endTimeCur-startTimeCur);
        return response;
    }

    //人脸删除服务
    @RequestMapping(value="/person/delete")
    public Map delete(@RequestBody Map params){
        long startTimeCur=System.currentTimeMillis();
        //调用查询接口
//
//        //判断自定义锁
//        if("0".equals(FaceDataRegAndDelSingleton.getSingleton().getDeleteFlage())){
//            LOGGER.error("当前已经线程进行人脸删除信息拉取，此次请求不进行后续操作");
//            HashMap errorMap=new HashMap();
//            errorMap.put(MemberConstants.RETURN_CODE,"1");
//            errorMap.put(MemberConstants.RETURN_MSG,"当前已经线程进行人脸删除信息拉取，此次请求不进行后续操作");
//            errorMap.put(MemberConstants.CLIENTTRANSNO,TransNoUtils.getInstance().getTransNoWithRandom());
//            return errorMap;
//        }

        //异步启动了
        Map<String,Object> response= faceLocalAllService.executeDelete(params);

        //构造返回结果
//        Map<String, Object> response = new HashMap<>();
//        response.put("return_code", "0");
//        response.put("clientTransNo", TransNoUtils.getInstance().getTransNoWithRandom());
//        response.put("return_msg", "人脸信息(删除)同步流程已经触发");

        //耗时计算
        response.put("startTime", TransNoUtils.getInstance().getTimeStamp(new Date(startTimeCur)));
        long endTimeCur=System.currentTimeMillis();
        response.put("endTime", TransNoUtils.getInstance().getTimeStamp(new Date(endTimeCur)));
        response.put("consumeTime", endTimeCur-startTimeCur);
        return  response;
    }





    //////////////////////////////////////////////////////
    //废弃方法
    @Deprecated
    public Map<String,Object> checkParamSimple(Map<String,Object> param){
        HashMap<String,Object> res=new HashMap<>();
        if (param==null||param.isEmpty()){
            LOGGER.error("参数集合为空，请重新请求");
            res.put("return_code","1");
            res.put("return_msg","参数集合为空，请重新请求");
        }
        return null;
    }



}

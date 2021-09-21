package com.icbc.bcss.trip.face.operationtools.controller;

import com.icbc.bcss.trip.face.operationtools.service.FaceAllService;
import com.icbc.bcss.trip.face.operationtools.service.FaceRecognizeAllService;
import com.icbc.bcss.trip.face.operationtools.service.RequestAsyncService;
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


//@RequestMapping(value = "/face")
//@RestController
public class FaceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FaceController.class);

    @Autowired
    private FaceAllService faceAllService;

    @Autowired
    private FaceRecognizeAllService faceRecognizeAllService;

    @Autowired
    private RequestAsyncService requestAsyncService;

    //人脸注册服务,拉member人脸注册
//    @RequestMapping(value="/person/register")
    public Map register(@RequestBody Map params){
        long startTimeCur=System.currentTimeMillis();
        //        return faceAllService.regANDdelFaceAllService(params);
        //调用查询接口
        String operType= (String) params.get("operType");
        if( !("1".equals(operType)||"2".equals(operType)) ){
            //不是注册的操作符，错误
            HashMap errorMap=new HashMap();
            errorMap.put(MemberConstants.RETURN_CODE,"1");
            errorMap.put(MemberConstants.RETURN_MSG,"查询方式operType字段上送的值不合法");
            errorMap.put(MemberConstants.CLIENTTRANSNO,TransNoUtils.getInstance().getTransNoWithRandom());
            return errorMap;
        }

        //判断自定义锁
        if("0".equals(FaceDataRegAndDelSingleton.getSingleton().getRegisterFlag())){
            LOGGER.error("当前已经线程进行人脸注册信息拉取，此次请求不进行后续操作");
            HashMap errorMap=new HashMap();
            errorMap.put(MemberConstants.RETURN_CODE,"1");
            errorMap.put(MemberConstants.RETURN_MSG,"当前已经线程进行人脸注册信息拉取，此次请求不进行后续操作");
            errorMap.put(MemberConstants.CLIENTTRANSNO,TransNoUtils.getInstance().getTransNoWithRandom());
            return errorMap;
        }

        //拒绝autoFullDown！=0的情况
        String authFullDownLoad = (String) params.get("autoFullDown");
        if(!"0".equals(authFullDownLoad)){
            LOGGER.error("目前“自动按数量自动下载的标识”="+authFullDownLoad+"的分支已经过时，请使用新接口完成业务逻辑");
            HashMap errorMap=new HashMap();
            errorMap.put(MemberConstants.RETURN_CODE,"1");
            errorMap.put(MemberConstants.RETURN_MSG,"目前“自动按数量自动下载的标识”="+authFullDownLoad+"的分支已经过时，请使用新接口完成业务逻辑");
            errorMap.put(MemberConstants.CLIENTTRANSNO,TransNoUtils.getInstance().getTransNoWithRandom());
            return errorMap;
        }
        //异步启动了
//        requestAsyncServiceForFace(params);
        requestAsyncService.execute(params);

        //构造返回结果
        Map<String, Object> response = new HashMap<>();
        response.put("return_code", "0");
        response.put("clientTransNo", TransNoUtils.getInstance().getTransNoWithRandom());
        response.put("return_msg", "人脸信息(注册)同步流程已经触发");

        //耗时计算
        response.put("startTime", TransNoUtils.getInstance().getTimeStamp(new Date(startTimeCur)));
        long endTimeCur=System.currentTimeMillis();
        response.put("endTime", TransNoUtils.getInstance().getTimeStamp(new Date(endTimeCur)));
        response.put("consumeTime", endTimeCur-startTimeCur);
        return response;
    }

    //人脸删除服务
//    @RequestMapping(value="/person/delete")
    public Map delete(@RequestBody Map params){
        long startTimeCur=System.currentTimeMillis();
        //调用查询接口
        String operType= (String) params.get("operType");
        if( !("7".equals(operType)||"8".equals(operType)) ){
            //不是注册的操作符，错误
            HashMap errorMap=new HashMap();
            errorMap.put(MemberConstants.RETURN_CODE,"1");
            errorMap.put(MemberConstants.RETURN_MSG,"查询方式operType字段上送的值不合法");
            errorMap.put(MemberConstants.CLIENTTRANSNO,TransNoUtils.getInstance().getTransNoWithRandom());
            return errorMap;
        }

        //判断自定义锁
        if("0".equals(FaceDataRegAndDelSingleton.getSingleton().getDeleteFlage())){
            LOGGER.error("当前已经线程进行人脸删除信息拉取，此次请求不进行后续操作");
            HashMap errorMap=new HashMap();
            errorMap.put(MemberConstants.RETURN_CODE,"1");
            errorMap.put(MemberConstants.RETURN_MSG,"当前已经线程进行人脸删除信息拉取，此次请求不进行后续操作");
            errorMap.put(MemberConstants.CLIENTTRANSNO,TransNoUtils.getInstance().getTransNoWithRandom());
            return errorMap;
        }

        //拒绝autoFullDown！=0的情况
        String authFullDownLoad = (String) params.get("autoFullDown");
        if(!"0".equals(authFullDownLoad)){
            LOGGER.error("目前“自动按数量自动下载的标识”="+authFullDownLoad+"的分支已经过时，请使用新接口完成业务逻辑");
            HashMap errorMap=new HashMap();
            errorMap.put(MemberConstants.RETURN_CODE,"1");
            errorMap.put(MemberConstants.RETURN_MSG,"目前“自动按数量自动下载的标识”="+authFullDownLoad+"的分支已经过时，请使用新接口完成业务逻辑");
            errorMap.put(MemberConstants.CLIENTTRANSNO,TransNoUtils.getInstance().getTransNoWithRandom());
            return errorMap;
        }
        //异步启动了
//        requestAsyncServiceForFace(params);
        requestAsyncService.execute(params);

        //构造返回结果
        Map<String, Object> response = new HashMap<>();
        response.put("return_code", "0");
        response.put("clientTransNo", TransNoUtils.getInstance().getTransNoWithRandom());
        response.put("return_msg", "人脸信息(删除)同步流程已经触发");

        //耗时计算
        response.put("startTime", TransNoUtils.getInstance().getTimeStamp(new Date(startTimeCur)));
        long endTimeCur=System.currentTimeMillis();
        response.put("endTime", TransNoUtils.getInstance().getTimeStamp(new Date(endTimeCur)));
        response.put("consumeTime", endTimeCur-startTimeCur);
        return  response;
    }

    //人脸注册或者删除进度查询
//    @RequestMapping(value="/person/queryProgress")
//    public Map queryProgress(@RequestBody Map params){
//        long start=System.currentTimeMillis();
//        String startTime= TransNoUtils.getInstance().getTimeStamp(new Date(start));
//
//        String operType= (String) params.get("queryType");
//        if( !("1".equals(operType)||"2".equals(operType)) ){
//            //不是注册的操作符，错误
//            HashMap errorMap=new HashMap();
//            errorMap.put(MemberConstants.RETURN_CODE,"1");
//            errorMap.put(MemberConstants.RETURN_MSG,"查询方式operType字段上送的值不合法");
//            errorMap.put(MemberConstants.CLIENTTRANSNO,TransNoUtils.getInstance().getTransNoWithRandom());
//            return errorMap;
//        }
//
////        Map result=faceAllService.recognizeFaceAllService(params);
//        Map result=faceAllService.queryProgressService(params);
//        long end=System.currentTimeMillis();
//        String endTime= TransNoUtils.getInstance().getTimeStamp(new Date(end));
//        result.put("startTime", startTime);
//        result.put("endTime", endTime);
//        result.put("consumeTime", end-start);
//        return result;
//    }

    /*
    * 函数：人脸1:1识别服务
    * */
//    @RequestMapping(value="/similarity/image")
//    public Map similarityImage(@RequestBody Map params){
//        long start=System.currentTimeMillis();
//        String startTime= TransNoUtils.getInstance().getTimeStamp(new Date(start));
//
////        Map result=faceAllService.recognizeFaceAllService(params);
//        //1:1人脸识别
//        Map result=faceRecognizeAllService.recognizeFaceWithOneByOne(params);
//        long end=System.currentTimeMillis();
//        String endTime= TransNoUtils.getInstance().getTimeStamp(new Date(end));
//
//        result.put("startTime", startTime);
//        result.put("endTime", endTime);
//        result.put("consumeTime", end-start);
//        return result;
//    }

    /*
     * 函数：人脸1:N识别服务(单库)
     * */
////    @RequestMapping(value="/person/search")
//    public Map search(@RequestBody Map params){
//        long start=System.currentTimeMillis();
//        String startTime= TransNoUtils.getInstance().getTimeStamp();
//
//        //1:N识别
////        Map result=faceAllService.recognizeFaceAllService(params);
//        Map result=faceRecognizeAllService.recognizeFaceWithOneByN(params);
//        long end=System.currentTimeMillis();
//        String endTime= TransNoUtils.getInstance().getTimeStamp();
//
//        result.put("startTime", startTime);
//        result.put("endTime", endTime);
//        result.put("consumeTime", end-start);
//        return result;
//    }

}

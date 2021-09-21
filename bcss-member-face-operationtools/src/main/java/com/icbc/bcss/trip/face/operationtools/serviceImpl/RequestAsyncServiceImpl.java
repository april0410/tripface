package com.icbc.bcss.trip.face.operationtools.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.service.FaceAllService;
import com.icbc.bcss.trip.face.operationtools.service.FaceRecognizeAllService;
import com.icbc.bcss.trip.face.operationtools.service.RequestAsyncService;
import com.icbc.bcss.trip.face.operationtools.service.order.OrderUploadAllService;
import com.icbc.bcss.trip.face.operationtools.utils.PropertyPlaceholder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service("requestAsyncService")
public class RequestAsyncServiceImpl implements RequestAsyncService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestAsyncServiceImpl.class);

    //注入自定义多线程池
    @Resource(name = "getDefaultAsyncExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private FaceAllService faceAllService;


    @Override
    /*
    * 人脸注册*/
    public Map<String, Object> execute(Map<String, Object> contentParam) {
        //开始执行
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                //正式业务
                Map res = requestAsyncServiceForFace(contentParam);
                LOGGER.warn("---------------------" + JSONObject.toJSONString(res));
            }
        });
        LOGGER.warn("RequestAsyncServiceImpl.execute()--");
        return null;
    }

    //注册和删除的总入口--异步的开始
//    @Async("getDefaultAsyncExecutor")
    public Map requestAsyncServiceForFace(Map<String, Object> params) {
        LOGGER.info("--------------------************开始异步线程启动人脸注册或者删除功能--------------------************");
        Map result=faceAllService.regANDdelFaceAllService(params);
        LOGGER.info("--------------------************结束通知---异步线程启动人脸注册或者删除功能--------------------************");
        return result;
    }

}

package com.icbc.bcss.trip.face.operationtools.utils;

import com.icbc.api.DefaultIcbcClient;
import com.icbc.api.IcbcApiException;
import com.icbc.bcss.trip.face.operationtools.requestmodel.BcssMemberFeatureQuerymemberinfoRequestV1;
import com.icbc.bcss.trip.face.operationtools.responsemodel.BcssMemberFeatureQuerymemberinfoResponseV1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class MyCallable implements Callable<BcssMemberFeatureQuerymemberinfoResponseV1> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyCallable.class);
    private BcssMemberFeatureQuerymemberinfoResponseV1 responseV1;
    private DefaultIcbcClient defaultIcbcClient;
    private BcssMemberFeatureQuerymemberinfoRequestV1 request;

    public MyCallable(DefaultIcbcClient defaultIcbcClient,BcssMemberFeatureQuerymemberinfoRequestV1 request){
        this.defaultIcbcClient=defaultIcbcClient;
        this.request=request;
    }

    public DefaultIcbcClient getDefaultIcbcClient() {
        return defaultIcbcClient;
    }

    public void setDefaultIcbcClient(DefaultIcbcClient defaultIcbcClient) {
        this.defaultIcbcClient = defaultIcbcClient;
    }

    public BcssMemberFeatureQuerymemberinfoRequestV1 getRequest() {
        return request;
    }

    public void setRequest(BcssMemberFeatureQuerymemberinfoRequestV1 request) {
        this.request = request;
    }

    @Override
    public BcssMemberFeatureQuerymemberinfoResponseV1 call() throws IcbcApiException {
        LOGGER.error("开始执行api的http请求（带有倒计时功能）");
        try{
            responseV1=this.defaultIcbcClient.execute(this.request);
            LOGGER.error("-------------正常结束MyCallable.run（）操作-----------");
            return responseV1;
        }catch (IcbcApiException e){
            LOGGER.error("请求api过程中抛出异常，具体信息如下");
            LOGGER.error(e.toString());
            LOGGER.error(e.getMessage(),e);
            LOGGER.error("-------------异常结束MyCallable.run（）操作-----------");
            throw  e;
        }

    }
}

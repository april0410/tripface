package com.icbc.bcss.trip.face.operationtools.api;

import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
import com.icbc.api.DefaultIcbcClient;
import com.icbc.api.IcbcApiException;
import com.icbc.api.internal.util.internal.util.fastjson.JSONObject;
import com.icbc.api.utils.IcbcEncrypt;
import com.icbc.bcss.trip.face.operationtools.configuration.DataKey;
import com.icbc.bcss.trip.face.operationtools.requestmodel.BcssMemberFeatureQuerymemberinfoRequestV1;
import com.icbc.bcss.trip.face.operationtools.responsemodel.BcssMemberFeatureQuerymemberinfoResponseV1;
import com.icbc.bcss.trip.face.operationtools.service.FaceAllService;
import com.icbc.bcss.trip.face.operationtools.utils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("bcssMemberFeatureQuerymemberinfoService")
public class BcssMemberFeatureQuerymemberinfoServiceImpl implements BcssMemberFeatureQuerymemberinfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BcssMemberFeatureQuerymemberinfoServiceImpl.class);

    @Autowired
    private PropertyPlaceholder environment;

    public BcssMemberFeatureQuerymemberinfoResponseV1 requestFaceData(Map<String,Object> param) {
        //获取私有参数
        String corpId= (String) param.get("corpId");
        String timeStamp=(String) param.get("timeStamp");
        String operType= (String) param.get("operType");
        String featureType=(String) param.get("featureType");
        String startDate=(String) param.get("startDate");
        String endDate=(String) param.get("endDate");
        String channel=(String) param.get("channel");
        String startNum=(String) param.get("startNum");
        String endNum=(String) param.get("endNum");
        String operInfo=(String) param.get("operInfo");
        String clientTransNo= (String) param.get("clientTransNo");
        LOGGER.error("-------------"+"requestFaceData is start:"+clientTransNo+"-------------------");

        //获取url
//        Map<String,String> loadParamMap= DefinePropertiesFileUtils.getInstance().commonloadParam("bcss.member.querymemberinfoURL");
//        String resCode=loadParamMap.get(MemberConstants.RETURN_CODE);
//        String queryMemberinfoUrl=null;
//        if(resCode==null||resCode.isEmpty()||!resCode.equals(MemberConstants.COMMON_SUCCESS)){
////            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL,loadParamMap.get(MemberConstants.RETURN_MSG),clientTransNo);
//            BcssMemberFeatureQuerymemberinfoResponseV1 responseV1=new BcssMemberFeatureQuerymemberinfoResponseV1();
//            responseV1.setReturnCode(Integer.parseInt(MemberConstants.COMMON_FAIL));
//            responseV1.setReturnMsg("获取请求人脸数据的URL失败，URL为空");
//            responseV1.setClientTransNo(clientTransNo);
//            responseV1.setDataList(null);
//            responseV1.setMemCount("0");
//        }else{
//            queryMemberinfoUrl=loadParamMap.get(MemberConstants.PROPERTIES_VALUE);
//        }

        //map转jsonobject
        String appId = environment.getProperty("bcss.member.api.appid");
        String myPrivateKey = environment.getProperty("bcss.member.api.my_private_key");
        String apigwPublicKey = environment.getProperty("bcss.member.api.apigw_public_key");
        String queryMemberinfoUrl=environment.getProperty("bcss.member.querymemberinfoURL");
        LOGGER.info("APP_ID:" + appId+" , URL:"+queryMemberinfoUrl);

        String signType="RSA";
        String setSignType=environment.getProperty("bcss.member.api.SignType");
        if(setSignType!=null){
            signType=setSignType;
        }
        LOGGER.warn("签名类型为："+signType);

        //对密文进行解密--商户密钥
        String plainPrivateKey=AESKeyUtils.decrypt(myPrivateKey);

        //api请求几大要素必须不能为空
        if(appId==null||appId.isEmpty()||queryMemberinfoUrl==null||queryMemberinfoUrl.isEmpty()||
                plainPrivateKey== null||plainPrivateKey.isEmpty()||  apigwPublicKey==null|| apigwPublicKey.isEmpty()){

            LOGGER.error("API请求参数校验失败，请配置参数后，重启程序");
            BcssMemberFeatureQuerymemberinfoResponseV1 responseV1=new BcssMemberFeatureQuerymemberinfoResponseV1();
            responseV1.setReturnCode(Integer.parseInt(MemberConstants.COMMON_ICBCAPI_EXCEPTION));
            responseV1.setReturnMsg("API请求参数校验失败，请配置参数后，重启程序");
            responseV1.setClientTransNo(clientTransNo);
            responseV1.setDataList(null);
            responseV1.setMemCount("0");
            return responseV1;
        }




        //构建参数集合体
        BcssMemberFeatureQuerymemberinfoRequestV1.BcssMemberFeatureQuerymemberinfoRequestBizV1 bdr = new BcssMemberFeatureQuerymemberinfoRequestV1.BcssMemberFeatureQuerymemberinfoRequestBizV1();
        bdr.setCorpId(corpId);
        bdr.setClientTransNo(clientTransNo);
        bdr.setTimeStamp(timeStamp);
        bdr.setOperType(operType);
        bdr.setFeatureType(featureType);
        bdr.setStartDate(startDate);
        bdr.setEndDate(endDate);
        bdr.setChannel(channel);
        bdr.setStartNum(startNum);
        bdr.setEndNum(endNum);
        bdr.setOperInfo(operInfo);

        // 设置请求
        BcssMemberFeatureQuerymemberinfoRequestV1 faceRequest= new BcssMemberFeatureQuerymemberinfoRequestV1();
        faceRequest.setServiceUrl(queryMemberinfoUrl);
        faceRequest.setBizContent(bdr);

        LOGGER.warn("--------------requestFaceData Param:" + JSONObject.toJSONString(faceRequest));

//        //超时时间
//        int TimeOut=8000; //单位毫秒
//        String setTimeOut=environment.getProperty("bcss.DefaultIcbcClient.httpTimeOut");
//        if(setTimeOut!=null){
//            TimeOut=Integer.parseInt(setTimeOut);
//        }
//        LOGGER.debug("超时时间为（单位：毫秒）："+TimeOut);

        // 执行发送请求
        DefaultIcbcClient client = new DefaultIcbcClient(appId,signType, plainPrivateKey, apigwPublicKey);
        BcssMemberFeatureQuerymemberinfoResponseV1 response = null;
        try {
            LOGGER.error("************开始请求API************");
            response = client.execute(faceRequest);
            //带有倒计时功能
//            MyCallable myCallable=new MyCallable(client,request);
//            ExecutorService exeservices = Executors.newSingleThreadExecutor();
//            Future<BcssGuardUserAuthResponseV1> future = exeservices.submit(myCallable);
//
//            response = future.get(TimeOut, TimeUnit.MILLISECONDS);
////            System.out.println(JSON.toJSONString(response));
        }
//        catch (TimeoutException ex) {
//            // TODO Auto-generated catch block
//            LOGGER.error("--------TimeoutException for execute UserAuth request -------");
//            LOGGER.error(ex.toString());
//            LOGGER.error(ex.getMessage());
//            ex.printStackTrace();
//            //组装返回体
//            response=new BcssGuardUserAuthResponseV1();
//            response.setReturnCode(-1);
//            response.setReturnMsg("抛出TimeoutException，请重新发起权限认证");
//            response.setMsgId(cientTransNo);
//
//        }
        catch (IcbcApiException  ex){
            LOGGER.error("--------IcbcApiException for execute requestFaceData  -------");
//            LOGGER.error(ex.toString());
            LOGGER.error(ex.getMessage(),ex);
            //组装返回体
            BcssMemberFeatureQuerymemberinfoResponseV1 responseV1=new BcssMemberFeatureQuerymemberinfoResponseV1();
            responseV1.setReturnCode(Integer.parseInt(MemberConstants.COMMON_ICBCAPI_EXCEPTION));
            responseV1.setReturnMsg("人脸数据请求抛出IcbcApiException，请重新发起请求");
            responseV1.setClientTransNo(clientTransNo);
            responseV1.setDataList(null);
            responseV1.setMemCount("0");
            return responseV1;
        }
        catch (Exception e) {
            LOGGER.error("--------Exception for execute requestFaceData  -------");
//            LOGGER.error(e.toString());
            LOGGER.error(e.getMessage(),e);
            //组装返回体
            BcssMemberFeatureQuerymemberinfoResponseV1 responseV1=new BcssMemberFeatureQuerymemberinfoResponseV1();
            responseV1.setReturnCode(Integer.parseInt(MemberConstants.COMMON_EXCEPTION));
            responseV1.setReturnMsg("人脸数据请求抛出Exception，请重新发起请求");
            responseV1.setClientTransNo(clientTransNo);
            responseV1.setDataList(null);
            responseV1.setMemCount("0");
            return responseV1;
        }

        //最后返回的报文
//        System.out.println(JSONObject.toJSONString(response.getReturnMsg()));
        JSONObject jsonObject=new JSONObject();
//        jsonObject.put("clientTransNo",response.getClientTransNo());
//        jsonObject.put("returnCode",response.getReturnCode());
//        jsonObject.put("returnMsg",response.getReturnMsg());
//        jsonObject.put("memCount",response.getMemCount());
//        jsonObject.put("corgNo",response.getCorgNo());
//        jsonObject.put("nextPage",response.getNextPage());
//        LOGGER.info("requestFaceData-Return------------"+jsonObject.toJSONString());
        if(response.getReturnCode()==0){
            if(response.getDataList()!=null&&!response.getDataList().isEmpty()){
                for(BcssMemberFeatureQuerymemberinfoResponseV1.DataInfo temp:response.getDataList()){
                    LOGGER.error("打印每一个人personId："+temp.getPersonId());
                }

            }
        }
        LOGGER.warn("requestFaceData-Return------------"+JSONObject.toJSONString(response));
        LOGGER.error("-------------"+"requestFaceData is end:"+clientTransNo+"-------------------");
        return response;
    }


    /*返回字符串*/
    @Override
    @Deprecated
    public String requestFaceDataOutString(Map<String, Object> param) {
        //获取私有参数
        String corpId= (String) param.get("corpId");
        String timeStamp=(String) param.get("timeStamp");
        String operType= (String) param.get("operType");
        String featureType=(String) param.get("featureType");
        String startDate=(String) param.get("startDate");
        String endDate=(String) param.get("endDate");
        String channel=(String) param.get("channel");
        String startNum=(String) param.get("startNum");
        String endNum=(String) param.get("endNum");
        String operInfo=(String) param.get("operInfo");
        String clientTransNo= (String) param.get("clientTransNo");
        LOGGER.error("-------------"+"requestFaceData is start:"+clientTransNo+"-------------------");


        //map转jsonobject
        //map转jsonobject
        String appId = environment.getProperty("bcss.member.api.appid");
        String myPrivateKey = environment.getProperty("bcss.member.api.my_private_key");
        String apigwPublicKey = environment.getProperty("bcss.member.api.apigw_public_key");
        String queryMemberinfoUrl=environment.getProperty("bcss.member.querymemberinfoURL");
        LOGGER.info("APP_ID:" + appId+" , URL:"+queryMemberinfoUrl);

        String signType="RSA";
        String setSignType=environment.getProperty("bcss.member.api.SignType");
        if(setSignType!=null){
            signType=setSignType;
        }
        LOGGER.warn("签名类型为："+signType);

        //对密文进行解密--商户密钥
        String plainPrivateKey=AESKeyUtils.decrypt(myPrivateKey);
        //api请求几大要素必须不能为空
        if(appId==null||appId.isEmpty()||queryMemberinfoUrl==null||queryMemberinfoUrl.isEmpty()||
                myPrivateKey== null||myPrivateKey.isEmpty()||  apigwPublicKey==null|| apigwPublicKey.isEmpty()){

            LOGGER.error("API请求参数校验失败，请配置参数后，重启程序");
            BcssMemberFeatureQuerymemberinfoResponseV1 responseV1=new BcssMemberFeatureQuerymemberinfoResponseV1();
            responseV1.setReturnCode(Integer.parseInt(MemberConstants.COMMON_ICBCAPI_EXCEPTION));
            responseV1.setReturnMsg("API请求参数校验失败，请配置参数后，重启程序");
            responseV1.setClientTransNo(clientTransNo);
            responseV1.setDataList(null);
            responseV1.setMemCount("0");
            return JSONObject.toJSONString(responseV1);
        }

        //构建参数集合体
        BcssMemberFeatureQuerymemberinfoRequestV1.BcssMemberFeatureQuerymemberinfoRequestBizV1 bdr = new BcssMemberFeatureQuerymemberinfoRequestV1.BcssMemberFeatureQuerymemberinfoRequestBizV1();
        bdr.setCorpId(corpId);
        bdr.setClientTransNo(clientTransNo);
        bdr.setTimeStamp(timeStamp);
        bdr.setOperType(operType);
        bdr.setFeatureType(featureType);
        bdr.setStartDate(startDate);
        bdr.setEndDate(endDate);
        bdr.setChannel(channel);
        bdr.setStartNum(startNum);
        bdr.setEndNum(endNum);
        bdr.setOperInfo(operInfo);

        // 设置请求
        BcssMemberFeatureQuerymemberinfoRequestV1 faceRequest= new BcssMemberFeatureQuerymemberinfoRequestV1();
        faceRequest.setServiceUrl(queryMemberinfoUrl);
        faceRequest.setBizContent(bdr);

        LOGGER.warn("--------------requestFaceData Param:" + JSONObject.toJSONString(faceRequest));



        // 执行发送请求
        DefaultIcbcClient client = new DefaultIcbcClient(appId,signType, myPrivateKey, apigwPublicKey);
        BcssMemberFeatureQuerymemberinfoResponseV1 response = null;
        try {
            LOGGER.error("************开始请求API************");
            response = client.execute(faceRequest);
            //带有倒计时功能
//            MyCallable myCallable=new MyCallable(client,request);
//            ExecutorService exeservices = Executors.newSingleThreadExecutor();
//            Future<BcssGuardUserAuthResponseV1> future = exeservices.submit(myCallable);
//
//            response = future.get(TimeOut, TimeUnit.MILLISECONDS);
////            System.out.println(JSON.toJSONString(response));
        }
//        catch (TimeoutException ex) {
//            // TODO Auto-generated catch block
//            LOGGER.error("--------TimeoutException for execute UserAuth request -------");
//            LOGGER.error(ex.toString());
//            LOGGER.error(ex.getMessage());
//            ex.printStackTrace();
//            //组装返回体
//            response=new BcssGuardUserAuthResponseV1();
//            response.setReturnCode(-1);
//            response.setReturnMsg("抛出TimeoutException，请重新发起权限认证");
//            response.setMsgId(cientTransNo);
//
//        }
        catch (IcbcApiException  ex){
            LOGGER.error("--------IcbcApiException for execute requestFaceData  -------");
//            LOGGER.error(ex.toString());
            LOGGER.error(ex.getMessage(),ex);
            //组装返回体
            BcssMemberFeatureQuerymemberinfoResponseV1 responseV1=new BcssMemberFeatureQuerymemberinfoResponseV1();
            responseV1.setReturnCode(Integer.parseInt(MemberConstants.COMMON_ICBCAPI_EXCEPTION));
            responseV1.setReturnMsg("人脸数据请求抛出IcbcApiException，请重新发起请求");
            responseV1.setClientTransNo(clientTransNo);
            responseV1.setDataList(null);
            responseV1.setMemCount("0");
//            return responseV1;
            return JSONObject.toJSONString(responseV1);
        }
        catch (Exception e) {
            LOGGER.error("--------Exception for execute requestFaceData  -------");
//            LOGGER.error(e.toString());
            LOGGER.error(e.getMessage(),e);
            //组装返回体
            BcssMemberFeatureQuerymemberinfoResponseV1 responseV1=new BcssMemberFeatureQuerymemberinfoResponseV1();
            responseV1.setReturnCode(Integer.parseInt(MemberConstants.COMMON_EXCEPTION));
            responseV1.setReturnMsg("人脸数据请求抛出Exception，请重新发起请求");
            responseV1.setClientTransNo(clientTransNo);
            responseV1.setDataList(null);
            responseV1.setMemCount("0");
//            return responseV1;
            return JSONObject.toJSONString(responseV1);
        }

        //最后返回的报文
        System.out.println(JSONObject.toJSONString(response.getReturnMsg()));
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("clientTransNo",response.getClientTransNo());
        jsonObject.put("returnCode",response.getReturnCode());
        jsonObject.put("returnMsg",response.getReturnMsg());
        jsonObject.put("memCount",response.getMemCount());
        jsonObject.put("corgNo",response.getCorgNo());
        jsonObject.put("nextPage",response.getNextPage());
        LOGGER.info("requestFaceData-Return------------"+jsonObject.toJSONString());
        LOGGER.error("-------------"+"requestFaceData is end:"+clientTransNo+"-------------------");
        return JSONObject.toJSONString(response);
    }


}

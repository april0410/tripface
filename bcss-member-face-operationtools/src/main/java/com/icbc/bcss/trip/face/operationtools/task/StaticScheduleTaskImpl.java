package com.icbc.bcss.trip.face.operationtools.task;

import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.enums.FaceOperType;
import com.icbc.bcss.trip.face.operationtools.enums.QuerymemberinfoType;
import com.icbc.bcss.trip.face.operationtools.service.FaceAllService;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemOrderInfoBaseService;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemOrderPayFaceLogBaseService;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemOrderProdInfoBaseService;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemOrdercouponInfoBaseService;
import com.icbc.bcss.trip.face.operationtools.service.order.OrderDeleteAllService;
import com.icbc.bcss.trip.face.operationtools.service.order.OrderUploadAllService;
import com.icbc.bcss.trip.face.operationtools.utils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Component
@Configuration
public class StaticScheduleTaskImpl implements StaticScheduleTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaticScheduleTaskImpl.class);
    @Autowired
    private PropertyPlaceholder environment;

    @Autowired
    private FaceAllService faceAllService;

    /*
              *  定时同步人脸(5分钟一次)
     * */
//    @Scheduled(cron = "* 0/5 * * * ?")
    @Scheduled(cron="${bcss.face.pull.jobs.schedule}")
    public void synchronizedRegisterInfoWithfrontDay(){
        LOGGER.error("--------------------**********"+"定时任务：定时进行人脸注册操作--同步（开始）"+"**********--------------------");
        long current=System.currentTimeMillis();
        Map<String,Object> finalResult=new HashMap<>();
        //获取暂存的时间
        Map<String,String> loadParamMap=DefinePropertiesFileUtils.getInstance().commonloadParam("bcss.init.register.time");
        String startDate=null;
        if(! (MemberConstants.COMMON_SUCCESS.equals(loadParamMap.get(MemberConstants.RETURN_CODE))) ){
            finalResult= CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL,loadParamMap.get(MemberConstants.RETURN_MSG),TransNoUtils.getInstance().getTransNoWithRandom());
            LOGGER.error("定时任务：人脸注册："+JSONObject.toJSONString(finalResult));
        }else{
            startDate=loadParamMap.get(MemberConstants.PROPERTIES_VALUE);
        }
        //
        if(startDate==null||startDate.isEmpty()){
            startDate=MemberConstants.ORIGINAL_START_DATE_DEFAULT;
        }
        //结束日期
        String endDate=TransNoUtils.getInstance().getDate(new Date(current));

        //判断自定义锁
        if("0".equals(FaceDataRegAndDelSingleton.getSingleton().getDeleteFlage())){
            LOGGER.error("当前已经线程进行人脸删除信息拉取，此次请求不进行后续操作");
            HashMap errorMap=new HashMap();
            errorMap.put(MemberConstants.RETURN_CODE,"1");
            errorMap.put(MemberConstants.RETURN_MSG,"当前已经线程进行人脸删除信息拉取，此次请求不进行后续操作");
            errorMap.put(MemberConstants.CLIENTTRANSNO,TransNoUtils.getInstance().getTransNoWithRandom());
            return;
        }

        //同步map集合，在多线程情况使用
        int failCount=0;
        StringBuffer stringBuffer=new StringBuffer();
        String corpIdByEnv = environment.getProperty("bcss.default.corpId");
        String corpIdArr[]=corpIdByEnv.split("\\|");
        for(int i=0;i<corpIdArr.length;i++){
            String clientTransNo=TransNoUtils.getInstance().getTransNoWithRandom();//生成流水号
            LOGGER.error("*****************---------------------" + "开始拉取人脸注册信息,企业编号为："+corpIdArr[i]+"流水号为："+clientTransNo + "*****************---------------------");
            String corpId=corpIdArr[i];
            String operType = FaceOperType.OPERTYPE_TWO_FACE_COUNT.getCode(); //获取注册人脸的数量
            String autoFullDown = "0";//0-需要，1-不需要
            String startNum = "1";
            String endNum = "10";

            Map<String, Object> param = new HashMap<>();
            param.put("corpId", corpId);
            param.put("timeStamp", TransNoUtils.getInstance().getTimeStamp());
            param.put("clientTransNo", clientTransNo);
            param.put("operType", operType);
            param.put("featureType", "1");
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            param.put("channel", null);
            param.put("startNum", startNum);
            param.put("endNum", endNum);
            param.put("operInfo", null);
            param.put("autoFullDown", autoFullDown);
            //跳过异步，直接同步调用
            Map faceresult=faceAllService.pullFaceService(param);
            if(faceresult!=null&&MemberConstants.COMMON_SUCCESS.equals(faceresult.get(MemberConstants.RETURN_CODE))){
                LOGGER.info("当前企业拉取人脸全部成功");
            }else{
                LOGGER.info("当前企业拉取人脸存在失败的情况");
                failCount=failCount+1;
                stringBuffer.append(corpIdArr[i]).append(MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME);
            }

            LOGGER.error("*****************---------------------" + "结束拉取人脸注册信息,企业编号为："+corpIdArr[i]+"流水号为："+clientTransNo + "*****************---------------------");
            LOGGER.error("当前注册结果为："+JSONObject.toJSONString(faceresult));
        }

        if(failCount==0){
            LOGGER.info("人脸注册--所有企业注册人脸执行完毕");
        }else {
            LOGGER.info("人脸注册--所有企业注册人脸执行完毕,存在失败情况，数量和明细为："+failCount+"||"+stringBuffer.toString());
        }


        LOGGER.error("--------------------**********"+"定时任务：定时进行人脸注册操作--同步（主要部分结束）"+"**********--------------------");

        //启动子处理---解决异常时间段
//        updateExceptionRegisterInSometimeCount();
//        updateExceptionRegisterInSometime();
        dealFailRequestRegister();
    }
    //处理失败文档的
    private void dealFailRequestRegister() {
        String operType="1";
        String fullName = System.getProperty("user.dir") + File.separator +
                environment.getProperty("face.saveFilePath.defaultName") + File.separator;

        //读入数量
        Vector<String> vcetor=FileUtils.getAllFile(fullName,".txt");
        for(int i=0;i<vcetor.size();i++){
            String pathName=fullName+vcetor.get(i);
            if(pathName.contains(MemberConstants.DELETE_REQUEST_API_FAILED_PRENAME)){
                LOGGER.error("当前为注册人脸场景，不需要遍历删除场景中失败情况，跳过");
                continue;
            }

            //获取所有失败
            List<String> readString=FileUtils.readFile(pathName);
            List<String> newString=new ArrayList<>();
            for(String strTemp:readString){
                //判断是否合法map，然后再提前判断一下为空
                Map<String,Object> params=CommonUtils.getInstance().isvalidJSON(strTemp);
                if(params==null){
                    LOGGER.error("当前转换Map类型字符串为："+strTemp+"，转换失败，跳过此条记录");
                    continue;
                }
                if(params.isEmpty()){
                    LOGGER.error("当前转换Map类型字符串为："+strTemp+"，转换后的map集合为空集，跳过此条记录");
                    continue;
                }
                //单独请求，所以把调用其他方法
                params.put("autoFullDown","1");
                Map result=faceAllService.regANDdelFaceAllService(params);
//                Map<String,Object> params=JSONObject.parseObject(strTemp,Map.class);
//                Map result=faceAllService.regANDdelFaceAllService(params);
                if(result!=null&&MemberConstants.COMMON_SUCCESS.equals(result.get(MemberConstants.RETURN_CODE))){
                    LOGGER.debug("当前请求成功");
                }else {
                    LOGGER.debug("当前请求失败："+strTemp);
                    newString.add(strTemp);
                }
            }
            LOGGER.warn("当前文件："+pathName+",失败情况有："+JSONObject.toJSONString(newString));
            //写会当前文件
            if(newString.size()==0){
                newString.add("");
            }
            int result11=FileUtils.writeFileByList(pathName,newString,"1",false);
            if(result11==0){
                LOGGER.warn("1-客户注册人脸明细--请求失败的参数写入文件成功");
            }else{
                LOGGER.error("1-客户注册人脸明细--请求失败的参数写入文件-失败");
            }
        }
        LOGGER.error("--------------------**********"+"定时任务：定时进行人脸注册操作--同步（全部结束）"+"**********--------------------");
    }


    /*定时任务--删除人脸，时间是前一天到今天*/
    //凌晨1点2分
    @Scheduled(cron="${bcss.face.delete.jobs.schedule}")
    public void synchronizedDeleteInfoWithfrontDayTask(){
        LOGGER.error("--------------------**********"+"定时任务：定时进行人脸删除操作--同步（开始）"+"**********--------------------");

        long current=System.currentTimeMillis();
        Map<String,Object> finalResult=new HashMap<>();
        //获取暂存的时间
        Map<String,String> loadParamMap=DefinePropertiesFileUtils.getInstance().commonloadParam("bcss.init.delete.time");
        String startDate=null;
        if(! (MemberConstants.COMMON_SUCCESS.equals(loadParamMap.get(MemberConstants.RETURN_CODE))) ){
            finalResult= CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL,loadParamMap.get(MemberConstants.RETURN_MSG),TransNoUtils.getInstance().getTransNoWithRandom());
            LOGGER.error("定时任务：人脸删除："+JSONObject.toJSONString(finalResult));
        }else{
            startDate=loadParamMap.get(MemberConstants.PROPERTIES_VALUE);
        }
        //
        if(startDate==null||startDate.isEmpty()){
            startDate=MemberConstants.ORIGINAL_START_DATE_DEFAULT;
        }
        //结束日期
        String endDate=TransNoUtils.getInstance().getDate(new Date(current));

        //判断自定义锁
        if("0".equals(FaceDataRegAndDelSingleton.getSingleton().getDeleteFlage())){
            LOGGER.error("当前已经线程进行人脸删除信息拉取，此次请求不进行后续操作");
            HashMap errorMap=new HashMap();
            errorMap.put(MemberConstants.RETURN_CODE,"1");
            errorMap.put(MemberConstants.RETURN_MSG,"当前已经线程进行人脸删除信息拉取，此次请求不进行后续操作");
            errorMap.put(MemberConstants.CLIENTTRANSNO,TransNoUtils.getInstance().getTransNoWithRandom());
            return ;
        }


        //同步map集合，在多线程情况使用
        int failCount=0;
        StringBuffer stringBuffer=new StringBuffer();
        String corpIdByEnv = environment.getProperty("bcss.default.corpId");
        String corpIdArr[]=corpIdByEnv.split("\\|");
        for(int i=0;i<corpIdArr.length;i++){
            String clientTransNo=TransNoUtils.getInstance().getTransNoWithRandom();
            LOGGER.error("*****************---------------------" + "开始拉取人脸删除信息,企业编号为："+corpIdArr[i]+"流水号为："+clientTransNo + "*****************---------------------");
            String corpId=corpIdArr[i];
            String operType = FaceOperType.OPERTYPE_EIGHT_FACE_DELETE_COUNT.getCode(); //注册人脸的数量
            String autoFullDown = "0";//0-需要，1-不需要
            String startNum = "1";
            String endNum = "10";

            Map<String, Object> param = new HashMap<>();
            param.put("corpId", corpId);
            param.put("timeStamp", TransNoUtils.getInstance().getTimeStamp());
            param.put("clientTransNo", clientTransNo);
            param.put("operType", operType);
            param.put("featureType", "1");
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            param.put("channel", null);
            param.put("startNum", startNum);
            param.put("endNum", endNum);
            param.put("operInfo", null);
            param.put("autoFullDown", autoFullDown);
            //跳过异步，直接同步调用
            Map faceresult=faceAllService.regANDdelFaceAllService(param);
            if(faceresult!=null&&MemberConstants.COMMON_SUCCESS.equals(faceresult.get(MemberConstants.RETURN_CODE))){
                LOGGER.info("当前企业拉取人脸全部成功");
            }else{
                LOGGER.info("当前企业拉取人脸存在失败的情况");
                failCount=failCount+1;
                stringBuffer.append(corpIdArr[i]).append(MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME);
            }

            LOGGER.error("*****************---------------------" + "结束拉取人脸删除信息,企业编号为："+corpIdArr[i]+"流水号为："+clientTransNo + "*****************---------------------");
            LOGGER.error("当前注册结果为："+JSONObject.toJSONString(faceresult));
        }

        if(failCount==0){
            LOGGER.info("人脸注册--所有企业注册人脸执行完毕");
        }else {
            LOGGER.info("人脸注册--所有企业注册人脸执行完毕,存在失败情况，数量和明细为："+failCount+"||"+stringBuffer.toString());
        }
        LOGGER.error("--------------------**********"+"定时任务：定时进行人脸删除操作--同步（主要部分结束）"+"**********--------------------");

        //子处理--删除人脸信息对应的异常时间段
//        updateExceptionDeleteInSometimeCount();
//        updateExceptionDeleteInSometime();
        dealFailRequestDelete();
    }


    //处理失败文档的
    private void dealFailRequestDelete() {
        String operType="7";
        String fullName = System.getProperty("user.dir") + File.separator +
                environment.getProperty("face.saveFilePath.defaultName") + File.separator;

        //读入数量
        Vector<String> vcetor=FileUtils.getAllFile(fullName,".txt");
        for(int i=0;i<vcetor.size();i++){
            String pathName=fullName+vcetor.get(i);

            if(pathName.contains(MemberConstants.REGISTER_REQUEST_API_FAILED_PRENAME)){
                LOGGER.error("当前为删除人脸场景，不需要遍历注册场景中失败情况，跳过");
                continue;
            }
            //获取所有失败
            List<String> readString=FileUtils.readFile(pathName);
            List<String> newString=new ArrayList<>();
            for(String strTemp:readString){
                //判断是否合法map，然后再提前判断一下为空
                Map<String,Object> params=CommonUtils.getInstance().isvalidJSON(strTemp);
                if(params==null){
                    LOGGER.error("当前转换Map类型字符串为："+strTemp+"，转换失败，跳过此条记录");
                    continue;
                }
                if(params.isEmpty()){
                    LOGGER.error("当前转换Map类型字符串为："+strTemp+"，转换后的map集合为空集，跳过此条记录");
                    continue;
                }
                //单独请求，所以把调用其他方法
                params.put("autoFullDown","1");
                Map result=faceAllService.regANDdelFaceAllService(params);
//                Map<String,Object> params=JSONObject.parseObject(strTemp,Map.class);
//                Map result=faceAllService.regANDdelFaceAllService(params);
                if(result!=null&&MemberConstants.COMMON_SUCCESS.equals(result.get(MemberConstants.RETURN_CODE))){
                    LOGGER.debug("当前请求成功");
                }else {
                    LOGGER.debug("当前请求失败："+strTemp);
                    newString.add(strTemp);
                }
            }
            LOGGER.warn("当前文件："+pathName+",失败情况有："+JSONObject.toJSONString(newString));
            //写会当前文件
            if(newString.size()==0){
                newString.add("");
            }
            int result11=FileUtils.writeFileByList(pathName,newString,"1",false);
            if(result11==0){
                LOGGER.warn("7-客户删除人脸明细--请求失败的参数写入文件成功");
            }else{
                LOGGER.error("7-客户删除人脸明细--请求失败的参数写入文件-失败");
            }
        }
        LOGGER.error("--------------------**********"+"定时任务：定时进行人脸删除操作--同步（全部结束）"+"**********--------------------");
    }


}

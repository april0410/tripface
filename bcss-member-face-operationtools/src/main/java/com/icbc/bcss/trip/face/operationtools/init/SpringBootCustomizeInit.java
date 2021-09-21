package com.icbc.bcss.trip.face.operationtools.init;

import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.api.BcssMemberFeatureQuerymemberinfoServiceImpl;
import com.icbc.bcss.trip.face.operationtools.enums.FaceOperType;
import com.icbc.bcss.trip.face.operationtools.enums.PropertiesType;
import com.icbc.bcss.trip.face.operationtools.http.HttpBaseService;
import com.icbc.bcss.trip.face.operationtools.http.HttpBaseServiceImpl;
import com.icbc.bcss.trip.face.operationtools.responsemodel.BcssMemberFeatureQuerymemberinfoResponseV1;
import com.icbc.bcss.trip.face.operationtools.service.FaceAllService;
import com.icbc.bcss.trip.face.operationtools.service.FaceAsynchronousControl;
import com.icbc.bcss.trip.face.operationtools.service.composite.BcssMemBuildGroupCompositeService;
import com.icbc.bcss.trip.face.operationtools.utils.*;

//import org.jasypt.encryption.StringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.*;

/*
 * 初始化发数据
 * */
@Component
public class SpringBootCustomizeInit implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootCustomizeInit.class);
    @Autowired
    private PropertyPlaceholder environment;

//    @Autowired
//    private StringEncryptor stringEncryptor;

    @Autowired
    private HttpBaseService httpBaseService;

    @Autowired
    private FaceAllService faceAllService;

    @Autowired
    private BcssMemBuildGroupCompositeService bcssMemBuildGroupCompositeService;

    @Override
    public void run(String... args) throws Exception {
        LOGGER.error("*****************---------------------" + "Spring boot启动的初始化--开始" + "*****************---------------------");

        FaceDataRegAndDelSingleton.getSingleton().setRegisterFlag("1");
        FaceDataRegAndDelSingleton.getSingleton().setDeleteFlage("1");
        FaceDataRegAndDelSingleton.getSingleton().setRegisterMemCount("0");
        FaceDataRegAndDelSingleton.getSingleton().setRegisterDealCount("0");


        //容器id
        String macStr = RandomInt.getContainIdByMac();
        String[] macArr = macStr.split("-");
        int containIdInt = Integer.valueOf(macArr[macArr.length - 1], 16);
        String fullcontainId = RandomInt.Lpad(String.valueOf(containIdInt), "0", 3);//默认3位长度
        LOGGER.warn("当前运行前置程序的容器id自编为：" + fullcontainId);
        //检测application.properties参数
        checkParam();

        //初始化define.properties
        initParam();

        //初始化路径
        initFile();

//        //粗糙删除人脸
//        initdeleteAllPersonForteste();

        LOGGER.error("*****************---------------------" + "Spring boot启动的初始化（中间）--结束" + "*****************---------------------");

//        //云从特征分库的建立、查询
//        checkAndBuildFeatureGroup();

//        //拉取云端人脸下来
//        pullICBCCloudFaceData();



        /*
         * 1.先获取全量删除人脸的信息，删除本地图库多余的图片
         * 2.增量获取距离当前的过去四天的人脸注册照片
         * 3.全量拉取初始时间到某天(即 距离当前的过去四天 )的全部人脸
         * */

        LOGGER.error("*****************---------------------" + "Spring boot启动的初始化--结束" + "*****************---------------------");
    }


    //创建文件夹和文件
    private void initFile() {
        String fullName = System.getProperty("user.dir") + File.separator +
                environment.getProperty("face.saveFilePath.defaultName") + File.separator;
        String detailMemberName = System.getProperty("user.dir") + File.separator +
                environment.getProperty("face.saveFilePath.defaultName.detailInfo") + File.separator;
        String standardInfo = System.getProperty("user.dir") + File.separator +
                environment.getProperty("face.saveFilePath.faceOper.standardInfo") + File.separator;

        //创建文件夹
        CommonUtils.getInstance().createFilePath(fullName);
        CommonUtils.getInstance().createFilePath(detailMemberName);
        //初始化记录请求失败的json请求体的文件名称--创建文件
        CommonUtils.getInstance().createFilePath(standardInfo);
    }


    //检测参数-主要检查application.properties文件的部分参数
    private void checkParam() {
        String myInd = environment.getProperty("bcss.default.industry");
        if (myInd == null || myInd.isEmpty()) {
            LOGGER.error("本地人脸库静态维护的行业场景标识符不存在，程序启动失败，请检查");
            System.exit(1);
        }

        String myReg = environment.getProperty("bcss.init.delete.time");
        Date tempReg = TransNoUtils.getInstance().convertDate(myReg);
        if (myReg == null || myReg.isEmpty()) {
            LOGGER.error("本地人脸库静态维护的删除人脸信息的初始时间不存在，程序启动失败，请检查");
            System.exit(1);
        }
        if (tempReg == null) {
            LOGGER.error("本地人脸库静态维护的删除人脸信息的初始时间格式异常，程序启动失败，请检查");
            System.exit(1);
        }

        String myDel = environment.getProperty("bcss.init.register.time");
        Date tempDel = TransNoUtils.getInstance().convertDate(myDel);
        if (myDel == null || myDel.isEmpty()) {
            LOGGER.error("本地人脸库静态维护的注册人脸信息的初始时间不存在，程序启动失败，请检查");
            System.exit(1);
        }
        if (tempDel == null) {
            LOGGER.error("本地人脸库静态维护的删除人脸信息的初始时间格式异常，程序启动失败，请检查");
            System.exit(1);
        }

        String corpIdS=environment.getProperty("bcss.default.corpId");
        String corpNameS=environment.getProperty("bcss.default.corpName");
        if(corpIdS==null||corpIdS.isEmpty()||corpNameS==null||corpNameS.isEmpty()){
            LOGGER.error("本地人脸库静态维护的企业编号和企业名称为空值，程序启动失败，请检查");
            System.exit(1);
        }
        //拆分
        String[] corpIdArray=corpIdS.split("\\|");
        String[] corpNameArray=corpNameS.split("\\|");
        if(corpIdArray.length!=corpNameArray.length){
            LOGGER.error("本地人脸库静态维护的企业编号和企业名称无法匹配，程序启动失败，请检查");
            System.exit(1);
        }

//        String channelCode=environment.getProperty("bcss.default.cloudwalk.channelCode");
//        if(channelCode==null||channelCode.isEmpty()){
//            LOGGER.error("本地人脸库静态维护的云从渠道编号为空值，程序启动失败，请检查");
//            System.exit(1);
//        }
//
//        String buildGroupUrl=environment.getProperty("bcss.default.face.create_ownGroup_URL");
//        String queryGroupUrl=environment.getProperty("bcss.default.face.query_ownGroup_URL");
//        if(buildGroupUrl==null||buildGroupUrl.isEmpty()||queryGroupUrl==null||queryGroupUrl.isEmpty()){
//            LOGGER.error("本地人脸库静态维护的云从建立或查询自定义特征分库的URL为空值，程序启动失败，请检查");
//            System.exit(1);
//        }

    }

    //初始化动态参数到文件中
    private void initParam() {
        //初始化 人脸注册、删除、和识别一些固定参数
        //机构类型
        String[] paramArray = new String[]{"bcss.default.industry", "bcss.init.delete.time", "bcss.init.register.time"};
        for (int i = 0; i < paramArray.length; i++) {
            String tempStr = paramArray[i];
            Map tempMap = DefinePropertiesFileUtils.getInstance().commonloadParam(tempStr);
            if (tempMap == null || tempMap.isEmpty()) {
                LOGGER.error("读取动态和静态参数文件，返回结果为空，系统异常并退出");
                System.exit(1);
            }

            String code = String.valueOf(tempMap.get(MemberConstants.RETURN_CODE));
            String source = String.valueOf(tempMap.get(MemberConstants.PROPERTIES_VALUE_SOURCE));
            if (code.equals(MemberConstants.COMMON_SUCCESS) && PropertiesType.PROPERTIES_VALUE_SOURCE_DEFINE.getCode().equals(source)) {
                LOGGER.info("读取动态参数的属性值成功，即不把静态文件属性同步到动态文件");
            } else {
                LOGGER.error("读取动态参数的属性值失败，马上把静态文件属性同步到动态文件");
                DefinePropertiesFileUtils.getInstance().commonSaveParam(tempStr, environment.getProperty(tempStr));
            }

        }

    }

    /*
    * 判断接口版本，并检查和创建分库
    * */
    private void checkAndBuildFeatureGroup() {
        String corpIdS=environment.getProperty("bcss.default.corpId");
        String corpNameS=environment.getProperty("bcss.default.corpName");
        //拆分
        String[] corpIdArray=corpIdS.split("\\|");
        String[] corpNameArray=corpNameS.split("\\|");
        Map<String,Object> corpParam=new HashMap<>();
        for(int i=0;i<corpIdArray.length;i++){
            corpParam.put(MemberConstants.KEY_CORP_ID,corpIdArray[i]);
            corpParam.put(MemberConstants.KEY_CORP_NAME,corpNameArray[i]);
            //①检测是否建立了特征分库
            Map<String,Object> buildRes=bcssMemBuildGroupCompositeService.execute(corpParam);

            if( !("0".equals(buildRes.get(MemberConstants.RETURN_CODE))) ){
                //有失败的情况，退出系统
                LOGGER.error("建立特征分库有失败情况，现在退出系统，请检查日志");
                System.exit(1);
            }
            LOGGER.warn("--------------------------"+"当前corpId遍历完成："+corpIdArray[i]+"--------------------------");
        }
        LOGGER.warn("--------------------------"+"所有特征分库检测完毕，正常启动步骤：完成第一步"+"--------------------------");
    }

    /*触发人脸下载*/
    private void pullICBCCloudFaceData() {
//        String  startDate=PropertisUtils.getPropertiValue("bcss.init.register.time");
//        if(startDate==null||startDate.isEmpty()){
//            startDate=environment.getProperty("bcss.init.register.time");
//            if(startDate==null||startDate.isEmpty()){
//                startDate="2019-01-01";
//            }
//        }
        String startDate=null;
        Map<String, String> loadParamMap=DefinePropertiesFileUtils.getInstance().commonloadParam("bcss.init.register.time");
        if(! (MemberConstants.COMMON_SUCCESS.equals(loadParamMap.get(MemberConstants.RETURN_CODE))) ){
            startDate=MemberConstants.ORIGINAL_START_DATE_DEFAULT;
        }else {
            startDate=loadParamMap.get(MemberConstants.PROPERTIES_VALUE);
        }
        //结束时间
        String endData=TransNoUtils.getInstance().getDate();

        //同步map集合，在多线程情况使用
        String corpIdByEnv = environment.getProperty("bcss.default.corpId");
        String corpIdArr[]=corpIdByEnv.split("\\|");
        for(int i=0;i<corpIdArr.length;i++){
            String clientTransNo=TransNoUtils.getInstance().getTransNoWithRandom();
            LOGGER.error("*****************---------------------" + "开始拉取人脸,流水号为："+clientTransNo + "*****************---------------------");
            String corpId=corpIdArr[i];
            String operType = FaceOperType.OPERTYPE_TWO_FACE_COUNT.getCode(); //注册人脸的数量
            String autoFullDown = "0";//0-需要，1-不需要
            String startNum = "1";
            String endNum = "10";

            Map<String, Object> param = new HashMap<>();
            param.put("corpId", corpId);
            param.put("timeStamp", TransNoUtils.getInstance().getTimeStamp());
            param.put(MemberConstants.CLIENTTRANSNO, clientTransNo);
            param.put("operType", operType);
            param.put("featureType", "1");
            param.put("startDate", startDate);
            param.put("endDate", endData);
            param.put("channel", null);
            param.put("startNum", startNum);
            param.put("endNum", endNum);
            param.put("operInfo", null);
            param.put("autoFullDown", autoFullDown);
            //跳过异步，直接同步调用
            faceAllService.regANDdelFaceAllService(param);
            LOGGER.error("所有线程执行成功--所有线程执行成功--所有线程执行成功--所有线程执行成功--所有线程执行成功--");
        }
        LOGGER.info("人脸注册--所有企业注册成功执行成功--");
    }


    //粗糙删除人脸，不考虑出错
    private void initdeleteAllPersonForteste() {
        String corpId=environment.getProperty("bcss.default.corpId");
        Map<String, Object> param = new HashMap<>();
        param.put("corpId", corpId);
        param.put("timeStamp", TransNoUtils.getInstance().getTimeStamp());
        param.put("clientTransNo", TransNoUtils.getInstance().getTransNoIn20());
        param.put("operType", "2");
        param.put("featureType","1");
        param.put("startDate", "2021-01-01");
        param.put("endDate", TransNoUtils.getInstance().getDate());
        param.put("channel", null);
        param.put("startNum", "1");
        param.put("endNum", "10");
        param.put("operInfo", null);

        BcssMemberFeatureQuerymemberinfoServiceImpl bcssMemberFeatureQuerymemberinfoService=new BcssMemberFeatureQuerymemberinfoServiceImpl();
        BcssMemberFeatureQuerymemberinfoResponseV1 responseV1=bcssMemberFeatureQuerymemberinfoService.requestFaceData(param);
        String channel=environment.getProperty("bcss.default.cloudwalk.channelCode");
        String delUrl=environment.getProperty("bcss.default.face.delete_URL");
//        HttpBaseServiceImpl httpBaseService=new HttpBaseServiceImpl();

        if(responseV1.getReturnCode()==0){
            BcssMemberFeatureQuerymemberinfoResponseV1 responseV2=null;
            for(int i=0;i<Integer.parseInt(responseV1.getMemCount());i=i+10){
                int mystart=i+1;
                int myend=i+10;

                param.put("startNum",String.valueOf(mystart));
                param.put("endNum", String.valueOf(myend));
                param.put("operType","1");

                responseV2=bcssMemberFeatureQuerymemberinfoService.requestFaceData(param);
                if(responseV2.getReturnCode()!=0){
                    continue;
                }

                List<BcssMemberFeatureQuerymemberinfoResponseV1.DataInfo> dataInfos=responseV2.getDataList();
                for(BcssMemberFeatureQuerymemberinfoResponseV1.DataInfo info:dataInfos){
                    String code=info.getPersonId()+MemberConstants.DEFAULT_SEPARATOR_FROM_PERSONID_TO_SEQNO+info.getSeqNo()+MemberConstants.DEFAULT_SEPARATOR_FROM_PERSONID_TO_SEQNO+corpId;
                    Map<String,Object> del=new HashMap<>();
                    del.put("channelCode",channel);
                    del.put("code",code);
                    Map deleteMapResult= httpBaseService.execute(del,delUrl);
                    LOGGER.debug("结果："+JSONObject.toJSONString(deleteMapResult));
                }
                LOGGER.debug("范围："+mystart+":"+myend+"删除请求启动完毕");
            }
        }
    }



    ///////////////////////////////////////////////////////
    /*第一：获取全量的删除人脸的记录*/
    @Deprecated
    public int initHttpRequestWithDel() {
        LOGGER.error("----------初始化本地人脸库(删除)的流程开始：-----");
        //拉取全量的记录
        long current = System.currentTimeMillis();

        Date tempCur = new Date(current);

        String endDate = TransNoUtils.getInstance().getDate(tempCur);

        //开始时间
//        String templsy=environment.getProperty("bcss.init.delete.time");
        Map tempMap = DefinePropertiesFileUtils.getInstance().commonloadParam("bcss.init.delete.time");
        String templsy = (String) tempMap.get(MemberConstants.PROPERTIES_VALUE);
        if (templsy == null || templsy.isEmpty()) {
            templsy = "2019-01-01";
            LOGGER.error("获取初始化的删除人脸的起始时间失败，采用默认值：" + templsy);
        }
        Date judgeDate = TransNoUtils.getInstance().convertDate(templsy);
        if (judgeDate == null) {
            LOGGER.error("非标准时间：" + templsy);
        }
        String startDate = templsy;

        //
        String corpId = environment.getProperty("bcss.default.corpId");
        String operType = FaceOperType.OPERTYPE_EIGHT_FACE_DELETE_COUNT.getCode();//删除人脸的数量
        String clientTransNo = TransNoUtils.getInstance().getTransNoIn20();
        String timeStamp = TransNoUtils.getInstance().getTimeStamp();
        String featureType = "1";//图片
        String channel = null;
        String autoFullDown = "0";//0-需要，1-不需要
        String operInfo = null;
        String startNum = "1";
        String endNum = "10";

        Map<String, Object> param = new HashMap<>();
        param.put("corpId", corpId);
        param.put("timeStamp", timeStamp);
        param.put("clientTransNo", clientTransNo);
        param.put("operType", operType);
        param.put("featureType", featureType);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("channel", channel);
        param.put("startNum", startNum);
        param.put("endNum", endNum);
        param.put("operInfo", operInfo);
        param.put("autoFullDown", autoFullDown);

        Map<String, Object> res = faceAllService.regANDdelFaceAllService(param);
        LOGGER.error("----------初始化本地人脸库(删除)的结果：-----" + JSONObject.toJSONString(res));
        String returncode = (String) res.get(MemberConstants.RETURN_CODE);

        if (returncode == null || returncode.isEmpty()) {
            LOGGER.error("----------初始化本地人脸库(删除)的结果(存在异常情况)-----");
            return 1;
        }
        if (returncode.equals(MemberConstants.COMMON_SUCCESS)) {
            LOGGER.error("----------初始化本地人脸库(删除)的结果：成功-----");
            return 0;
        } else {
            LOGGER.error("----------初始化本地人脸库(删除)的结果：存在失败情况-----");
            return 1;
        }
    }



    /*第二：请求从初始天开始到三天组成*/
    @Deprecated
    public int initHttpRequest() {
        LOGGER.error("----------初始化本地人脸库(注册)的流程开始：-----");
//        BcssMemberFeatureQuerymemberinfoTestV1 testV1=new BcssMemberFeatureQuerymemberinfoTestV1();
//        try {
//            testV1.RequestFaceFromMember();
//        } catch (IcbcApiException e) {
//            e.printStackTrace();
//        }
//        return 0;

        long current = System.currentTimeMillis();
        long lsy = current - MemberConstants.REGISTER_STEP;

        Date tempCur = new Date(current);
        Date tempLsy = new Date(lsy);

        String startDate = TransNoUtils.getInstance().getDate(tempLsy);
        String endDate = TransNoUtils.getInstance().getDate(tempCur);

        //
        String corpId = environment.getProperty("bcss.default.corpId");
        String operType = FaceOperType.OPERTYPE_TWO_FACE_COUNT.getCode(); //注册人脸的数量
        String clientTransNo = TransNoUtils.getInstance().getTransNoIn20();
        String timeStamp = TransNoUtils.getInstance().getTimeStamp();
        String featureType = "1";//图片
        String channel = null;
        String autoFullDown = "0";//0-需要，1-不需要
        String operInfo = null;
        String startNum = "1";
        String endNum = "10";

        Map<String, Object> param = new HashMap<>();
        param.put("corpId", corpId);
        param.put("timeStamp", timeStamp);
        param.put("clientTransNo", clientTransNo);
        param.put("operType", operType);
        param.put("featureType", featureType);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("channel", channel);
        param.put("startNum", startNum);
        param.put("endNum", endNum);
        param.put("operInfo", operInfo);
        param.put("autoFullDown", autoFullDown);

        Map<String, Object> res = faceAllService.regANDdelFaceAllService(param);
        LOGGER.error("----------初始化本地人脸库(注册)的结果：-----" + JSONObject.toJSONString(res));
        String returncode = (String) res.get(MemberConstants.RETURN_CODE);

        if (returncode == null || returncode.isEmpty()) {
            LOGGER.error("----------初始化本地人脸库(注册)的结果(存在异常情况)-----");
            return 1;
        }
        if (returncode.equals(MemberConstants.COMMON_SUCCESS)) {
            LOGGER.error("----------初始化本地人脸库(注册)的结果：成功-----");
            return 0;
        } else {
            LOGGER.error("----------初始化本地人脸库(注册)的结果：存在失败情况-----");
            return 1;
        }
    }


    //第三个
    /*
     * 全量采用的策略：
     * 1,小批量，每一天获取一次当天修改人脸的时间
     * 2，判断是否为0，为零则跳过当天，并跳到下一天
     * 3,如果不为0，则按流程进行注册，注册完跳到下一天
     * 4，循环直到最后一天
     * */
    @Deprecated
    public int initHttpFullRequest() {
        long start = System.currentTimeMillis();
        LOGGER.error("----------全量同步人脸到本地人脸库的流程开始：-----" + start);

        ////最后时间
        long current = System.currentTimeMillis() - MemberConstants.REGISTER_STEP;
        Date tempCur = new Date(current);
        String endDate = TransNoUtils.getInstance().getDate(tempCur);

        //开始时间
//        String templsy=environment.getProperty("bcss.init.register.time");
        Map tempMap = DefinePropertiesFileUtils.getInstance().commonloadParam("bcss.init.register.time");
        String templsy = (String) tempMap.get(MemberConstants.PROPERTIES_VALUE);
        if (templsy == null || templsy.isEmpty()) {
            templsy = "2019-01-01";
            LOGGER.error("获取初始化的注册人脸的起始时间失败，采用默认值：" + templsy);
        }
        Date judgeDate = TransNoUtils.getInstance().convertDate(templsy);
        if (judgeDate == null) {
            LOGGER.error("非标准时间：" + templsy);
        }
        String startDate = templsy;

        //循环读取每一天
        String startArray[] = startDate.split("-");
        String endArray[] = endDate.split("-");
        if (startArray == null || startArray.length != 3 || endArray == null || endArray.length != 3) {
            LOGGER.error("开始时间和结束时间的时间格式异常" + startDate + "," + endDate);
            System.exit(1);
//            return 1;
        }
        //使用calendar注意月份需要-1
        Calendar startCalendarDate = Calendar.getInstance();
        startCalendarDate.set(Integer.parseInt(startArray[0]), Integer.parseInt(startArray[1]) - 1, Integer.parseInt(startArray[2]));
        long starTime = startCalendarDate.getTimeInMillis();
        Calendar endCalendarDate = Calendar.getInstance();
        endCalendarDate.set(Integer.parseInt(endArray[0]), Integer.parseInt(endArray[1]) - 1, Integer.parseInt(endArray[2]));
        long endTime = endCalendarDate.getTimeInMillis();

        long tempDate = starTime;
        long oneDay = 24 * 60 * 60 * 1000l;
        int failCount = 0;
        int succCount = 0;
        LOGGER.error("循环自然日的之前，打印开始时间和结束时间,它们分别为：" + startDate + " , " + endDate);
        while (tempDate <= endTime) {
            String target = TransNoUtils.getInstance().getDate(new Date(tempDate));
            System.out.println("----" + target);
            LOGGER.error("----------" + target + "开始注册：-----");
            //
            String corpId = environment.getProperty("bcss.default.corpId");
            String operType = FaceOperType.OPERTYPE_TWO_FACE_COUNT.getCode(); //注册人脸的数量
            String clientTransNo = TransNoUtils.getInstance().getTransNoIn20();
            String timeStamp = TransNoUtils.getInstance().getTimeStamp();
            String featureType = "1";//图片
            String channel = null;
            String autoFullDown = "0";//0-需要，1-不需要
            String operInfo = null;
            String startNum = "1";
            String endNum = "10";

            Map<String, Object> param = new HashMap<>();
            param.put("corpId", corpId);
            param.put("timeStamp", timeStamp);
            param.put("clientTransNo", clientTransNo);
            param.put("operType", operType);
            param.put("featureType", featureType);
            param.put("startDate", target);
            param.put("endDate", target);
            param.put("channel", channel);
            param.put("startNum", startNum);
            param.put("endNum", endNum);
            param.put("operInfo", operInfo);
            param.put("autoFullDown", autoFullDown);

            Map<String, Object> res = faceAllService.regANDdelFaceAllService(param);
            LOGGER.error("----------" + target + "结束注册，结果：-----" + JSONObject.toJSONString(res));
            String returncode = (String) res.get(MemberConstants.RETURN_CODE);
            if (returncode == null || returncode.isEmpty()) {
                LOGGER.error("----------" + target + "结束注册，结果存在异常：-----");
//                return 1;
                failCount = failCount + 1;
            }
            if (returncode.equals(MemberConstants.COMMON_SUCCESS)) {
                LOGGER.error("----------" + target + "结束注册，结果成功：-----");
//                return 0;

                succCount = succCount + 1;
            } else {
                LOGGER.error("----------" + target + "结束注册，结果失败：-----");
//                return 1;
                failCount = failCount + 1;
            }


            tempDate = tempDate + oneDay;
        }


        long end = System.currentTimeMillis();
        LOGGER.error("----------全量同步人脸到本地人脸库的流程结束：-----" + end);

        LOGGER.debug("failCount:" + failCount + ",succCount:" + succCount);
        if (failCount == 0) {
            return 0;
        } else {
            return 1;
        }
    }





    @Deprecated
    private void checkAndBuildFeatureGroupBcak() {
//        String corpIdS=environment.getProperty("bcss.default.corpId");
//        String corpNameS=environment.getProperty("bcss.default.corpName");
//        //拆分
//        String[] corpIdArray=corpIdS.split("\\|");
//        String[] corpNameArray=corpNameS.split("\\|");
//        int pageSize=50;
//        int pageNum=1;
//        Map<String,Object> corpParam=new HashMap<>();
//        for(int i=0;i<corpIdArray.length;i++){
//            corpParam.put(MemberConstants.KEY_CORP_ID,corpIdArray[i]);
//            corpParam.put(MemberConstants.KEY_CORP_NAME,corpNameArray[i]);
//            corpParam.put("pageNum",String.valueOf(pageNum));
//            corpParam.put("pageSize",String.valueOf(pageSize));
//            //①检测是否建立了特征分库
//            Map<String,Object> buildRes=bcssMemBuildGroupCompositeService.executeBackup(corpParam);
//            String isLoopFlag= (String) buildRes.get("loopFlag");
//            Map<String,Object> nextMap=new HashMap<>();
//            nextMap.putAll(corpParam);
//            while ("1".equals(isLoopFlag)){
//                pageNum=pageNum+1;
//                nextMap.put("pageNum",String.valueOf(pageNum));
//                buildRes=bcssMemBuildGroupCompositeService.executeBackup(nextMap);
//                isLoopFlag=(String) buildRes.get("loopFlag");
//            }
//
//            if( !("0".equals(buildRes.get(MemberConstants.RETURN_CODE))) ){
//                //有失败的情况，退出系统
//                LOGGER.error("建立特征分库有失败情况，现在退出系统，请检查日志");
//                System.exit(1);
//            }
//            LOGGER.warn("--------------------------"+"当前corpId遍历完成："+corpIdArray[i]+"--------------------------");
//        }
//        LOGGER.warn("--------------------------"+"所有特征分库检测完毕，正常启动步骤：完成第一步"+"--------------------------");
    }
}

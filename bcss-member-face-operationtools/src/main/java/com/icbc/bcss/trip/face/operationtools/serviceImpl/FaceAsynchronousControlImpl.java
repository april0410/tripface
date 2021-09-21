package com.icbc.bcss.trip.face.operationtools.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.api.BcssMemberFeatureService;
import com.icbc.bcss.trip.face.operationtools.enums.RecordInfoType;
import com.icbc.bcss.trip.face.operationtools.responsemodel.BcssMemberFeatureQuerymemberinfoResponseV1;
import com.icbc.bcss.trip.face.operationtools.service.BcssMemRecordInfoCompositeService;
import com.icbc.bcss.trip.face.operationtools.service.FaceAllService;
import com.icbc.bcss.trip.face.operationtools.service.FaceAsynchronousControl;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceDeleteService;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceRegisterService;
import com.icbc.bcss.trip.face.operationtools.utils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.icbc.bcss.trip.face.operationtools.utils.FileUtils.recordErrorTimeAndReturnResult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Service("faceAsynchronousControl")
public class FaceAsynchronousControlImpl implements FaceAsynchronousControl {
    private static final Logger LOGGER = LoggerFactory.getLogger(FaceAsynchronousControlImpl.class);

    //注入自定义多线程池
    @Resource(name = "getAsyncExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private PropertyPlaceholder environment;

    @Autowired
    private FaceAllService faceAllService;

//    @Autowired
//    private FaceRegisterService faceRegisterService;

//    @Autowired
//    private FaceDeleteService faceDeleteService;

    @Autowired
    private BcssMemberFeatureService bcssMemberFeatureService;

    @Autowired
    private BcssMemRecordInfoCompositeService bcssMemRecordInfoCompositeService;

    /*
     * 作用：触发异步拉取人脸的入口
     * */
    @Override
    public Map<String, Object> startDownloadFaceRegInfoByAsync(Map<String, Object> parentParam, int toalCount) {
        String cliTranNo = (String) parentParam.get(MemberConstants.CLIENTTRANSNO);
        LOGGER.error("服务：startDownloadFaceRegInfoByAsync()--流水号：" + cliTranNo + ",开始时间为：" + TransNoUtils.getInstance().getTimeStamp());
        Map<String, Object> returnRes = new HashMap<>();//返回结果

        String faceRegThreadPool = environment.getProperty("bcss.default.faceRegister.threadCount");//人脸注册线程梳理
        int faceRegThreadPoolInt = 1;
        try {
            faceRegThreadPoolInt = Integer.parseInt(faceRegThreadPool);
        } catch (NumberFormatException e) {
            LOGGER.error("初始化阶段--获取并转换线程数量格式异常：" + e.getMessage());
            LOGGER.error(e.toString(), e);
            returnRes.put(MemberConstants.RETURN_CODE, "1");
            returnRes.put(MemberConstants.RETURN_MSG, "初始化阶段--获取并转换线程数量格式异常：" + e.getMessage());
            returnRes.put(MemberConstants.CLIENTTRANSNO, cliTranNo);
            return returnRes;
        }
        //小于100个人脸，就不需要多线程
        if (toalCount <= 100) {
            faceRegThreadPoolInt = 1;
        }

        //同步辅助类需要通过这个类来控制所有的线程都执行完成;
        CountDownLatch countDownLatch = new CountDownLatch(faceRegThreadPoolInt);
        //同时记录每一个线程对应的开始位置和候选位置
        List<Integer> listStart = new ArrayList<>();
        List<Integer> listEnd = new ArrayList<>();
        int averCount = toalCount / faceRegThreadPoolInt;
        for (int index = 0; index < faceRegThreadPoolInt; index++) {
            if (index < (faceRegThreadPoolInt - 1)) {
                listStart.add(index, index * averCount + 1);
                listEnd.add(index, (index + 1) * averCount);
            } else {
                listStart.add(index, index * averCount + 1);
                listEnd.add(index, toalCount);
            }

        }

        //行业
        String compareIndustry = (String) parentParam.get("compareIndustry");
        String operType = (String) parentParam.get("operType");
        String corpId = (String) parentParam.get("corpId");
//        final String txtPre="faceRegister";
        //循环启动多个线程
        for (int i = 0; i < faceRegThreadPoolInt; i++) {
            final int j = i;//子线程序号
            int startNum = listStart.get(i);
            int endNum = listEnd.get(i);
            LOGGER.warn("---------------------线程完成所指定范围的请求，范围为："+startNum+"||"+endNum+". " );
            //开始执行
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        String tip=null;
                        if("1".equals(operType)){
                            tip="register";
                        }else {
                            tip="delete";
                        }

                        System.out.println(Thread.currentThread().getName() +"--"+tip+ "---" + j);
                        Thread.currentThread().setName(Thread.currentThread().getName() +"--"+tip+ "---" + j);
                        System.out.println(Thread.currentThread().getName());
                        //正式业务
                        Map aynrcRes = registerAndDeleteDealDetailService(startNum, endNum, parentParam, compareIndustry, String.valueOf(j), operType, corpId);
                        LOGGER.warn("---------------------线程完成所指定范围的请求，范围为："+startNum+"||"+endNum+". " + JSONObject.toJSONString(aynrcRes));
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOGGER.error("初始化阶段--获取并转换线程数量格式异常：" + e.getMessage());
                        LOGGER.error(e.toString(), e);
                    } finally {
                        countDownLatch.countDown();  //这个不管是否异常都需要数量减,否则会被堵塞无法结束
                    }
                }
            });
            //启动结束，正在执行
            LOGGER.error("当前线程编号已经到" + j);
        }

        //尝试等待线程
        Map<String, Object> AsyncResult = new HashMap<>();
        try {
            LOGGER.error("*****************---------------------" + "所有子线程已经全部启动" + ",中间时间为：" + TransNoUtils.getInstance().getTimeStamp() + "*****************---------------------");
            countDownLatch.await(); //保证之前的所有的线程都执行完成，才会走下面的；
            LOGGER.error("*****************---------------------" + "所有子线程已经全部启动" + ",结束时间为：" + TransNoUtils.getInstance().getTimeStamp() + "*****************---------------------");
            // 这样就可以在下面拿到所有线程执行完的集合结果
            AsyncResult.put(MemberConstants.RETURN_CODE, "0");
            String outMsg=null;
            if("1".equals(operType)){
                outMsg="获取云端人脸注册信息的同步流程全部结果，成功";
            }else {
                outMsg="获取云端人脸删除信息的同步流程全部结果，成功";
            }
            AsyncResult.put(MemberConstants.RETURN_MSG, outMsg);

            //任意线程更新最后的数量
            setFaceDealCountByAll(operType,String.valueOf(toalCount));
            //更新define.properties
            recordErrorTimeAndReturnResult(parentParam, MemberConstants.COMMON_SUCCESS, outMsg);
        } catch (Exception e) {
            LOGGER.error("等待线程阶段--阻塞异常：" + e.getMessage());
            LOGGER.error(e.toString(), e);
            LOGGER.error("*****************---------------------" + "所有子线程已经全部启动" + ",结束时间为：" + TransNoUtils.getInstance().getTimeStamp() + "*****************---------------------");
            AsyncResult.put(MemberConstants.RETURN_CODE, "-1");
            AsyncResult.put(MemberConstants.RETURN_MSG, "等待线程阶段--阻塞异常：需要通过查询方法确定下载流程是否结束");
            //更新define.properties
            recordErrorTimeAndReturnResult(parentParam, "-1", "等待线程阶段--阻塞异常：需要通过查询方法确定下载流程是否结束");
        }

        //此处为单线程
        try {
            LOGGER.error("---=======结束多线程后的锁状态：" + FaceDataRegAndDelSingleton.getSingleton().getRegisterFlag() + "|" + FaceDataRegAndDelSingleton.getSingleton().getDeleteFlage() + "|" +
                    FaceDataRegAndDelSingleton.getSingleton().getRegisterDealCount() + "|" + FaceDataRegAndDelSingleton.getSingleton().getRegisterMemCount() + "|" +
                    FaceDataRegAndDelSingleton.getSingleton().getDeleteDealCount() + "|" + FaceDataRegAndDelSingleton.getSingleton().getDeleteMemCount());
            synchronized (this) {
                //解锁
                if ("1".equals(operType)) {
                    LOGGER.error("所有线程完成人脸信息拉取，operType为：" + operType + "，重置RegisterFlag为1");
                    FaceDataRegAndDelSingleton.getSingleton().setRegisterFlag("1");
                    FaceDataRegAndDelSingleton.getSingleton().setRegisterMemCount("0");
                    FaceDataRegAndDelSingleton.getSingleton().setRegisterDealCount("0");
                    FaceDataRegAndDelSingleton.getSingleton().setCorpIdForRegister(null);
                    FaceDataRegAndDelSingleton.getSingleton().getDealDelCountList().clear();
                } else {
                    LOGGER.error("所有线程完成人脸信息拉取，operType为：" + operType + "，重置DeleteFlage锁为1");
                    FaceDataRegAndDelSingleton.getSingleton().setDeleteFlage("1");
                    FaceDataRegAndDelSingleton.getSingleton().setDeleteMemCount("0");
                    FaceDataRegAndDelSingleton.getSingleton().setDeleteDealCount("0");
                    FaceDataRegAndDelSingleton.getSingleton().setCorpIdForDelete(null);
                    FaceDataRegAndDelSingleton.getSingleton().getDealDelCountList().clear();
                }
            }
            LOGGER.error("---=======结束多线程后的锁状态（已经重置）：" + FaceDataRegAndDelSingleton.getSingleton().getRegisterFlag() + "|" + FaceDataRegAndDelSingleton.getSingleton().getDeleteFlage() + "|" +
                    FaceDataRegAndDelSingleton.getSingleton().getRegisterDealCount() + "|" + FaceDataRegAndDelSingleton.getSingleton().getRegisterMemCount() + "|" +
                    FaceDataRegAndDelSingleton.getSingleton().getDeleteDealCount() + "|" + FaceDataRegAndDelSingleton.getSingleton().getDeleteMemCount());

        } catch (Exception e) {
            LOGGER.error("重置自定义锁异常，问题：" + e.toString());
            LOGGER.error(e.getMessage(), e);
            if ("1".equals(operType)) {
                LOGGER.error("所有线程完成人脸信息拉取，operType为：" + operType + "，重置RegisterFlag为1");
                FaceDataRegAndDelSingleton.getSingleton().setRegisterFlag("1");
            } else {
                LOGGER.error("所有线程完成人脸信息拉取，operType为：" + operType + "，重置DeleteFlage锁为1");
                FaceDataRegAndDelSingleton.getSingleton().setDeleteFlage("1");
            }
        }

//        LOGGER.error("*****************---------------------" + "Spring boot启动的初始化--开始" + "*****************---------------------");
        return AsyncResult;
    }


    /*
     * @处理明细和梳理
     * */
    private Map<String, Object> registerAndDeleteDealDetailService(int startNum, int endNum, Map<String, Object> tempParam, String compareIndustry, String ThreadNo, String operType, String corpId) {
        //循环获取
        int succCount = 0;//记录成功
        int failCount = 0;//记录失败
        int sevenCount = 0;//opertype的专用记录，因为operType与operType的记录不一定相同
//        Map outputtemp = new HashMap<String, Object>();
        String fullName = System.getProperty("user.dir") + File.separator +
                environment.getProperty("face.saveFilePath.defaultName") + File.separator;
        String detailMemberName = System.getProperty("user.dir") + File.separator +
                environment.getProperty("face.saveFilePath.defaultName.detailInfo") + File.separator;
        String ThreadpoolCount = environment.getProperty("bcss.default.faceRegister.threadCount");

        //组装文件名
        String infoTypeRecord=null;
        if ("1".equals(operType)) {
            //注册
            fullName = fullName + MemberConstants.REGISTER_REQUEST_API_FAILED_PRENAME + ThreadNo + MemberConstants.RECORD_FILE_SUFFIXNAME;
            detailMemberName = detailMemberName + MemberConstants.REGISTER_DETAIL_INFO_FAILED_PRENAME + MemberConstants.RECORD_FILE_SUFFIXNAME; //????验证多线程下的情况
            infoTypeRecord=RecordInfoType.INFO_TYPE_REGISTER_FACE_API_FAIL.getCode();
        } else {
            fullName = fullName + MemberConstants.DELETE_REQUEST_API_FAILED_PRENAME + ThreadNo + MemberConstants.RECORD_FILE_SUFFIXNAME;
            detailMemberName = detailMemberName + MemberConstants.DELETE_DETAIL_INFO_FAILED_PRENAME + MemberConstants.RECORD_FILE_SUFFIXNAME;
            infoTypeRecord=RecordInfoType.INFO_TYPE_DELETE_FACE_API_FAIL.getCode();
        }
        LOGGER.error("当前写入的文件的全路径为：" + fullName);

        int countDown = 1;
        for (long i = startNum; i <= endNum; i = i + MemberConstants.DEFAULT_INCREASE_STEP_FROM_FACE, countDown = countDown + 1) {
            //设置锁记录器的正在处理数量
            int ownCount = (countDown - 1) * Integer.parseInt(ThreadpoolCount);
            setFaceDealCountByThread0(operType, String.valueOf(ownCount));


            //计算结束位置
            long j = i + MemberConstants.DEFAULT_INCREASE_STEP_FROM_FACE - Long.parseLong("1");
            if(j>endNum){
                LOGGER.warn("当前拉取范围：最后计算值："+ j+",实际值："+endNum+". 取实际值");
                j=endNum;
            }

            //添加元素
            Long element=new Long(j-i+1);
            if ("1".equals(operType)) {
                FaceDataRegAndDelSingleton.getSingleton().getDealRegCountList().add(element.intValue());
            } else {
                FaceDataRegAndDelSingleton.getSingleton().getDealDelCountList().add(element.intValue());
            }

            //多线程的情况
            HashMap<String,Object> multiMap=new HashMap<>();
            multiMap.putAll(tempParam);
            multiMap.put("startNum", String.valueOf(i));
            multiMap.put("endNum", String.valueOf(j));

//            //重置startnum和endnum
//            temp.put("startNum", String.valueOf(i));
//            temp.put("endNum", String.valueOf(j));
            LOGGER.info("当前拉取范围：" + i + "," + j);
//            Map<String,Object> detail=bcssMemberFeatureService.executeUpdateFaceDetail(temp);
            //调用函数完成api请求
            Map<String, Object> detail = bcssMemberFeatureService.execute(multiMap);

            if (detail == null || detail.isEmpty()) {
                failCount = failCount + 1;
                LOGGER.error("API-请求返回的人脸数据为空，请稍后重试!（记录日期）,记录文本");
//                outputtemp = recordErrorTimeAndReturnResult(temp, MemberConstants.COMMON_FAIL, "Detail-请求返回的人脸数据为空，请稍后重试!"
                FileUtils.saveRequestAPIFailWithMap(multiMap, operType, true, fullName);
                //记录数据库
                bcssMemRecordInfoCompositeService.insertModel(multiMap, infoTypeRecord,"API-请求返回的人脸数据为空，请稍后重试!（记录日期）,记录文本",
                        RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs());
                continue;
            }

            //判断返回结果
            String checkCode = (String) detail.get(MemberConstants.RETURN_CODE);
            if (!checkCode.equals("0")) {
                failCount = failCount + 1;
                LOGGER.error("API-请求返回的人脸数据交易失败，请稍后重试!（记录日期）");
//                return recordErrorTimeAndReturnResult(temp,MemberConstants.COMMON_FAIL,"Detail-请求返回的人脸数据交易失败，请稍后重试!");
//                outputtemp = recordErrorTimeAndReturnResult(temp, MemberConstants.COMMON_FAIL, "Detail-请求返回的人脸数据交易失败，请稍后重试!");
                FileUtils.saveRequestAPIFailWithMap(multiMap, operType, true, fullName);
                //记录数据库
                bcssMemRecordInfoCompositeService.insertModel(multiMap, infoTypeRecord,"API-请求返回的人脸数据交易失败，请稍后重试!（记录日期）",
                        RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs());
                continue;
            }

//            String dataList= (String) detail.get("dataList");
//            List<JSONObject> dataList=JSONObject.parseArray(JSONObject.toJSONString(detail.get("dataList")),JSONObject.class);
            List<JSONObject> dataListOld = (List<JSONObject>) detail.get("dataList");
            if (dataListOld == null || dataListOld.isEmpty()) {
                failCount = failCount + 1;
                LOGGER.error("API-请求返回的人脸数据明细列表为空，请稍后重试!（记录日期）");
                FileUtils.saveRequestAPIFailWithMap(multiMap, operType, true, fullName);
                //记录数据库
                bcssMemRecordInfoCompositeService.insertModel(multiMap, infoTypeRecord,"API-请求返回的人脸数据明细列表为空，请稍后重试!（记录日期）",
                        RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs());
                continue;
            }

            String dataList = JSONObject.toJSONString(dataListOld);

            int failDataListCount = 0;
            int succDataListCount = 0;
            //拆出dataList
            List<BcssMemberFeatureQuerymemberinfoResponseV1.DataInfo> dataInofList = JSONObject.parseArray(dataList, BcssMemberFeatureQuerymemberinfoResponseV1.DataInfo.class);
            for (BcssMemberFeatureQuerymemberinfoResponseV1.DataInfo tempDataInfo : dataInofList) {

                String custId = tempDataInfo.getPersonId() + MemberConstants.DEFAULT_SEPARATOR_FROM_PERSONID_TO_SEQNO + tempDataInfo.getSeqNo() +
                        MemberConstants.DEFAULT_SEPARATOR_FROM_PERSONID_TO_SEQNO + corpId;
                LOGGER.warn("当前人脸唯一编号：" + custId);
                //梳理会员信息
                Map memInfo = null;
                if (tempDataInfo.getMemDetail1() == null || tempDataInfo.getMemDetail1().isEmpty()) {
                    memInfo = new HashMap<String, Object>();
                } else {
                    memInfo = JSONObject.parseObject(tempDataInfo.getMemDetail1(), Map.class);
                }
                //转换结果Map
                String jsonResult = JSONObject.toJSONString(tempDataInfo);
                Map mapResult = JSONObject.parseObject(jsonResult, Map.class);
                mapResult.put(MemberConstants.CLIENTTRANSNO, tempParam.get(MemberConstants.CLIENTTRANSNO));
                mapResult.put("personUniqueFaceNo", custId);
                mapResult.put("corpId", corpId);
                //下载下来的人脸是否命中指定
                if (operType.equals("1")) {
                    LOGGER.debug("当前使用的operType为：" + operType);
                    //多行业情况--一般有最宽松规则，和最严格规则
                    //最宽松规则--只要云端人脸的所对应的行业标识符，命中本地规定的行业标识符一个，则可以注册；若全部不中，则调用删除接口
                    //最严格规则--云端人脸的所对应的行业标识符，必须全部命中（包含、大于）本地的行业标识符，才能注册，否则删除
                    boolean targetFlag = BcssMemberCompareUtils.judgeCloudIndustryHintInLocal(compareIndustry, tempDataInfo.getIndustry());
                    if (targetFlag == true) {//最宽松原则
                        //判断人脸在不在,不在就下一个
                        String faceBase = tempDataInfo.getMemParam();
                        if (faceBase == null || faceBase.isEmpty()) {
                            LOGGER.error("当前人脸数据为空，进行下一个注册,编号为" + custId);
                            failDataListCount = failDataListCount + 1;
                            //记录注册失败的memNo和memCardNO
//                            RecordMemberinfoUtils.getInstance().judgeMemberIndustry(tempDataInfo,compareIndustry,"1");//不设返回值
                            String context = custId + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + memInfo.get("memCardNo") + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME +
                                    tempDataInfo.getMemName() + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + tempDataInfo.getMemNo() + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + TransNoUtils.getInstance().getTimeStamp()+
                                    System.lineSeparator();
                            FileUtils.writeFile(detailMemberName, context, operType, true);
                            //记录数据库
                            int tempRes=bcssMemRecordInfoCompositeService.insertModelByDetail(multiMap, RecordInfoType.INFO_TYPE_REGISTER_FACE_DEAL_DETAIL_FAIL.getCode(),"当前人脸数据为空，进行下一个注册,编号为" + custId,
                                    RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs(),context,null);
                            if(tempRes==0){
                                LOGGER.debug("registerAndDeleteDealDetailService--插入日志记录成功");
                            }else{
                                LOGGER.debug("registerAndDeleteDealDetailService--插入日志记录失败");
                            }
                            continue;
                        }

                        //新增来源字段
                        mapResult.put("dataSource","0");//0--云端
                        //命中，调用注册接口
                        //注册人脸
//                        Map<String, Object> regRes = faceRegisterService.registerService(mapResult);
//                        if (regRes == null || regRes.isEmpty()) {
//                            LOGGER.error("注册结果为空，请查看日志,该客户编号：" + custId);
//                            failDataListCount = failDataListCount + 1;
//                            //记录注册失败的memNo和memCardNO
//                            String context = custId + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + memInfo.get("memCardNo") + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME +
//                                    tempDataInfo.getMemName() + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + tempDataInfo.getMemNo() + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + TransNoUtils.getInstance().getTimeStamp()+
//                                    System.lineSeparator();
//                            FileUtils.writeFile(detailMemberName, context, operType, true);
//                            //记录数据库
//                            int tempRes=bcssMemRecordInfoCompositeService.insertModelByDetail(multiMap, RecordInfoType.INFO_TYPE_REGISTER_FACE_DEAL_DETAIL_FAIL.getCode(),"注册结果为空，请查看日志,该客户编号：" + custId,
//                                    RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs(),context,regRes);
//                            if(tempRes==0){
//                                LOGGER.debug("registerAndDeleteDealDetailService--插入日志记录成功");
//                            }else{
//                                LOGGER.debug("registerAndDeleteDealDetailService--插入日志记录失败");
//                            }
//                            continue;
//                        }

//                        String judge = (String) regRes.get(MemberConstants.RETURN_CODE);
//                        if (!judge.equals(MemberConstants.COMMON_SUCCESS)) {
//                            LOGGER.error("注册结果为失败，请查看日志,该客户编号：" + custId);
//                            failDataListCount = failDataListCount + 1;
//                            //记录注册失败的memNo和memCardNO
////                            RecordMemberinfoUtils.getInstance().judgeMemberIndustry(tempDataInfo, compareIndustry, "1");//不设返回值
//                            String context = custId + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + memInfo.get("memCardNo") + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME +
//                                    tempDataInfo.getMemName() + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + tempDataInfo.getMemNo() + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + TransNoUtils.getInstance().getTimeStamp()+
//                                    System.lineSeparator();
//                            FileUtils.writeFile(detailMemberName, context, operType, true);
//                            //记录数据库
//                            int tempRes=bcssMemRecordInfoCompositeService.insertModelByDetail(multiMap, RecordInfoType.INFO_TYPE_REGISTER_FACE_DEAL_DETAIL_FAIL.getCode(),"注册结果为失败，请查看日志,该客户编号：" + custId,
//                                    RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs(),context,regRes);
//                            if(tempRes==0){
//                                LOGGER.debug("registerAndDeleteDealDetailService--插入日志记录成功");
//                            }else{
//                                LOGGER.debug("registerAndDeleteDealDetailService--插入日志记录失败");
//                            }
//                            continue;
//                        } else {
//                            LOGGER.error("注册结果为成功，该客户编号：" + custId);
//                            succDataListCount = succDataListCount + 1;
//                            //注册成功，剔除memNo和memCardNO
////                            RecordMemberinfoUtils.getInstance().judgeMemberIndustry(tempDataInfo, compareIndustry, "0");//不设返回值
////                            continue;
//                        }

                    } else {
                        //没有命中，调用删除接口
//                    continue;
//                        Map<String, Object> delRes = faceDeleteService.deleteService(mapResult);
//                        if (delRes == null || delRes.isEmpty()) {
//                            LOGGER.error("没有命中industry--删除结果为空，请查看日志");
//                            failDataListCount = failDataListCount + 1;
//                            String context = custId + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + memInfo.get("memCardNo") + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME +
//                                    tempDataInfo.getMemName() + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + tempDataInfo.getMemNo() + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + TransNoUtils.getInstance().getTimeStamp()+
//                                    System.lineSeparator();
//                            FileUtils.writeFile(detailMemberName, context, operType, true);
//                            //记录数据库
//                            int tempRes=bcssMemRecordInfoCompositeService.insertModelByDetail(multiMap, RecordInfoType.INFO_TYPE_DELETE_FACE_DEAL_DETAIL_FAIL.getCode(),"没有命中industry--删除结果为空，请查看日志" + custId,
//                                    RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs(),context,delRes);
//                            if(tempRes==0){
//                                LOGGER.debug("registerAndDeleteDealDetailService--插入日志记录成功");
//                            }else{
//                                LOGGER.debug("registerAndDeleteDealDetailService--插入日志记录失败");
//                            }
//                            continue;
//                        }
//
//                        String judge = (String) delRes.get(MemberConstants.RETURN_CODE);
//                        if (!judge.equals(MemberConstants.COMMON_SUCCESS)) {
//                            LOGGER.error("没有命中industry--删除结果为失败，请查看日志");
//                            failDataListCount = failDataListCount + 1;
//                            String context = custId + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + memInfo.get("memCardNo") + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME +
//                                    tempDataInfo.getMemName() + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + tempDataInfo.getMemNo() + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + TransNoUtils.getInstance().getTimeStamp()+
//                                    System.lineSeparator();
//                            FileUtils.writeFile(detailMemberName, context, operType, true);
//                            //记录数据库
//                            int tempRes=bcssMemRecordInfoCompositeService.insertModelByDetail(multiMap, RecordInfoType.INFO_TYPE_DELETE_FACE_DEAL_DETAIL_FAIL.getCode(),"没有命中industry--删除结果为失败，请查看日志" + custId,
//                                    RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs(),context,delRes);
//                            if(tempRes==0){
//                                LOGGER.debug("registerAndDeleteDealDetailService--插入日志记录成功");
//                            }else{
//                                LOGGER.debug("registerAndDeleteDealDetailService--插入日志记录失败");
//                            }
//                            continue;
//                        } else {
//                            LOGGER.debug("删除人脸结果为成功");
//                            succDataListCount = succDataListCount + 1;
////                            continue;
//                        }
                        //
                    }
                } else if (operType.equals("7")) {
                    sevenCount = sevenCount + 1;
                    LOGGER.debug("当前使用的operType为：" + operType);
//                    Map<String, Object> delRes = faceDeleteService.deleteService(mapResult);
//                    if (delRes == null || delRes.isEmpty()) {
//                        LOGGER.error("7-删除结果为空，请查看日志");
//                        failDataListCount = failDataListCount + 1;
//                        String context = custId + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + memInfo.get("memCardNo") + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME +
//                                tempDataInfo.getMemName() + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + tempDataInfo.getMemNo() + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + TransNoUtils.getInstance().getTimeStamp()+
//                                System.lineSeparator();
//                        FileUtils.writeFile(detailMemberName, context, operType, true);
//                        //记录数据库
//                        int tempRes=bcssMemRecordInfoCompositeService.insertModelByDetail(multiMap, RecordInfoType.INFO_TYPE_DELETE_FACE_DEAL_DETAIL_FAIL.getCode(),"7-删除结果为空，请查看日志" + custId,
//                                RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs(),context,delRes);
//                        if(tempRes==0){
//                            LOGGER.debug("registerAndDeleteDealDetailService--插入日志记录成功");
//                        }else{
//                            LOGGER.debug("registerAndDeleteDealDetailService--插入日志记录失败");
//                        }
//                        continue;
//                    }
//
//                    String judge = (String) delRes.get(MemberConstants.RETURN_CODE);
//                    if (!judge.equals(MemberConstants.COMMON_SUCCESS)) {
//                        LOGGER.error("7-删除结果为失败，请查看日志");
//                        failDataListCount = failDataListCount + 1;
//                        String context = custId + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + memInfo.get("memCardNo") + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME +
//                                tempDataInfo.getMemName() + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + tempDataInfo.getMemNo() + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + TransNoUtils.getInstance().getTimeStamp()+
//                                System.lineSeparator();
//                        FileUtils.writeFile(detailMemberName, context, operType, true);
//                        //记录数据库
//                        int tempRes=bcssMemRecordInfoCompositeService.insertModelByDetail(multiMap, RecordInfoType.INFO_TYPE_DELETE_FACE_DEAL_DETAIL_FAIL.getCode(),"7-删除结果为失败，请查看日志" + custId,
//                                RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs(),context,delRes);
//                        if(tempRes==0){
//                            LOGGER.debug("registerAndDeleteDealDetailService--插入日志记录成功");
//                        }else{
//                            LOGGER.debug("registerAndDeleteDealDetailService--插入日志记录失败");
//                        }
//                        continue;
//                    } else {
//                        LOGGER.debug("7-删除人脸结果为成功");
//                        succDataListCount = succDataListCount + 1;
//                    }
                }
                //opertype=7
                else {
                    LOGGER.error("系统异常，当前operType的值非指定值，异常抛出" + operType);
                    Map exRes = recordErrorTimeAndReturnResult(tempParam, MemberConstants.COMMON_OPERTYPE_EXCEPTION, "系统异常，当前operType的值非指定值，异常抛出" + operType);
                    return exRes;
//                    throw new BusinessException(JSONObject.toJSONString(exRes));
                }
            }//for结果
            LOGGER.warn("当前dataList明细注册结束,failDataListCount:" + failDataListCount + " ,succDataListCount:" + succDataListCount);

            //根据dataList的for循环来判断succCount
            if (failDataListCount == 0) {
                LOGGER.warn("dataList的人脸数据同步成功");
                succCount = succCount + 1;
            } else {
                LOGGER.warn("dataList的人脸数据同步有失败情况");
                failCount = failCount + 1;
            }
        }
        //更新范围
        LOGGER.info("***********更新范围分割线*************,failCount:" + failCount + " ,succCount:" + succCount + " ,severnCount:" + sevenCount);

        //根据memCount的for循环来判断succCount
        String reCode = null;
        String reMsg = null;
        if (failCount == 0) {
            LOGGER.warn("总数为:" + endNum + "，人脸数据同步成功");
//            succCount=succCount+1;
            reCode = MemberConstants.COMMON_SUCCESS;
            reMsg = "指定时间范围的人脸数据全部成功同步到本地，数量为：" + endNum;
        } else {
            LOGGER.warn("总数为:" + endNum + "，人脸数据同步有失败情况");
//            failCount=failCount+1;
            reCode = MemberConstants.COMMON_FAIL;
            reMsg = "指定时间范围的人脸数据全部同步到本地,有失败情况";
        }


        HashMap succRes = new HashMap<String, Object>();
        succRes.put(MemberConstants.RETURN_CODE, reCode);
        succRes.put(MemberConstants.RETURN_MSG, reMsg);
        succRes.put(MemberConstants.CLIENTTRANSNO, tempParam.get(MemberConstants.CLIENTTRANSNO));

        LOGGER.info("---***---***" + "此次根据查询数量自动下载对应的全部人脸数据流程 结束" + "***---***---");
        return succRes;

    }

    //判断是否线程0，如果是，则更新正在处理的记录
    private void setFaceDealCountByThread0(String operType, String dealCount) {
        String name = Thread.currentThread().getName();
        if (name.endsWith("0")) {//0号线程
            LOGGER.info("当前线程为：" + name + ",是0号线程，更新处理数量");
            if ("1".equals(operType)) {
                FaceDataRegAndDelSingleton.getSingleton().setRegisterDealCount(dealCount);
            } else {
                FaceDataRegAndDelSingleton.getSingleton().setDeleteDealCount(dealCount);
            }
        }
    }

    private void setFaceDealCountByAll(String operType, String dealCount) {
        LOGGER.info("当前线程为：" + Thread.currentThread().getName() + "，更新处理数量");
        if ("1".equals(operType)) {
            FaceDataRegAndDelSingleton.getSingleton().setRegisterDealCount(dealCount);
        } else {
            FaceDataRegAndDelSingleton.getSingleton().setDeleteDealCount(dealCount);
        }

    }


}

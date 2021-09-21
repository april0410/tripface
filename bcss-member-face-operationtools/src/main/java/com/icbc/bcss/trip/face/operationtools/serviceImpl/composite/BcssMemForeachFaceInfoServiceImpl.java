package com.icbc.bcss.trip.face.operationtools.serviceImpl.composite;

import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.enums.RecordInfoType;
import com.icbc.bcss.trip.face.operationtools.requestmodel.BcssMemberFeatureQuerymemberinfoRequestV1;
import com.icbc.bcss.trip.face.operationtools.responsemodel.BcssMemberFeatureQuerymemberinfoResponseV1;
import com.icbc.bcss.trip.face.operationtools.service.BcssMemRecordInfoCompositeService;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceDeleteService;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceRegisterService;
import com.icbc.bcss.trip.face.operationtools.service.composite.BcssMemForeachFaceInfoService;
import com.icbc.bcss.trip.face.operationtools.utils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
* 针对此次api请求（人脸明细），进行循环注册或者删除
* */
@Service("bcssMemForeachFaceInfoService")
public class BcssMemForeachFaceInfoServiceImpl implements BcssMemForeachFaceInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BcssMemForeachFaceInfoServiceImpl.class);
//    @Autowired
//    private FaceRegisterService faceRegisterService;

//    @Autowired
//    private FaceDeleteService faceDeleteService;

    @Autowired
    private PropertyPlaceholder environment;

    @Autowired
    private BcssMemRecordInfoCompositeService bcssMemRecordInfoCompositeService;

    @Override
    public Map<String, Object> foreachRegisterInfo(Map<String, Object> sourceMap,Map<String, Object> parentMap) {
        String clientTransNo= (String) parentMap.get("clientTransNo");
//        return null;
        String corpId= (String) sourceMap.get("corpId");
        String operType= (String) sourceMap.get("operType");
        String fullName = System.getProperty("user.dir") + File.separator +
                environment.getProperty("face.saveFilePath.defaultName") + File.separator;
        String detailMemberName = System.getProperty("user.dir") + File.separator +
                environment.getProperty("face.saveFilePath.defaultName.detailInfo") + File.separator;
        String ThreadNo="0";
        String compareIndustry=environment.getProperty("bcss.default.industry");
        //组装文件名
        String infoTypeRecord=null;
        if ("1".equals(operType)) {
            //注册
            fullName = fullName + MemberConstants.REGISTER_REQUEST_API_FAILED_PRENAME + ThreadNo + MemberConstants.RECORD_FILE_SUFFIXNAME;
            detailMemberName = detailMemberName + MemberConstants.REGISTER_DETAIL_INFO_FAILED_PRENAME + MemberConstants.RECORD_FILE_SUFFIXNAME; //????验证多线程下的情况
            infoTypeRecord=RecordInfoType.INFO_TYPE_REGISTER_FACE_RANGE_DETAIL_FAIL.getCode();
        } else {
            fullName = fullName + MemberConstants.DELETE_REQUEST_API_FAILED_PRENAME + ThreadNo + MemberConstants.RECORD_FILE_SUFFIXNAME;
            detailMemberName = detailMemberName + MemberConstants.DELETE_DETAIL_INFO_FAILED_PRENAME + MemberConstants.RECORD_FILE_SUFFIXNAME;
            infoTypeRecord= RecordInfoType.INFO_TYPE_DELETE_FACE_RANGE_DETAIL_FAIL.getCode();
        }
        LOGGER.error("当前写入的文件的全路径为：" + fullName);

        //判断结果
        if (parentMap == null || parentMap.isEmpty()) {
            LOGGER.error("Detail-请求返回的人脸数据为空，请稍后重试!（记录日期）,记录文本");
//            BcssMemberFeatureQuerymemberinfoRequestV1.BcssMemberFeatureQuerymemberinfoRequestBizV1 bizV1 =
//                    new BcssMemberFeatureQuerymemberinfoRequestV1.BcssMemberFeatureQuerymemberinfoRequestBizV1();
//            BeanUtils.copyProperties(sourceMap, bizV1);
//            FileUtils.saveRequestFailBiz(bizV1, operType, true, fullName);

            //记录数据库
//            int tempRes=bcssMemRecordInfoCompositeService.insertModelByDetail(sourceMap, infoTypeRecord,"Detail-请求返回的人脸数据为空，请稍后重试!（记录日期）,记录文本",
//                    RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs(),null,parentMap);
//            if(tempRes==0){
//                LOGGER.debug("foreachRegisterInfo--插入日志记录成功");
//            }else{
//                LOGGER.debug("foreachRegisterInfo--插入日志记录失败");
//            }
            return CommonMapReturn.constructMapWithClientTransNo("-1","Detail-请求返回的人脸数据为空，请稍后重试!（记录日期）,记录文本",clientTransNo);
        }

        //判断返回结果
        String checkCode = (String) parentMap.get(MemberConstants.RETURN_CODE);
        if (!checkCode.equals("0")) {
            LOGGER.error("Detail-请求返回的人脸数据交易失败，请稍后重试!（记录日期）");
            return CommonMapReturn.constructMapWithClientTransNo("-1","Detail-请求返回的人脸数据交易失败，请稍后重试!（记录日期）",clientTransNo);
        }

//            List<JSONObject> dataList=JSONObject.parseArray(JSONObject.toJSONString(detail.get("dataList")),JSONObject.class);
        List<JSONObject> dataListOld = (List<JSONObject>) parentMap.get("dataList");
        if (dataListOld == null || dataListOld.isEmpty()) {
            LOGGER.error("Detail-请求返回的人脸数据明细列表为空，请稍后重试!（记录日期）");
            return CommonMapReturn.constructMapWithClientTransNo("-1","Detail-请求返回的人脸数据明细列表为空，请稍后重试!（记录日期）",clientTransNo);
        }

        String dataList = JSONObject.toJSONString(dataListOld);

        int failDataListCount = 0;
        int succDataListCount = 0;
        //拆出dataList
        List<BcssMemberFeatureQuerymemberinfoResponseV1.DataInfo> dataInofList = JSONObject.parseArray(dataList, BcssMemberFeatureQuerymemberinfoResponseV1.DataInfo.class);
//            HashMap<String,Object> dataInfoList2=JSONObject.parseObject(dataList, HashMap.class);
        for (BcssMemberFeatureQuerymemberinfoResponseV1.DataInfo tempDataInfo : dataInofList) {

            String custId = tempDataInfo.getPersonId() + MemberConstants.DEFAULT_SEPARATOR_FROM_PERSONID_TO_SEQNO + tempDataInfo.getSeqNo() +
                    MemberConstants.DEFAULT_SEPARATOR_FROM_PERSONID_TO_SEQNO + corpId;
            LOGGER.warn("当前人脸唯一编号：" + custId);
            //梳理会员信息
            Map memInfo=null;
            if(tempDataInfo.getMemDetail1()==null||tempDataInfo.getMemDetail1().isEmpty()){
                memInfo=new HashMap<String,Object>();
            }else {
                memInfo=JSONObject.parseObject(tempDataInfo.getMemDetail1(),Map.class);
            }
            //转换结果Map
            String jsonResult=JSONObject.toJSONString(tempDataInfo);
            Map mapResult=JSONObject.parseObject(jsonResult,Map.class);
            mapResult.put(MemberConstants.CLIENTTRANSNO,sourceMap.get(MemberConstants.CLIENTTRANSNO));
            mapResult.put("personUniqueFaceNo",custId);
            mapResult.put("corpId",corpId);
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
                        String context=custId+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+memInfo.get("memCardNo")+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+
                                tempDataInfo.getMemName()+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+tempDataInfo.getMemNo()+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+ TransNoUtils.getInstance().getTimeStamp();
//                        FileUtils.writeFile(detailMemberName,context,operType,true);
                        //记录数据库
                        int tempRes=bcssMemRecordInfoCompositeService.insertModelByDetail(sourceMap, infoTypeRecord,"当前人脸数据为空，进行下一个注册,编号为" + custId,
                                RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs(),context,parentMap);
                        if(tempRes==0){
                            LOGGER.debug("foreachRegisterInfo--插入日志记录成功");
                        }else{
                            LOGGER.debug("foreachRegisterInfo--插入日志记录失败");
                        }
                        continue;
                    }

                    //新增来源字段
                    mapResult.put("dataSource","0");//0--云端
                    //命中，调用注册接口
                    //注册人脸
//                    Map<String, Object> regRes = faceRegisterService.registerService(mapResult);
//                    if (regRes == null || regRes.isEmpty()) {
//                        LOGGER.error("注册结果为空，请查看日志,该客户编号：" + custId);
//                        failDataListCount = failDataListCount + 1;
//                        //记录注册失败的memNo和memCardNO
//                        String context=custId+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+memInfo.get("memCardNo")+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+
//                                tempDataInfo.getMemName()+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+tempDataInfo.getMemNo()+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+TransNoUtils.getInstance().getTimeStamp();
////                        FileUtils.writeFile(detailMemberName,context,operType,true);
//                        //记录数据库
//                        int tempRes=bcssMemRecordInfoCompositeService.insertModelByDetail(sourceMap, infoTypeRecord,"注册结果为空，请查看日志,该客户编号：" + custId,
//                                RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs(),context,parentMap);
//                        if(tempRes==0){
//                            LOGGER.debug("foreachRegisterInfo--插入日志记录成功");
//                        }else{
//                            LOGGER.debug("foreachRegisterInfo--插入日志记录失败");
//                        }
//                        continue;
//                    }
//
//                    String judge = (String) regRes.get(MemberConstants.RETURN_CODE);
//                    if (!judge.equals(MemberConstants.COMMON_SUCCESS)) {
//                        LOGGER.error("注册结果为失败，请查看日志,该客户编号：" + custId);
//                        failDataListCount = failDataListCount + 1;
//                        //记录注册失败的memNo和memCardNO
////                            RecordMemberinfoUtils.getInstance().judgeMemberIndustry(tempDataInfo, compareIndustry, "1");//不设返回值
//                        String context=custId+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+memInfo.get("memCardNo")+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+
//                                tempDataInfo.getMemName()+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+tempDataInfo.getMemNo()+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+TransNoUtils.getInstance().getTimeStamp();
////                        FileUtils.writeFile(detailMemberName,context,operType,true);
//                        //记录数据库
//                        int tempRes=bcssMemRecordInfoCompositeService.insertModelByDetail(sourceMap, infoTypeRecord,"注册结果为失败，请查看日志,该客户编号：" + custId,
//                                RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs(),context,parentMap);
//                        if(tempRes==0){
//                            LOGGER.debug("foreachRegisterInfo--插入日志记录成功");
//                        }else{
//                            LOGGER.debug("foreachRegisterInfo--插入日志记录失败");
//                        }
//                        continue;
//                    } else {
//                        LOGGER.error("注册结果为成功，该客户编号：" + custId);
//                        succDataListCount = succDataListCount + 1;
//                        //注册成功，剔除memNo和memCardNO
////                            RecordMemberinfoUtils.getInstance().judgeMemberIndustry(tempDataInfo, compareIndustry, "0");//不设返回值
////                            continue;
//                    }

                } else {
                    //没有命中，调用删除接口
//                    Map<String, Object> delRes = faceDeleteService.deleteService(mapResult);
//                    if (delRes == null || delRes.isEmpty()) {
//                        LOGGER.error("删除结果为空，请查看日志,该客户编号：" + custId);
//                        failDataListCount = failDataListCount + 1;
//                        String context=custId+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+memInfo.get("memCardNo")+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+
//                                tempDataInfo.getMemName()+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+tempDataInfo.getMemNo()+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+TransNoUtils.getInstance().getTimeStamp();
////                        FileUtils.writeFile(detailMemberName,context,operType,true);
//                        //记录数据库
//                        int tempRes=bcssMemRecordInfoCompositeService.insertModelByDetail(sourceMap, infoTypeRecord,"删除结果为空，请查看日志,该客户编号：" + custId,
//                                RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs(),context,parentMap);
//                        if(tempRes==0){
//                            LOGGER.debug("foreachRegisterInfo--插入日志记录成功");
//                        }else{
//                            LOGGER.debug("foreachRegisterInfo--插入日志记录失败");
//                        }
//                        continue;
//                    }
//
//                    String judge = (String) delRes.get(MemberConstants.RETURN_CODE);
//                    if (!judge.equals(MemberConstants.COMMON_SUCCESS)) {
//                        LOGGER.error("删除结果为失败，请查看日志,该客户编号：" + custId);
//                        failDataListCount = failDataListCount + 1;
//                        String context=custId+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+memInfo.get("memCardNo")+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+
//                                tempDataInfo.getMemName()+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+tempDataInfo.getMemNo()+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+TransNoUtils.getInstance().getTimeStamp();
////                        FileUtils.writeFile(detailMemberName,context,operType,true);
//                        int tempRes=bcssMemRecordInfoCompositeService.insertModelByDetail(sourceMap, infoTypeRecord,"删除结果为失败，请查看日志,该客户编号：" + custId,
//                                RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs(),context,parentMap);
//                        if(tempRes==0){
//                            LOGGER.debug("foreachRegisterInfo--插入日志记录成功");
//                        }else{
//                            LOGGER.debug("foreachRegisterInfo--插入日志记录失败");
//                        }
//                        continue;
//                    } else {
//                        LOGGER.debug("删除人脸结果为成功");
//                        succDataListCount = succDataListCount + 1;
////                            continue;
//                    }
                    //
                }
            } else if (operType.equals("7")) {
                LOGGER.debug("当前使用的operType为：" + operType);
//                Map<String, Object> delRes = faceDeleteService.deleteService(mapResult);
//                if (delRes == null || delRes.isEmpty()) {
//                    LOGGER.error("7-删除结果为空，请查看日志,该客户编号：" + custId);
//                    failDataListCount = failDataListCount + 1;
//                    String context=custId+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+memInfo.get("memCardNo")+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+
//                            tempDataInfo.getMemName()+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+tempDataInfo.getMemNo()+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+TransNoUtils.getInstance().getTimeStamp();
////                    FileUtils.writeFile(detailMemberName,context,operType,true);
//                    int tempRes=bcssMemRecordInfoCompositeService.insertModelByDetail(sourceMap, infoTypeRecord,"7-删除结果为空，请查看日志,该客户编号：" + custId,
//                            RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs(),context,parentMap);
//                    if(tempRes==0){
//                        LOGGER.debug("foreachRegisterInfo--插入日志记录成功");
//                    }else{
//                        LOGGER.debug("foreachRegisterInfo--插入日志记录失败");
//                    }
//                    continue;
//                }

//                String judge = (String) delRes.get(MemberConstants.RETURN_CODE);
//                if (!judge.equals(MemberConstants.COMMON_SUCCESS)) {
//                    LOGGER.error("7-删除结果为失败，请查看日志,该客户编号：" + custId);
//                    failDataListCount = failDataListCount + 1;
//                    String context=custId+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+memInfo.get("memCardNo")+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+
//                            tempDataInfo.getMemName()+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+tempDataInfo.getMemNo()+MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME+TransNoUtils.getInstance().getTimeStamp();
////                    FileUtils.writeFile(detailMemberName,context,operType,true);
//                    int tempRes=bcssMemRecordInfoCompositeService.insertModelByDetail(sourceMap, infoTypeRecord,"7-删除结果为失败，请查看日志,该客户编号：" + custId,
//                            RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs(),context,parentMap);
//                    if(tempRes==0){
//                        LOGGER.debug("foreachRegisterInfo--插入日志记录成功");
//                    }else{
//                        LOGGER.debug("foreachRegisterInfo--插入日志记录失败");
//                    }
//                    continue;
//                } else {
//                    LOGGER.debug("7-删除人脸结果为成功");
//                    succDataListCount = succDataListCount + 1;
//                }
            }
            //opertype=7
            else {
                LOGGER.error("系统异常，当前operType的值非指定值，异常抛出" + operType);
                Map exRes=new HashMap<String,Object>();
                exRes.put(MemberConstants.RETURN_CODE,"-1");
                exRes.put(MemberConstants.RETURN_MSG,"系统异常，当前operType的值非指定值，异常抛出" + operType);
                exRes.put(MemberConstants.CLIENTTRANSNO,sourceMap.get(MemberConstants.CLIENTTRANSNO));
                return exRes;
//                    throw new BusinessException(JSONObject.toJSONString(exRes));
            }
        }//for结果
        LOGGER.warn("当前dataList明细注册结束,failDataListCount:" + failDataListCount + " ,succDataListCount:" + succDataListCount);

        Map<String,Object> finalRes=new HashMap<>();
        //根据dataList的for循环来判断succCount
        if (failDataListCount == 0) {
            LOGGER.warn("dataList的人脸数据同步成功");
            finalRes.put(MemberConstants.RETURN_CODE,"0");
            finalRes.put(MemberConstants.RETURN_MSG,"单次请求api---dataList的人脸数据同步成功");
            finalRes.put(MemberConstants.CLIENTTRANSNO, sourceMap.get(MemberConstants.CLIENTTRANSNO));
        } else {
            LOGGER.warn("dataList的人脸数据同步有失败情况");
            finalRes.put(MemberConstants.RETURN_CODE,"1");
            finalRes.put(MemberConstants.RETURN_MSG,"单次请求api---dataList的人脸数据同步有失败情况");
            finalRes.put(MemberConstants.CLIENTTRANSNO, sourceMap.get(MemberConstants.CLIENTTRANSNO));
        }
        return finalRes;
//        faceRegisterService.registerService(parentMap);
    }

    @Override
    public Map<String, Object> foreachDeleteInfo(Map<String, Object> parentMap) {
//        return null;
//        faceDeleteService.deleteService(parentMap);
        return null;
    }
}

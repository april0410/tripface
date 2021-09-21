package com.icbc.bcss.trip.face.operationtools.serviceImpl.composite;

import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.enums.FailMessage;
import com.icbc.bcss.trip.face.operationtools.http.HttpBaseService;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemCloudwalkGroupInfoModel;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemCloudwalkGroupInfoBaseService;
import com.icbc.bcss.trip.face.operationtools.service.composite.BcssMemBuildGroupCompositeService;
import com.icbc.bcss.trip.face.operationtools.utils.MemberConstants;
import com.icbc.bcss.trip.face.operationtools.utils.PropertyPlaceholder;
import com.icbc.bcss.trip.face.operationtools.utils.TransNoUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("bcssMemBuildGroupCompositeService")
public class BcssMemBuildGroupCompositeServiceImpl implements BcssMemBuildGroupCompositeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BcssMemBuildGroupCompositeServiceImpl.class);
    @Autowired
    private PropertyPlaceholder environment;
    @Autowired
    private HttpBaseService httpBaseService;
    @Autowired
    private BcssMemCloudwalkGroupInfoBaseService bcssMemCloudwalkGroupInfoBaseService;

    /*
     * ①调用接口查询所有底库
     * ②匹配有此分库，跳过创建
     * ③没有匹配就创建接口
     * */
    @Override
    public Map<String, Object> execute(Map<String, Object> parentParam) {
        LOGGER.error("-------------query is start-------------------");

        Map<String, Object> result = new HashMap<>();
        if (parentParam == null || parentParam.isEmpty()) {
            LOGGER.error("参数集合为空，无法查创建特征分库");
            result.put(MemberConstants.RETURN_CODE, "1");
            result.put(MemberConstants.RETURN_MSG, "参数集合为空，无法查询、创建特征分库");
            return result;
        }

        String corpId = (String) parentParam.get(MemberConstants.KEY_CORP_ID);
        String corpName = (String) parentParam.get(MemberConstants.KEY_CORP_NAME);
        if (StringUtils.isBlank(corpId) || StringUtils.isBlank(corpName)) {
            LOGGER.error("参数集合中关键参数：企业编号和名称为空，请重新上送");
            result.put(MemberConstants.RETURN_CODE, "5");
            result.put(MemberConstants.RETURN_MSG, "参数集合中关键参数：企业编号和名称为空，请重新上送");
            return result;
        }

        //groupcode和groupName--
        //默认每一个企业只有一个分库，而且默认分库编号：corpId+序号1
        //如果需要接入多个企业、多个编号，则需要建立：新的数据表，数据库记录每一个企业对应所有分库编号
        String groupCode = corpId + MemberConstants.COMMON_SEPATATOR + MemberConstants.DEFAULT_GROUP_SEQNO;
        String groupName = corpName;

        String channelCode = environment.getProperty("bcss.default.cloudwalk.channelCode");
        String buildGroupUrl = environment.getProperty("bcss.default.face.create_ownGroup_URL");
        String queryGroupUrl = environment.getProperty("bcss.default.face.query_ownGroup_URL");
        if (channelCode == null || channelCode.isEmpty()) {
            LOGGER.error("云从渠道编号为空，无法查询、创建特征分库");
            result.put(MemberConstants.RETURN_CODE, "2");
            result.put(MemberConstants.RETURN_MSG, "云从渠道编号为空，无法查询、创建特征分库");
            return result;
        }
        if (buildGroupUrl == null || buildGroupUrl.isEmpty() || queryGroupUrl == null || queryGroupUrl.isEmpty()) {
            LOGGER.error("云从服务器查询、创建特征分库URL地址为空，无法查询、创建特征分库");
            result.put(MemberConstants.RETURN_CODE, "3");
            result.put(MemberConstants.RETURN_MSG, "云从服务器查询、创建特征分库URL地址为空，无法查询、创建特征分库");
            return result;
        }

        //构造返回结果
        Map<String, Object> request = new HashMap<>();
        request.put(MemberConstants.KEY_CLOUDWALK_GROUP_CODE, groupCode);
//        request.put(MemberConstants.KEY_CLOUDWALK_GROUP_NAME, groupName);
        request.put(MemberConstants.KEY_CLOUDWALK_CHANNEL_CODE, channelCode);

        //统一调用基础服务完成网络请求
//        Map httpresult=httpBaseService.execute(request,queryGroupUrl);
        String dealString = httpBaseService.executeAll(request, queryGroupUrl);
        //返回null，则请求http异常，则失败
        if (dealString == null || dealString.isEmpty()) {
            LOGGER.error("网络请求失败或者异常，请检查日志，具体封装后返回结果为：" + dealString);
            result.put(MemberConstants.RETURN_CODE, "4");
            result.put(MemberConstants.RETURN_MSG, "网络请求失败或者异常，请检查日志，具体封装后返回结果为：" + dealString);
            return result;
        }

        //获取云从结果
        Map<String, Object> bodyRes = JSONObject.parseObject(dealString, Map.class);
        if (MemberConstants.VALUE_FULL_SUCC.equals(
                bodyRes.get(MemberConstants.KEY_RESPONSE_CODE))) {
            //existStatus--人员是否存在  1:已存在、0:不存在
            Map groupData = (Map) bodyRes.get("data");

            //交易成功,调用函数
//            BcssMemCloudwalkGroupInfoModel groupInfoDto=new BcssMemCloudwalkGroupInfoModel();
            BcssMemCloudwalkGroupInfoModel newRecord = new BcssMemCloudwalkGroupInfoModel();
            newRecord.setCorpId(corpId);
            newRecord.setCorpName(corpName);
            newRecord.setGroupcode(groupCode);
//            newRecord.setCorpName(groupName);
            newRecord.setGroupname(groupName);
            newRecord.setFlownum((String) bodyRes.get(MemberConstants.KEY_RESPONSE_FLOWNUM));
            newRecord.setSeqno(MemberConstants.DEFAULT_GROUP_SEQNO);

            if (groupData != null && 1 == (int) groupData.get("existStatus")) {
                LOGGER.error("当前企业编号：" + corpId + "，对应库编号：" + groupCode + "，存在");
                //调用了函数--
                //查询本地是否建立数据，如果没有补建立
                Map outPutMap = dealQueryResult(bodyRes, newRecord);
                return outPutMap;
            } else {
                //调用建立库的接口，并删除本地数据重新建立记录
                return buildGroupInfo(newRecord, buildGroupUrl, channelCode);
            }

        } else {
            LOGGER.error("查询特征分库信息失败：" + dealString);
            result.put(MemberConstants.RETURN_CODE, bodyRes.get(MemberConstants.KEY_RESPONSE_CODE));
            result.put(MemberConstants.RETURN_MSG, bodyRes.get(MemberConstants.KEY_RESPONSE_MESSAGE));
            result.put(MemberConstants.CLIENTTRANSNO, TransNoUtils.getInstance().getTransNoWithRandom());
            result.put(MemberConstants.CLOUDWALK_CLIENTTRANSNO, bodyRes.get(MemberConstants.KEY_RESPONSE_FLOWNUM));
            return result;
        }
    }


    /*
     * 建立自定义库
     * */
    private HashMap<String, Object> buildGroupInfo(BcssMemCloudwalkGroupInfoModel newRecord, String buildGroupUrl, String channelCode) {
        HashMap<String, Object> result = new HashMap<>();
        //构造返回结果
        Map<String, Object> request = new HashMap<>();
        request.put(MemberConstants.KEY_CLOUDWALK_GROUP_CODE, newRecord.getGroupcode());
        request.put(MemberConstants.KEY_CLOUDWALK_GROUP_NAME, newRecord.getGroupname());
        request.put(MemberConstants.KEY_CLOUDWALK_CHANNEL_CODE, channelCode);

        //统一调用基础服务完成网络请求
        String dealString = httpBaseService.executeAll(request, buildGroupUrl);
        if (dealString == null || dealString.isEmpty() ) {
            LOGGER.error("建立特征分库--网络请求失败或者异常，请检查日志，具体封装后返回结果为：" + dealString);
            result.put(MemberConstants.RETURN_CODE, "4");
            result.put(MemberConstants.RETURN_MSG, "建立特征分库--网络请求失败或者异常，请检查日志，具体封装后返回结果为：" + dealString);
            return result;
        }

        Map<String, Object> bodyRes = JSONObject.parseObject(dealString, Map.class);
        if (MemberConstants.VALUE_FULL_SUCC.equals(
                bodyRes.get(MemberConstants.KEY_RESPONSE_CODE))) {
            LOGGER.warn("调用建立特征分库接口成功，返回成功结果:" + dealString);
            newRecord.setFlownum((String) bodyRes.get(MemberConstants.KEY_RESPONSE_FLOWNUM));
            newRecord.setSetdate(new Date());
            newRecord.setLstmdate(new Date());
            int res = bcssMemCloudwalkGroupInfoBaseService.replaceByGroupcode(newRecord);
            if (res >0 ) {
                LOGGER.warn("建立特征分库--插入数据成功");
                result.put(MemberConstants.RETURN_CODE, "0");
                result.put(MemberConstants.RETURN_MSG, "当前特征分库已经建立");
            } else {
                LOGGER.error("本地数据库插入分库信息失败");
                result.put(MemberConstants.RETURN_CODE, FailMessage.GROUP_INFO_ADD_FAILED_LOCALDATABASE.getCode());
                result.put(MemberConstants.RETURN_MSG, FailMessage.GROUP_INFO_ADD_FAILED_LOCALDATABASE.getDecs());
            }
        } else {
            LOGGER.error("调用建立特征分库接口失败，返回错误信息:" + dealString);
            result.put(MemberConstants.RETURN_CODE, "1");
            result.put(MemberConstants.RETURN_MSG, "调用建立特征分库接口失败，返回错误信息:" + bodyRes.get(MemberConstants.KEY_RESPONSE_MESSAGE));
            result.put(MemberConstants.CLIENTTRANSNO, TransNoUtils.getInstance().getTransNoWithRandom());
            result.put(MemberConstants.CLOUDWALK_CLIENTTRANSNO, bodyRes.get(MemberConstants.KEY_RESPONSE_FLOWNUM));
        }
        return result;
    }


    /*
     * 函数：云从数据库已经存在该分库，现在检测本地数据库
     * ③list可能为空，需要主要
     * */
    private Map<String, Object> dealQueryResult(Map<String, Object> bodyRes, BcssMemCloudwalkGroupInfoModel groupInfoModel) {
        //查询本地数据库
        BcssMemCloudwalkGroupInfoModel queryModel = bcssMemCloudwalkGroupInfoBaseService.selectByGroupCode(groupInfoModel.getGroupcode());
        Map<String,Object> result=new HashMap<>();
        if (queryModel == null) {
            LOGGER.info("当前groupCode已经在云从服务器注册,但是数据库不存在该分库信息记录，补登记数据");
            //再本地数据库插入数据
            groupInfoModel.setSetdate(new Date());
            groupInfoModel.setLstmdate(new Date());
            int res = bcssMemCloudwalkGroupInfoBaseService.insert(groupInfoModel);
            if (res == 1) {
                LOGGER.warn("插入数据成功");
                result.put(MemberConstants.RETURN_CODE, "0");
                result.put(MemberConstants.RETURN_MSG, "当前特征分库已经建立");
            } else {
                LOGGER.error("本地数据库插入分库信息失败");
                result.put(MemberConstants.RETURN_CODE, FailMessage.GROUP_INFO_ADD_FAILED_LOCALDATABASE.getCode());
                result.put(MemberConstants.RETURN_MSG, FailMessage.GROUP_INFO_ADD_FAILED_LOCALDATABASE.getDecs());
            }
        } else {
            LOGGER.warn("当前特征分库已经在云从服务器建立，本地数据库不更新数据");
            result.put(MemberConstants.RETURN_CODE, "0");
            result.put(MemberConstants.RETURN_MSG, "当前特征分库已经建立");
        }

        return result;
    //如果命中corpid，则返回成功
    //如果没有命中，继续查询，查询回来有命中，成功，如果还是没有命中则返回失败
}


    @Deprecated
    public Map<String, Object> executeBackup(Map<String, Object> parentParam) {
        LOGGER.error("-------------query is start-------------------");
        /*
         * ①调用接口查询所有底库
         * ②匹配有此分库，跳过创建
         * ③没有匹配就创建接口
         * */
        Map<String, Object> result = new HashMap<>();
        if (parentParam == null || parentParam.isEmpty()) {
            LOGGER.error("参数集合为空，无法查创建特征分库");
            result.put(MemberConstants.RETURN_CODE, "1");
            result.put(MemberConstants.RETURN_MSG, "参数集合为空，无法查询、创建特征分库");
            return result;
        }

        String corpId = (String) parentParam.get(MemberConstants.KEY_CORP_ID);
        String corpName = (String) parentParam.get(MemberConstants.KEY_CORP_NAME);
        String pageNum = (String) parentParam.get("pageNum");
        String pageSize = (String) parentParam.get("pageSize");
        //groupcode和groupName
        String groupCode = corpId + MemberConstants.COMMON_SEPATATOR + MemberConstants.DEFAULT_GROUP_SEQNO;
        String groupName = corpName;

        String channelCode = environment.getProperty("bcss.default.cloudwalk.channelCode");
        String buildGroupUrl = environment.getProperty("bcss.default.face.create_ownGroup_URL");
        String queryGroupUrl = environment.getProperty("bcss.default.face.query_ownGroup_URL");
        if (channelCode == null || channelCode.isEmpty()) {
            LOGGER.error("云从渠道编号为空，无法查询、创建特征分库");
            result.put(MemberConstants.RETURN_CODE, "2");
            result.put(MemberConstants.RETURN_MSG, "云从渠道编号为空，无法查询、创建特征分库");
            return result;
        }
        if (buildGroupUrl == null || buildGroupUrl.isEmpty() || queryGroupUrl == null || queryGroupUrl.isEmpty()) {
            LOGGER.error("云从服务器查询、创建特征分库URL地址为空，无法查询、创建特征分库");
            result.put(MemberConstants.RETURN_CODE, "3");
            result.put(MemberConstants.RETURN_MSG, "云从服务器查询、创建特征分库URL地址为空，无法查询、创建特征分库");
            return result;
        }

        //构造返回结果
        Map<String, Object> request = new HashMap<>();
        request.put(MemberConstants.KEY_CLOUDWALK_GROUP_CODE, groupCode);
        request.put(MemberConstants.KEY_CLOUDWALK_GROUP_NAME, groupName);
        request.put(MemberConstants.KEY_CLOUDWALK_CHANNEL_CODE, channelCode);
        request.put("pageSize", pageSize);
        request.put("pageNum", pageNum);

        //统一调用基础服务完成网络请求
//        Map httpresult=httpBaseService.execute(request,queryGroupUrl);
        String dealString = httpBaseService.executeAll(request, queryGroupUrl);
        if (dealString == null || dealString.isEmpty()) {
            LOGGER.error("网络请求失败或者异常，请检查日志，具体封装后返回结果为：" + dealString);
            result.put(MemberConstants.RETURN_CODE, "4");
            result.put(MemberConstants.RETURN_MSG, "网络请求失败或者异常，请检查日志，具体封装后返回结果为：" + dealString);
            return result;
        }

        //获取云从结果
        Map<String, Object> bodyRes = JSONObject.parseObject(dealString, Map.class);
        if (MemberConstants.VALUE_FULL_SUCC.equals(
                bodyRes.get(MemberConstants.KEY_RESPONSE_CODE))) {
            //交易成功,调用函数
//            BcssMemCloudwalkGroupInfoModel groupInfoDto=new BcssMemCloudwalkGroupInfoModel();
            BcssMemCloudwalkGroupInfoModel newRecord = new BcssMemCloudwalkGroupInfoModel();
            newRecord.setCorpId(corpId);
            newRecord.setCorpName(corpName);
            newRecord.setGroupcode(groupCode);
            newRecord.setCorpName(groupName);
            newRecord.setFlownum((String) bodyRes.get(MemberConstants.KEY_RESPONSE_FLOWNUM));
            newRecord.setSeqno(MemberConstants.DEFAULT_GROUP_SEQNO);
            //调用了函数
            Map outPutMap = dealQueryResult(bodyRes, newRecord);
            //判断是否有第二页
            boolean hasNextPage = (boolean) outPutMap.get("hasNextPage");
            boolean isLastPage = (boolean) outPutMap.get("isLastPage");
            if ("0".equals(outPutMap.get(MemberConstants.RETURN_CODE))) {
                //成功，服务器和本地都有
                result.put(MemberConstants.RETURN_CODE, "0");
                result.put(MemberConstants.RETURN_MSG, "特征库成功在云从服务器和本地数据库建立");
                result.put(MemberConstants.CLIENTTRANSNO, TransNoUtils.getInstance().getTransNoWithRandom());
                result.put(MemberConstants.CLOUDWALK_CLIENTTRANSNO, bodyRes.get(MemberConstants.KEY_RESPONSE_FLOWNUM));
                return result;
            } else {
                //失败，包含没有在本地库建立数据
                //如果有多企业情况，任一企业建库失败，则退出程序
                if (FailMessage.GROUP_INFO_ADD_FAILED_LOCALDATABASE.getCode().
                        equals(outPutMap.get(MemberConstants.RETURN_CODE))) {
                    //已经找到云从记录，但是本地建立库失败
                    return outPutMap;
                }

                //判断
                if (isLastPage == false) {
                    //还需要查询--需要循环
//                    return secondQuery(newRecord,request,"2");
                    result.put(MemberConstants.RETURN_CODE, FailMessage.GROUP_INFO_QUERY_ISLASTPAGE_FALSE.getCode());
                    result.put(MemberConstants.RETURN_MSG, FailMessage.GROUP_INFO_QUERY_ISLASTPAGE_FALSE.getDecs());
                    result.put("hasNextPage", hasNextPage);
                    result.put("isLastPage", isLastPage);
                    result.put("loopFlag", "1");//循环标识
                    return result;
                } else {
                    //最后一页，没有查询到，则建立库
                    return buildGroupInfo(newRecord, buildGroupUrl, channelCode);
                }
            }

        } else {
            LOGGER.error("查询特征分库信息失败：" + dealString);
            result.put(MemberConstants.RETURN_CODE, bodyRes.get(MemberConstants.KEY_RESPONSE_CODE));
            result.put(MemberConstants.RETURN_MSG, bodyRes.get(MemberConstants.KEY_RESPONSE_MESSAGE));
            result.put(MemberConstants.CLIENTTRANSNO, TransNoUtils.getInstance().getTransNoWithRandom());
            result.put(MemberConstants.CLOUDWALK_CLIENTTRANSNO, bodyRes.get(MemberConstants.KEY_RESPONSE_FLOWNUM));
            return result;
        }
//        return null;
    }
}

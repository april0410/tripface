package com.icbc.bcss.trip.face.operationtools.serviceImpl;


import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.enums.RecordInfoType;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemRecordInfoLogModel;
import com.icbc.bcss.trip.face.operationtools.service.BcssMemRecordInfoCompositeService;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemRecordInfoLogBaseService;
import com.icbc.bcss.trip.face.operationtools.utils.MemberConstants;
import com.icbc.bcss.trip.face.operationtools.utils.PropertyPlaceholder;
import com.icbc.bcss.trip.face.operationtools.utils.TransNoUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sun.security.util.ManifestEntryVerifier;

import javax.annotation.Resource;
import java.util.*;

@Service("bcssMemRecordInfoCompositeService")
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class BcssMemRecordInfoCompositeServiceImpl implements BcssMemRecordInfoCompositeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BcssMemRecordInfoCompositeServiceImpl.class);

    @Autowired
    private PropertyPlaceholder environment;

    @Autowired
    private BcssMemRecordInfoLogBaseService bcssMemRecordInfoLogBaseService;

    @Override
    public int insertModel(Map<String, Object> param,String infoType,String msg,String sort,String resultDesc) {
        try{
            BcssMemRecordInfoLogModel bcssMemRecordInfoLogModel=new BcssMemRecordInfoLogModel();
            String corpId=(String) param.get("corpId");
            String industry= (String) param.get("industry");
            String randomNum=environment.getProperty("bcss.random.num");
            int randomInt=Integer.parseInt(randomNum);
            String personId= (String) param.get("personId");
            String seqNo= (String) param.get("seqNo");
            String faceId= (String) param.get("personUniqueCode");
            //时间
            String recordDate=TransNoUtils.getInstance().getDate();
            String recordTime=TransNoUtils.getInstance().getTimeSuff();
            String infoContent= JSONObject.toJSONString(param);//具体内容
            String tempMsg= (String) param.get(MemberConstants.RETURN_MSG);
            if(msg!=null){
                tempMsg=msg;
            }
            String message=tempMsg;
            if(tempMsg.length()>1000){
                message=tempMsg.substring(0,1000);
            }

            String memCardNo= (String) param.get("memCardNo");
            String memName= (String) param.get("memName");
            String dealStatus= RecordInfoType.DEALSTATUS_DESC_WAITING.getCode();  //0-待处理，1-处理成功
            String dealMessage=RecordInfoType.DEALSTATUS_DESC_WAITING.getDecs();
//        if(infoType)

            //更新值
            bcssMemRecordInfoLogModel.setCorpId(corpId);
            bcssMemRecordInfoLogModel.setTrxseqno(TransNoUtils.getInstance().getTransNoWithCorpId(corpId,industry,randomInt));
            bcssMemRecordInfoLogModel.setInfoType(infoType);
            bcssMemRecordInfoLogModel.setPersonId(personId);
            bcssMemRecordInfoLogModel.setSeqno(seqNo);
            bcssMemRecordInfoLogModel.setFaceId(faceId);
            bcssMemRecordInfoLogModel.setRecordDate(recordDate);
            bcssMemRecordInfoLogModel.setRecordTime(recordTime);
            bcssMemRecordInfoLogModel.setInfoContent(infoContent);
            bcssMemRecordInfoLogModel.setMessage(message);
            bcssMemRecordInfoLogModel.setBusinessNo1(null);
            bcssMemRecordInfoLogModel.setBusinessNo2(null);
            bcssMemRecordInfoLogModel.setSort(sort);
            bcssMemRecordInfoLogModel.setResultDesc(resultDesc);
            bcssMemRecordInfoLogModel.setMemcardno(memCardNo);
            bcssMemRecordInfoLogModel.setMemname(memName);
            bcssMemRecordInfoLogModel.setDealStatus(dealStatus);
            bcssMemRecordInfoLogModel.setDealDesc(dealMessage);

            bcssMemRecordInfoLogModel.setSetdate(new Date());
            bcssMemRecordInfoLogModel.setLstmdate(new Date());

            int res=bcssMemRecordInfoLogBaseService.insert(bcssMemRecordInfoLogModel);
            if(res==1){
                LOGGER.info("insertModel--插入日志记录成功");
                return 0;
            }else{
                LOGGER.error("insertModel--插入日志记录失败");
                return 1;
            }
//            return 0;
        }catch (Exception e){
            LOGGER.error("insertModel方法--插入日志记录异常，原因："+e.getMessage());
            LOGGER.error(e.toString(),e);
            return -1;
        }

    }


        /*
        * 记录明细装用
        * */
    @Override
    public int insertModelByDetail(Map<String, Object> param,String infoType,String msg,String sort,String resultDesc,String backupInfo,Map<String,Object> dealResultMap) {
        try{
            BcssMemRecordInfoLogModel bcssMemRecordInfoLogModel=new BcssMemRecordInfoLogModel();
            String corpId=(String) param.get("corpId");
            String industry= (String) param.get("industry");
            String randomNum=environment.getProperty("bcss.random.num");
            int randomInt=Integer.parseInt(randomNum);
            String personId= (String) param.get("personId");
            String seqNo= (String) param.get("seqNo");
            String faceId= (String) param.get("personUniqueCode");
            //时间
            String recordDate=TransNoUtils.getInstance().getDate();
            String recordTime=TransNoUtils.getInstance().getTimeSuff();

            Map<String,Object> synchronizedMap= Collections.synchronizedMap(new HashMap<>());
//            synchronizedMap.putAll(param);
            synchronizedMap.put("paramData",param);
            synchronizedMap.put("personInfoDetail",backupInfo);
            synchronizedMap.put("resultData",dealResultMap);
            String infoContent= JSONObject.toJSONString(synchronizedMap);//具体内容


            String tempMsg= (String) param.get(MemberConstants.RETURN_MSG);
            if(msg!=null){
                tempMsg=msg;
            }
            String message=tempMsg;
            if(tempMsg.length()>1000){
                message=tempMsg.substring(0,1000);
            }

            String memCardNo= (String) param.get("memCardNo");
            String memName= (String) param.get("memName");
            String dealStatus= RecordInfoType.DEALSTATUS_DESC_WAITING.getCode();  //0-待处理，1-处理成功
            String dealMessage=RecordInfoType.DEALSTATUS_DESC_WAITING.getDecs();
//        if(infoType)

            //更新值
            bcssMemRecordInfoLogModel.setCorpId(corpId);
            bcssMemRecordInfoLogModel.setTrxseqno(TransNoUtils.getInstance().getTransNoWithCorpId(corpId,industry,randomInt));
            bcssMemRecordInfoLogModel.setInfoType(infoType);
            bcssMemRecordInfoLogModel.setPersonId(personId);
            bcssMemRecordInfoLogModel.setSeqno(seqNo);
            bcssMemRecordInfoLogModel.setFaceId(faceId);
            bcssMemRecordInfoLogModel.setRecordDate(recordDate);
            bcssMemRecordInfoLogModel.setRecordTime(recordTime);
            bcssMemRecordInfoLogModel.setInfoContent(infoContent);
            bcssMemRecordInfoLogModel.setMessage(message);
            bcssMemRecordInfoLogModel.setBusinessNo1(null);
            bcssMemRecordInfoLogModel.setBusinessNo2(null);
            bcssMemRecordInfoLogModel.setSort(sort);
            bcssMemRecordInfoLogModel.setResultDesc(resultDesc);
            bcssMemRecordInfoLogModel.setMemcardno(memCardNo);
            bcssMemRecordInfoLogModel.setMemname(memName);
            bcssMemRecordInfoLogModel.setDealStatus(dealStatus);
            bcssMemRecordInfoLogModel.setDealDesc(dealMessage);

            bcssMemRecordInfoLogModel.setSetdate(new Date());
            bcssMemRecordInfoLogModel.setLstmdate(new Date());

            int res=bcssMemRecordInfoLogBaseService.insert(bcssMemRecordInfoLogModel);
            if(res==1){
                LOGGER.info("insertModel--插入日志记录成功");
                return 0;
            }else{
                LOGGER.error("insertModel--插入日志记录失败");
                return 1;
            }
//            return 0;
        }catch (Exception e){
            LOGGER.error("insertModel方法--插入日志记录异常，原因："+e.getMessage());
            LOGGER.error(e.toString(),e);
            return -1;
        }

    }
}

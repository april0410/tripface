package com.icbc.bcss.trip.face.operationtools.serviceImpl.localFace;

import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.enums.RecordInfoType;
import com.icbc.bcss.trip.face.operationtools.exception.BusinessException;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemCustBaseInfoModel;
import com.icbc.bcss.trip.face.operationtools.service.BcssMemRecordInfoCompositeService;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemCustBaseInfoBaseService;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceDeleteService;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceRegisterService;
import com.icbc.bcss.trip.face.operationtools.service.localFace.FaceLocalAllService;
import com.icbc.bcss.trip.face.operationtools.service.localFace.FaceLocalParamCheckService;
import com.icbc.bcss.trip.face.operationtools.serviceImpl.FaceAllServiceImpl;
import com.icbc.bcss.trip.face.operationtools.utils.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//@Service("faceLocalAllService")
public class FaceLocalAllServiceImpl implements FaceLocalAllService {
    private static final String MODEL_NAME=FaceLocalAllServiceImpl.class.getName();

    private static final Logger LOGGER = LoggerFactory.getLogger(FaceAllServiceImpl.class);
    @Autowired
    private PropertyPlaceholder environment;

    @Autowired
    private FaceRegisterService faceRegisterService;

    @Autowired
    private FaceDeleteService faceDeleteService;

    @Autowired
    private FaceLocalParamCheckService faceLocalParamCheckService;

    @Autowired
    private BcssMemRecordInfoCompositeService bcssMemRecordInfoCompositeService;

    @Autowired
    private BcssMemCustBaseInfoBaseService bcssMemCustBaseInfoBaseService;

    /*
    * 校验参数
    * 请求云从接口
    * */
    @Override
    public Map<String, Object> execute(Map<String, Object> parentParams) {
        LOGGER.debug("上送参数为："+ JSONObject.toJSONString(parentParams));

        String clientTransNo= TransNoUtils.getInstance().getTransNoIn20();
        LOGGER.warn("------***"+"开始注册人脸(单人标准)流程,流水号为："+clientTransNo+"***------");
        //校验参数
        Map<String,Object> returnMap=new HashMap<>();
        try{
            faceLocalParamCheckService.checkRegisterCommonParam(parentParams);

            //业务参数字段的逻辑判断
            checkAllParams(parentParams);
        }catch (Exception e){
            LOGGER.error("注册人脸(单人标准)--校验参数过程中，抛出异常，原因："+e.getMessage());
            LOGGER.error(e.toString(),e);
            returnMap.put(MemberConstants.RETURN_CODE,"-1");
            returnMap.put(MemberConstants.RETURN_MSG,"注册人脸(单人标准)--校验参数过程中，抛出异常，原因："+e.getMessage());
            returnMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
            return returnMap;
        }
        LOGGER.debug("参数校验通过");


        //参数转换personUniqueFaceNo
        Map<String,Object> tempMap=new HashMap<>();
        try{
            tempMap=convertFieldName(parentParams);
        }catch (Exception e){
            LOGGER.error("注册人脸(单人标准)--业务参数转换过程中，抛出异常，原因："+e.getMessage());
            LOGGER.error(e.toString(),e);
            returnMap.put(MemberConstants.RETURN_CODE,"-2");
            returnMap.put(MemberConstants.RETURN_MSG,"注册人脸(单人标准)--业务参数转换过程中，抛出异常，原因："+e.getMessage());
            returnMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
            return returnMap;
        }


        //构建唯一编号
        String personId= (String) tempMap.get("personId");
        String seqNo= (String) tempMap.get("seqNo");
        String corpId= (String) tempMap.get("corpId");
        String personUniqueFaceNo=personId.concat(MemberConstants.COMMON_SEPATATOR).concat(seqNo).
                concat(MemberConstants.COMMON_SEPATATOR).concat(corpId);
        if(personUniqueFaceNo==null||personUniqueFaceNo.isEmpty()){
            LOGGER.error("构建的唯一编号为空，请检查日志，返回失败");
            returnMap.put(MemberConstants.RETURN_CODE,"-2");
            returnMap.put(MemberConstants.RETURN_MSG,"构建的唯一编号为空，请检查日志，返回失败");
            returnMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
            return returnMap;
        }
        tempMap.put("personUniqueFaceNo",personUniqueFaceNo);
        tempMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);

        //放入模块方便后续判断
        tempMap.put(MemberConstants.FACE_SINGLE_REGISTER_MODEL_NAME,MODEL_NAME);

        //调用注册接口--缓存区
        returnMap=dealRegiserSub(tempMap);

        LOGGER.warn("------***"+"结束注册人脸(单人标准)流程,流水号为："+clientTransNo+"***------");
        return returnMap;
    }

    //子服务
    private Map<String, Object> dealRegiserSub(Map<String, Object> parentParams) {
        Map<String,Object> returnMap=new HashMap<>();
        //注册人脸
        returnMap=faceRegisterService.registerService(parentParams);
        LOGGER.warn("调用faceRegisterService.registerService进行注册人脸(单人标准)后,结果为："+JSONObject.toJSONString(returnMap));
        if(! "0".equals(returnMap.get(MemberConstants.RETURN_CODE)) ){
            //失败就记录日志
            recordErrorReturnResult(parentParams,returnMap,"5");
            //记录文本
            String standardInfo = System.getProperty("user.dir") + File.separator +
                    environment.getProperty("face.saveFilePath.faceOper.standardInfo") + File.separator;
            String fileName=standardInfo+"standardRegisterFail.txt";
            //记录注册失败的memNo和memCardNO
            String context = parentParams.get("personUniqueFaceNo") + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + parentParams.get("memCardNo") + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME +
                    parentParams.get("memName")  + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + TransNoUtils.getInstance().getTimeStamp()+ System.lineSeparator();
            FileUtils.writeFile(fileName, context, "标准人脸注册", true);

        }else{
            //成功需要返回personUniqueCode
            String uniqueNo= (String) parentParams.get("personUniqueFaceNo");
//            String uniqueCode= (String) returnMap.get("personUniqueCode");//对外
            returnMap.put("personUniqueCode",uniqueNo);
            //非必输
            returnMap.put("memName",parentParams.get("memName"));
            returnMap.put("memCardNo",parentParams.get("memCardNo"));
            //覆盖返回信息
            returnMap.put(MemberConstants.RETURN_MSG,"注册成功");
        }

        //判断结果
        return returnMap;
    }



    /*
    * 删除人脸
    * */
    @Override
    public Map<String, Object> executeDelete(Map<String, Object> parentParams) {
        LOGGER.debug("上送参数为："+ JSONObject.toJSONString(parentParams));

        String clientTransNo= TransNoUtils.getInstance().getTransNoIn20();
        LOGGER.warn("------***"+"开始删除人脸(单人标准)流程,流水号为："+clientTransNo+"***------");
        //校验参数
        Map<String,Object> returnMap=new HashMap<>();
        String faceUniqueNo=null;
        Map<String,Object> tempMap=new HashMap<>();
        try{
            faceLocalParamCheckService.checkDeleteCommonParam(parentParams);

            //业务参数字段的逻辑判断
            faceUniqueNo=getUniqueNoInDelete(parentParams);

            //构建唯一编号
            parentParams.put("personUniqueFaceNo",faceUniqueNo);

            //参数转换personUniqueFaceNo
            tempMap=convertFieldNameInDelete(parentParams);
        }catch (Exception e){
            LOGGER.error("删除人脸(单人标准)--校验参数过程中，抛出异常，原因："+e.getMessage());
            LOGGER.error(e.toString(),e);
            returnMap.put(MemberConstants.RETURN_CODE,"-1");
            returnMap.put(MemberConstants.RETURN_MSG,"删除人脸(单人标准)--校验参数过程中，抛出异常，原因："+e.getMessage());
            returnMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
            return returnMap;
        }
        LOGGER.debug("参数校验通过");

        tempMap.put(MemberConstants.CLIENTTRANSNO,clientTransNo);
        //放入模块方便后续判断
        tempMap.put(MemberConstants.FACE_SINGLE_DELETE_MODEL_NAME,MODEL_NAME);

        //调用注册接口--缓存区
        returnMap=dealDeleteSub(tempMap);

        LOGGER.warn("------***"+"结束删除人脸(单人标准)流程,流水号为："+clientTransNo+"***------");
        return returnMap;
//        return null;
    }

    private Map<String, Object> dealDeleteSub(Map<String, Object> tempMap) {
        Map<String,Object> returnMap=new HashMap<>();
        //注册人脸
        returnMap=faceDeleteService.deleteService(tempMap);
        LOGGER.warn("调用faceDeleteService.deleteService进行删除人脸(单人标准)后,结果为："+JSONObject.toJSONString(returnMap));
        if(! "0".equals(returnMap.get(MemberConstants.RETURN_CODE)) ){
            //失败就记录日志
            recordErrorReturnResult(tempMap,returnMap,"6");
            //记录文本
            //记录文本
            String standardInfo = System.getProperty("user.dir") + File.separator +
                    environment.getProperty("face.saveFilePath.faceOper.standardInfo") + File.separator;
            String fileName=standardInfo+"standardDeleteFail.txt";
            //记录注册失败的memNo和memCardNO
            String context = tempMap.get("personUniqueFaceNo") + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + tempMap.get("memCardNo") + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME +
                    tempMap.get("memName")  + MemberConstants.DEFAULT_CONNECTION_SYMBOL_WITHTIME + TransNoUtils.getInstance().getTimeStamp()+ System.lineSeparator();
            FileUtils.writeFile(fileName, context, "标准人脸删除", true);

        }

        //判断结果
        return returnMap;
    }


    /////////////////////
    /*
    * 构建部分属性名
    * */
    private Map<String, Object> convertFieldName(Map<String, Object> parentParams) {
        HashMap<String,Object> res=new HashMap<>();
        res.putAll(parentParams);

//        String industry= (String) parentParams.get("industry");
//        if(industry==null||industry.isEmpty()){
//            LOGGER.error("通过了参数校验，但是industry字段为空，异常，默认赋予1--会员");
//            industry="1";
//            res.put("industry",industry);
//        }

        res.put("seqNo",parentParams.get("bcssSeqNo"));
        res.put("personId",parentParams.get("bcssPersonId"));
        //人脸信息
        res.put("memParam",parentParams.get("featureContent"));
        //字典转换
        String certtype= (String) parentParams.get("certType");
        if(certtype!=null&&!certtype.isEmpty()){
            String realCertType=environment.getProperty(MemberConstants.MEMBER_CLOUDWALK_CERT_TYPE_STR+certtype);
            if(realCertType==null||realCertType.isEmpty()){
                LOGGER.error("转换证件类型的字典值，更新参数集合异常");
                throw new BusinessException("转换证件类型的字典值，更新参数集合异常");
            }
            res.put("certType",realCertType);
        }

        return res;
    }

    /*
     * 构建部分属性名
     * */
    private Map<String, Object> convertFieldNameInDelete(Map<String, Object> parentParams) {
        HashMap<String,Object> res=new HashMap<>();
        res.putAll(parentParams);

        String personUniqueCode= (String) parentParams.get("personUniqueFaceNo");
        String[] personInfo=personUniqueCode.split(MemberConstants.DEFAULT_SEPARATOR_FROM_PERSONID_TO_SEQNO);
        String seqNo=null;
        String personId=null;
        for(int i=0;i<personInfo.length;i++){
            if(0==i){
                personId=personInfo[i];
            }else if(1==i){
                seqNo=personInfo[i];
            }
        }
        if (seqNo==null||personId==null){
            LOGGER.error("拆分personId和seqNo存在异常");
            throw new BusinessException("拆分personId和seqNo存在异常");
        }

        res.put("seqNo",seqNo);
        res.put("personId",personId);
        //人脸信息
        return res;
    }


    /*
     * 子函数-校验参数
     * */
    public Map<String, Object> checkAllParams(Map<String, Object> params) {
        /*
        * ①dataSource--此接口情况下，必须设定为1-本地调用，区别于0--member云端拉取
        * ②featureType--由于云从支持1：注册照，2：身份证，3：联网核查， 4 日立， 5 曙光， 6 圣点， 7滨昇；但是目前只需要人脸，所以业务上限制只用人脸
        * ③featureSeqNo--序号，人脸只支持1~3张，但是目前看需要只需要一张，所以要限制1
        * */
        String dataSource = (String) params.get("dataSource");
        if ( ! "1".equals(dataSource)) {
            String msg = "业务校验--上送的信息来源不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        String featureType= (String) params.get("featureType");
        if(  !("1".equals(featureType)) ){
            String msg = "业务参数校验--上送的特征类型不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        String featureSeqNo= (String) params.get("featureSeqNo");
        if(  !("1".equals(featureSeqNo)) ){
            String msg = "业务参数校验--上送的特征值顺序不是1，当前流程不支持大于1";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        HashMap<String,Object> resultMap=new HashMap<>();
        //校验成功
        resultMap.put(MemberConstants.RETURN_CODE,"0");
        resultMap.put(MemberConstants.RETURN_MSG, "基础参数校验成功");
        return resultMap;
//        return null;
    }


    /*
     * 子函数-校验删除人脸的独有逻辑
     * */
    public String  getUniqueNoInDelete(Map<String, Object> params) {
        //1-按personUniqueCode删除；2-按bcssPersonId和bcssSeqNo删除 ，3-按会员卡删除
        String corpId= (String) params.get("corpId");
        String operType= (String) params.get("operType");
        String uniqueNo=null;
        if("2".equals(operType)){
            LOGGER.error("业务参数校验---删除的操作类型为2-按bcssPersonId和bcssSeqNo删除，组装唯一编号");
            String personId= (String) params.get("bcssPersonId");
            String seqNo= (String) params.get("bcssSeqNo");
            uniqueNo=personId+MemberConstants.DEFAULT_SEPARATOR_FROM_PERSONID_TO_SEQNO+seqNo+
                    MemberConstants.DEFAULT_SEPARATOR_FROM_PERSONID_TO_SEQNO+corpId;
        }else if("3".equals(operType)){
            //按卡号删除有一个天生的缺憾
            //因为必须用卡号查询本地数据库，得到卡号对应的Personid,然后再去构建唯一编号，请求删除云从数据库；
            //假设本地数据不在编号A记录，但是云从存在A记录，那么使用卡号就一直删除不了云从记录
            LOGGER.error("业务参数校验---删除的操作类型为3-按memCardNo删除，即需要找到唯一对应的编号");
            String memCardNo= (String) params.get("memCardNo");
            List<BcssMemCustBaseInfoModel> bcssMemCustBaseInfoModelList=bcssMemCustBaseInfoBaseService.selectByCorpId2MemCardNo(corpId,memCardNo);
            if(bcssMemCustBaseInfoModelList==null||bcssMemCustBaseInfoModelList.isEmpty()){
                LOGGER.error("业务参数校验---根据corpId和memCardNo查询对应的唯一编号，查询结果为空，则默认数据被清除，删除失败");
                throw new BusinessException("业务参数校验---根据corpId和memCardNo查询对应的唯一编号，查询结果为空，则默认数据被清除，删除失败");
            }else if(bcssMemCustBaseInfoModelList.size()>1){
                String seqNoTemp= (String) params.get("bcssSeqNo");
                boolean seqFlag=false;
                BcssMemCustBaseInfoModel rightModel=null;
                for(BcssMemCustBaseInfoModel model:bcssMemCustBaseInfoModelList){
                    if(model.getSeqno().equals(seqNoTemp)){
                        rightModel=model;
                        seqFlag=true;
                        break;
                    }
                }
                //判断循环结果
                if(seqFlag==false){
                    LOGGER.error("业务参数校验---根据corpId和memCardNo查询对应的唯一编号，查询结果数量大于1，则无法找到对应唯一编号，删除失败");
                    throw new BusinessException("业务参数校验---根据corpId和memCardNo查询对应的唯一编号，查询结果数量大于1，则无法找到对应唯一编号，删除失败");
                }
               LOGGER.info("根据corpId和memCardNo查询对应的唯一编号，查询结果数量大于1;但是通过bcssSeqNo确认了唯一记录，校验通过");
                uniqueNo=rightModel.getFaceId();

            }else{
                uniqueNo=bcssMemCustBaseInfoModelList.get(0).getFaceId();
            }

        }else {
            uniqueNo= (String) params.get("personUniqueCode");
        }

        //二次校验
        if(uniqueNo==null||uniqueNo.isEmpty()){
            LOGGER.error("业务参数校验---操作类型："+operType+"--从参数集合获取唯一编号异常，请检查参数校验模块");
            throw new BusinessException("业务参数校验---操作类型："+operType+"从参数集合获取唯一编号异常，请检查参数校验模块");
        }
        LOGGER.debug("业务参数校验---标准删除人脸--唯一编号为："+uniqueNo);

        return uniqueNo;

//        HashMap<String,Object> resultMap=new HashMap<>();
//        //校验成功
//        resultMap.put(MemberConstants.RETURN_CODE,"0");
//        resultMap.put(MemberConstants.RETURN_MSG, "基础参数校验成功");
//        return resultMap;
    }

    /*
     * 子函数-记录错注册失败的日志
     * */
    public Map<String, Object> recordErrorReturnResult(Map<String, Object> param, Map<String,Object> result,String infoType) {
        Map<String,Object> res=new HashMap<>();
        String msg=null;
        if("5".equals(infoType)){
            msg="单个标准人脸注册失败，唯一编号为："+param.get("personUniqueFaceNo");
        }else {
            msg="单个标准人脸删除失败，唯一编号为："+param.get("personUniqueFaceNo");
        }
        LOGGER.error(msg);

        String uniqueNo= (String) param.get("personUniqueFaceNo");
        int tempRes=bcssMemRecordInfoCompositeService.insertModelByDetail(param, RecordInfoType.INFO_TYPE_REGISTER_FACE_DEAL_DETAIL_FAIL.getCode(),"当前人脸数据为空，进行下一个注册,编号为" + uniqueNo,
                RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getCode(),RecordInfoType.SORT_RESULTDESC_CLOUD_TO_LOCAL_FAIL.getDecs(),uniqueNo,result);
        if(tempRes==0){
            LOGGER.debug("registerAndDeleteDealDetailService--插入日志记录成功");
        }else{
            LOGGER.debug("registerAndDeleteDealDetailService--插入日志记录失败");
        }
        return null;
    }
}

package com.icbc.bcss.trip.face.operationtools.serviceImpl.cloudwalk;

import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.exception.BusinessException;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemCustBaseInfoModel;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemCustBaseInfoBaseService;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceRegisterDesktopService;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceRegisterServerService;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceRegisterService;
import com.icbc.bcss.trip.face.operationtools.utils.MemberConstants;
import com.icbc.bcss.trip.face.operationtools.utils.PropertyPlaceholder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@Service("faceRegisterService")
public class FaceRegisterServiceImpl implements FaceRegisterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FaceRegisterServiceImpl.class);

    @Autowired
    private PropertyPlaceholder environment;

    @Autowired
    private FaceRegisterServerService faceRegisterServerService;

    @Autowired
    private FaceRegisterDesktopService faceRegisterDesktopService;

    @Autowired
    private BcssMemCustBaseInfoBaseService bcssMemCustBaseInfoBaseService;

    @Autowired

    @Override
    public Map<String, Object> registerService(Map<String, Object> param) {
        //判断一下必要参数
        //应该判断一下关键信息 Personid、seqno和行业信息
        String bcssPersonId= (String) param.get("personId");
        String bcssSeqNo= (String) param.get("seqNo");
        String industry= (String) param.get("industry");
        if(bcssPersonId==null||bcssPersonId.isEmpty()||bcssSeqNo==null||bcssSeqNo.isEmpty()||
            industry==null||industry.isEmpty()){
            LOGGER.error("关键参数PersonId、seqNo、industry为空，注册默认失败");
            Map error=new HashMap<String,Object>();
            error.put(MemberConstants.RETURN_CODE,"1");
            error.put(MemberConstants.RETURN_MSG,"关键参数PersonId、seqNo、industry为空，注册默认失败");
            error.put(MemberConstants.CLIENTTRANSNO,param.get(MemberConstants.CLIENTTRANSNO));
            return error;
        }

        LOGGER.warn("------***"+"开始人脸注册流程"+"***------");
        String versionnew=environment.getProperty("bcss.default.face.hardwareVersion");
        Map<String,Object> result=new HashMap<>();
        if(versionnew.equals("2")){
            LOGGER.error("当前本地人脸服务平台的版本设置为Server Level");
            try{
                //更新
                result=faceRegisterServerService.registerInServer(param);
//                return faceRegisterServerService.registerInServer(param);
                //判断结果是否成功，如果成功才更新本地数据库
                Map baseRes=dealRegisterByServer(param,result);

                /*记录日志*/

                return baseRes;
            }catch (Exception e){
                LOGGER.error("服务器--人脸服务平台注册发生异常："+e.getMessage());
                LOGGER.error(e.toString(),e);
                //不插入客户基础信息表
                result.put(MemberConstants.RETURN_CODE,"2");
                result.put(MemberConstants.RETURN_MSG,"服务器--人脸服务平台注册发生异常："+e.getMessage());
                result.put(MemberConstants.CLIENTTRANSNO,param.get(MemberConstants.CLIENTTRANSNO));

                //记录日志--
            }

        }else if(versionnew.equals("1")){
            LOGGER.error("当前本地人脸服务平台的版本设置为PC Level，但功能尚在规划，请确认！！！");
//            faceRegisterDesktopService.registerInDesktop(param);
            result.put(MemberConstants.RETURN_CODE,MemberConstants.COMMON_FAIL);
            result.put(MemberConstants.RETURN_MSG,"当前本地人脸服务平台的版本设置为PC Level，但功能尚在规划，请确认");
//            return result;
        }
        else{
            LOGGER.error("当前获取本地人脸服务平台的版本不存在功能接口，请关闭程序检测！！！");
//            Map<String,Object> result=new HashMap<>();
            result.put(MemberConstants.RETURN_CODE,MemberConstants.COMMON_FAIL);
            result.put(MemberConstants.RETURN_MSG,"当前获取本地人脸服务平台的版本不存在功能接口，请关闭程序检测！！！");
//            return result;
        }
        LOGGER.warn("------***"+"结束人脸注册流程"+"***------");

        return result;
    }

    /*
    * 处理服务器注册后的结果
    * */
    private Map<String, Object> dealRegisterByServer(Map<String, Object> param,Map<String, Object> result) {
        String returnCode= (String) result.get(MemberConstants.RETURN_CODE);
        String returnMsg= (String) result.get(MemberConstants.RETURN_MSG);
        if(MemberConstants.COMMON_SUCCESS.equals(returnCode)){
            //获取member云端所能知道的信息
            String faceNo= (String) param.get("personUniqueFaceNo");//唯一编号
            String corpId= (String) param.get("corpId");
            String bcssPersonId= (String) param.get("personId");
            String bcssSeqNo= (String) param.get("seqNo");
            String industry= (String) param.get("industry");
            String memName= (String) param.get("memName");
            String memNo= (String) param.get("memNo");
            String memDetail1Info= (String) param.get("memDetail1");
            Map<String,Object> memDetail1InfoMap=null;
            if(memDetail1Info==null||memDetail1Info.isEmpty()){
                memDetail1InfoMap=new HashMap<String,Object>();
            }else {
                memDetail1InfoMap=JSONObject.parseObject(memDetail1Info,Map.class);
            }
            String memCardNo= (String) memDetail1InfoMap.get("memCardNo");
            Integer memCardStatus= (Integer) memDetail1InfoMap.get("cardStatus");
            String expDate= (String) memDetail1InfoMap.get("expDate");
            String memIdentity= (String) memDetail1InfoMap.get("memIdentity");
            String proNo= (String) memDetail1InfoMap.get("prodNo");

            //其他参数
            String memDetail2Info= (String) param.get("memDetail2");
            String memDetail3Info= (String) param.get("memDetail3");
            //Byte
            Byte cardStatusBy=null;
            if(memCardStatus!=null){
                cardStatusBy=new Byte(memCardStatus.byteValue());
            }
            //服务器编号
            String serverRegisterInfoCode= (String) result.get("server_register_info_code");

            //信息来源
            String dataSource= (String) param.get("dataSource");
            if(dataSource==null||dataSource.isEmpty()){
                dataSource="0"; //0-云端
            }
            //判断
            if("1".equals(dataSource)){
                memCardNo= (String) param.get("memCardNo");
            }

            //组装实体
            BcssMemCustBaseInfoModel bcssMemCustBaseInfoModelByNew=new BcssMemCustBaseInfoModel();
            bcssMemCustBaseInfoModelByNew.setFaceId(faceNo);
            bcssMemCustBaseInfoModelByNew.setCorpId(corpId);
            bcssMemCustBaseInfoModelByNew.setPersonId(bcssPersonId);
            bcssMemCustBaseInfoModelByNew.setSeqno(bcssSeqNo);
            bcssMemCustBaseInfoModelByNew.setFaceIndustry(industry);
            bcssMemCustBaseInfoModelByNew.setMemcardno(memCardNo);
            bcssMemCustBaseInfoModelByNew.setMemname(memName);
            bcssMemCustBaseInfoModelByNew.setMemno(memNo);
            bcssMemCustBaseInfoModelByNew.setMemprodno(proNo);
            bcssMemCustBaseInfoModelByNew.setMemproddesc(memIdentity);
            //            判断是否为空
            bcssMemCustBaseInfoModelByNew.setMemCardstatus(cardStatusBy);
            bcssMemCustBaseInfoModelByNew.setMemdetail1(memDetail1Info);
            bcssMemCustBaseInfoModelByNew.setMemdetail2(memDetail2Info);
            bcssMemCustBaseInfoModelByNew.setMemdetail3(memDetail3Info);
            bcssMemCustBaseInfoModelByNew.setCloudFaceCreatdate(null);
            bcssMemCustBaseInfoModelByNew.setCloudFaceModifieddate(null);
//            bcssMemCustBaseInfoModelByNew.setServerRegisterInfoCode(null);
            if(serverRegisterInfoCode!=null){
                bcssMemCustBaseInfoModelByNew.setServerRegisterInfoCode(serverRegisterInfoCode);
            }

            //增加信息来源
            bcssMemCustBaseInfoModelByNew.setDataSource(dataSource);

            //数据库操作--更新
            BcssMemCustBaseInfoModel baseInfoModel=bcssMemCustBaseInfoBaseService.selectByFaceId(faceNo);
            if (baseInfoModel == null) {
                LOGGER.error("本地数据库操作- baseInfoModel-查询结果为空，插入数据");
                //如果人员查询结果为全0，则是服务器查询成功，本地不存在数据
                bcssMemCustBaseInfoModelByNew.setSetdate(new Date());
                bcssMemCustBaseInfoModelByNew.setLstmdate(new Date());
                int res = bcssMemCustBaseInfoBaseService.insert(bcssMemCustBaseInfoModelByNew);
                if (res == 1) {
                    LOGGER.info(" baseInfoModel插入数据库正常");
                } else {
                    LOGGER.error(" baseInfoModel插入数据库异常");
                    throw new BusinessException(" baseInfoModel插入数据库异常");
                }
                //否则，则是服务器查询失败，本地不存在数据
            } else {
                LOGGER.error("本地数据库操作- baseInfoModel-查询结果存在，更新数据");//不管服务器是更新还是注册
                bcssMemCustBaseInfoModelByNew.setLstmdate(new Date());
                bcssMemCustBaseInfoModelByNew.setSeqId(baseInfoModel.getSeqId());
                int res = bcssMemCustBaseInfoBaseService.updateByPrimaryKeySelective(bcssMemCustBaseInfoModelByNew);
                if (res >= 0) {
                    LOGGER.info(" baseInfoModel更新数据库正常");
                } else {
                    LOGGER.error(" baseInfoModel更新数据库异常");
                    throw new BusinessException(" baseInfoModel更新数据库异常");
                }
            }
            //成功
            result.put(MemberConstants.RETURN_CODE,"0");
            result.put(MemberConstants.RETURN_MSG,"成功操作数据库");
            return  result;
        }else{
            //失败
            LOGGER.error("调用服务器版本的人脸注册平台失败，具体为："+ JSONObject.toJSONString(result));
            return result;
        }
    }

}

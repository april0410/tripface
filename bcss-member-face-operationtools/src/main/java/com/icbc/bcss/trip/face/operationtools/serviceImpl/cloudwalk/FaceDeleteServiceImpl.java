package com.icbc.bcss.trip.face.operationtools.serviceImpl.cloudwalk;

import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.exception.BusinessException;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemCustBaseInfoBaseService;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceDeleteDesktopService;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceDeleteServerService;
import com.icbc.bcss.trip.face.operationtools.service.cloudwalk.FaceDeleteService;
import com.icbc.bcss.trip.face.operationtools.utils.MemberConstants;
import com.icbc.bcss.trip.face.operationtools.utils.PropertyPlaceholder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

//@Service("faceDeleteService")
public class FaceDeleteServiceImpl implements FaceDeleteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FaceDeleteServiceImpl.class);

    @Autowired
    private PropertyPlaceholder environment;

    @Autowired
    private FaceDeleteServerService faceDeleteServerService;

    @Autowired
    private FaceDeleteDesktopService faceDeleteDesktopService;

    @Autowired
    private BcssMemCustBaseInfoBaseService bcssMemCustBaseInfoBaseService;


    @Override
    public Map<String, Object> deleteService(Map<String, Object> param) {
        String bcssPersonId= (String) param.get("personId");
        String bcssSeqNo= (String) param.get("seqNo");
        if(bcssPersonId==null||bcssPersonId.isEmpty()||bcssSeqNo==null||bcssSeqNo.isEmpty()){
            LOGGER.error("关键参数PersonId、seqNo为空，注册默认失败");
            Map error=new HashMap<String,Object>();
            error.put(MemberConstants.RETURN_CODE,"1");
            error.put(MemberConstants.RETURN_MSG,"关键参数PersonId、seqNo为空，注册默认失败");
            error.put(MemberConstants.CLIENTTRANSNO,param.get(MemberConstants.CLIENTTRANSNO));
            return error;
        }

        LOGGER.warn("------***"+"开始人脸删除流程"+"***------");
        String versionnew=environment.getProperty("bcss.default.face.hardwareVersion");
        Map<String,Object> result=new HashMap<>();
        if(versionnew.equals("2")){
            LOGGER.error("当前本地人脸服务平台的版本设置为Server Level");
//            return faceDeleteServerService.deleteInServer(param);
            try{
                //更新
                result=faceDeleteServerService.deleteInServer(param);
//                return faceRegisterServerService.registerInServer(param);
                //判断结果是否成功，如果成功才更新本地数据库
                Map baseRes=dealDeleteByServer(param,result);
                return baseRes;
            }catch (Exception e){
                LOGGER.error("服务器--人脸服务平台删除发生异常："+e.getMessage());
                LOGGER.error(e.toString(),e);
                //不删除客户基础信息表
                result.put(MemberConstants.RETURN_CODE,"-4");
                result.put(MemberConstants.RETURN_MSG,"服务器--人脸服务平台删除发生异常："+e.getMessage());
                result.put(MemberConstants.CLIENTTRANSNO,param.get(MemberConstants.CLIENTTRANSNO));
            }
        }else if(versionnew.equals("1")){
            LOGGER.error("当前本地人脸服务平台的版本设置为PC Level，但功能尚在规划，请确认！！！");
//            faceDeleteDesktopService.deleteInDesktop(param);
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
        LOGGER.warn("------***"+"结束人脸删除流程"+"***------");
        return result;
    }


    /*
     * 处理服务器删除后，本地数据库：客户基础信息表的删除
     * */
    private Map<String, Object> dealDeleteByServer(Map<String, Object> param,Map<String, Object> result) {
        String returnCode= (String) result.get(MemberConstants.RETURN_CODE);
        String returnMsg= (String) result.get(MemberConstants.RETURN_MSG);
        if("0".equals(returnCode)){
            //获取member云端所能知道的信息
            String faceNo= (String) param.get("personUniqueFaceNo");//唯一编号
            String corpId= (String) param.get("corpId");
            String bcssPersonId= (String) param.get("personId");
            String bcssSeqNo= (String) param.get("seqNo");
            String memName= (String) param.get("memName");
            String memNo= (String) param.get("memNo");

            //数据库操作--更新
            int baseInfoCount=bcssMemCustBaseInfoBaseService.deleteByFaceId(faceNo);
            if (baseInfoCount >= 0) {
                LOGGER.info(" baseInfoModel删除数据成功");
            } else {
                LOGGER.error("baseInfoModel删除数据异常");
                throw new BusinessException("baseInfoModel删除数据异常");
            }
            //成功
            result.put(MemberConstants.RETURN_CODE,"0");
            result.put(MemberConstants.RETURN_MSG,"成功操作数据库");
            return  result;
        }else{
            //失败
            LOGGER.error("调用本地删除人员相关表的数据失败，具体为："+ JSONObject.toJSONString(result));
            return result;
        }
    }
}

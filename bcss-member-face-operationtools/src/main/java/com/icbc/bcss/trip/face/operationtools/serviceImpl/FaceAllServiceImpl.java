package com.icbc.bcss.trip.face.operationtools.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.api.BcssMemberFeatureService;
import com.icbc.bcss.trip.face.operationtools.enums.FaceOperType;
import com.icbc.bcss.trip.face.operationtools.enums.QuerymemberinfoType;
import com.icbc.bcss.trip.face.operationtools.service.*;
import com.icbc.bcss.trip.face.operationtools.service.composite.BcssMemForeachFaceInfoService;
import com.icbc.bcss.trip.face.operationtools.utils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("faceAllService")
public class FaceAllServiceImpl implements FaceAllService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FaceAllServiceImpl.class);
    @Autowired
    private BcssMemberFeatureService bcssMemberFeatureService;

//    @Autowired
//    private FaceRegisterService faceRegisterService;

//    @Autowired
//    private FaceDeleteService faceDeleteService;

    @Autowired
    private FaceAsynchronousControl faceAsynchronousControl;

    @Autowired
    private BcssMemForeachFaceInfoService bcssMemForeachFaceInfoService;

    //先从云端获取人脸
    //然后处理云端结果，即注册 //判断版本
    //最后返回注册结果
    /*
     * 注册人脸和删除人脸--动态维护服务
     * */
    @Override
    public Map<String, Object> regANDdelFaceAllService(Map<String, Object> paramParent) {
        Map<String,Object> param= Collections.synchronizedMap(new HashMap<>(30));
        param.putAll(paramParent);

        String clientTransNo = (String) param.get(MemberConstants.CLIENTTRANSNO);
        if (clientTransNo == null || clientTransNo.equals("")) {
            clientTransNo = TransNoUtils.getInstance().getTransNoWithRandom();
            LOGGER.warn("调用方没有上送交易流水号，因此此次请求系统自行生成流水号：" + clientTransNo);
            param.put("clientTransNo", clientTransNo);
        } else {
            LOGGER.info("默认采用调用方上送的流水号，即为：" + clientTransNo);
        }
        LOGGER.error("初始化参数为：" + JSONObject.toJSONString(param));

        /*判断是否根据查询数量全部拉取记录*/
        String authFullDownLoad = (String) param.get("autoFullDown");
//        if (authFullDownLoad == null || authFullDownLoad.isEmpty()) {
//            LOGGER.error("autoFullDown（自动下载查询数量对应的明细）的值为空，默认不需要"); //0-需要，1-不需要
//            param.put("autoFullDown", "1");
//            authFullDownLoad="1";
//        }


        if ("0".equals(authFullDownLoad)) {//需要==全自动拉取人脸
            Map<String, Object> outResult = new HashMap<>();
            try {
                outResult = registerFaceAllSubService(param);
            } catch (Exception e) {
                LOGGER.error("入口方法异常：" + e.toString());
//                LOGGER.error(e.getLocalizedMessage());
                LOGGER.error(e.getMessage(), e);
                outResult.put(MemberConstants.RETURN_MSG, "System Exception");
                outResult.put(MemberConstants.RETURN_CODE, MemberConstants.COMMON_EXCEPTION);
            }
            return outResult;
        } else {//仅调用api一次，并原样返回结果，不做过多处理
            LOGGER.info("autoFullDown的值为:" + authFullDownLoad + ",默认走原流程"); //0-需要，1-不需要
            Map<String, Object> downloadResult = bcssMemberFeatureService.execute(param);
            if (downloadResult == null || downloadResult.isEmpty()) {
                LOGGER.error("请求返回的人脸数据为空，请重试");
                return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "请求返回的人脸数据为空，请重试", clientTransNo);
            }
            //判断分支
            String operType = (String) param.get("operType");
            if (operType.equals(FaceOperType.OPERTYPE_TWO_FACE_COUNT.getCode()) || operType.equals(FaceOperType.OPERTYPE_EIGHT_FACE_DELETE_COUNT.getCode())) {
                return downloadResult;
            } else if (operType.equals(FaceOperType.OPERTYPE_ONE_FACE_DETAIL.getCode())) {
                //调用人脸注册接口
//                return faceRegisterService.registerService(downloadResult);
                //每一个人脸需要处理
                return bcssMemForeachFaceInfoService.foreachRegisterInfo(param,downloadResult);
            } else if (operType.equals(FaceOperType.OPERTYPE_SEVEN_FACE_DELETE_DETAIL.getCode())) {
//                return faceDeleteService.deleteService(downloadResult);
                //每一个人脸需要处理
                return bcssMemForeachFaceInfoService.foreachRegisterInfo(param,downloadResult);
            } else {
                LOGGER.error("operType为非合法指定值，该值为：" + operType + ",返回错误信息");
                return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION, "系统异常，请重试", clientTransNo);
            }
        }
    }

    ////////////////////////////子处理
    //注册和删除的子处理
    public Map<String, Object> registerFaceAllSubService(Map<String, Object> param) {
        long statTimeMill=System.currentTimeMillis();
        String operType = (String) param.get("operType");
        String clientTransNo = (String) param.get("clientTransNo");
        //如果是根据查询数量把所有人脸拉去下来，必须要operType=2或者8，所以需要判断operType
        if (operType == null || operType.isEmpty()) {
            LOGGER.error("operType的参数值为空");
            return CommonMapReturn.constructMapWithClientTransNo(QuerymemberinfoType.PARAM_CHECK_OPERTYPE_EMPTY.getCode(), QuerymemberinfoType.PARAM_CHECK_OPERTYPE_EMPTY.getDecs(), clientTransNo);
        }
        if (!(FaceOperType.OPERTYPE_TWO_FACE_COUNT.getCode().equals(operType) || FaceOperType.OPERTYPE_EIGHT_FACE_DELETE_COUNT.getCode().equals(operType))) {
            LOGGER.error("operType查询方式不是用于数量查询");
            return CommonMapReturn.constructMapWithClientTransNo(QuerymemberinfoType.PARAM_CHECK_OPERTYPE_EMPTY.getCode(), QuerymemberinfoType.PARAM_CHECK_OPERTYPE_EMPTY.getDecs(), clientTransNo);
        }


        //①请求数量
        Map<String, Object> downloadResult = bcssMemberFeatureService.execute(param);
        if (downloadResult == null || downloadResult.isEmpty()) {
            LOGGER.error("请求返回的人脸数据数量为空，请稍后重试!");
            return CommonMapReturn.constructMapWithClientTransNo( MemberConstants.COMMON_FAIL, "请求返回的人脸数据为空，请稍后重试!",clientTransNo);
        }
        //判断返回结果
        String checkCountCode = (String) downloadResult.get(MemberConstants.RETURN_CODE);
        if (!checkCountCode.equals("0")) {
            LOGGER.error("请求返回的人脸数据数量交易失败，请稍后重试!");
            return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL,"请求返回的人脸数据数量交易失败，请稍后重试!",clientTransNo);
        }


        //转换数字
        int count = 0;
        try {
            count = Integer.parseInt((String) downloadResult.get("memCount"));
        } catch (Exception e) {
//            LOGGER.error(e.toString());
            LOGGER.error("人脸数量数字转换异常!");
            LOGGER.error(e.getMessage(),e);
            return CommonMapReturn.constructMapWithClientTransNo( MemberConstants.COMMON_EXCEPTION, "人脸数量数字转换异常!",clientTransNo);
        }

        //②正常得到数量后，开始锁线程；
        //③重置数量
        //需要根据operType进入对应分支，否则注册和删除都被同一个线程控制
        //设置注册锁或者删除锁的数量
        if(FaceOperType.OPERTYPE_TWO_FACE_COUNT.getCode().equals(operType)){
            //注册场景
//            faceRegLock("0",String.valueOf(count),"0");
            synchronized (this){
                //判断走哪一个分支
                LOGGER.error("---======="+FaceDataRegAndDelSingleton.getSingleton().getRegisterFlag()+"|"+FaceDataRegAndDelSingleton.getSingleton().getDeleteFlage()+"|"+
                        FaceDataRegAndDelSingleton.getSingleton().getRegisterDealCount()+"|"+FaceDataRegAndDelSingleton.getSingleton().getRegisterMemCount()+"|"+
                        FaceDataRegAndDelSingleton.getSingleton().getDeleteDealCount()+"|"+ FaceDataRegAndDelSingleton.getSingleton().getDeleteMemCount());

                if(FaceDataRegAndDelSingleton.getSingleton().getRegisterFlag().equals("0")){
                    LOGGER.error("当前有线程正在获取参数，跳过此次请求");
                    return  CommonMapReturn.constructMapWithClientTransNo("-2","当前有线程正在获取参数，跳过此次请求",clientTransNo);
                }else if(FaceDataRegAndDelSingleton.getSingleton().getRegisterFlag().equals("1")){
                    FaceDataRegAndDelSingleton.getSingleton().setRegisterFlag("0");
                    FaceDataRegAndDelSingleton.getSingleton().setRegisterMemCount(String.valueOf(count));
                    FaceDataRegAndDelSingleton.getSingleton().setCorpIdForRegister((String) param.get("corpId"));
                    if(FaceDataRegAndDelSingleton.getSingleton().getDealRegCountList()==null){
                        FaceDataRegAndDelSingleton.getSingleton().setDealRegCountList(new ArrayList<>());
                    }else {
                        FaceDataRegAndDelSingleton.getSingleton().getDealRegCountList().clear();
                    }
                }
            }
        }else{
            synchronized (this){
                //判断走哪一个分支
                LOGGER.error("---======="+FaceDataRegAndDelSingleton.getSingleton().getRegisterFlag()+"|"+FaceDataRegAndDelSingleton.getSingleton().getDeleteFlage()+"|"+
                        FaceDataRegAndDelSingleton.getSingleton().getRegisterDealCount()+"|"+FaceDataRegAndDelSingleton.getSingleton().getRegisterMemCount()+"|"+
                        FaceDataRegAndDelSingleton.getSingleton().getDeleteDealCount()+"|"+ FaceDataRegAndDelSingleton.getSingleton().getDeleteMemCount());

                if(FaceDataRegAndDelSingleton.getSingleton().getDeleteFlage().equals("0")){
                    LOGGER.error("当前有线程正在获取参数，跳过此次请求--删除");
                    return  CommonMapReturn.constructMapWithClientTransNo("-2","当前有线程正在获取参数，跳过此次请求--删除",clientTransNo);
                }else if(FaceDataRegAndDelSingleton.getSingleton().getDeleteFlage().equals("1")){
                    FaceDataRegAndDelSingleton.getSingleton().setDeleteFlage("0");
                    FaceDataRegAndDelSingleton.getSingleton().setDeleteMemCount(String.valueOf(count));
                    FaceDataRegAndDelSingleton.getSingleton().setCorpIdForDelete((String) param.get("corpId"));
                    if(FaceDataRegAndDelSingleton.getSingleton().getDealDelCountList()==null){
                        FaceDataRegAndDelSingleton.getSingleton().setDealDelCountList(new ArrayList<>());
                    }else {
                        FaceDataRegAndDelSingleton.getSingleton().getDealDelCountList().clear();
                    }
                }
            }
        }

        //复制副本
        Map<String, Object> temp = new HashMap<>();
        temp.putAll(param);
        //读入允许注册的行业--多行业情况
        Map localIndustry = DefinePropertiesFileUtils.getInstance().commonloadParam("bcss.default.industry");
        String compareIndustry=FileUtils.operateFile(localIndustry);
        if(compareIndustry==null){
            LOGGER.error("获取允许注册人脸的行业值的交易失败，" + JSONObject.toJSONString(localIndustry));
            return  CommonMapReturn.constructMapWithClientTransNo( MemberConstants.COMMON_FAIL, "获取允许注册人脸的行业值的交易失败",clientTransNo);
        }
        temp.put("compareIndustry",compareIndustry);

//        if (localIndustry == null || localIndustry.isEmpty() |
//                !(((String) localIndustry.get(MemberConstants.RETURN_CODE)).equals(MemberConstants.COMMON_SUCCESS))) {
//            LOGGER.error("获取允许注册人脸的行业值的交易失败，" + JSONObject.toJSONString(localIndustry));
//            return FileUtils.returnFailResult(temp, MemberConstants.COMMON_FAIL, "获取允许注册人脸的行业值的交易失败");
//        }
//        String compareIndustry = localIndustry.get(MemberConstants.PROPERTIES_VALUE);

        //转换operType
        if (FaceOperType.OPERTYPE_TWO_FACE_COUNT.getCode().equals(operType)) {
            temp.put("operType", "1");
        } else if (FaceOperType.OPERTYPE_EIGHT_FACE_DELETE_COUNT.getCode().equals(operType)) {
            temp.put("operType", "7");
        } else {
            LOGGER.error("当前的operType为：" + operType + ",找不到对应的查询明细操作的代码");
            return  CommonMapReturn.constructMapWithClientTransNo( MemberConstants.COMMON_EXCEPTION, "当前的operType为：" + operType + ",找不到对应的查询明细操作的代码",clientTransNo);
        }

        Map finalCurRes= faceAsynchronousControl.startDownloadFaceRegInfoByAsync(temp,count);
        long endTimeMill=System.currentTimeMillis();
        LOGGER.error("人脸注册所花费耗时间为："+(endTimeMill-statTimeMill)+"----开始时间："+TransNoUtils.getInstance().getTimeStamp(new Date(statTimeMill))+"----结束时间："+TransNoUtils.getInstance().getTimeStamp(new Date(endTimeMill)));
        return finalCurRes;
    }


    /*
    * 函数:查询人脸注册或者人脸删除的进度
    * */
    @Override
    //return_code=1(或者其他值)则没有线程拉取信息；return_code=-1，查询异常，检测系统日志；
    // return_code=0—则有线程拉取信息，增加Count、totoalCount字段返回此次当前人脸已经拉取人脸信息，以及总人脸
    public Map<String, Object> queryProgressService(Map<String, Object> param) {
        //读取FaceDataRegAndDelSingleton类属性，获取数据
        String operType= (String) param.get("queryType");
        Map<String,Object> result=new HashMap<>();
        if("1".equals(operType)){
            LOGGER.info("注册人员信息进度查询--");
            try{
                //0有任何线程
                if("0".equals(FaceDataRegAndDelSingleton.getSingleton().getRegisterFlag())){
                    result=CommonMapReturn.constructMapWithClientTransNo("0","存在线程进行注册人员信息的操作",TransNoUtils.getInstance().getTransNoWithRandom());
                    result.put("corpId",param.get("corpId"));
                    result.put("queryType",param.get("queryType"));
                    result.put("dealCorpId",FaceDataRegAndDelSingleton.getSingleton().getCorpIdForRegister());
//                    result.put("dealCount",FaceDataRegAndDelSingleton.getSingleton().getRegisterDealCount());
                    result.put("memCount",FaceDataRegAndDelSingleton.getSingleton().getRegisterMemCount());

                    int res=0;
                    for(Integer i:FaceDataRegAndDelSingleton.getSingleton().getDealRegCountList()){
                        res=res+i.intValue();
                    }
                    result.put("dealCount",res);
                }else{
                    //没有线程
                    result=CommonMapReturn.constructMapWithClientTransNo("1","当前没有线程进行注册人员信息操作",TransNoUtils.getInstance().getTransNoWithRandom());
                    result.put("corpId",param.get("corpId"));
                    result.put("queryType",param.get("queryType"));
                }
            }catch (Exception e){
                LOGGER.error("注册人员信息进度查询--发生异常，原因："+e.getMessage());
                LOGGER.error(e.toString(),e);
                //没有线程
                result=CommonMapReturn.constructMapWithClientTransNo("-1","注册人员信息进度查询--发生异常，原因："+e.getMessage(),TransNoUtils.getInstance().getTransNoWithRandom());
                result.put("corpId",param.get("corpId"));
                result.put("queryType",param.get("queryType"));
            }

            LOGGER.warn("注册人员信息进度查询--结果："+JSONObject.toJSONString(result));
            return result;
        }else {
            LOGGER.info("删除人员信息进度查询--");
            try{
                //0有任何线程
                if("0".equals(FaceDataRegAndDelSingleton.getSingleton().getDeleteFlage())){
                    result=CommonMapReturn.constructMapWithClientTransNo("0","存在线程进行删除人员信息的操作",TransNoUtils.getInstance().getTransNoWithRandom());
                    result.put("corpId",param.get("corpId"));
                    result.put("queryType",param.get("queryType"));
                    result.put("dealCorpId",FaceDataRegAndDelSingleton.getSingleton().getCorpIdForDelete());
//                    result.put("dealCount",FaceDataRegAndDelSingleton.getSingleton().getDeleteDealCount());
                    result.put("memCount",FaceDataRegAndDelSingleton.getSingleton().getDeleteMemCount());

                    int res=0;
                    for(Integer i:FaceDataRegAndDelSingleton.getSingleton().getDealDelCountList()){
                        res=res+i.intValue();
                    }
                    result.put("dealCount",res);
                }else{
                    //没有线程
                    result=CommonMapReturn.constructMapWithClientTransNo("1","当前没有线程进行删除人员信息操作",TransNoUtils.getInstance().getTransNoWithRandom());
                    result.put("corpId",param.get("corpId"));
                    result.put("queryType",param.get("queryType"));
                }
            }catch (Exception e){
                LOGGER.error("删除人员信息进度查询--发生异常，原因："+e.getMessage());
                LOGGER.error(e.toString(),e);
                //没有线程
                result=CommonMapReturn.constructMapWithClientTransNo("-1","删除人员信息进度查询--发生异常，原因："+e.getMessage(),TransNoUtils.getInstance().getTransNoWithRandom());
                result.put("corpId",param.get("corpId"));
                result.put("queryType",param.get("queryType"));
            }

            LOGGER.warn("删除人员信息进度查询--结果："+JSONObject.toJSONString(result));
            return result;
        }
//        return null;
    }




    /*
    * synchronized方法
    * 判断并重置注册线程，并设置数量
    * */
    @Deprecated
    public synchronized void faceRegLock(String regFlag,String memCount,String dealCount){

    }

    /*
     * synchronized方法
     * 判断并重置删除线程，并设置数量
     * */
    @Deprecated
    public synchronized void faceDelLock(){

    }

	@Override
	public Map<String, Object> pullFaceService(Map<String, Object> paramParent) {
		
		Map<String,Object> param= Collections.synchronizedMap(new HashMap<>(30));
        param.putAll(paramParent);

        String clientTransNo = (String) param.get(MemberConstants.CLIENTTRANSNO);
        if (clientTransNo == null || clientTransNo.equals("")) {
            clientTransNo = TransNoUtils.getInstance().getTransNoWithRandom();
            LOGGER.warn("调用方没有上送交易流水号，因此此次请求系统自行生成流水号：" + clientTransNo);
            param.put("clientTransNo", clientTransNo);
        } else {
            LOGGER.info("默认采用调用方上送的流水号，即为：" + clientTransNo);
        }
        LOGGER.error("初始化参数为：" + JSONObject.toJSONString(param));

        /*判断是否根据查询数量全部拉取记录*/
        String authFullDownLoad = (String) param.get("autoFullDown");

        if ("0".equals(authFullDownLoad)) {//需要==全自动拉取人脸
            Map<String, Object> outResult = new HashMap<>();
            try {
                outResult = registerFaceAllSubService(param);
            } catch (Exception e) {
                LOGGER.error("入口方法异常：" + e.toString());
//                LOGGER.error(e.getLocalizedMessage());
                LOGGER.error(e.getMessage(), e);
                outResult.put(MemberConstants.RETURN_MSG, "System Exception");
                outResult.put(MemberConstants.RETURN_CODE, MemberConstants.COMMON_EXCEPTION);
            }
            return outResult;
        } else {//仅调用api一次，并原样返回结果，不做过多处理
            LOGGER.info("autoFullDown的值为:" + authFullDownLoad + ",默认走原流程"); //0-需要，1-不需要
            Map<String, Object> downloadResult = bcssMemberFeatureService.execute(param);
            if (downloadResult == null || downloadResult.isEmpty()) {
                LOGGER.error("请求返回的人脸数据为空，请重试");
                return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_FAIL, "请求返回的人脸数据为空，请重试", clientTransNo);
            }
            //判断分支
            String operType = (String) param.get("operType");
            if (operType.equals(FaceOperType.OPERTYPE_TWO_FACE_COUNT.getCode()) || operType.equals(FaceOperType.OPERTYPE_EIGHT_FACE_DELETE_COUNT.getCode())) {
                return downloadResult;
            } else if (operType.equals(FaceOperType.OPERTYPE_ONE_FACE_DETAIL.getCode())) {
                //调用人脸注册接口
//                return faceRegisterService.registerService(downloadResult);
                //每一个人脸需要处理
                return bcssMemForeachFaceInfoService.foreachRegisterInfo(param,downloadResult);
            } else if (operType.equals(FaceOperType.OPERTYPE_SEVEN_FACE_DELETE_DETAIL.getCode())) {
//                return faceDeleteService.deleteService(downloadResult);
                //每一个人脸需要处理
                return bcssMemForeachFaceInfoService.foreachRegisterInfo(param,downloadResult);
            } else {
                LOGGER.error("operType为非合法指定值，该值为：" + operType + ",返回错误信息");
                return CommonMapReturn.constructMapWithClientTransNo(MemberConstants.COMMON_EXCEPTION, "系统异常，请重试", clientTransNo);
            }
        }
	}



}

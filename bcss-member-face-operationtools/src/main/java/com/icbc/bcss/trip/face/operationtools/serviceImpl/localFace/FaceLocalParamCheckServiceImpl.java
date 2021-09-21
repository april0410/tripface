package com.icbc.bcss.trip.face.operationtools.serviceImpl.localFace;

import com.icbc.bcss.trip.face.operationtools.exception.BusinessException;
import com.icbc.bcss.trip.face.operationtools.service.localFace.FaceLocalParamCheckService;
import com.icbc.bcss.trip.face.operationtools.serviceImpl.FaceAllServiceImpl;
import com.icbc.bcss.trip.face.operationtools.utils.MemberConstants;
import com.icbc.bcss.trip.face.operationtools.utils.MemberUtil;
import com.icbc.bcss.trip.face.operationtools.utils.TransNoUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("faceLocalParamCheckService")
public class FaceLocalParamCheckServiceImpl implements FaceLocalParamCheckService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FaceLocalParamCheckServiceImpl.class);

    /*
    * 检测标准单个人脸注册的参数
    * */
    @Override
    public Map<String, Object> checkRegisterCommonParam(Map<String, Object> params) {
        HashMap<String, Object> resultMap = new HashMap<>();
        String msg = "";

        String corpId = (String) params.get("corpId");
        if (StringUtils.isBlank(corpId) || !MemberUtil.checkNumber(corpId)) {
            msg = "上送参数基础校验--上送的企业编号不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }
        if(corpId.length()> MemberConstants.MAX_LENGTH_CORPID){
            msg = "上送参数基础校验--上送的企业编号长度超过限制";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        //corgNo不校验
        String timeStamp= (String) params.get("timeStamp");
        if (StringUtils.isBlank(timeStamp)){
            msg = "上送参数基础校验--上送的时间戳为空";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }
        Date timeStampDate= TransNoUtils.getInstance().convertTimeStamp(timeStamp);
        if (timeStampDate==null){
            msg = "上送参数基础校验--上送的时间戳不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

//        String clientTransNo = (String) params.get("clientTransNo");
//        if (StringUtils.isBlank(clientTransNo) || clientTransNo.length() > MemberConstants.MAX_LENGTH_CLIENTTRANSNO || !MemberUtil.checkNumber(clientTransNo)) {
//            msg = "上送的终端交易流水号不合法";
//            LOGGER.error(msg);
//            throw new BusinessException(msg);
//        }

        //////////////////////////////
        String dataSource = (String) params.get("dataSource");
        if (StringUtils.isBlank(dataSource) || dataSource.length() > MemberConstants.MAX_LENGTH_REG_DATA_SOURCE) {
            msg = "上送参数基础校验--上送的信息来源不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }
        //0--云端，1--本地，此接口默认送1
        if(  !("0".equals(dataSource)||"1".equals(dataSource)) ){
            msg = "上送参数基础校验--上送的信息来源字典值不合法，请重新输入";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        //personid检测
        String bcssPersonId = (String) params.get("bcssPersonId");
        if (StringUtils.isBlank(bcssPersonId)) {
            msg = "上送参数基础校验--上送的银行卡场景个人编号为空，请检查";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }
        if( bcssPersonId.length() > MemberConstants.MAX_LENGTH_PERSONID){
            if(MemberConstants.PERSONID_FULL_DEFAULT.equals(bcssPersonId)){
                msg="上送参数基础校验--上送的银行卡场景个人编号为全0，长度为20，默认通过检查";
                LOGGER.error(msg);
            }else{
                msg = "上送参数基础校验--上送的银行卡场景个人编号超过长度限制，请检查";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }
        //长度合法情况下，需要校验是不是全零
        boolean personIdFlag=MemberUtil.checkNumber(bcssPersonId);
        if(personIdFlag==false){
            msg="上送参数基础校验--上送的银行卡场景个人编号包含非数字的字符，请检查";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        //人员序号
        String bcssSeqNo = (String) params.get("bcssSeqNo");
        if (StringUtils.isBlank(bcssSeqNo) || bcssSeqNo.length() > MemberConstants.MAX_LENGTH_SEQNO) {
            msg = "上送参数基础校验--上送的人员序号不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }
        if(!MemberUtil.checkNumber(bcssSeqNo)){
            msg = "上送参数基础校验--上送的人员序号包含非数字的字符,请检查";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        /**/
        String industry= (String) params.get("industry");
        if(StringUtils.isBlank(industry)||industry.length()>MemberConstants.MAX_LENGTH_INDUSTRY){
            msg = "上送参数基础校验--上送的行业标识不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }
        if(!MemberUtil.checkNumber(industry)){
            msg = "上送参数基础校验--上送的行业标识包含非数字的字符,请检查";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }
        /**/

        //性别
        String sex = (String) params.get("sex");
        if(StringUtils.isBlank(sex)){
            msg = "上送参数基础校验--上送的性别为空，请重新上送";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }else{
            if(sex.length()>MemberConstants.MAX_LENGTH_SEX){
                msg = "上送参数基础校验--上送的性别长度超过限制";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }

            if( !( "1".equals(sex)||"2".equals(sex)||"3".equals(sex)) ){
                msg = "上送参数基础校验--上送的性别不合法";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }

        //选填--校验
        String memCardNo = (String) params.get("memCardNo");
        if (StringUtils.isNotBlank(memCardNo) ) {
            if (memCardNo.length() > MemberConstants.MAX_LENGTH_MEMCARDN0||  !MemberUtil.checkNumber(memCardNo)) {
                msg = "上送参数基础校验--上送的会员卡号不合法";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }


        String memName = (String) params.get("memName");
        if(StringUtils.isNotBlank(memName) ){
            if (memCardNo.length() > MemberConstants.MAX_LENGTH_REG_THREE_INDIFIED_INFO) {
                msg = "上送参数基础校验--上送的会员姓名不合法";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }

        //证件类型
        String certType = (String) params.get("certType");
        if (StringUtils.isNotBlank(certType)) {
            if (memCardNo.length() > MemberConstants.MAX_LENGTH_REG_THREE_INDIFIED_INFO) {
                msg = "上送参数基础校验--上送的会员证件类型为空";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }else if(MemberConstants.EMPTY_STRING_SYMBOL.equals(certType)){
            msg = "上送参数基础校验--上送的会员证件类型为空字符串，不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        //证件类型
        String certNo = (String) params.get("certNo");
        if (StringUtils.isNotBlank(certNo)) {
            if (memCardNo.length() > MemberConstants.MAX_LENGTH_REG_THREE_INDIFIED_INFO) {
                msg = "上送参数基础校验--上送的会员证件号码为空";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }else if(MemberConstants.EMPTY_STRING_SYMBOL.equals(certNo)){
            msg = "上送参数基础校验--上送的会员证件号码为空字符串，不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        /*证件类型和证件号码组合判断*/
        if(certType!=null){
            if (StringUtils.isBlank(certNo)) {
                msg = "上送参数基础校验--上送的会员证件号码为空2";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
            //单独校验
            if (certType.equals("0")){
                boolean idNorealFlag=MemberUtil.isIdNumber(certNo);
                if(idNorealFlag==false){
                    msg = "上送参数基础校验--上送的会员证件类型为0-身份证，但是证件号码为非法身份证号码，重新上送";
                    LOGGER.error(msg);
                    throw new BusinessException(msg);
                }
                //身份证和性别关联校验
                String  sexFlag=MemberUtil.getSexByIdNo(certNo);
                if( "3".equals(sex)|| !sex.equals(sexFlag) ){
                    msg = "上送参数基础校验--上送的会员证件类型为0-身份证，但是性别与身份证不一致，重新上送";
                    LOGGER.error(msg);
                    throw new BusinessException(msg);
                }
            }else if(certType.equals("1")){
                msg="当前是户口本校验，程序暂不支持该证件类型";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }else{
                msg="当前的证件类型不存在，程序暂不支持该证件类型";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }else{
            if (certNo!=null||"".equals(certNo)){
                msg = "上送参数基础校验--上送的会员证件类型为空，但是证件号码已经上送";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }

        //
        String featureType = (String) params.get("featureType");
        if (StringUtils.isBlank(featureType) || !MemberUtil.checkNumber(featureType) ){
            msg = "上送参数基础校验--上送的特征类型不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }
        if (featureType.length()>MemberConstants.MAX_LENGTH_FEATURE_TYPE){
            msg = "上送参数基础校验--上送的特征类型长度超过限制";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }
        //人脸校验
        String featureContent = (String) params.get("featureContent");
        if (StringUtils.isBlank(featureContent)) {
            msg = "上送参数基础校验--上送的特征值为空";
            LOGGER.error(msg);
            throw new BusinessException(msg);

        }
        //大小
        try {
            ByteArrayInputStream fileInputStreamA = new ByteArrayInputStream(featureContent.getBytes("UTF-8"));
            int countA = fileInputStreamA.available();

            if (countA > MemberConstants.FACE_IMAGE_SIZE_MAX_VALUE||countA<MemberConstants.FACE_IMAGE_SIZE_MIN_VALUE) {
                msg="上送参数基础校验--判断图片大小为：" + countA + "字节，图片大小不符合规范";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }

        } catch (UnsupportedEncodingException e) {
            LOGGER.error("上送参数基础校验--判断图片大小失败，返回失败信息"+e.getMessage());
            LOGGER.error(e.toString(),e);
            throw new BusinessException("上送参数基础校验--判断图片大小失败，返回失败信息"+e.getMessage());
        }

        //  序号
        String featureSeqNo = (String) params.get("featureSeqNo");
        if (StringUtils.isBlank(featureSeqNo) || featureSeqNo.length() > MemberConstants.MAX_LENGTH_FEATURE_SEQNO) {
            msg = "上送参数基础校验--上送的特征值顺序不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }
        if (!(MemberUtil.checkNumber(featureSeqNo))) {
            msg = "上送参数基础校验--上送的特征值顺序包含非数字的字符";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }




        // birthday
        String birthday = (String) params.get("birthday");
        if (StringUtils.isNotBlank(birthday)) {
            if(birthday.length()>MemberConstants.MAX_LENGTH_BIRTHDAY){
                msg = "上送参数基础校验--上送的生日不合法";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }

            Date checkBirthday=TransNoUtils.getInstance().convertDate(birthday);
            if(checkBirthday==null){
                msg = "上送参数基础校验--上送的生日日期格式不合法";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }else if(MemberConstants.EMPTY_STRING_SYMBOL.equals(birthday)){
            msg = "上送参数基础校验--上送的生日日期为空字符串，不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        String duty = (String) params.get("duty");
        if (StringUtils.isNotBlank(duty) && duty.length() > MemberConstants.MAX_LENGTH_COMMON_CLOUDWALK_64) {
            msg = "上送参数基础校验--上送的职务不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }else if(MemberConstants.EMPTY_STRING_SYMBOL.equals(duty)){
            msg = "上送参数基础校验--上送的职务为空字符串，不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        String position = (String) params.get("position");
        if (StringUtils.isNotBlank(position) && position.length() > MemberConstants.MAX_LENGTH_COMMON_CLOUDWALK_64) {
            msg = "上送参数基础校验--上送的职位不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }else if(MemberConstants.EMPTY_STRING_SYMBOL.equals(position)){
            msg = "上送参数基础校验--上送的职位为空字符串，不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }
        //otherData、operUser、definedAmtFlag暂不校验

        String telephone= (String) params.get("telephone");
        if (StringUtils.isNotBlank(telephone) ) {
            if(telephone.length()>MemberConstants.MAX_LENGTH_COMMON_CLOUDWALK_32){
                msg = "上送参数基础校验--上送的手机号码不合法，长度超过限制";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }else if(MemberConstants.EMPTY_STRING_SYMBOL.equals(telephone)){
            msg = "上送参数基础校验--上送的手机号码为空字符串，不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        //人脸或者二维码
        String email= (String) params.get("email");
        if (StringUtils.isNotBlank(email) ) {
            if(telephone.length()>MemberConstants.MAX_LENGTH_COMMON_CLOUDWALK_64){
                msg = "上送参数基础校验--上送的电子邮箱长度超过限制";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
            if(!MemberUtil.chcekEmail(email)){
                msg = "上送参数基础校验--上送的电子邮箱不合法";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }else if(MemberConstants.EMPTY_STRING_SYMBOL.equals(email)){
            msg = "上送参数基础校验--上送的电子邮箱为空字符串，不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }


        /*地址*/
        String address= (String) params.get("address");
        if (StringUtils.isNotBlank(address) ) {
            if(address.length()>MemberConstants.MAX_LENGTH_COMMON_CLOUDWALK_255){
                msg = "上送参数基础校验--上送的地址长度超过限制";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }else if(MemberConstants.EMPTY_STRING_SYMBOL.equals(address)){
            msg = "上送参数基础校验--上送的地址为空字符串，不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        String remark= (String) params.get("remark");
        if (StringUtils.isNotBlank(remark) ) {
            if(remark.length()>MemberConstants.MAX_LENGTH_COMMON_CLOUDWALK_255){
                msg = "上送参数基础校验--上送的备注信息超过限制";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }else if(MemberConstants.EMPTY_STRING_SYMBOL.equals(remark)){
            msg = "上送参数基础校验--上送的备注信息为空字符串，不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        //省，市区
        String province= (String) params.get("province");
        if (StringUtils.isNotBlank(province) ) {
            if(province.length()>MemberConstants.MAX_LENGTH_COMMON_CLOUDWALK_32){
                msg = "上送参数基础校验--上送的省名称不合法，长度超过限制";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }else if(MemberConstants.EMPTY_STRING_SYMBOL.equals(province)){
            msg = "上送参数基础校验--上送的省名称为空字符串，不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        String city= (String) params.get("city");
        if (StringUtils.isNotBlank(city) ) {
            if(city.length()>MemberConstants.MAX_LENGTH_COMMON_CLOUDWALK_32){
                msg = "上送参数基础校验--上送的市名称不合法，长度超过限制";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }else if(MemberConstants.EMPTY_STRING_SYMBOL.equals(city)){
            msg = "上送参数基础校验--上送的市名称为空字符串，不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        String area= (String) params.get("area");
        if (StringUtils.isNotBlank(area) ) {
            if(area.length()>MemberConstants.MAX_LENGTH_COMMON_CLOUDWALK_32){
                msg = "上送参数基础校验--上送的区名称不合法，长度超过限制";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }else if(MemberConstants.EMPTY_STRING_SYMBOL.equals(area)){
            msg = "上送参数基础校验--上送的区名称为空字符串，不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        //
        String backup1= (String) params.get("backup1");
        if (StringUtils.isNotBlank(backup1) ) {
            if(backup1.length()>200){
                msg = "上送参数基础校验--上送的备用字段1信息长度超过限制";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }else if(MemberConstants.EMPTY_STRING_SYMBOL.equals(backup1)){
            msg = "上送参数基础校验--上送的备用字段1为空字符串，不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        //其他信息
        String otherData= (String) params.get("otherData");
        if (StringUtils.isNotBlank(otherData) ) {
            if(otherData.length()>200){
                msg = "上送参数基础校验--上送的其他数据信息长度超过限制";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }else if(MemberConstants.EMPTY_STRING_SYMBOL.equals(otherData)){
            msg = "上送参数基础校验--上送的其他数据为空字符串，不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }


        //校验成功
        resultMap.put(MemberConstants.RETURN_CODE,"0");
        resultMap.put(MemberConstants.RETURN_MSG, "基础参数校验成功");
        return resultMap;
//        return null;
    }

    @Override
    public Map<String, Object> checkDeleteCommonParam(Map<String, Object> params) {
        HashMap<String, Object> resultMap = new HashMap<>();
        String msg = "";

        String corpId = (String) params.get("corpId");
        if (StringUtils.isBlank(corpId) || !MemberUtil.checkNumber(corpId)) {
            msg = "上送参数基础校验--上送的企业编号不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }
        if(corpId.length()> MemberConstants.MAX_LENGTH_CORPID){
            msg = "上送参数基础校验--上送的企业编号长度超过限制";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        //corgNo不校验
        String timeStamp= (String) params.get("timeStamp");
        if (StringUtils.isBlank(timeStamp)){
            msg = "上送参数基础校验--上送的时间戳为空";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }
        Date timeStampDate= TransNoUtils.getInstance().convertTimeStamp(timeStamp);
        if (timeStampDate==null){
            msg = "上送参数基础校验--上送的时间戳不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

//        String clientTransNo = (String) params.get("clientTransNo");
//        if (StringUtils.isBlank(clientTransNo) || clientTransNo.length() > MemberConstants.MAX_LENGTH_CLIENTTRANSNO || !MemberUtil.checkNumber(clientTransNo)) {
//            msg = "上送的终端交易流水号不合法";
//            LOGGER.error(msg);
//            throw new BusinessException(msg);
//        }

        //////////////////////////////
        String operType = (String) params.get("operType");
        if (StringUtils.isBlank(operType) || operType.length() > 10) {
            msg = "上送参数基础校验--上送的删除方式不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        //0--云端，1--本地，此接口默认送1
        String personUniqueCode= (String) params.get("personUniqueCode");
        String bcssPersonId= (String) params.get("bcssPersonId");
        String  bcssSeqNo= (String) params.get("bcssSeqNo");
        String memCardNo= (String) params.get("memCardNo");
        if(  "1".equals(operType) ){

            if (StringUtils.isBlank(personUniqueCode)) {
                msg = "上送参数基础校验--上送的人员编号为空，请检查";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
            if( personUniqueCode.length() > 32){
                if(!MemberConstants.PERSONID_FULL_DEFAULT.equals(personUniqueCode)){
                    msg = "上送参数基础校验--上送的人员编号超过长度限制，请检查";
                    LOGGER.error(msg);
                    throw new BusinessException(msg);
                }
            }
            //其他字段不能上送
            if(StringUtils.isNotBlank(bcssPersonId)){
                msg = "上送参数基础校验--上送的银行卡场景个人编号不需要上送，请检查";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
            if(StringUtils.isNotBlank(bcssSeqNo)){
                msg = "上送参数基础校验--上送的人员序号不需要上送，请检查";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
            if(StringUtils.isNotBlank(memCardNo)){
                msg = "上送参数基础校验--上送的会员卡号不需要上送，请检查";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }

        }else if("2".equals(operType)){
            //personid检测
//            String bcssPersonId = (String) params.get("bcssPersonId");
            if (StringUtils.isBlank(bcssPersonId)) {
                msg = "上送参数基础校验--上送的银行卡场景个人编号为空，请检查";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
            if( bcssPersonId.length() > MemberConstants.MAX_LENGTH_PERSONID){
                if(MemberConstants.PERSONID_FULL_DEFAULT.equals(bcssPersonId)){
                    msg="上送参数基础校验--上送的银行卡场景个人编号为全0，长度为20，默认通过检查";
                    LOGGER.error(msg);
                }else{
                    msg = "上送参数基础校验--上送的银行卡场景个人编号超过长度限制，请检查";
                    LOGGER.error(msg);
                    throw new BusinessException(msg);
                }
            }
            //长度合法情况下，需要校验是不是全零
            boolean personIdFlag=MemberUtil.checkNumber(bcssPersonId);
            if(personIdFlag==false){
                msg="上送参数基础校验--上送的银行卡场景个人编号包含非数字的字符，请检查";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }

            //人员序号
//            String bcssSeqNo = (String) params.get("bcssSeqNo");
            if (StringUtils.isBlank(bcssSeqNo) || bcssSeqNo.length() > MemberConstants.MAX_LENGTH_SEQNO) {
                msg = "上送参数基础校验--上送的人员序号不合法";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
            if(!MemberUtil.checkNumber(bcssSeqNo)){
                msg = "上送参数基础校验--上送的人员序号包含非数字的字符,请检查";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }

            //其他字段
            if(StringUtils.isNotBlank(personUniqueCode)){
                msg = "上送参数基础校验--上送的人员编号不需要上送，请检查";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
            if(StringUtils.isNotBlank(memCardNo)){
                msg = "上送参数基础校验--上送的会员卡号不需要上送，请检查";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }

        }else if("3".equals(operType)){

//            String memCardNo= (String) params.get("memCardNo");
            if (StringUtils.isBlank(memCardNo)) {
                msg = "上送参数基础校验--上送的会员卡卡号为空，请检查";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
            if( memCardNo.length() > 20){
                if(MemberConstants.PERSONID_FULL_DEFAULT.equals(memCardNo)){
                    msg = "上送参数基础校验--上送的会员卡号超过长度限制，请检查";
                    LOGGER.error(msg);
                    throw new BusinessException(msg);
                }
            }
            //人员序号
//            String bcssSeqNo = (String) params.get("bcssSeqNo");
            if (StringUtils.isBlank(bcssSeqNo) || bcssSeqNo.length() > MemberConstants.MAX_LENGTH_SEQNO) {
                msg = "上送参数基础校验--上送的人员序号不合法";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
            if(!MemberUtil.checkNumber(bcssSeqNo)){
                msg = "上送参数基础校验--上送的人员序号包含非数字的字符,请检查";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }


            //其他字段不能上送
            if(StringUtils.isNotBlank(personUniqueCode)){
                msg = "上送参数基础校验--上送的人员编号不需要上送，请检查";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
            if(StringUtils.isNotBlank(bcssPersonId)){
                msg = "上送参数基础校验--上送的银行卡场景个人编号不需要上送，请检查";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
//            if(StringUtils.isNotBlank(bcssSeqNo)){
//                msg = "上送参数基础校验--上送的人员序号不需要上送，请检查";
//                LOGGER.error(msg);
//                throw new BusinessException(msg);
//            }

        }else {
            msg = "上送参数基础校验--上送的删除方式异常";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        /*行业标识*/
        String industry= (String) params.get("industry");
        if(StringUtils.isBlank(industry)||industry.length()>MemberConstants.MAX_LENGTH_INDUSTRY){
            msg = "上送参数基础校验--上送的行业标识不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }
        if(!MemberUtil.checkNumber(industry)){
            msg = "上送参数基础校验--上送的行业标识包含非数字的字符,请检查";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        /**/
        //
        String backup1= (String) params.get("backup1");
        if (StringUtils.isNotBlank(backup1) ) {
            if(backup1.length()>200){
                msg = "上送参数基础校验--上送的备用字段1信息长度超过限制";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }else if(MemberConstants.EMPTY_STRING_SYMBOL.equals(backup1)){
            msg = "上送参数基础校验--上送的备用字段1为空字符串，不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }

        //其他信息
        String otherData= (String) params.get("otherData");
        if (StringUtils.isNotBlank(otherData) ) {
            if(otherData.length()>200){
                msg = "上送参数基础校验--上送的其他数据信息长度超过限制";
                LOGGER.error(msg);
                throw new BusinessException(msg);
            }
        }else if(MemberConstants.EMPTY_STRING_SYMBOL.equals(otherData)){
            msg = "上送参数基础校验--上送的其他数据为空字符串，不合法";
            LOGGER.error(msg);
            throw new BusinessException(msg);
        }
        //校验成功
        resultMap.put(MemberConstants.RETURN_CODE,"0");
        resultMap.put(MemberConstants.RETURN_MSG, "基础参数校验成功");
        return resultMap;
//        return null;
    }
}

package com.icbc.bcss.trip.face.operationtools.utils;

import com.alibaba.fastjson.JSONObject;
import com.icbc.api.crypt.AES;
import com.icbc.bcss.trip.face.operationtools.exception.BusinessException;
import com.icbc.bcss.trip.face.operationtools.serviceImpl.localFace.FaceLocalParamCheckServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.server.InactiveGroupException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class MemberUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberUtil.class);

    private static final String NUMBER_N_REGEX=new String("^[0-9]*$");
    private static final String PHONENO_REGEX=new String("^[0-9]{11}$");
    private static final String EMAIL_REGEX=new String("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    /*
    * 检测是否数字
    * */
    public static boolean checkNumber(String number) {
        if(number==null){
            number="";
        }
        if(number.matches(NUMBER_N_REGEX)){
            return true;
        }else {
            return false;
        }
    }

    public static boolean chcekEmail(String email){
        if(email==null){
            email="";
        }
        if(email.matches(EMAIL_REGEX)){
            return true;
        }else {
            return false;
        }
    }

    public static boolean checkData(Map<String,Object> params){
        String msg="";
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        if ((StringUtils.isBlank(startDate) && StringUtils.isNotBlank(endDate)) || (StringUtils.isBlank(endDate) && StringUtils.isNotBlank(startDate))) {
            msg = "开始与结束日期必须同时输入或为空";
        }
        if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String now = sdf.format(new Date()).replaceAll("-", "");
                if (Integer.parseInt(endDate.replaceAll("-","")) < Integer.parseInt(startDate.replaceAll("-",""))) {
                    msg = "结束日期必须大于等于开始日期";
                }
                if (Integer.parseInt(startDate.replaceAll("-","")) > Integer.parseInt(now)) {
                    msg = "开始日期必须小于等于当前日期";
                }
                if (Integer.parseInt(endDate.replaceAll("-","")) > Integer.parseInt(now)) {
                    msg = "结束日期必须小于等于当前日期";
                }
            } catch (Exception e) {
                msg = "开始或结束日期不合法";
            }
        }
        return false;
    }


    public static boolean isIdNumber(String idNumber){
        if (StringUtils.isBlank(idNumber)) {
            return false;
        }
        int len = idNumber.length();
        if (len != 15 && len != 18) {
            return false;
        }

        if (len == 15) {
            //补全年份
            idNumber = idNumber.substring(0, 6) + "19" + idNumber.substring(6);
            //增加一个无效校验位，仅为补全长度，不做校验
            idNumber += "0";
        }
        HashMap<String, String> map = new HashMap<String, String>(64);
        map.put("11", "北京");
        map.put("12", "天津");
        map.put("13", "河北");
        map.put("14", "山西");
        map.put("15", "内蒙古");
        map.put("21", "辽宁");

        map.put("22", "吉林");
        map.put("23", "黑龙江");
        map.put("31", "上海");
        map.put("32", "江苏");
        map.put("33", "浙江");
        map.put("34", "安徽");

        map.put("35", "福建");
        map.put("36", "江西");
        map.put("37", "山东");
        map.put("41", "河南");
        map.put("42", "湖北");
        map.put("43", "湖南");

        map.put("44", "广东");
        map.put("45", "广西");
        map.put("46", "海南");
        map.put("50", "重庆");
        map.put("51", "四川");
        map.put("52", "贵州");

        map.put("53", "云南");
        map.put("54", "西藏");
        map.put("61", "陕西");
        map.put("62", "甘肃");
        map.put("63", "青海");
        map.put("64", "宁夏");

        map.put("65", "新疆");
        map.put("71", "台湾");
        map.put("81", "香港");
        map.put("82", "澳门");
        map.put("91", "国外");


        String city = idNumber.substring(0,2);
        if (map.get(city) == null) {
            return false;
        }

        String year = idNumber.substring(6, 10);
        int yearInt = 0;
        try {
            yearInt = Integer.valueOf(year);
        } catch (Exception e) {
            LOGGER.error("身份证年份不为数字，无效");
            return false;
        }
//        boolean isLeapYear = DateUtils.isGregorianLeapYear(yearInt);
        boolean isLeapYear = isGregorianLeapYear(yearInt);
        //比较年份
        String regex = null;
        if (isLeapYear) {
            regex = "^[1-9][0-9]{5}(18|19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9X]$";
        } else {
            regex = "^[1-9][0-9]{5}(18|19|20)[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9X]$";
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(idNumber);
        if (!matcher.matches()) {
            return false;
        }

        String brnDate = idNumber.substring(6, 14);
        if(!TransNoUtils.isValidDate2(brnDate)){
            return false;
        }

        // 15位身份证不需要检查校验位
        if (len != 15) {
            //身份证校验码
            int[] coefficientArray = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
            //身份证号的尾数逻辑
            String[] identityMantissa = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

            char[] chars = idNumber.toCharArray();
            long sum = IntStream.range(0,17).map(index -> {
                char ch = chars[index];
                int digit = Character.digit(ch, 10);
                int coefficient = coefficientArray[index];
                return digit*coefficient;
            }).summaryStatistics().getSum();

            // 计算出的尾数索引
            int mantissaIndex = (int)(sum%11);
            String mantissa = identityMantissa[mantissaIndex];
            String lastChar = idNumber.substring(17);
            if (lastChar.equalsIgnoreCase(mantissa)) {
                return true;
            }else {
                return false;
            }
        }
        return true;
    }

    public static boolean isGregorianLeapYear(int year){
        if(year%4==0&&year%100!=0||year%400==0) {
            System.out.println("是闰年");
            return true;
        }else{
            System.out.println("不是闰年");
            return false;
        }
    }

    /*
    * 校验性别*/
    public static String  getSexByIdNo(String custCode){
        if(StringUtils.isEmpty(custCode)||custCode.length()<15){
            return null;
        }
        int num;
        if(custCode.length()==15){
            num=Integer.parseInt(custCode.substring(14,15));
        }else{
            num= Integer.parseInt(custCode.substring(16,17));
        }

        if(num%2==1){
            return "1"; //奇数--男生
        }
        return "2";
    }

    /*
    * 判断是否转出BigDecimal
    * */
    public static boolean judgeBigDecimalFormat(String str){
        try{
            BigDecimal temp=new BigDecimal(str);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    /*
    * 组织信息
    * */
    /*组装用户信息*/
    public static   String constructMemAccNo(String key,String dt,String clientTransNo,String context) throws Exception {
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("dt", dt);
        jsonObject.put("clientTransNo", clientTransNo);
        if("5".equals(dt)){
            jsonObject.put("mobile", "");
            jsonObject.put("staffno", "");
            jsonObject.put("name", "");
            jsonObject.put("custcode", "");
            jsonObject.put("custsort", "");
            jsonObject.put("personId", context);//312112185478410240
            jsonObject.put("memCardNo", "");
        }else if("1".equals(dt)){
            jsonObject.put("mobile", "");
            jsonObject.put("staffno", "");
            jsonObject.put("name", "");
            jsonObject.put("custcode", "");
            jsonObject.put("custsort", "");
            jsonObject.put("personId", "");//312112185478410240
            jsonObject.put("memCardNo", context);
        }else {
            LOGGER.error("加密信息的顺序属性不正确，请检查");
            throw new BusinessException("加密信息的顺序属性不正确，请检查");
        }

        String encrypt= AES.aesEncrypt(jsonObject.toJSONString(),key);
        return encrypt;
    }


}

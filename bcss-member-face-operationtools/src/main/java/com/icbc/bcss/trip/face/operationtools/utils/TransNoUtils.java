package com.icbc.bcss.trip.face.operationtools.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TransNoUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransNoUtils.class);

    @Autowired
    private PropertyPlaceholder environment;

    private static TransNoUtils instance;

    private static TimeZone china=TimeZone.getTimeZone("GMT+08:00");

    public static TransNoUtils getInstance(){
        if(instance==null){
            return new TransNoUtils();
        }
        return instance;
    }

    /*
    * 生成20+random位随机数*/
    public  String getTransNoWithRandom(){
        String baseClientTransNo = getTransNoIn20();

        PropertisUtils.initProperties();
        String containId = environment.getProperty("bcss.random.num");

        String ranNext=RandomInt.getRandomLong(Integer.parseInt(containId));
        if(ranNext==null||ranNext.isEmpty()){
            StringBuilder stringBuilder=new StringBuilder();
            for(int i=1;i<=Integer.parseInt(containId);i++){
                stringBuilder.append(i);
            }
            ranNext=stringBuilder.toString();
        }

        return baseClientTransNo+ranNext;
    }
    /*
     * 生成标准20位随机数*/
    public  String getTransNoIn20(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmssSSS");
        simpleDateFormat.setTimeZone(china);
        String clientTransNoPre = simpleDateFormat.format(new Date());
//        String randomNum=environment.getProperty("bcss.random.num");
        String randomNum="3";

        String ran=RandomInt.getRandomInt(Integer.parseInt(randomNum));
        if(ran==null||ran.isEmpty()){
            StringBuilder stringBuilder=new StringBuilder();
            for(int i=1;i<=Integer.parseInt(randomNum);i++){
                stringBuilder.append(i);
            }
            ran=stringBuilder.toString();
        }
        String clientTransNo=clientTransNoPre+ ran;
        return clientTransNo;
    }

    //默认30+random位长度
    //corpId+17位时间戳+行业标识+随机位数
    public  String getTransNoWithCorpId(String corpId,String industryFull,int randomNum){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmssSSS");
        simpleDateFormat.setTimeZone(china);
        String clientTransNoPre = simpleDateFormat.format(new Date());
        //判断industryFull
        if(industryFull==null||industryFull.isEmpty()){
            StringBuilder stringBuilder=new StringBuilder();
            for(int i=0;i<MemberConstants.MAX_LENGTH_INDUSTRY;i++){
                stringBuilder.append("0");
            }
            industryFull=stringBuilder.toString();
        }
        //随机数五位
        String ran=RandomInt.getRandomInt(randomNum);
        if(ran==null||ran.isEmpty()){
            StringBuilder stringBuilder=new StringBuilder();
            for(int i=1;i<=randomNum;i++){
                stringBuilder.append(i);
            }
            ran=stringBuilder.toString();
        }
        String clientTransNo=corpId+clientTransNoPre+ industryFull+ran;
        return clientTransNo;
    }


    public String getTimeStamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(china);
        String timeStamp =simpleDateFormat.format(new Date());
        return timeStamp;
    }

    public String getTimeStamp(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(china);
        String timeStamp = simpleDateFormat.format(date);
        return timeStamp;
    }

    //判断
    public Date convertTimeStamp(String date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(china);
        simpleDateFormat.setLenient(false);// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
        Date result=null;
        try {
            result=simpleDateFormat.parse(date);
        } catch (ParseException e) {
            LOGGER.error("日期转换错误，所以即将插入null:"+e.toString());
            LOGGER.error(e.toString(),e);
            return null;
        }
        return result;
    }

/*日期（不含时间）*/
    public String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(china);
        String timeStamp =simpleDateFormat.format(new Date());
        return timeStamp;
    }

    public String getDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(china);
        String timeStamp = simpleDateFormat.format(date);
        return timeStamp;
    }

    //判断
    public Date convertDate(String date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(china);
        simpleDateFormat.setLenient(false);// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
        Date result=null;
        try {
            result=simpleDateFormat.parse(date);
        } catch (ParseException e) {
            LOGGER.error("日期转换错误，所以即将插入null");
            LOGGER.error(e.toString());
            LOGGER.error(e.getMessage(),e);
            return null;
        }
        return result;
    }


    public String getTimeSuff(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        simpleDateFormat.setTimeZone(china);
        String timeStamp =simpleDateFormat.format(new Date());
        return timeStamp;
    }

    public String getTimeSuff(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        simpleDateFormat.setTimeZone(china);
        String timeStamp = simpleDateFormat.format(date);
        return timeStamp;
    }


    //判断
    public Integer convertInt(String num){
        if (num==null||num.isEmpty()){
            return null;
        }
        Integer x=null;
        try {
            x=Integer.parseInt(num);
        } catch (NumberFormatException e) {
            LOGGER.error("日期转换错误，所以即将插入null:"+e.toString());
            LOGGER.error(e.toString(),e);
            return null;
        }
        return x;
    }

    /*
    * 当前日期+1*/
    public Date addDay(Date current,int day){
        try{
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(current);
            calendar.add(Calendar.DATE,day);
            return calendar.getTime();
        }catch (Exception e){
            LOGGER.error("日期叠加若干天数异常:"+e.toString());
            LOGGER.error(e.toString(),e);
            return null;
        }
    }

    /*
     * 当前日期-1*/
    public Date reduceDay(Date current,int day){
        try{
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(current);
            calendar.add(Calendar.DATE,day);
            return calendar.getTime();
        }catch (Exception e){
            LOGGER.error("日期叠加若干天数异常:"+e.toString());
            LOGGER.error(e.toString(),e);
            return null;
        }
    }

    //判断
    public static boolean isValidDate2(String date){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
        simpleDateFormat.setTimeZone(china);
        simpleDateFormat.setLenient(false);// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
        Date result=null;
        try {
            result=simpleDateFormat.parse(date);
            if(result!=null){
                return true;
            }else {
                return false;
            }
        } catch (ParseException e) {
            LOGGER.error("日期转换错误，所以即将插入null");
            LOGGER.error(e.toString());
            LOGGER.error(e.getMessage(),e);
            return false;
        }
    }
}

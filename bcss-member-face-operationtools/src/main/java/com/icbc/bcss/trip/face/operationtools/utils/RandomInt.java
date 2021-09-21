package com.icbc.bcss.trip.face.operationtools.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Arrays;

public class RandomInt {
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomInt.class);

    public static String getRandomInt(int n){
        Integer num=1;
        for(int i=1;i<n;i++){
            num=num*10;
        }
        num=(int)(num*Math.random());
        return Lpad(num.toString(),"0",n);
    }

    public static String getRandomLong(int n){
        Long num=new Long(1);
        for(int i=1;i<n;i++){
            num=num*10;
        }
        num=(long)(num*Math.random());
        return Lpad(num.toString(),"0",n);
    }

    public static  String Lpad(String i,String fillStr,int lenght){
        if(i==null){
            return null;
        }
        String num=i;
        while(num.length()<lenght){
            num=fillStr+num;
        }
        return num;
    }


    public static String getContainIdByMac(){
        try{
            String hostName = InetAddress.getLocalHost().getHostName();
            InetAddress inetAddress=InetAddress.getLocalHost();
            byte[] mac= NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
            if(mac==null||mac.length<1){
                LOGGER.error("网络连接已经断开，无法获取MAC地址");
            }else{
                LOGGER.info("网络已经连接，MAC地址为："+ Arrays.toString(mac));
            }
            LOGGER.info("获取到主机名称为："+hostName+",mac地址字节数组"+ Arrays.toString(mac)+"，InetAddress："+inetAddress.toString());

            StringBuilder stringBuilder=new StringBuilder();
            for(int i=0;i<mac.length;i++){
                if(i!=0){
                    stringBuilder.append("-");
                }
                String s=Integer.toHexString(mac[i]&0xff);
                stringBuilder.append(s.length()==1?0+s:s);
            }
            LOGGER.info("mac地址为："+stringBuilder.toString().toUpperCase().trim());

            return stringBuilder.toString().toUpperCase().trim();

        }catch (Exception e){
            LOGGER.error("获取MAC地址并转换Id异常："+e.getMessage(),e);
            String macEx="20-16-D8-6E-B0-C4";
            LOGGER.error("返回默认的mac地址："+macEx);
            return macEx;
        }
    }
}

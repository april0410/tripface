package com.icbc.bcss.trip.face.operationtools.utils;

import com.icbc.api.internal.util.codec.Base64;
import com.icbc.api.internal.util.internal.util.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.enums.PropertiesType;
import com.icbc.bcss.trip.face.operationtools.requestmodel.BcssMemberFeatureQuerymemberinfoRequestV1;

import org.omg.CORBA.OBJ_ADAPTER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    public static String  operateFile(Map<String,Object> tempMap){
        if(tempMap==null||tempMap.isEmpty()){
            LOGGER.error("读取动态文件的参数，不存在");
            return null;
        }

        String code= (String) tempMap.get(MemberConstants.RETURN_CODE);
        String source= (String) tempMap.get(MemberConstants.PROPERTIES_VALUE_SOURCE);
        if(MemberConstants.COMMON_SUCCESS.equals(code)&& PropertiesType.PROPERTIES_VALUE_SOURCE_DEFINE.getCode().equals(source)){
            LOGGER.info("读取动态文件的参数，该参数存在");
//            return 0;

            String exceptionTime= (String) tempMap.get(MemberConstants.PROPERTIES_VALUE);
            return  exceptionTime;
//            String[] startEnd=exceptionTime.split("")

        }else{
            LOGGER.error("读取动态文件的参数，不存在");
            return null;
        }
//        return 0;
    }


    /*
     * 子函数-记录错误时间在动态文件中并返回错误结果--应该所有线程后才调用
     * */
    @Deprecated
    public static Map<String, Object> returnFailResult(Map<String, Object> param, String code, String msg) {
        String operType = (String) param.get("operType");
        String clientTransNo = (String) param.get("clientcTransNo");

        return CommonMapReturn.constructMapWithClientTransNo(code, msg, clientTransNo);

    }

    /*
     * 子函数-记录任何时间
     * */
    public static Map<String, Object> recordErrorTimeAndReturnResult(Map<String, Object> param, String code, String msg) {

        String operType = (String) param.get("operType");
        String clientTransNo = (String) param.get("clientcTransNo");
        String recordDate= (String) param.get("endDate");
        //处理时间保存
        if("1".equals(operType)){
            LOGGER.error("当前拉取人脸的操作标识是:"+operType+"--获取注册人脸明细,需要更新当前时间到define.properties");
            DefinePropertiesFileUtils.getInstance().commonSaveParam("bcss.init.register.time",recordDate);
        }else if("7".equals(operType)){
            LOGGER.error("当前拉取人脸的操作标识是:"+operType+"--获取删除人脸明细,需要更新当前时间到define.properties");
            DefinePropertiesFileUtils.getInstance().commonSaveParam("bcss.init.delete.time",recordDate);
        }else {
            LOGGER.error("当前拉取人脸的操作标识是:"+operType+",不需要更新当前时间到define.properties");
        }

        return CommonMapReturn.constructMapWithClientTransNo(code, msg, clientTransNo);
    }



    //获取所有文件--除去文件夹外
    public static Vector<String> getAllFile(String path,String suffixName){
        Vector<String> vector=new Vector<>();
        File file=new File(path);

        File[] subFile=file.listFiles();

        for(int i=0;i<subFile.length;i++){
            boolean flag=subFile[i].isDirectory(); //是否为目录
            if(!flag){
                String fileName=subFile[i].getName();
                //判断是否jpg格式图片
//                if(fileName.trim().toLowerCase().endsWith(".jpg")){
                if(fileName.trim().toLowerCase().endsWith(suffixName)){
                    vector.add(fileName);
                }else {
                   LOGGER.error("当前获取的图片名称为："+fileName+" ,不属于"+suffixName+"格式的图片，因而注册到本地数据库");
                }
            }else {//如果为目录
                LOGGER.error("当前获取的是目录，名称为："+subFile[i].getName());
            }
        }
        return vector;
    }

    //写入人脸数量
    @Deprecated
    public static void saveRequestAPIFailWithMap(Map<String, Object> bizContent, String type, boolean fileCoverFlag, String fullName){

        String text= JSONObject.toJSONString(bizContent)+System.getProperty("line.separator");//加上换行符
        int res=writeFile(fullName, text,type,fileCoverFlag);

        if(res==0){
            LOGGER.debug("请求人脸数据失败（类型为："+type+"），记录上送参数成功--");
        }else {
            LOGGER.debug("请求人脸数据失败（类型为："+type+"）,记录上送参数失败--");
        }
    }

    //写入人脸数量
    @Deprecated
    public static void saveRequestAPIFailWithBiz(BcssMemberFeatureQuerymemberinfoRequestV1.BcssMemberFeatureQuerymemberinfoRequestBizV1 bizContent, String type, boolean fileCoverFlag,String fullName){

        String text= JSONObject.toJSONString(bizContent)+System.getProperty("line.separator");//加上换行符
        int res=writeFile(fullName, text,type,fileCoverFlag);

        if(res==0){
            LOGGER.debug("请求人脸数据失败（类型为："+type+"），记录上送参数成功--");
        }else {
            LOGGER.debug("请求人脸数据失败（类型为："+type+"）,记录上送参数失败--");
        }
    }

    /*
     * 写入文件内容--不保留旧内容
     * fileCoverFlag--false 不保留，true保留旧内容
     * */
    @Deprecated
    public static int writeFile(String fileName,String context,String type,boolean fileCoverFlag){
        //判断路径的文件是否存在
        //判断是否创建文件夹
        String path=fileName.substring(0,fileName.lastIndexOf(File.separator));
        File dirsFile = new File(path); //自动获取外部存储的路径+自定义文件夹
        if (!dirsFile.exists()){
            dirsFile.mkdirs();
        }

        File realFile=new File(fileName);
        if(!realFile.exists()){
            try {
                dirsFile.createNewFile();
            }catch (Exception e){
                LOGGER.error("创建文件："+fileName+"失败"+e.getMessage());
                LOGGER.error(e.toString(),e);
                return 1;
            }
        }


        Writer out = null;
        try {
            out = new FileWriter(fileName,fileCoverFlag);
//            System.getProperty("line.separator");
            out.write(context);
            out.close();
            return 0;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            LOGGER.error("写入"+type+"请求数据发生异常，请查看");
            LOGGER.error( e.toString(),e);
            return 1;
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException ex) {
                LOGGER.error("写入"+type+"请求数据,准备关闭写入流时发生异常，请查看");
                LOGGER.error(ex.toString(),ex);
            }
        }
    }


    /*
     * 读入文件内容--不保留旧内容
     * fileCoverFlag--false 不保留，true保留旧内容
     * */
    public static List<String> readFile(String fileName){
        String temString = "";
        BufferedReader reader = null;
        StringBuilder stringBuilder=new StringBuilder();
        try {
            File file = new File(fileName);
            if(!file.exists()){
                LOGGER.error("读入的文件不存在");
                return null;
            }
            reader = new BufferedReader(new FileReader(file));
            //循环读取
//			temString = reader.readLine();
//			StringBuilder stringBuilder=new StringBuilder();
            List<String> readList=new ArrayList<>();
            while ((temString=reader.readLine())!=null){
                readList.add(temString);
            }
            LOGGER.info("遍历记录失败请求的文件结束");

            reader.close();
            return readList;
        } catch (Exception e) {
//            e.printStackTrace();
            LOGGER.error("读取文件发生异常");
            LOGGER.error(e.toString(),e);
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("准备关闭 读取的文件发生异常");
                LOGGER.error(e.toString(),e);
            }

        }
    }


    /*
     * 写入文件内容--不保留旧内容
     * fileCoverFlag--false 不保留，true保留旧内容
     * */
    public static int writeFileByList(String fileName,List<String> context,String type,boolean fileCoverFlag){
        Writer out = null;
        try {
            out = new FileWriter(fileName,fileCoverFlag);
            String s=System.getProperty("line.separator");
            for(String str:context){
                out.write(str+s);
            }

            out.close();
            return 0;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            LOGGER.error("写入"+type+"请求数据发生异常，请查看");
            LOGGER.error(e.toString(),e);
            return 1;
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                LOGGER.error("写入"+type+"请求数据,准备关闭写入流时发生异常，请查看");
                LOGGER.error(e.toString(),e);
            }
        }
    }


}

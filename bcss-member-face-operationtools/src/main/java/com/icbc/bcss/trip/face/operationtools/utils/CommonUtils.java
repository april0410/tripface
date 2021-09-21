package com.icbc.bcss.trip.face.operationtools.utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class CommonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private static CommonUtils INSTANCE=new CommonUtils();
    //默认构造函数
    public CommonUtils(){}
    //单例
    public static CommonUtils getInstance(){
        return INSTANCE;
    }


    //初始化---创建文件夹
    public  void createFilePath(String localimageFullPath) {
        File file = new File(localimageFullPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    //初始化---创建文件
    public  boolean createFile(String FullPathName) {
        try{
            File file = new File(FullPathName);
            if (!file.exists()) {
                file.createNewFile();
            }
            return true;
        }catch (Exception e){
            LOGGER.error("创建文件失败"+e.getMessage());
            LOGGER.error(e.toString(),e);
            return false;
        }

    }


    //删除本地指定目录下指定尾缀所有文件
    public int deleteFileBySuff(String localimageFullPath,String suffixName){
        Vector<String> listname=FileUtils.getAllFile(localimageFullPath,suffixName);
        int count=0;
        for(int i=0;i<listname.size();i++){
            File temp=new File(localimageFullPath+ File.separator+listname.get(i));
            boolean delRes=temp.delete();
            if(delRes==true){
                LOGGER.info(listname.get(i)+"删除成功");
                count=count+1;
            }else{
                LOGGER.error(listname.get(i)+"删除失败");
            }
        }
        if(count==listname.size()){
            LOGGER.error("成功删除文件数量为："+count+",全部文件数量为："+listname.size()+"，两者数量一致，全部删除成功");
            return 0;
        }else {
            LOGGER.error("成功删除文件数量为："+count+",全部文件数量为："+listname.size()+"，两者数量不一致，部分删除失败");
            return 1;
        }

    }



    /*图片转base64*/
    public  String imageToBase64(String imapath){
        InputStream in=null;
        byte[] data=null;
        try {
            in=new FileInputStream(imapath);
            data=new byte[in.available()];
            in.read(data);
            in.close();

            String base64=Base64.getEncoder().encodeToString(data);
//            com.icbc.api.internal.util.codec.Base64.encodeBase64String()
            return base64;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("图片转换base64异常，具体原因为："+e.getMessage());
            LOGGER.error(e.toString(),e);
            return null;
        }
    }

    /*图片转base64*/
    public  boolean base64ToImage(String imagePath,String base64String){
        byte[] byteString = Base64.getDecoder().decode(base64String); // Base64解码

        String fullName=imagePath;
        OutputStream out = null;
        try {
            out = new FileOutputStream(fullName);
            for (int i = 0; i < byteString.length; ++i) {
                if (byteString[i] < 0) {// 调整异常数据
                    byteString[i] += 256;
                }
            }
            out.write(byteString);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
//                e.printStackTrace();
            LOGGER.error("******base64字符串转为图片异常(FileNotFoundException)*****"+e.getMessage());
            LOGGER.error( e.toString(),e);
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            LOGGER.error("******base64字符串转为图片异常(IOException)*****"+e.getMessage());
            LOGGER.error( e.toString(),e);
            return false;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (Exception e) {
                LOGGER.error("******刷新写入缓冲区时发生异常(IOException)*****"+e);
                LOGGER.error( e.toString(),e);
                return false;
            }
        }
        return true;
    }

    public Map<String,Object> isvalidJSON(String temp){
        if(temp==null||temp.isEmpty()){
            return null;
        }

        try {
            return JSONObject.parseObject(temp,Map.class);
        }catch (Exception e){
            LOGGER.error("字符串转换Map类型异常："+e.getMessage());
            LOGGER.error(e.toString(),e);
            return null;
        }
    }

}

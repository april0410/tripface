package com.icbc.bcss.trip.face.operationtools.utils;

import com.alibaba.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.responsemodel.BcssMemberFeatureQuerymemberinfoResponseV1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class RecordMemberinfoUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordMemberinfoUtils.class);

    @Autowired
    private PropertyPlaceholder environment;

    private static RecordMemberinfoUtils singleton=null;
    private RecordMemberinfoUtils(){};

    public static RecordMemberinfoUtils getInstance(){
        synchronized (RecordMemberinfoUtils.class){
            if(singleton == null){
                singleton = new RecordMemberinfoUtils();
            }
        }
        return singleton;
    }


    //直接调用方法判断是否注册人脸，如果不是会员行业，则返回（不记录错误的信息），否则进行下一步，不设定返回值
    //如果是会员行业，则根据注册结果（注册失败--增加记录当前的memNo和MemCardNo，如果注册成功，删除当前memNo和MemCardNo（如果有的情况下））
    @Deprecated
    public void judgeMemberIndustry(BcssMemberFeatureQuerymemberinfoResponseV1.DataInfo tempDataInfo, String compareIndustry, String registerCode) {
        String tempIndustry = tempDataInfo.getIndustry();
        if (tempIndustry == null || tempIndustry.isEmpty()) {
            LOGGER.error("judgeMemberIndustry--云端获取的行业标识为空");
            return;
        }

        if (compareIndustry == null || compareIndustry.isEmpty()) {
            LOGGER.error("judgeMemberIndustry--传入的本地行业标识为空");
            return;
        }

        Map tempMap = dtoConvertMap(tempDataInfo);
        if (tempMap == null || tempMap.isEmpty()) {
            LOGGER.error("judgeMemberIndustry--dto转换map异常，读取到的memNo和memCardNo存在为空的情况");
            return;
        } else {
            recordMemNoAndMemCard(tempMap, registerCode);
        }
    }

    /*dto转换map*/
    private Map<String, String> dtoConvertMap(BcssMemberFeatureQuerymemberinfoResponseV1.DataInfo tempDataInfo) {
        try {
            String js = JSONObject.toJSONString(tempDataInfo);
            JSONObject temRes = JSONObject.parseObject(js);
            String memNo = temRes.getString("memNo");
            String detail = temRes.getString("memDetail1");
            JSONObject memDetail=JSONObject.parseObject(detail);
            String memCardNo = memDetail.getString("memCardNo");

            LOGGER.info("dtoConvertMap--读取到的memNo和memCardNo分别为：" + memNo + " , " + memCardNo);

            if (memCardNo == null || memCardNo.isEmpty() || memNo == null || memNo.isEmpty()) {
                LOGGER.error("dtoConvertMap--读取到的memNo和memCardNo存在为空的情况");
                return null;
            }

            HashMap<String, String> result = new HashMap<>();
            result.put("memNo", memNo);
            result.put("memCardNo", memCardNo);
            return result;
        } catch (Exception e) {
            LOGGER.error("dtoConvertMap--datainfo转换成json异常");
            LOGGER.error(e.getMessage(), e);
            return null;
        }

    }

    //子函数--如果注册失败，就记录卡号和memNO的对应关系
    //注册成功，则剔除文件中的记录
    private void recordMemNoAndMemCard(Map<String, Object> context, String registerCode) {
        String path = environment.getProperty("face.registerFail.recordFile");
        if (path == null || path.isEmpty()) {
            String filePacket = "selfRcord";
            String name = "failRecord.txt";

            path = filePacket + File.separator + name;
            LOGGER.error("recordMemNoAndMemCard--采用代码中的默认的相对路径：" + path);
        }

        String memNo = (String) context.get("memNo");
        String memCardNo = (String) context.get("memCardNo");

        String prePath = System.getProperty("user.dir") + File.separator;
        if (registerCode.equals("0")) {//注册成功

            File log = new File(prePath + path);
            if (!log.exists()) {
                log.getParentFile().mkdirs();
                return;
            }

            String temString = "";
            BufferedReader reader = null;
            StringBuilder stringBuilder = new StringBuilder();
            try {
                File file = new File(prePath + path);
                reader = new BufferedReader(new FileReader(file));
                //循环读取
                while ((temString = reader.readLine()) != null) {
                    LOGGER.warn("重新注册--当前读取到的字符串为：" + temString + ",传入的字符串为：" + memNo);
                    if (temString.contains(memNo)) {
                        LOGGER.debug("两个字符串相等，删除文档中的记录");

                    } else {
                        stringBuilder.append(temString).append(System.lineSeparator());
                    }

                }
                LOGGER.info("遍历结束失败注册名单结束");

                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage(), e);
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    LOGGER.error(e.getMessage(), e);
                }
            }

            //第一次注册成功（ 没有找到旧记录），直接返回
            if (stringBuilder == null || stringBuilder.length() < 1) {
                return;
            }

            /*写入*/
            LOGGER.info("开始重新写入名单");
            Writer out = null;
            try {
                out = new FileWriter(prePath + path);

                out.write(stringBuilder.toString());
                out.flush();
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LOGGER.error("重新写入注册失败的会员卡时发生异常，请查看");
                LOGGER.error(e.getMessage(), e);
            } finally {
                try {
                    if (out != null)
                        out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    LOGGER.error("关闭写入流发生异常，请查看");
                    LOGGER.error(e.getMessage(), e);
                }
            }
            LOGGER.error("写入名单操作结束");
            LOGGER.warn("recordMemNoAndMemCard--注册成功情况处理完毕");
            //////////////////////////////////
        } else {  //注册失败的处理
            try {
                File log = new File(prePath + path);
                if (!log.exists()) {
                    log.getParentFile().mkdirs();
                    log.createNewFile();
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }


            String systemLineseparator = System.lineSeparator();
            LOGGER.debug("换行符为：" + systemLineseparator);
            //用+详解
            String record = memNo + MemberConstants.DEFAULT_CONNECTION_SYMBOL + memCardNo + systemLineseparator;

            File log = new File(prePath + path);
            Writer out = null;
            try {
                out = new FileWriter(log, true);

                out.write(record);
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                LOGGER.error("写入注册失败的会员卡时发生异常，请查看");
                LOGGER.error(e.getMessage(), e);
            } finally {
                try {
                    if (out != null)
                        out.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    LOGGER.error("关闭写入流发生异常，请查看");
                    LOGGER.error(e.getMessage(), e);
                }
            }
            LOGGER.warn("recordMemNoAndMemCard--注册失败情况处理完毕");
        }

        return;
    }
}

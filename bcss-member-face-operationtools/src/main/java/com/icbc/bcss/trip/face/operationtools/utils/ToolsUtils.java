package com.icbc.bcss.trip.face.operationtools.utils;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Deprecated
public class ToolsUtils {
    /**
     * 发送报文
     *
     * @param reqUrl   请求url
     * @param reqData  报文数据
     * @param encoding 报文编码
     * @return
     */
    public static String postStr(String reqUrl, String reqData, String encoding) throws IOException {

        HttpURLConnection conn = null;
        ByteArrayOutputStream bs = null;
        OutputStream os = null;
        InputStream is = null;

        byte[] bytes = new byte[1024];
        try {
            conn = (HttpURLConnection) new URL(reqUrl).openConnection();
            if (conn != null) {
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setInstanceFollowRedirects(false);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.connect();
                byte[] b = reqData.getBytes(encoding);
                if (b != null) {
                    os = conn.getOutputStream();
                    os.write(b, 0, b.length);
                    os.flush();
                }

                is = conn.getInputStream();
                bs = new ByteArrayOutputStream();

                int len = 0;
                if (is != null) {
                    while ((len = is.read(bytes)) != -1) {
                        if (bs != null) {
                            bs.write(bytes, 0, len);
                        }
                    }
                }

                return new String(bs.toByteArray(), encoding);
            }
            return "";
        } catch (Exception e) {
            System.out.println("调用异常");
            throw e;
        } finally {
            IOUtils.closeQuietly(bs);
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}

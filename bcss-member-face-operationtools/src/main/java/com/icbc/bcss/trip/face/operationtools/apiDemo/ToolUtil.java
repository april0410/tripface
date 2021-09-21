package com.icbc.bcss.trip.face.operationtools.apiDemo;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Deprecated
public class ToolUtil {

    /**
     * AES加密
     *
     * @param secretKey
     * @param text
     * @return
     */
    public static String encrypt(String secretKey, String text) {
        try {
            if (StringUtils.isBlank(text)) {
                return text;
            }
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(secretKey.getBytes());
            keygen.init(128, secureRandom);
            SecretKey originalKey = keygen.generateKey();
            byte[] raw = originalKey.getEncoded();
            SecretKey cypherKey = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(1, cypherKey);
            byte[] byteEncode = text.getBytes("utf-8");
            byte[] byteAES = cipher.doFinal(byteEncode);
            return new BASE64Encoder().encode(byteAES);
        } catch (Exception e) {
            System.out.println("AES加密出现错误");
            throw new IllegalArgumentException("encode error.");
        }
    }

    /**
     * AES解密
     *
     * @param secretKey
     * @param text
     * @return
     */
    public static String decrypt(String secretKey, String text) {
        try {
            if (StringUtils.isBlank(text)) {
                return text;
            }
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(secretKey.getBytes());
            keygen.init(128, secureRandom);
            SecretKey originalKey = keygen.generateKey();
            byte[] raw = originalKey.getEncoded();
            SecretKey cypherKey = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(2, cypherKey);
            byte[] byteContent = (new BASE64Decoder()).decodeBuffer(text);
            byte[] byteDecode = cipher.doFinal(byteContent);
            return new String(byteDecode, "utf-8");
        } catch (Exception e) {
            throw new IllegalArgumentException("decode error.");
        }
    }

    /**
     * 请求签名
     *
     * @param params
     * @param secret
     * @return
     * @throws Exception
     */
    public static String makeSig(Map<String, Object> params, String secret) throws Exception {
        String sig;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), mac.getAlgorithm());
            mac.init(secretKey);

            List<String> keys = new ArrayList();
            StringBuilder authInfo = new StringBuilder();
            Iterator<String> it = params.keySet().iterator();
            while (it.hasNext()) {
                String key = String.valueOf(it.next());
                keys.add(key);
            }
            Collections.sort(keys);
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                if (!"sign".equals(key)) {
                    String value = params.get(key).toString().replace("\\", "");
                    authInfo.append(buildKeyValue(key, value, false));
                    if (i < keys.size() - 1) {
                        authInfo.append("&");
                    }
                }
            }
            byte[] hash = mac.doFinal(authInfo.toString().getBytes("UTF-8"));
            sig = new BASE64Encoder().encode(hash);
        } catch (NoSuchAlgorithmException e) {
            throw e;
        }

        return sig;
    }

    /**
     * kev=value对应
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

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

package com.share.manage.utils;

import java.io.*;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;


import javax.crypto.Cipher;


import com.share.manage.model.Keystore;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;

/**
 * 加密
 */


public class SecurityUtil {
    public static final String KEY_ALGORITHM = "RSA";
    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    /**
     *  * RSA最大加密明文大小
     *  
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     *  * RSA最大解密密文大小
     *  
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    //获得公钥字符串
    public static String getPublicKeyStr(Map<String, Object> keyMap) throws Exception {
        //获得map中的公钥对象 转为key对象
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }


    //获得私钥字符串
    public static String getPrivateKeyStr(Map<String, Object> keyMap) throws Exception {
        //获得map中的私钥对象 转为key对象
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        //编码返回字符串
        return encryptBASE64(key.getEncoded());
    }


    //获取公钥
    public static PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    //获取私钥
    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }


    //解码返回byte
    public static byte[] decryptBASE64(String key) throws Exception {
        return Base64.decode(key);
    }


    //编码返回字符串
    public static String encryptBASE64(byte[] key) throws Exception {
        return Base64.encode(key);
    }


    //***************************签名和验证*******************************
    public static byte[] sign(byte[] data, String privateKeyStr) throws Exception {
        PrivateKey priK = getPrivateKey(privateKeyStr);
        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        sig.initSign(priK);
        sig.update(data);
        return sig.sign();
    }

    public static boolean verify(byte[] data, byte[] sign, String publicKeyStr) throws Exception {
        PublicKey pubK = getPublicKey(publicKeyStr);
        Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
        sig.initVerify(pubK);
        sig.update(data);
        return sig.verify(sign);
    }

    //************************加密解密**************************
    public static String encrypt(byte[] plainText, String publicKeyStr) throws Exception {
        PublicKey publicKey = getPublicKey(publicKeyStr);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM); //获取RSA加密方式
        cipher.init(Cipher.ENCRYPT_MODE, publicKey); //用密钥初始化
        int inputLen = plainText.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        int i = 0;
        byte[] cache;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(plainText, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(plainText);
           }
            out.write(cache,0,cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptText = out.toByteArray();
        out.close();
        return encryptBASE64(encryptText);
    }

    public static String decrypt(String encryptText, String privateKeyStr) throws Exception {
        byte[] decode = decryptBASE64(encryptText);
        PrivateKey privateKey = getPrivateKey(privateKeyStr);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int inputLen = decode.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(decode);
            } else {
                cache = cipher.doFinal(decode);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] plainText = out.toByteArray();
        out.close();
        return new String(plainText);
    }


    public static void main(String[] args) {
        try {
       /* Map<String, Object> keyMap;
        String cipherText;
        String input = "123456";
        try {
            keyMap = initKey();
            String publicKey = getPublicKeyStr(keyMap);
            System.out.println("公钥------------------");
            System.out.println(publicKey);
            String privateKey = getPrivateKeyStr(keyMap);
            System.out.println("私钥------------------");
            System.out.println(privateKey);

            System.out.println("测试可行性-------------------");
            System.out.println("明文=======" + input);
           // System.out.println(new String(input.getBytes("utf-8")));
            cipherText = encrypt(input.getBytes(), publicKey);
            //加密后的东西 
            System.out.println("密文========" + cipherText);
            //开始解密 
            String plainText = decrypt(cipherText, privateKey);
            System.out.println("解密后明文===== " + plainText);
            System.out.println("验证签名-----------");

            String str = "888";
            System.out.println("\n原文:" + str);
            byte[] signature = sign(str.getBytes(), privateKey);
            Map<String , String> map = new HashMap<>();
            String sign = Base64.encode(signature);
            map.put("sign",sign);
            map.put("pwd",cipherText);
            boolean status = verify(str.getBytes(), Base64.decode(map.get("sign")), publicKey);
            System.out.println("验证情况：" + status);*/
            String s = encryptByuser("123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator
                .getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    public static String encryptByuser(String str) throws Exception {
        Map<String, Object> keyMap;
        Map<String,Keystore> map = new HashedMap();
        Keystore keyStore = new Keystore();
        keyMap = initKey();
        String publicKey = getPublicKeyStr(keyMap);
        String privateKey = getPrivateKeyStr(keyMap);
        keyStore.setPrivateKey(privateKey);
        keyStore.setPublicKey(publicKey);
        map.put("key",keyStore);
        JSONObject jsonObject = JSONObject.fromObject(map);
        /*System.out.println("json加密：----------------"+Base64.encode(jsonObject.toString().getBytes()));
        //将加密字符串保存在文件中
        FileOutputStream fos = new FileOutputStream("F:/keyStore.key",true);
        //true表示在文件末尾追加
        fos.write(Base64.encode(jsonObject.toString().getBytes()).getBytes());
        fos.close();*/
        String pathname = "F:/keyStore.key"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
        File filename = new File(pathname); // 要读取以上路径的input。txt文件
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename)); // 建立一个输入流对象reader
        BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
        StringBuffer stringBuffer = new StringBuffer();
        String line = "";
        line = br.readLine();
        while (line != null) {
            line = br.readLine(); // 一次读入一行数据
            stringBuffer.append(line);
        }
        //System.out.println(stringBuffer);
        System.out.println(new String(Base64.decode(stringBuffer.toString().getBytes())));
        //System.out.println("json解密：----------------"+new String(Base64.decode(Base64.encode(jsonObject.toString().getBytes()))));
        return encrypt(str.getBytes(), publicKey);
    }

    public static String decryptByuser(String str,String userid) throws Exception {
        Map<String, Object> keyMap;
        keyMap = initKey();
        String privateKey = getPrivateKeyStr(keyMap);
        return decrypt(str, privateKey);
    }

}


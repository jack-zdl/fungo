package com.game.common.util;


import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化；
 */
public class AesUtil {
	/*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    private String sKey = "7435562899123452";//key，加密的key
    private String ivParameter = "7601230125462244";//偏移量,4*4矩阵
    private static AesUtil instance = null;

    private AesUtil() {

    }

    public static AesUtil getInstance() {
        if (instance == null) {
            instance = new AesUtil();
        }
        return instance;
    }

    /**
     * 加密
     * @param encData 要加密的内容
     * @param secretKey 加密的秘钥
     * @param vector 偏移量
     * @return 加密后的字符串
     * @throws Exception
     */
    public String encrypt(String encData ,String secretKey,String vector) throws Exception {
        if(secretKey == null) {
            return null;
        }
        if(secretKey.length() != 16) {
            return null;
        }
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = secretKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(vector.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(encData.getBytes("utf-8"));
        return new BASE64Encoder().encode(encrypted).replaceAll("\r|\n", "");// 此处使用BASE64做转码。
    }


    /**
     * 加密
     * @param sSrc 要加密的内容
     * @return 加密后的字符串
     * @throws Exception
     */
    public String encrypt(String sSrc) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return new BASE64Encoder().encode(encrypted).replaceAll("\r|\n", "");// 此处使用BASE64做转码。
    }
    
   
    /**
     * 加密
     * @param sSrc
     * @param key
     * @return
     * @throws Exception
     */
    public String encrypt(String sSrc,String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
        return new BASE64Encoder().encode(encrypted).replaceAll("\r|\n", "");// 此处使用BASE64做转码。
    }

    /**
     * 解密
     * @param sSrc 要解密的内容
     * @return 解密后的字符串
     * @throws Exception
     */
    public String decrypt(String sSrc) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, "utf-8");
        } catch (Exception ex) {
            return null;
        }
    }
    
  /**
   * 解密
   * @param sSrc
   * @param key
   * @return
   * @throws Exception
   */
    public String decrypt(String sSrc,String key) throws Exception {
        try {
            byte[] raw = key.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, "utf-8");
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 解密
     * @param sSrc 要解密的内容
     * @param key 解密要秘钥
     * @param ivs 偏移量
     * @return 解密后的字符串
     * @throws Exception
     */
    public String decrypt(String sSrc,String key,String ivs) throws Exception {
        try {
            byte[] raw = key.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, "utf-8");
        } catch (Exception ex) {
            return null;
        }
    }
    public static void main(String[] args) {
    	try {
//			System.out.println(AesUtil.getInstance().encrypt("s善良的放假啊是的"));//加密
//			System.out.println(AesUtil.getInstance().decrypt("f94Wcn0PRySFefPkhztBpOcF1BfJ7SEY0cAIQo3T88E="));//解密
//    		String encrypt = AesUtil.getInstance().encrypt("{\"gameId\":\"123\",\"packageName\":\"456\"}");
//    		System.out.println(encrypt);
//    		String decrypt = AesUtil.getInstance().decrypt(encrypt);
//    		System.out.println(decrypt);
    		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}

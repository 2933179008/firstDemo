package com.tbl.modules.platform.util;


 
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

import java.security.Key;

/**
 * 接口权限验证加密算法
 */
public class EncryptUtil {
    // 密钥 长度不得小于24
    private final static String secretKey = "/**@@fj!as>lfka@#sf%**@j!as>lfka@#sf%!as>lfka@#sf%**@@f@fj**@@fj!as>lfka@#sf%" ;
    // 向量 可有可无 终端后台也要约定
    private final static String iv = "01234567";
    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8";

    /**
     * 3DES加密
     *
     * @param plainText
     *            普通文本
     * @return
     * @throws Exception
     */
    public static String encryptAsc(String plainText) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey .getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance( "desede");
        deskey = keyfactory.generateSecret( spec);

        Cipher cipher = Cipher.getInstance( "desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec( iv.getBytes());
        cipher.init(Cipher. ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal( plainText.getBytes( encoding));
        Base64 base64=new Base64();  
        return new String(base64.encode(encryptData));
    }

    /**
     * 3DES解密
     *
     * @param encryptText
     *            加密文本
     * @return
     * @throws Exception
     */
    public static String deCiphering(String encryptText) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec( secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance( "desede");
        deskey = keyfactory. generateSecret( spec);
        Cipher cipher = Cipher.getInstance( "desede/CBC/PKCS5Padding" );
        IvParameterSpec ips = new IvParameterSpec( iv.getBytes());
        cipher. init(Cipher. DECRYPT_MODE, deskey, ips);
        Base64 base64=new Base64();
        byte[] decryptData = cipher. doFinal((byte[]) base64.decode(encryptText ));
        return new String( decryptData, encoding);
    }
}

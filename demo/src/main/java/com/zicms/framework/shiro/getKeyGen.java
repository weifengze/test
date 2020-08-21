package com.zicms.framework.shiro;

import org.apache.shiro.codec.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

/**
 * @program: bjjh-demo
 * @Description: getKeyGen
 * @Author: 魏丰泽
 * @Create: 2020-07-28 16:33
 **/
public class getKeyGen {

    public static void main(String[] args) {
        KeyGenerator keygen = null;
        try {
            keygen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        SecretKey deskey = keygen.generateKey();
        System.out.println(Base64.encodeToString(deskey.getEncoded()));
    }
}

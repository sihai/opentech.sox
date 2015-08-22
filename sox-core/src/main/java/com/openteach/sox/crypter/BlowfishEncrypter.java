/*
 * openteach inc.
 * sox
 */
package com.openteach.sox.crypter;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Blowfish加解密的方法
 * @author sihai
 *
 */
public class BlowfishEncrypter implements Encrypter {
	
	private static String          CIPHER_KEY;
    private static String          CIPHER_NAME   = "Blowfish/CFB8/NoPadding";
    private static String          KEY_SPEC_NAME = "Blowfish";
    private static SecretKeySpec   secretKeySpec = null;
    private static IvParameterSpec ivParameterSpec = null;
    
    // 
    private Cipher          enCipher;
    private Cipher          deCipher;
    
    public BlowfishEncrypter() {}

    /**
     * 初始化
     */
    public void initialize(String privateKey) {
    	try {
    		if(StringUtils.isBlank(privateKey)) {
    			throw new IllegalArgumentException("privateKey can not blank");
    		}
    		CIPHER_KEY = privateKey;
        	secretKeySpec = new SecretKeySpec(CIPHER_KEY.getBytes(), KEY_SPEC_NAME);
        	ivParameterSpec = new IvParameterSpec((DigestUtils.md5Hex(CIPHER_KEY).substring(0, 8)).getBytes());
        	
            enCipher = Cipher.getInstance(CIPHER_NAME);
            deCipher = Cipher.getInstance(CIPHER_NAME);
            enCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            deCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("initialize BlowfishEncrypter failed", e);
        } catch (NoSuchPaddingException e) {
        	throw new RuntimeException("initialize BlowfishEncrypter failed", e);
        } catch (NoSuchAlgorithmException e) {
        	throw new RuntimeException("initialize BlowfishEncrypter failed", e);
        } catch (InvalidKeyException e) {
        	throw new RuntimeException("initialize BlowfishEncrypter failed", e);
        } catch (InvalidAlgorithmParameterException e) {
        	throw new RuntimeException("initialize BlowfishEncrypter failed", e);
        }
    }
    
    public String getByteString(byte[] b) {
        StringBuffer s = new StringBuffer(b.length * 3);
        for (int i = 0; i < b.length; i++) {
            s.append("|" + Integer.toHexString(b[i] & 0xff).toUpperCase());
        }
        return s.toString();
    }

    /**
     * 加密的方法
     * @param str
     * @return
     */
    @Override
    public String encrypt(String str) {
        String result = null;

        if (StringUtils.isNotBlank(str)) {
            try {
                byte[] enc = enCipher.doFinal(str.getBytes());
                result = new String(Base64.encodeBase64(enc));; 
            } catch (IllegalBlockSizeException e) {
                throw new RuntimeException("encrypt: " + str + "faile", e);
            } catch (BadPaddingException e) {
                throw new RuntimeException("encrypt: " + str + "faile", e);
            }
        }

        return result;
    }

    /**
     * 解密的方法
     * @param str
     * @return
     */
    public String decrypt(String str) {
        String result = null;

        if (StringUtils.isNotBlank(str)) {
            try {
                result = new String(deCipher.doFinal(Base64.decodeBase64(str.getBytes())));
            } catch (IllegalBlockSizeException e) {
                throw new RuntimeException("encrypt: " + str + "faile", e);
            } catch (BadPaddingException e) {
                throw new RuntimeException("encrypt: " + str + "faile", e);
            }
        }

        return result;
    }
    
    public byte[] decrypt(byte[] tar) {
        String src = this.decrypt(new String(tar));
        return src.getBytes();
    }

    public void destroy() {}

    public byte[] encrypt(byte[] src) {
        String tar = this.encrypt(new String(src));
        return tar.getBytes();
    }
}
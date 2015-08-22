package com.openteach.sox.crypter;

/**
 * 加解密接口
 * @author sihai
 *
 */
public interface Encrypter {

	/**
	 * 
	 * @param src
	 * @return
	 */
	byte[] encrypt(byte[] src);
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	String encrypt(String str);
	
	/**
	 * 
	 * @param tar
	 * @return
	 */
	byte[] decrypt(byte[] tar);
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	String decrypt(String str);
	
	/**
	 * 
	 */
	void destroy();
}

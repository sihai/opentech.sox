package com.openteach.sox.common.configuration;


/**
 * Session 属性配置
 * @author sihai
 *
 */
public class SessionAttributeConfiguration {
	
	/**
	 * 是否是系统属性
	 */
	private boolean isSystemAttribute;

	/**
	 * 
	 */
	private Class<?> clazz;

	/**
	 * cookie path
	 */
	private String cookiePath;
	
	/**
	 * cookie http access only
	 */
	private boolean httpOnly;
	
	/**
	 * 是否启用压缩
	 */
	private boolean isCompress;
	
	/**
	 * 是否启用加密
	 */
	private boolean isEncrypt;
	
	/**
	 * 存储key
	 */
	private String key;

	public boolean isSystemAttribute() {
		return isSystemAttribute;
	}

	public void setSystemAttribute(boolean isSystemAttribute) {
		this.isSystemAttribute = isSystemAttribute;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public String getCookiePath() {
		return cookiePath;
	}

	public void setCookiePath(String cookiePath) {
		this.cookiePath = cookiePath;
	}

	public boolean isHttpOnly() {
		return httpOnly;
	}

	public void setHttpOnly(boolean httpOnly) {
		this.httpOnly = httpOnly;
	}

	public boolean isCompress() {
		return isCompress;
	}

	public void setCompress(boolean isCompress) {
		this.isCompress = isCompress;
	}

	public boolean isEncrypt() {
		return isEncrypt;
	}

	public void setEncrypt(boolean isEncrypt) {
		this.isEncrypt = isEncrypt;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
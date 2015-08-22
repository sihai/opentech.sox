package com.openteach.sox.common.configuration;

import java.util.HashMap;
import java.util.Map;

import com.openteach.sox.common.storage.StorageType;

/**
 * session 配置
 * @author sihai
 *
 */
public class SessionConfiguration {

	/**
	 * cookie 域名
	 */
	private String domain;

	/**
	 * 存储类型
	 */
	private StorageType storageType;
	
	/**
	 * 有效期,单位秒
	 */
	private int timeout;
	
	/**
	 * 加解密方法
	 */
	private String encrypterAlgorithm;

	/**
	 * 加密的密钥
	 */
	private String privateKey;

	/**
	 * configuraiton map
	 */
	private Map<String, SessionAttributeConfiguration> sacmap = new HashMap<String, SessionAttributeConfiguration>();
	
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public String getEncrypterAlgorithm() {
		return encrypterAlgorithm;
	}

	public void setEncrypterAlgorithm(String encrypterAlgorithm) {
		this.encrypterAlgorithm = encrypterAlgorithm;
	}
	
	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
	public StorageType getStorageType() {
		return storageType;
	}

	public void setStorageType(StorageType storageType) {
		this.storageType = storageType;
	}
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	/**
	 * put
	 * @param key
	 * @param sac
	 */
	public void put(String key, SessionAttributeConfiguration sac) {
		sacmap.put(key, sac);
	}
	
	/**
	 * get
	 * @param key
	 * @return
	 */
	public SessionAttributeConfiguration get(String key) {
		return sacmap.get(key);
	}
	
	/**
	 * get all
	 * @return
	 */
	public Map<String, SessionAttributeConfiguration> getAll() {
		return sacmap;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean containsKey(String key) {
		return sacmap.containsKey(key);
	}
}

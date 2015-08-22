/*
 * ihome inc.
 * soc
 */
package com.openteach.sox.session;

import com.openteach.sox.common.storage.StorageType;
import com.openteach.sox.store.SessionStorage;


/**
 * Session的管理器
 * @author sihai
 *
 */
public interface SessionManager {
	
	/**
	 * 设置它所关联的session
	 * @param session
	 */
	void setSession(SoxSession session);
	
	/**
	 * 取得manager所管理的session
	 * @return
	 */
	SoxSession getSession();
	
	/**
	 * 保存
	 *
	 */
	void save();
	
	/**
	 * 使过期的session值失效
	 *invalidate
	 */
	void invalidate();
	
	/**
	 * 根据store的类型返回实现的store
	 * @param type
	 * @return
	 */
	SessionStorage getSessionStore(StorageType type);
	
	/**
	 * 读取属性值
	 * @param key
	 * @return
	 */
	Object getAttribute(String key);
	
	
	/**
	 * 判断要求的key是否存在于配置文件之中
	 * @param key
	 * @return
	 */
	boolean isExistKey(String key);
}

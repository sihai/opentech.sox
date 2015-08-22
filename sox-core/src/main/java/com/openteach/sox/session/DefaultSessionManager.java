/*
 * opentech inc.
 * sox
 */
package com.openteach.sox.session;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.openteach.sox.common.configuration.SessionAttributeConfiguration;
import com.openteach.sox.common.configuration.SessionConfiguration;
import com.openteach.sox.common.storage.StorageType;
import com.openteach.sox.store.SessionStorage;

/**
 * 默认的SoxSessionManager的实现
 * @author sihai
 *
 */
public class DefaultSessionManager implements SessionManager {

	private final Log logger  = LogFactory.getLog(getClass());
	
	// session存储的设置
    private Map<StorageType, SessionStorage> sm = new HashMap<StorageType, SessionStorage>();
    
    // 属性值的配置
    private SessionConfiguration sc;
    
	// 当前管理的session
	ThreadLocal<SoxSession> threadLocal = new ThreadLocal<SoxSession>();
	
	/**
	 * 初始化
	 * @param sc
	 */
	public void initialize(SessionConfiguration sc) {
		this.sc = sc;
	}
	
	@Override
	public void setSession(SoxSession session) {
		threadLocal.set(session);
	}

	@Override
	public SoxSession getSession() {
		return threadLocal.get();
	}

	@Override
	public void save() {
		
		logger.debug("start save session attribute!");
		SoxSession session = getSession();
		Map<String, Boolean> change = session.getChangedMarkMap();		
		
		for(Iterator<Entry<String, Boolean>> iterator = change.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Boolean> e = iterator.next();
			String key = e.getKey();
			if(e.getValue().booleanValue()) {
				
				//取得该KEY配置的STORE, 此处需防止没有取到STORE
		        StorageType type = sc.getStorageType();
		        SessionStorage store = getStorage(type);
		        
				//取得它的STORE, 保存它
				SessionAttributeConfiguration config = sc.get(key);
				
				if (null == config) {
					continue;
				}
			
		        store.save(this.getSession().getHttpContext(), key);
			}			
		}		
	}

	@Override
	public void invalidate() {
		// 遍历配置，再转给保存的STORE处理
		for (Iterator<Entry<String, SessionAttributeConfiguration>> iterator = sc.getAll().entrySet().iterator(); iterator.hasNext();) {
			Entry<String, SessionAttributeConfiguration> e = iterator.next();
			SessionAttributeConfiguration config = e.getValue();
			getStorage(sc.getStorageType()).invalidate(e.getKey());
		}	
	}

	@Override
	public SessionStorage getSessionStore(StorageType type) {
		return sm.get(type);
	}
	
	@Override
	public Object getAttribute(String key) {
		//取得该key配置的storage
        StorageType storageType = sc.getStorageType();
        SessionStorage storage = getStorage(storageType);

        return storage.getAttribute(key);
	}

	@Override
	public boolean isExistKey(String key) {
		return sc.containsKey(key);
	}
	
	/**
	 * 
	 * @param storageType
	 * @return
	 */
	private SessionStorage getStorage(StorageType storageType) {
		
		SessionStorage storage = this.getSession().getSessionStorage(storageType);

        //如果当前环境上下文中没有STORE，则新建一个。
        if(null == storage) {
            storage = (SessionStorage)SessionStoreFactory.newInstance(storageType);
            storage.initialize(this.getSession(), this.sc);
            getSession().setSessionStorage(storageType, storage);
        }
		return storage;
	}
}

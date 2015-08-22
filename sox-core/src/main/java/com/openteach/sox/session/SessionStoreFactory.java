/*
 * ihome inc.
 * soc
 */
package com.openteach.sox.session;

import com.openteach.sox.common.storage.StorageType;
import com.openteach.sox.store.CookieStore;
import com.openteach.sox.store.SessionStorage;

/**
 * Session Store Factory
 * @author sihai
 *
 */
public abstract class SessionStoreFactory {
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static SessionStorage newInstance(StorageType type) {
		if(StorageType.COOKIE == type) {
			return new CookieStore();
		} else {
			throw new IllegalArgumentException(String.format("Unknown session store type: %s", type));
		}
	}
}

/*
 * ihome inc.
 * soc
 */
package com.openteach.sox.session;

import com.openteach.sox.common.configuration.SessionConfiguration;

/**
 * 单例SocSessionManager工厂
 * @author sihai
 *
 */
public abstract class SingletonSessionManagerFactory {
	
	/**
	 * 
	 */
	private static SessionManager sessionManager;
	
	/**
	 * 
	 */
	private static boolean initialized = false;
	
	/**
	 * 
	 * @param sc
	 */
	public synchronized static void initialize(SessionConfiguration sc) {
		if(initialized) {
			return;
		}
		sessionManager = new DefaultSessionManager();
		((DefaultSessionManager)sessionManager).initialize(sc);
		initialized = true;
	}
	
	/**
	 * 返回单例的实例
	 */
	public static SessionManager getInstance() {
		return sessionManager;
	}
}

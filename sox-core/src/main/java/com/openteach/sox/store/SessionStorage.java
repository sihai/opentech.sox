/*
 * ihome inc.
 * soc
 */
package com.openteach.sox.store;

import com.openteach.sox.SoxHttpContext;
import com.openteach.sox.common.configuration.SessionConfiguration;
import com.openteach.sox.session.SoxSession;

/**
 * 用来持久化session attribute的存储。
 * @author sihai
 *
 */
public interface SessionStorage {
	
	String SESSION = "session";
	String CONFIG = "config";
	
	/**
     * 初始化每个STORE的环境,包括两个部分：1、实时数据 2、配置
     *
     * @param session
     * @param sc
     */
    void initialize(SoxSession session, SessionConfiguration sc);
    
	/**
     * 根据单个KEY返回值
     *
     * @param key
     *
     * @return
     */
    Object getAttribute(String key);
    
    /**
     * 将值写回存储
     * @param httpContext
     */
    void save(SoxHttpContext httpContext);

    /**
     * 将指定的值写回存储
     * @param httpContext
     * @param key
     */
    void save(SoxHttpContext httpContext, String key);
    
    /**
     * 过期失效
     * @param key
     */
    void invalidate(String key);
    
    /**
     * 过期失效全部
     */
    void invalidate();
}

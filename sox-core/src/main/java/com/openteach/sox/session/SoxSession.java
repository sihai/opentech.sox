/*
 * ihome inc.
 * soc
 */
package com.openteach.sox.session;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.apache.commons.lang.StringUtils;

import com.openteach.sox.SoxHttpContext;
import com.openteach.sox.common.SoxConstants;
import com.openteach.sox.common.configuration.SystemAttribute;
import com.openteach.sox.common.storage.StorageType;
import com.openteach.sox.crypter.Encrypter;
import com.openteach.sox.store.SessionStorage;

/**
 * session的实现，对未经过包装的方法，一律代理给原来的SESSION，主要实现取得属性的方法
 * 
 * @author sihai
 *
 */
public class SoxSession implements HttpSession {
	
	private String             		sessionId           = null;
    private SoxHttpContext     		httpContext         = null;
	private Map<String, Boolean>	changedMarkMap      = new HashMap<String, Boolean>(); 					// 属性修改标记
    private Map<String, Object>     attributeMap        = new HashMap<String, Object>(); 					// session属性的内部保存
    private long               		createTime			= System.currentTimeMillis();						// 
    private int                		maxInactiveInterval = SoxConstants.DEFAULT_LIFE_CYCLE;

    // session的管理器
    SessionManager 	sessionManager  = null;
    // 
    Map<StorageType, SessionStorage> sessionStorageMap = new HashMap<StorageType, SessionStorage>();
    //
    private boolean isCommited = false;
    
    /**
     * 构造函数，以便将通过系统获得容器的SESSION
     *
     * @param httpContext
     */
    public SoxSession(SoxHttpContext httpContext) {
    	
        this.httpContext = httpContext;

        //初始化 SESSIONID        
        sessionId = (String) getAttribute(SystemAttribute.SOX_SID.name());
        
        if (StringUtils.isBlank(sessionId)) {
            sessionId = UUID.randomUUID().toString();
            //并写入COOKIE中
            setAttribute(SystemAttribute.SOX_SID.name(), sessionId);
        }
    }
	    
	@Override
	public long getCreationTime() {
		return this.createTime;
	}

	@Override
	public String getId() {
		return sessionId;
	}

	@Override
	public long getLastAccessedTime() {
		// FIXME
		return this.createTime;
	}

	@Override
	public ServletContext getServletContext() {
		return httpContext.getServletContext();
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		this.maxInactiveInterval = interval;
	}

	@Override
	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		throw new UnsupportedOperationException("No longer supported method: getSessionContext");
	}

	@Override
	public Object getAttribute(String name) {
		
		if (name == null) {
            return null;
        }

        //如果该属性已经存在，则直接返回        
        if (attributeMap.containsKey(name)) {
        	return attributeMap.get(name);
		}

        //如果不存在 则分两种情况：
        //1、session配置中是否存在  若不存在 则返回NULL
        if (!getSessionManager().isExistKey(name)) {
            return null;
        }

        //2、如果存在并改变了值 Why? 被删除了?
        if (changedMarkMap.containsKey(name)) {
			return null;
		}
        
        //3、如果配置中存在，则试图从指定的配置中读取该值, 并存入被包装的session中
        Object attribute = getSessionManager().getAttribute(name);

        if (attribute != null) {
        	attributeMap.put(name, attribute);
            return attribute;
        }

        return null;

	}

	@Override
	public Object getValue(String name) {
		return getAttribute(name);
	}

	@Override
	public Enumeration getAttributeNames() {
		return new IteratorEnumeration(attributeMap.keySet().iterator());
	}

	@Override
	public String[] getValueNames() {
		
		List<String> names = new ArrayList<String>();
        for (Enumeration e = getAttributeNames(); e.hasMoreElements();) {
            names.add((String)e.nextElement());
        }
        return (String[]) names.toArray( new String[names.size()]);
	}

	@Override
	public void setAttribute(String name, Object value) {
		attributeMap.put(name, value);
        changedMarkMap.put(name, Boolean.TRUE);
	}

	@Override
	public void putValue(String name, Object value) {
		setAttribute(name, value);
	}

	@Override
	public void removeAttribute(String name) {
		attributeMap.remove(name);
        changedMarkMap.put(name, Boolean.TRUE);
	}

	@Override
	public void removeValue(String name) {
		removeAttribute(name);
	}
	
	/**
	 * 将SESSION失效，如果是COOKIE的话，将所有时间配置为-1的全部失效
	 */
	@Override
	public void invalidate() {
		getSessionManager().invalidate();
	}
	
	public void invalidate(String key) {
		getSessionManager().invalidate();
	}

	@Override
	public boolean isNew() {
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	private SessionManager getSessionManager() {
        if (sessionManager == null) {
        	sessionManager = (SessionManager) SingletonSessionManagerFactory.getInstance();
        	sessionManager.setSession(this);
        }

        return this.sessionManager;
    }
	
	/**
	 * 
	 * @return
	 */
	public SoxHttpContext getHttpContext() {
		return httpContext;
	}
	
	/**
	 * 
	 * @return
	 */
	public HttpServletRequest getRequest() {
		return httpContext.getRequest();
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public SessionStorage getSessionStorage(StorageType type) {
		return sessionStorageMap.get(type);
	}
	
	/**
	 * 
	 * @param type
	 * @param storage
	 */
	public void setSessionStorage(StorageType type, SessionStorage storage) {
		sessionStorageMap.put(type, storage);
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<String, Boolean> getChangedMarkMap() {
		return changedMarkMap;
	}
	
	/**
	 * 
	 * @param httpContext
	 */
	public void setHttpContext(SoxHttpContext httpContext) {
		this.httpContext = httpContext;
	}
	
	/**
	 * 
	 */
	public void commit() {
		if(!isCommited) {
			isCommited = true;
			this.setAttribute(SystemAttribute.SOX_LAST_VISIT_TIME.name(), System.currentTimeMillis());
			getSessionManager().save();
		}
	}
}

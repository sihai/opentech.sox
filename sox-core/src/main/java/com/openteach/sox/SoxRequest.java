/*
 * ihome inc.
 * soc
 */
package com.openteach.sox;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.openteach.sox.session.SoxSession;

/**
 * 
 * @author sihai
 *
 */
public class SoxRequest extends HttpServletRequestWrapper {

	private SoxSession soxSession = null;
	
	/**
	 * 
	 * @param httpContext
	 */
	public SoxRequest(SoxHttpContext httpContext) {
		super(httpContext.getRequest());
		soxSession = new SoxSession(httpContext);
	}
	
	/**
     * 返回经过封装的SESSION
     */
    public HttpSession getSession() {
        return soxSession;
    }
    
    /**
     * 返回经过封装的SESSION
     */
    public HttpSession getSession(boolean create) {
    	return soxSession;
    }
}
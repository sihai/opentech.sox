/*
 * openteach inc.
 * sox
 */
package com.openteach.sox;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.openteach.sox.crypter.Encrypter;

/**
 * 上下文
 * @author sihai
 *
 */
public class SoxHttpContext {

	private final HttpServletRequest  request;			//	HTTP 请求
    private final HttpServletResponse response;			//  HTTP 响应
    private final ServletContext      servletContext; 	//	Servlet上下文
    private final Encrypter 		  encrypter;		//  加解密

    /**
     * 
     * @param request
     * @param response
     * @param servletContext
     * @param encrypter
     */
    public SoxHttpContext(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext, Encrypter encrypter) {
    	this.request  = request;
        this.response = response;
        this.servletContext  = servletContext;
        this.encrypter = encrypter;
    }

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public Encrypter getEncrypter() {
		return encrypter;
	}
}
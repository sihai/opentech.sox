/*
 * openteach inc.
 * sox
 */
package com.openteach.sox.store;

import java.security.SecureRandom;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.openteach.sox.SoxHttpContext;
import com.openteach.sox.common.SoxConstants;
import com.openteach.sox.common.configuration.SessionAttributeConfiguration;
import com.openteach.sox.common.configuration.SessionConfiguration;
import com.openteach.sox.session.SoxSession;
import com.openteach.sox.utils.Decoder;
import com.openteach.sox.utils.Enecoder;
import com.openteach.sox.utils.Null;

/**
 * 基于cookie的session持久化实现
 * @author sihai
 *
 */
public class CookieStore implements SessionStorage {

	private final Log logger  = LogFactory.getLog(getClass());
	
	public static final Random  random = new SecureRandom();
	
	private Map<String, String> cookieMap         = new HashMap<String, String>();
    private Map<String, Object> cacheAttributeMap = new HashMap<String, Object>();
	
    private SoxSession session;
    
    private SessionConfiguration sc;
    
	/**
     * 初始化COOKIE的值对
     */
    private void initialize(Cookie[] cookies) {
        //将cookies的值拆成key->value
    	if (cookies == null) {
			return ;
		}
        for (int i = 0; i < cookies.length; i++) {
            String name  = cookies[i].getName();
            String value = cookies[i].getValue();
            cookieMap.put(name, value);
        }
    }
    
    /**
     * 
     */
    @Override
    public void initialize(SoxSession session, SessionConfiguration sc) {
        this.session  = session;
        this.sc = sc;
        this.initialize(session.getRequest().getCookies());
    }
    
	@Override
	public Object getAttribute(String key) {
		
		 //先试图从cache存储空间中返回
        Object value = (String) cacheAttributeMap.get(key);
        if (null != value) {
        	if(!(value instanceof Null)) {
        		return value;
        	}
        }
        
        SessionAttributeConfiguration sac = sc.get(key);
        if (null == sac) {
            logger.warn(String.format("There is no configuration for key:%s", key));
            return null;
        }

        //进一步从COOKIE中解析
        String cookieValue = (String) cookieMap.get(key);

        //如果值不空的话，则根据配置文件进行解析
        if (null != cookieValue) {
            Object v = parseValue(sac, cookieValue);
            // cache
            cacheAttributeMap.put(key, null == v ? Null.getInstance() : v);
            return v;
        }

        return null;
	}

	@Override
	public void save(SoxHttpContext httpContext) {
		this.save(httpContext, null);
	}

	@Override
	public void save(SoxHttpContext httpContext, String key) {
		//根据整个配置与SESSION的值重写
        if (null != key) {
        	this.saveSingleKey(httpContext, key);
        } else {
            for(String k : sc.getAll().keySet()) {
            	this.saveSingleKey(httpContext, k);
            }
        }
	}

	@Override
	public void invalidate(String key) {
        if (sc.containsKey(key)) {
            session.setAttribute(key, null);
        }
	}
	
	@Override
	public void invalidate() {
        for(String key : sc.getAll().keySet()) {
        	invalidate(key);
        }
	}

	/**
     * 根据配置文件，决定如何将cookie中的字符串或字节流解析成合适的对象
     *
     * @param sac
     * @param value
     *
     * @return
     */
    private Object parseValue(SessionAttributeConfiguration sac, String value) {
        
    	String result = null;
        if (StringUtils.isBlank(value)) {
            return result;
        }
        
        result = Decoder.decode(value);

        //如果是加密过的
        if (sac.isEncrypt()) {
            result = session.getHttpContext().getEncrypter().decrypt(result);
            if ((result != null) && (result.length() > 6)) {
                //去掉BASE64时增加的头
            	result = result.substring(6);
            }
        } else {
            result = new String(Base64.decodeBase64(result));
        }
        
        if(sac.isSystemAttribute()) {
        	return result;
        } else {
        	return JSON.parseObject(result, sac.getClazz());
        }
    }
    
    /**
     * 保存单个的值到cookie中去
     *
     * @param context
     * @param key
     *
     */
    private void saveSingleKey(SoxHttpContext httpContext, String key) {
    	if(sc.containsKey(key)) {
    		HttpServletResponse response = httpContext.getResponse();
    		//分析该KEY是否是组合KEY
    		this.saveCookie(response, sc.get(key));
    	}
    }
    
    /**
     * @param response
     * @param sac
     *
     */
    private void saveCookie(HttpServletResponse response, SessionAttributeConfiguration sac) {

    	String cookieName = sac.getKey();
        //得到cookie的值
        String cookieValue = getCookieValue(sac);
        Cookie cookie = null;

        if (cookieValue != null) {
            cookie = new Cookie(cookieName, cookieValue);
        } else {
            cookie = new Cookie(cookieName, "");
        }

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("cookie name: %s, cookie value: %s", cookieName, cookieValue));
        }

        //设置一些COOKIE的其它相关属性
        cookie.setPath(sac.getCookiePath());
        
        cookie.setMaxAge(sc.getTimeout());

        String domain = sc.getDomain();
        if (StringUtils.isNotBlank(domain)) {
            cookie.setDomain(domain);
        }

        if(sac.isHttpOnly()) {
        	cookie.setHttpOnly(true);
        }
        response.addCookie(cookie);
    }
    
    /**
     * 构造cookie value
     *
     * @return
     */
    protected String getCookieValue(SessionAttributeConfiguration sac) {
        //得到需要保存的COOKIE的值,此处需进一步考虑是否需要序列化
        String attributeValue = null;
        Object obj = session.getAttribute(sac.getKey());

        if(obj == null) {
            return null;
        }

        if(!sac.isSystemAttribute()) {
        	attributeValue = JSON.toJSONString(obj);
        } else {
        	attributeValue = String.valueOf(obj);
        }

        if (StringUtils.isBlank(attributeValue)) {
            return attributeValue;
        }

        if (sac.isEncrypt()) {
            //add encryption here
            attributeValue = random.nextInt(10) + "a#b$^" + attributeValue;
            attributeValue = session.getHttpContext().getEncrypter().encrypt(attributeValue);
        } else {
        	attributeValue = new String(Base64.encodeBase64(attributeValue.getBytes()));
        }
        
        attributeValue = Enecoder.encode(attributeValue);

        return attributeValue;
    }
    
    private static String getCookieExpries(Cookie cookie) {
    	
    	String result = null;
        int maxAge = cookie.getMaxAge();
        if (maxAge > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.SECOND, maxAge);
            result = SoxConstants.DATE_FORMAT.format(calendar);
        } else { // maxAge == 0
            result = SoxConstants.DATE_FORMAT.format(0); // maxAge为0时表示需要删除该cookie，因此将时间设为最小时间，即1970年1月1日
        }

        return result;
    }
}

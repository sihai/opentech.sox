/*
 * openteach inc.
 * sox
 */
package com.openteach.sox.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

import com.openteach.sox.common.SoxConstants;

/**
 * 包装了一下
 * @author sihai
 *
 */
public class Enecoder {
	
	/**
	 * 
	 * @param aStr
	 * @return
	 */
	public static String encode(String v) {
		
        String result = null;
        if (StringUtils.isNotBlank(v)) {
	        try {
	            result = URLEncoder.encode(v, SoxConstants.DEFAULT_CHARSET);
	        } catch (UnsupportedEncodingException e) {
	        	throw new RuntimeException("url encode failed", e);
	        }
        }

        return result;
    }
}

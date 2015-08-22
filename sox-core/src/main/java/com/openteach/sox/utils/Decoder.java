/*
 * openteach inc.
 * sox
 */
package com.openteach.sox.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang.StringUtils;

import com.openteach.sox.common.SoxConstants;

/**
 * 包装了一下
 * @author sihai
 *
 */
public class Decoder {
	
	/**
	 * 
	 * @param encoded
	 * @return
	 */
	public static String decode(String encoded) {
		
        String result = null;
        if (StringUtils.isNotBlank(encoded)) {
            try {
            	result = URLDecoder.decode(encoded, SoxConstants.DEFAULT_CHARSET);
            } catch (UnsupportedEncodingException e) {
            	throw new RuntimeException("url decode failed", e);
            }
        }

        return result;
    }
}

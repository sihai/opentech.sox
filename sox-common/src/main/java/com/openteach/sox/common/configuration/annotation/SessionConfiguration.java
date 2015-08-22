package com.openteach.sox.common.configuration.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.openteach.sox.common.SoxConstants;

/**
 * 
 * @author sihai
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SessionConfiguration {

	/**
	 * <b>是否开启压缩</b><p>
	 * 默认开启
	 * @return
	 */
	boolean compress() default true;
	
	/**
	 * 是否加密
	 * @return
	 */
	boolean encrypt() default true;
	
	/**
	 * 存储key
	 */
	String key();
	
	/**
	 * cookie path
	 * @return
	 */
	String cookiePath() default SoxConstants.DEFAULT_COOKIE_PATH;
	
	/**
	 * cookie http access only
	 * @return
	 */
	boolean httpOnly() default true;
}

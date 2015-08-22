package com.openteach.sox.common.configuration.annotation;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;

import com.openteach.sox.common.configuration.SessionAttributeConfiguration;

import eu.infomas.annotation.AnnotationDetector;
import eu.infomas.annotation.AnnotationDetector.Reporter;
import eu.infomas.annotation.AnnotationDetector.TypeReporter;

/**
 * 注解扫描
 * @author sihai
 *
 */
public class AnnontationConfigParser {
	
	/**
	 * 扫描
	 */
	public com.openteach.sox.common.configuration.SessionConfiguration scan(String ... packageNames) {
		
		final com.openteach.sox.common.configuration.SessionConfiguration sc = new com.openteach.sox.common.configuration.SessionConfiguration();
		
		final Reporter reporter = new TypeReporter() {

			@SuppressWarnings("unchecked")
            @Override
            public Class<? extends Annotation>[] annotations() {
                return new Class[] { SessionConfiguration.class };
            }

            @Override
            public void reportTypeAnnotation(Class<? extends Annotation> annotation, String className) {
                parse(sc, className);
            }

		};
		
		final AnnotationDetector cf = new AnnotationDetector(reporter);
		
		try {
			cf.detect(packageNames);
			return sc;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * @param sc
	 * @param className
	 */
	private void parse(com.openteach.sox.common.configuration.SessionConfiguration sc, String className) {
		try {
			
			Class clazz = Class.forName(className);
			SessionConfiguration sca = (SessionConfiguration)clazz.getAnnotation(SessionConfiguration.class);
			if(null == sca) {
				throw new RuntimeException("Class:" + className + "not annotationed");
			}
			
			SessionAttributeConfiguration sac = new SessionAttributeConfiguration();
			sac.setClazz(clazz);
			sac.setCompress(sca.compress());
			sac.setCookiePath(sca.cookiePath());
			sac.setEncrypt(sca.encrypt());
			sac.setHttpOnly(sca.httpOnly());
			sac.setKey(sca.key());
			
			sc.put(sac.getKey(), sac);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Parse class: " + className, e);
		}
	}
}

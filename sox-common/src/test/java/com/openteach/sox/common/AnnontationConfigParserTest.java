package com.openteach.sox.common;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.openteach.sox.common.configuration.annotation.AnnontationConfigParser;

/**
 * 
 * @author sihai
 *
 */
public class AnnontationConfigParserTest {
	
	/**
	 * 
	 */
	@Test
	public void test() {
		AnnontationConfigParser acp = new AnnontationConfigParser();
		com.openteach.sox.common.configuration.SessionConfiguration sc = acp.scan();
		System.out.println(JSON.toJSONString(sc));
	}
}

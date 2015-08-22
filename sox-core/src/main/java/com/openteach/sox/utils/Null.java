/*
 * openteach inc.
 * sox
 */
package com.openteach.sox.utils;

/**
 * 表示nul的对象
 * @author sihai
 *
 */
public class Null {
	
	private static Null instance = new Null();
	
	/**
	 * 
	 */
	private Null(){}
	
	/**
	 * 
	 * @return
	 */
	public static Null getInstance() {
		return instance;
	}
}

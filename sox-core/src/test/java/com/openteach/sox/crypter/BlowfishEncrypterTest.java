package com.openteach.sox.crypter;

import org.junit.Test;

/**
 * 
 * @author sihai
 *
 */
public class BlowfishEncrypterTest {

	@Test
	public void test() {
		BlowfishEncrypter bfe = new BlowfishEncrypter();
		bfe.initialize("taobao123");
		System.out.println(bfe.encrypt("2c33f0a22995043954bf0fc9eb7dbbab"));
	}

}

package com.openteach.sox.crypter;

/**
 * 
 * @author sihai
 *
 */
public class EncrypterFactory {
	
	/**
	 * 
	 * @return
	 */
	public static Encrypter newEncrypter(EncrypterAlgorithm algorithm, String privateKey) {
		if(EncrypterAlgorithm.BLOWFISH == algorithm) {
			BlowfishEncrypter e = new BlowfishEncrypter();
			e.initialize(privateKey);
			return e;
		} else {
			throw new IllegalArgumentException("not supported algorithm: " + algorithm);
		}
	}
}

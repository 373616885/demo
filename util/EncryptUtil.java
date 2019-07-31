package com.qin.strem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.springframework.util.DigestUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncryptUtil {
	// 公钥字符串
	private static final String PUBLIC_KEY_STRING = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCaC8Zogizl3ddFwh2u3VMtAMkhOOvHz54HWDZKNl2CTOX+DgSuHDnwfD6lKEGyLsZgJ2WvJsPVg/pKXtXfE0GrvOGLZpdQMnNoQMnVNLhmW8ZMjgaaJikeCOVrGISE3RjrbfcRI0U/glnGMbAdlnRl3tbctrEqAMGdwjjhMV/LfwIDAQAB";
	// 私钥字符串
	private static final String PRIVATE_KEY_STRING = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAJoLxmiCLOXd10XCHa7dUy0AySE468fPngdYNko2XYJM5f4OBK4cOfB8PqUoQbIuxmAnZa8mw9WD+kpe1d8TQau84Ytml1Ayc2hAydU0uGZbxkyOBpomKR4I5WsYhITdGOtt9xEjRT+CWcYxsB2WdGXe1ty2sSoAwZ3COOExX8t/AgMBAAECgYEAjw14Rvz8LMsCaqmFXynxX81m+g8eBgPrwO5OHES4OZSn0HG8LuPPemAm3MCxoYKGfiyX5TueiN1yxTWkbvA2/oxpnKX1/6tzcE9u3b71ZCp+d66EmNf9SYjWAIG3duEcJfAr6UtxXhpzanM9r92tCuQ/Nepm+AcR2sISdP58OaECQQDk+H7kA2j49UX2lJdDhdSGT49A2/pmzj5yoKnWJigV2cncjSjmk6n38Xfv4CLDLCsWyRicAEYozHtmi1iewINLAkEArDsNSSzBi9LZ8elFFdjcGiJq77u61YWjLKx2L19rf4pYWbr3Xa1jWde4ajFrhnP9h/2A5XwqXhUg7aJMSHNEHQJBAONIf7yaXfLylTZFHTmbePCxvMNnGNI/2+Ew9iVqztOR0jypqnXnEgramHF2DUurZkJB77m60+nfwIZzv84lzq0CQAu96uX2OyvBMEKh2KfopPjTJ984OUCdX8aH63bHVpjXYbacXIjybTeO0W/L9ryv47UFv2fl0y2e2yPYUMMPuLECQQCj9D8tECbrnlG28njfCmO90Zc3LdwEJMhFmu+J8B/Annxnq8KiMQNcjPgkTAk8VStQ6OPlyInZl43y5P6lm+2F";
	
	public static void main(String[] args) throws Exception {
		//加密字符串
		String message = "df723820";
		String messageEn = encrypt(message);
		System.out.println(message + "  加密后的字符串为:" + messageEn);
		String messageDe = decrypt(messageEn);
		System.out.println("还原后的字符串为:" + messageDe);
	}
	
	/**
	 * RSA公钥加密
	 */
	public static String encrypt(String str) throws Exception {
		// base64编码的公钥
		byte[] decoded = Base64.getDecoder().decode(PUBLIC_KEY_STRING);
		
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
				.generatePublic(new X509EncodedKeySpec(decoded));
		// RSA加密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		String outStr = Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
		return outStr;
	}
	
	
	/** 
	 * RSA私钥解密
	 */  
	public static String decrypt(String str) throws Exception{
		//64位解码加密后的字符串
		byte[] inputByte = Base64.getDecoder().decode(str.getBytes("UTF-8"));
		//base64编码的私钥
		byte[] decoded = Base64.getDecoder().decode(PRIVATE_KEY_STRING);  
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));  
		//RSA解密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		String outStr = new String(cipher.doFinal(inputByte));
		return outStr;
	}
	

	/**
	 * 1.随机生成密钥对
	 */
	public static void genKeyPair() throws NoSuchAlgorithmException {
		// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		// 初始化密钥对生成器，密钥大小为96-1024位
		keyPairGen.initialize(1024, new SecureRandom());
		// 生成一个密钥对，保存在keyPair中
		KeyPair keyPair = keyPairGen.generateKeyPair();
		// 得到私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// 得到公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		// 得到公钥字符串
		String publicKeyString = new String(Base64.getEncoder().encode(publicKey.getEncoded()));
		// 得到私钥字符串
		String privateKeyString = new String(Base64.getEncoder().encode((privateKey.getEncoded())));
	}

	/**
	 * MD5原生的
	 */
	// 盐，用于混交md5
	private static final String slat = "&%5123***&&%%$$#@";

	/**
	 * MD5 是对字节数组进行加密得到 二进制数据 为了方便就转换成16进制的字符串 MD5 将二进制数据转换成16进制的字符串通常得到32位的编码
	 * 不少地方会用到16位的编码 16 位实际上是从 32 位字符串中取中间的第 9 位到第 24 位的部分 java 语言：String md5_16 =
	 * md5_32.substring(8, 24);
	 */
	public static String md5(String dataStr) {
		try {
			dataStr = dataStr + slat;
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(dataStr.getBytes("UTF8"));
			byte s[] = m.digest();
			String result = "";
			for (int i = 0; i < s.length; i++) {
				result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * spring 封装的md5加密
	 */
	public static String springMd5(String dataStr) {
		dataStr = dataStr + slat;
		return DigestUtils.md5DigestAsHex(dataStr.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * base64 加密 jdk1.7 BASE64Encoder
	 */
	public static String base64Encoder(String dataStr) {
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(dataStr.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * base64 解密 jdk1.7 BASE64Encoder
	 */
	public static String base64Decoder(String dataStr) {
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] bytes = null;
			bytes = decoder.decodeBuffer(dataStr);
			return new String(bytes, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * base64 加密 jdk1.8 java.util.Base64
	 */
	public static String base64EncoderByJdk8(String dataStr) {
		return Base64.getEncoder().encodeToString(dataStr.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * base64 解密 jdk1.8 java.util.Base64
	 */
	public static String base64DecoderByJdk8(String dataStr) {
		byte[] bytes = Base64.getDecoder().decode(dataStr);
		return new String(bytes, StandardCharsets.UTF_8);
	}

}

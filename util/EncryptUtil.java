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
	// ��Կ�ַ���
	private static final String PUBLIC_KEY_STRING = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCaC8Zogizl3ddFwh2u3VMtAMkhOOvHz54HWDZKNl2CTOX+DgSuHDnwfD6lKEGyLsZgJ2WvJsPVg/pKXtXfE0GrvOGLZpdQMnNoQMnVNLhmW8ZMjgaaJikeCOVrGISE3RjrbfcRI0U/glnGMbAdlnRl3tbctrEqAMGdwjjhMV/LfwIDAQAB";
	// ˽Կ�ַ���
	private static final String PRIVATE_KEY_STRING = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAJoLxmiCLOXd10XCHa7dUy0AySE468fPngdYNko2XYJM5f4OBK4cOfB8PqUoQbIuxmAnZa8mw9WD+kpe1d8TQau84Ytml1Ayc2hAydU0uGZbxkyOBpomKR4I5WsYhITdGOtt9xEjRT+CWcYxsB2WdGXe1ty2sSoAwZ3COOExX8t/AgMBAAECgYEAjw14Rvz8LMsCaqmFXynxX81m+g8eBgPrwO5OHES4OZSn0HG8LuPPemAm3MCxoYKGfiyX5TueiN1yxTWkbvA2/oxpnKX1/6tzcE9u3b71ZCp+d66EmNf9SYjWAIG3duEcJfAr6UtxXhpzanM9r92tCuQ/Nepm+AcR2sISdP58OaECQQDk+H7kA2j49UX2lJdDhdSGT49A2/pmzj5yoKnWJigV2cncjSjmk6n38Xfv4CLDLCsWyRicAEYozHtmi1iewINLAkEArDsNSSzBi9LZ8elFFdjcGiJq77u61YWjLKx2L19rf4pYWbr3Xa1jWde4ajFrhnP9h/2A5XwqXhUg7aJMSHNEHQJBAONIf7yaXfLylTZFHTmbePCxvMNnGNI/2+Ew9iVqztOR0jypqnXnEgramHF2DUurZkJB77m60+nfwIZzv84lzq0CQAu96uX2OyvBMEKh2KfopPjTJ984OUCdX8aH63bHVpjXYbacXIjybTeO0W/L9ryv47UFv2fl0y2e2yPYUMMPuLECQQCj9D8tECbrnlG28njfCmO90Zc3LdwEJMhFmu+J8B/Annxnq8KiMQNcjPgkTAk8VStQ6OPlyInZl43y5P6lm+2F";
	
	public static void main(String[] args) throws Exception {
		//�����ַ���
		String message = "df723820";
		String messageEn = encrypt(message);
		System.out.println(message + "  ���ܺ���ַ���Ϊ:" + messageEn);
		String messageDe = decrypt(messageEn);
		System.out.println("��ԭ����ַ���Ϊ:" + messageDe);
	}
	
	/**
	 * RSA��Կ����
	 */
	public static String encrypt(String str) throws Exception {
		// base64����Ĺ�Կ
		byte[] decoded = Base64.getDecoder().decode(PUBLIC_KEY_STRING);
		
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
				.generatePublic(new X509EncodedKeySpec(decoded));
		// RSA����
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		String outStr = Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
		return outStr;
	}
	
	
	/** 
	 * RSA˽Կ����
	 */  
	public static String decrypt(String str) throws Exception{
		//64λ������ܺ���ַ���
		byte[] inputByte = Base64.getDecoder().decode(str.getBytes("UTF-8"));
		//base64�����˽Կ
		byte[] decoded = Base64.getDecoder().decode(PRIVATE_KEY_STRING);  
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));  
		//RSA����
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		String outStr = new String(cipher.doFinal(inputByte));
		return outStr;
	}
	

	/**
	 * 1.���������Կ��
	 */
	public static void genKeyPair() throws NoSuchAlgorithmException {
		// KeyPairGenerator���������ɹ�Կ��˽Կ�ԣ�����RSA�㷨���ɶ���
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		// ��ʼ����Կ������������Կ��СΪ96-1024λ
		keyPairGen.initialize(1024, new SecureRandom());
		// ����һ����Կ�ԣ�������keyPair��
		KeyPair keyPair = keyPairGen.generateKeyPair();
		// �õ�˽Կ
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// �õ���Կ
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		// �õ���Կ�ַ���
		String publicKeyString = new String(Base64.getEncoder().encode(publicKey.getEncoded()));
		// �õ�˽Կ�ַ���
		String privateKeyString = new String(Base64.getEncoder().encode((privateKey.getEncoded())));
	}

	/**
	 * MD5ԭ����
	 */
	// �Σ����ڻ콻md5
	private static final String slat = "&%5123***&&%%$$#@";

	/**
	 * MD5 �Ƕ��ֽ�������м��ܵõ� ���������� Ϊ�˷����ת����16���Ƶ��ַ��� MD5 ������������ת����16���Ƶ��ַ���ͨ���õ�32λ�ı���
	 * ���ٵط����õ�16λ�ı��� 16 λʵ�����Ǵ� 32 λ�ַ�����ȡ�м�ĵ� 9 λ���� 24 λ�Ĳ��� java ���ԣ�String md5_16 =
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
	 * spring ��װ��md5����
	 */
	public static String springMd5(String dataStr) {
		dataStr = dataStr + slat;
		return DigestUtils.md5DigestAsHex(dataStr.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * base64 ���� jdk1.7 BASE64Encoder
	 */
	public static String base64Encoder(String dataStr) {
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(dataStr.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * base64 ���� jdk1.7 BASE64Encoder
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
	 * base64 ���� jdk1.8 java.util.Base64
	 */
	public static String base64EncoderByJdk8(String dataStr) {
		return Base64.getEncoder().encodeToString(dataStr.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * base64 ���� jdk1.8 java.util.Base64
	 */
	public static String base64DecoderByJdk8(String dataStr) {
		byte[] bytes = Base64.getDecoder().decode(dataStr);
		return new String(bytes, StandardCharsets.UTF_8);
	}

}

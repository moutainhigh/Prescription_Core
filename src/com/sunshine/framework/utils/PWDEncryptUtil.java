/**
 * <html>
 * <body>
 *  <P> Copyright 2014-2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年8月28日</p>
 *  <p> Created by wumingzi/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @Project YiChenTong_Server
 * @Package com.sunshine.framework.utils
 * @ClassName PWDEncryptUtil.java
 * @Description 密码/密钥的加密与验证
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年8月28日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class PWDEncryptUtil {
	/**
	 * 生成含有随机盐的密码
	 */
	public static String encrypt(String password) throws NoSuchAlgorithmException {
		Random r = new Random();
		StringBuilder sb = new StringBuilder(4);
		for (int i = 0; i < 4; i++) {
			sb.append(password.charAt(r.nextInt(password.length())));
		}

		String salt = sb.toString();
		String md5password = getMd5String16(password + salt);
		char[] encryptPassWord = new char[20];
		for (int i = 0; i < 4; i++) {
			encryptPassWord[i * 5] = salt.charAt(i);
			encryptPassWord[i * 5 + 1] = md5password.charAt(i * 4);
			encryptPassWord[i * 5 + 2] = md5password.charAt(i * 4 + 1);
			encryptPassWord[i * 5 + 3] = md5password.charAt(i * 4 + 2);
			encryptPassWord[i * 5 + 4] = md5password.charAt(i * 4 + 3);
		}
		return String.valueOf(encryptPassWord);
	}

	/**
	 * 校验密码是否正确
	 */
	public static boolean verify(String password, String encryptPassWord) throws NoSuchAlgorithmException {
		char[] md5PassWord = new char[16];
		char[] salt = new char[4];
		try {
			for (int i = 0; i < 4; i++) {
				salt[i] = encryptPassWord.charAt(i * 5);
				md5PassWord[i * 4] = encryptPassWord.charAt(i * 5 + 1);
				md5PassWord[i * 4 + 1] = encryptPassWord.charAt(i * 5 + 2);
				md5PassWord[i * 4 + 2] = encryptPassWord.charAt(i * 5 + 3);
				md5PassWord[i * 4 + 3] = encryptPassWord.charAt(i * 5 + 4);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return getMd5String16(password + String.valueOf(salt)).equals(String.valueOf(md5PassWord));
	}

	/*** 获得16位的加密字符 **/
	public static String getMd5String16(String str) throws NoSuchAlgorithmException {
		String md5str = getMd5String32(str).substring(8);
		return md5str.substring(0, md5str.length() - 8);
	}

	/** 获得24位的MD5加密字符 **/
	public static String getMd5String24(String str) throws NoSuchAlgorithmException {
		String md5str = getMd5String32(str).substring(4);
		return md5str.substring(0, md5str.length() - 4);
	}

	/** 获得32位的MD5加密算法 **/
	public static String getMd5String32(String str) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(str.getBytes());
		byte b[] = md.digest();
		int i;
		StringBuffer buf = new StringBuffer();
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];

			if (i < 0)
				i += 256;

			if (i < 16)
				buf.append("0");

			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}

	public static void main(String[] args) {
		String password = "123456";
		try {
			String encryptPassWord = null;
			for (int i = 0; i < 10; i++) {
				encryptPassWord = encrypt(password);
				System.out.println("encryptPassWord:" + encryptPassWord);
			}

			// System.out.println("verify:" + verify(password, encryptPassWord));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

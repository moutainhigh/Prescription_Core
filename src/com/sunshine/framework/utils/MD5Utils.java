/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众 </p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2016年6月26日</p>
 *  <p> Created on 于策</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @Package: com.sunshine.framework.utils
 * @ClassName: MD5Utils
 * @Statement:
 *             <p>
 *             </p>
 * @JDK version used:
 * @Author: 于策
 * @Create Date: 2016-4-2
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class MD5Utils {

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

	/**
	 * add by tianzhuzi due to data transfer 20160723.
	 * 
	 * @param input
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMD5Pwd(String password, String salt) throws NoSuchAlgorithmException {
		String result = null;
		if (StringUtils.isNotBlank(salt)) {
			result = getMD5(getMD5(password) + salt);
		} else {
			result = getMD5(password);
		}
		return result;
	}

	/**
	 * 获取MD5加密数据-天竹子
	 * 
	 * @param input
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMD5(String input) throws NoSuchAlgorithmException {
		String result = input;
		if (input != null) {
			MessageDigest md = MessageDigest.getInstance("MD5"); // or "SHA-1"
			md.update(input.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			result = hash.toString(16);
			while (result.length() < 32) {// 40 for SHA-1
				result = "0" + result;
			}
		}
		return result;
	}

	public static void main(String[] args) {
		try {
			// String jsonData = "111111account18026266968appCodeyctAppappIdappId123456789cityCode440100";
			String jsonData = "1111111adasd";
			System.out.println(getMd5String32(jsonData));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

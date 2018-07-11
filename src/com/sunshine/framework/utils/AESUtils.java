/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众 </p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年4月10日</p>
 *  <p> Created by Administrator</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESUtils {
	// 基于java的base64算法
	public static String byteTobase64(byte[] bytes) {
		BASE64Encoder base64Encoder = new BASE64Encoder();
		return base64Encoder.encode(bytes);
	}

	public static byte[] base64Tobyte(String base64) throws IOException {
		BASE64Decoder base64Decoder = new BASE64Decoder();
		return base64Decoder.decodeBuffer(base64);
	}

	/**
	 * @Description 生成AES密钥
	 * @return
	 * @throws Exception
	 * @date 2017年8月10日
	 */
	public static String genKeyAES() throws Exception {
		KeyGenerator kenGen = KeyGenerator.getInstance("AES");
		kenGen.init(128);
		SecretKey key = kenGen.generateKey();
		String base64Str = byteTobase64(key.getEncoded());
		return base64Str;
	}

	public static SecretKey loadKeyAES(String base64Key) throws Exception {
		byte[] bytes = base64Tobyte(base64Key);
		SecretKey key = new SecretKeySpec(bytes, "AES");
		return key;
	}

	/**
	 * 加密
	 * 
	 * @param content
	 *            待加密内容
	 * @param key
	 *            加密的密钥
	 * @return
	 */

	public static String encrypt(String content, String key) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(key.getBytes());
			kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			// AES算法密钥
			SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");

			Cipher cipher = Cipher.getInstance("AES");
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] byteRresult = cipher.doFinal(byteContent);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteRresult.length; i++) {
				String hex = Integer.toHexString(byteRresult[i] & 0xFF);
				if (hex.length() == 1) {
					hex = '0' + hex;
				}
				sb.append(hex.toUpperCase());
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param content
	 *            待解密内容
	 * @param key
	 *            解密的密钥
	 * @return
	 */
	public static String decrypt(String content, String key) {
		if (content.length() < 1)
			return null;
		byte[] byteRresult = new byte[content.length() / 2];
		for (int i = 0; i < content.length() / 2; i++) {
			int high = Integer.parseInt(content.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(content.substring(i * 2 + 1, i * 2 + 2), 16);
			byteRresult[i] = (byte) (high * 16 + low);
		}
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(key.getBytes());
			kgen.init(128, secureRandom);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			byte[] result = cipher.doFinal(byteRresult);
			return new String(result, "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		Properties p = System.getProperties();
		System.out.println(p.keys());
		String content = "{\"mediaSource\":\"YGKZ\",\"ahsPolicy\":{\"subjectInfo\":[{\"subjectInfo\":{\"totalModalPremium\":\"800\",\"insurantInfo\":[{\"insurantInfo\":{\"sexCode\":\"M\",\"mobileTelephone\":\"18069448315\",\"birthday\":\"1980-04-01\",\"firstNameSpell\":\"\",\"certificateType\":\"01\",\"certificateNo\":\"421081200007042990\",\"familyNameSpell\":\"\",\"email\":\"\",\"personnelAttribute\":\"100\",\"personnelName\":\"苏格拉底\",\"personnelAge\":\"\",\"virtualInsuredNum\":\"\"}},{\"insurantInfo\":{\"personnelAttribute\":\"100\",\"familyNameSpell\":\"\",\"email\":\"\",\"personnelAge\":\"\",\"certificateType\":\"01\",\"firstNameSpell\":\"\",\"mobileTelephone\":\"18069448315\",\"certificateNo\":\"12010119600004013832\",\"birthday\":\"1980-04-01\",\"personnelName\":\"奔牛节\",\"sexCode\":\"M\",\"virtualInsuredNum\":\"\"}}],\"productInfo\":[{\"productInfo\":{\"totalModalPremium\":\"400\",\"applyNum\":\"1\",\"productCode\":\"05000397\"}}]}}],\"policyPayInfo\":{\"paymentEndDate\":\"2017-03-22 08:57:14\",\"paymentPath\":\"05\",\"paymentDate\":\"2017-03-21\",\"currencyCode\":\"01\",\"bankAttribute\":\"2\",\"paymentPersonName\":\"中国平安人寿保险股份有限公司\",\"comID\":\"PR2015081001\",\"reinforceFlag\":\"Y\",\"accountType\":\"PR\",\"totalModalPremium\":\"800\",\"bankTradeDate\":\"2017-03-21\",\"certificateNo\":\"440101000101465\",\"certificateType\":\"01\",\"paymentBeginDate\":\"2017-03-21 08:57:14\"},\"policyBaseInfo\":{\"totalModalPremium\":\"800\",\"insuranceBeginTime\":\"2017-04-10 00:00:00\",\"alterableSpecialPromise\":\"\",\"currecyCode\":\"01\",\"applyDay\":\"28\",\"relationshipWithInsured\":\"09\",\"businessType\":\"2\",\"applyPersonnelNum\":\"2\",\"insuranceEndTime\":\"2017-04-28 23:59:59\",\"applyMonth\":\"\"},\"policyExtendInfo\":{\"partnerSystemSeriesNo\":\"asdf123999999900\"},\"insuranceApplicantInfo\":{\"groupPersonnelInfo\":{\"bankCode\":\"\",\"linkManMobileTelephone\":\"18291909006\",\"businessRegionType\":\"\",\"groupCertificateNo\":\"440101000101465\",\"companyAttribute\":\"03\",\"phoneExchangeArea\":\"\",\"groupAbbr\":\"黑牛保险\",\"address\":\"深圳福田\",\"phoneExchange\":\"\",\"groupName\":\"中国平安人寿保险股份有限公司\",\"linkManEmail\":\"\",\"groupCertificateType\":\"01\",\"businessRegisterId\":\"\",\"postCode\":\"331700\",\"linkManName\":\"联系人\",\"bankAccount\":\"\",\"industryCode\":\"\",\"linkManSexCode\":\"M\"}}},\"PTP_CODE\":\"00000001\",\"BANK_CODE\":\"P0010010\",\"TELLERNO\":\"P0010010\",\"REGION_CODE\":\"000000\",\"BK_SERIAL\":\"asdf1239999999\",\"BRNO\":\"P0010010\",\"TRAN_CODE\":\"000095\"}";

		// content = ReadFile("D:\\data\\test.json");
		Map<String, Object> paramMap = JSON.parseObject(content, HashMap.class);
		System.out.println(paramMap);
		String json = JSON.toJSONString(paramMap, SerializerFeature.SortField);
		System.out.println("Map排序后:" + json);
		// 加密
		String encryptResult = encrypt(json, "icpygkzacc123");
		System.out.println("AES加密后:" + encryptResult);

		encryptResult = MD5Utils.getMd5String32(encryptResult);
		System.out.println("MD5加密后：" + encryptResult);

	}

	// 读文件，返回字符串
	public static String ReadFile(String path) {
		File file = new File(path);
		BufferedReader reader = null;
		String laststr = "";
		try {
			// System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				System.out.println("line " + line + ": " + tempString);
				laststr = laststr + tempString;
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return laststr;
	}
}

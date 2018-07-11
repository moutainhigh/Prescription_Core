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

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.sunshine.framework.utils.EncodeUtils;
import com.sunshine.framework.utils.UrlUtil;
import com.sunshine.framework.common.http.HttpClient;
import com.sunshine.framework.common.http.HttpResponse;
import com.sunshine.framework.config.SystemConfig;

/**
 * 短链接转换类
 * @Package: com.sunshine.framework.utils
 * @ClassName: UrlUtil
 * @Statement: <p></p>
 * @JDK version used: 1.6
 * @Author: 于策
 * @Create Date: 2015-6-22
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class UrlUtil {

	private final static String SECRET = SystemConfig.getStringValue("ask_doctor_secret");
	private final static String URL = SystemConfig.getStringValue("ask_doctor_url");

	private final static String SUCCESS_CODE = "40000";

	private static Logger logger = Logger.getLogger(UrlUtil.class);

	/**
	 * 返回短链接，失败返回null
	 * 
	 * @param longUrl 长链接
	 * @return
	 */
	public static String getShortUrl(String longUrl) {
		String shortUrl = null;

		Map<String, String> params = new HashMap<String, String>();
		params.put("controller", "Shorturl");
		params.put("action", "GetShortUrl");
		params.put("secret", SECRET);
		params.put("long_url", EncodeUtils.encodeBase64(longUrl.getBytes()));

		HttpResponse response = new HttpClient().post(URL, params);

		if (response != null && response.getStatusCode() == 200) {
			JSONObject jres = JSONObject.parseObject(response.getResponseAsString());
			if (SUCCESS_CODE.equals(jres.getString("code"))) {
				shortUrl = jres.getString("shortlink");
			} else {
				logger.debug(jres.toJSONString());
			}
		}
		return shortUrl;
	}
}

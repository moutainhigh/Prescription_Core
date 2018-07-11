/**
 * <html>
 * <body>
 *  <P> Copyright 2014 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月6日</p>
 *  <p> Created by 51397</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.common.http;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.druid.util.StringUtils;
import com.sunshine.framework.exception.SystemException;
import com.sunshine.framework.rest.RestResponse;
import com.sunshine.framework.rest.RestStatusEnum;

import net.sf.json.JSONObject;

/**
 * @Project: prescription 
 * @Package: com.sunshine.framework.common.http
 * @ClassName: HttpClientNew
 * @Description: <p>restful httpClient请求类</p>
 * @JDK version used: 
 * @Author: 天竹子
 * @Create Date: 2017年9月6日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class HttpClientNew implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8247710825588211059L;
	protected static Logger logger = Logger.getLogger(HttpClientNew.class.getName());

	/**
	 * 
	 * @param url 请求地址
	 * @param headerParams http的addHeader的map
	 * @param jsonValue json格式的value值
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws SystemException
	 */
	public static RestResponse post(String url, Map<String, String> headerParams, String jsonValue) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse closeableResponse = null;
		HttpPost request = new HttpPost(url);
		RestResponse response = new RestResponse();

		try {
			if (headerParams == null || headerParams.size() == 0) {//设置header默认值
				request.addHeader(HttpConstants.CONTENT_TYPE, HttpConstants.JSON_TYPE_UTF8);
				request.addHeader(HttpConstants.ACCEPT, HttpConstants.JSON_TYPE);
			} else {
				for (Map.Entry<String, String> entry : headerParams.entrySet()) {
					request.addHeader(entry.getKey(), entry.getValue());
				}
			}

			StringEntity params = new StringEntity(jsonValue, HttpConstants.CHARACTER_ENCODING_UTF8);
			request.setEntity(params);
			closeableResponse = httpClient.execute(request);
			response = responseConventor(closeableResponse);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if (closeableResponse != null) {
				try {
					closeableResponse.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return response;
	}

	/**
	 * 将CloseableHttpResponse转化成平台使用的response对象
	 * @param closeableResponse
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private static RestResponse responseConventor(CloseableHttpResponse closeableResponse) throws ParseException, IOException {
		RestResponse response = new RestResponse();
		if (200 == closeableResponse.getStatusLine().getStatusCode()) {
			String json = EntityUtils.toString(closeableResponse.getEntity(), HttpConstants.CHARACTER_ENCODING_UTF8);
			JSONObject myJsonObject = JSONObject.fromObject(json);
			//			JSONArray jsonArray = JSONArray.fromObject(myJsonObject.get("data"));
			Object data = myJsonObject.get("data");
			if (data != null) {
				response.setData(myJsonObject.get("data"));
			} else {
				response.setData(json);
			}
			response.setStatus(RestStatusEnum.OK);
		} else {
			response.setStatus(RestStatusEnum.ERROR);
		}
		return response;
	}

	/**
	 * GET请求的response返回值封装。
	 * @param closeableResponse
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	//	private static RestResponse getResponseConventor(CloseableHttpResponse closeableResponse) throws ParseException,
	//			IOException {
	//		RestResponse response = new RestResponse();
	//		String json = EntityUtils.toString(closeableResponse.getEntity(), HttpConstants.CHARACTER_ENCODING_UTF8);
	//		JSONObject myJsonObject = JSONObject.fromObject(json);
	//		String status = myJsonObject.getString("status");
	//		JSONArray jsonArray = JSONArray.fromObject(myJsonObject.get("data"));
	//		if ("OK".equals(status)) {
	//			response.setStatus(RestStatusEnum.OK);
	//		} else {
	//			response.setStatus(RestStatusEnum.ERROR);
	//		}
	//		response.setData(jsonArray);//EntityUtils.toString(entity, "utf-8")
	//		return response;
	//	}

	/**
	 * 
	 * @param url
	 * @param headerParams
	 * @param params
	 * @param charset 编码格式
	 * @param socketTimeOut  暂时不用，给默认值5000
	 * @param connectTimeOut 暂时不用，给默认值5000
	 * @param connReqTimeOut 暂时不用，给默认值5000
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static RestResponse get(String url, Map<String, String> headerParams, Map<String, Object> params, String charset)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse closeableResponse = null;
		RestResponse response = new RestResponse();
		HttpGet httpGet = new HttpGet(url);

		try {
			if (StringUtils.isEmpty(charset)) {
				charset = HttpConstants.JSON_TYPE_UTF8;
			}
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			for (Entry<String, Object> entry : params.entrySet()) {
				formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString()));
			}
			if (params.size() > 0) {
				url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(formparams), charset);
			}
			if (headerParams == null || headerParams.size() == 0) {//设置header默认值
				httpGet.addHeader(new BasicHeader(HttpConstants.CONTENT_TYPE, HttpConstants.JSON_TYPE_UTF8));
				httpGet.addHeader(new BasicHeader(HttpConstants.ACCEPT, HttpConstants.JSON_TYPE));
			} else {
				for (Map.Entry<String, String> entry : headerParams.entrySet()) {
					httpGet.addHeader(new BasicHeader(entry.getKey(), entry.getValue()));
				}
			}

			RequestConfig requestConfig =
					RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(6000).setConnectionRequestTimeout(60000).build();//设置请求和传输超时时间
			httpGet.setConfig(requestConfig);
			closeableResponse = httpClient.execute(httpGet);
			response = responseConventor(closeableResponse);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if (closeableResponse != null) {
				try {
					closeableResponse.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return response;
	}

}

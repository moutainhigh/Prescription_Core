/**
 * <html>
 * <body>
 *  <P> Copyright 2014-2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月5日</p>
 *  <p> Created by wumingzi/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.rest.auth;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sunshine.framework.common.spring.ext.SpringContextHolder;
import com.sunshine.framework.rest.RestResponse;
import com.sunshine.framework.rest.RestStatusEnum;

/**
 * @Project YiChenTong_Server
 * @Package com.alibaba.dubbo.rpc.protocol.rest.extension
 * @ClassName AuthRequestFilter.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年9月5日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
@Priority(Priorities.USER)
public class AuthRequestFilter implements ContainerRequestFilter, ContainerResponseFilter {
	private static final Logger logger = LoggerFactory.getLogger(AuthRequestFilter.class);
	private static final Charset CHARACTER_SET = Charset.forName("utf-8");
	private static final String METHOD_GET = "GET";
	private static final String METHOD_POST = "POST";
	private static final String HTTP_HEADERS_AUTHORIZATION = "Authorization";
	private static final String HTTP_HEADERS_ORGCODE = "OrgCode";

	private static final String NO_CONFIG_AUTH_ERROR = "系统未授权,请联系API提供方";
	private static final String SYSTEM_ERROR = "系统错误,请联系API提供方";
	private static final String NO_PARAN_ERROR = "Headers中缺失参数:";
	private static final String IS_VALID_HEADER = "isValidHeader";
	private static final String INVALID_HEADER_RESP = "inValidResp";
	private static final String AUTH_VERIFY_FAIL = "权限验证失败!";
	private static final String PARAM_AND_CHAR = "&";
	private static final String PARAM_ASSIGN_CHAR = "=";
	private static final String PARAM_SPLIT_CHAR = ",";
	private RestAuthorizationService authService = SpringContextHolder.getBean(RestAuthorizationService.class);

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// TODO Auto-generated method stub
		// 验证Header参数是否存在OrgCode与Authorization参数
		Map<String, Object> resMap = checkHeaderParam(requestContext);
		Boolean isValid = (Boolean) resMap.get(IS_VALID_HEADER);
		if (isValid) {
			String method = requestContext.getMethod();
			RestResponse restResponse = null;
			// GET方法处理
			String sortParam = null;
			if (METHOD_GET.equalsIgnoreCase(method)) {
				sortParam = buildSortParam(requestContext);
			} else if (METHOD_POST.equalsIgnoreCase(method)) {
				byte[] buffer = IOUtils.toByteArray(requestContext.getEntityStream());
				byte[] copyBuffer = Arrays.copyOf(buffer, buffer.length);
				requestContext.setEntityStream(new ByteArrayInputStream(buffer));
				String requestData = new String(copyBuffer, "UTF-8");
				sortParam = buildSortJson(requestData);
			} else {
				restResponse = new RestResponse();
				restResponse.setStatus(RestStatusEnum.ERROR);
				restResponse.setMsg("不支持" + method + "请求");
				logger.info("method:{}", method);
			}
			logger.info("request sort data:{}", sortParam);

			String orgCode = requestContext.getHeaders().getFirst(HTTP_HEADERS_ORGCODE);
			String authorization = requestContext.getHeaders().getFirst(HTTP_HEADERS_AUTHORIZATION);
			String configAuthCode = authService.findAuthorization(orgCode);
			if (StringUtils.isEmpty(configAuthCode)) {
				restResponse = new RestResponse();
				restResponse.setStatus(RestStatusEnum.ERROR);
				restResponse.setMsg(NO_CONFIG_AUTH_ERROR);
			} else {
				try {
					String verifyAuthorCode = null;
					if (StringUtils.isEmpty(sortParam)) {
						verifyAuthorCode = configAuthCode;
					} else {
						verifyAuthorCode = getMd5String32(configAuthCode.concat(sortParam));
					}
					if (!authorization.equals(verifyAuthorCode)) {
						restResponse = new RestResponse();
						restResponse.setStatus(RestStatusEnum.ERROR);
						restResponse.setMsg(AUTH_VERIFY_FAIL);
					}

				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					restResponse = new RestResponse();
					restResponse.setStatus(RestStatusEnum.ERROR);
					restResponse.setMsg(SYSTEM_ERROR);
					logger.error("error:configAuthCode:{},sortParam:{}生成MD5失败", configAuthCode, sortParam);
				}
			}

			if (restResponse != null) {
				requestContext.abortWith(Response.status(401).entity(restResponse).build());
			}
		} else {
			requestContext.abortWith(Response.status(401).entity(resMap.get(INVALID_HEADER_RESP)).build());
		}
	}

	/**
	 * @Description 检查是否缺失OrgCode与Authorization参数
	 * @param requestContext
	 * @return
	 * @date 2017年9月6日
	 */
	private Map<String, Object> checkHeaderParam(ContainerRequestContext requestContext) {
		Map<String, Object> resMap = new HashMap<>();
		String orgCode = requestContext.getHeaders().getFirst(HTTP_HEADERS_ORGCODE);
		String msg = null;
		Boolean isValid = true;
		if (StringUtils.isEmpty(orgCode)) {
			isValid = false;
			msg = NO_PARAN_ERROR.concat(HTTP_HEADERS_ORGCODE);
		}

		if (isValid) {
			String authorization = requestContext.getHeaders().getFirst(HTTP_HEADERS_AUTHORIZATION);
			if (StringUtils.isEmpty(authorization)) {
				msg = NO_PARAN_ERROR.concat(HTTP_HEADERS_AUTHORIZATION);
				isValid = false;
			}
		}
		resMap.put(IS_VALID_HEADER, isValid);
		if (!isValid) {
			RestResponse restResponse = new RestResponse();
			restResponse.setStatus(RestStatusEnum.ERROR);
			restResponse.setMsg(msg);
			resMap.put(INVALID_HEADER_RESP, restResponse);
		}
		return resMap;
	}

	/**
	 * 
	 * @Description 构建顺序参数
	 * @param requestContext
	 * @return
	 * @date 2017年9月6日
	 */
	private String buildSortParam(ContainerRequestContext requestContext) {
		MultivaluedMap<String, String> valMap = requestContext.getUriInfo().getPathParameters();
		List<String> reqDataList = new ArrayList<>();
		StringBuffer reqDataSb = new StringBuffer();
		if (!CollectionUtils.isEmpty(valMap)) {
			for (Map.Entry<String, List<String>> entry : valMap.entrySet()) {
				reqDataSb.append(entry.getKey()).append(PARAM_ASSIGN_CHAR);
				for (int i = 0; i < entry.getValue().size(); i++) {
					reqDataSb.append(entry.getValue().get(i));
					if (i < entry.getValue().size() - 1) {
						reqDataSb.append(PARAM_SPLIT_CHAR);
					}
				}
				reqDataList.add(reqDataSb.toString());
				reqDataSb.setLength(0);
			}
			if (reqDataList.size() > 0) {
				Collections.sort(reqDataList);
				for (String reqData : reqDataList) {
					if (reqDataSb.length() > 0) {
						reqDataSb.append(PARAM_AND_CHAR);
					}
					reqDataSb.append(reqData);
				}
			}
		}
		return reqDataSb.toString();
	}

	private String buildSortJson(String jsonData) {
		StringBuffer sortSb = new StringBuffer();
		if (!StringUtils.isEmpty(jsonData)) {
			Map<String, String> dataMap = JSON.parseObject(jsonData, new TypeReference<Map<String, String>>() {
			});

			List<String> jsonKeys = new ArrayList<>();
			for (String key : dataMap.keySet()) {
				jsonKeys.add(key);
			}
			Collections.sort(jsonKeys);

			for (String jsonKey : jsonKeys) {
				if (sortSb.length() > 0) {
					sortSb.append(PARAM_AND_CHAR);
				}
				sortSb.append(jsonKey).append(PARAM_ASSIGN_CHAR).append(dataMap.get(jsonKey));
			}

		}
		return sortSb.toString();
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		// TODO Auto-generated method stub
		RestResponse restResponse = null;
		if (responseContext.getStatus() == 405) {
			String method = requestContext.getMethod();
			restResponse = new RestResponse();
			restResponse.setStatus(RestStatusEnum.ERROR);
			restResponse.setMsg("不支持" + method + "请求,请按API文档指定的Method访问");
		}
		if (restResponse != null) {
			responseContext.setEntity(restResponse, null, MediaType.APPLICATION_JSON_TYPE);
		} else {
			String orgCode = requestContext.getHeaders().getFirst(HTTP_HEADERS_ORGCODE);
			String configAuthCode = authService.findAuthorization(orgCode);
			if (!StringUtils.isEmpty(configAuthCode)) {
				Object respObj = responseContext.getEntity();
				if (respObj != null) {
					String respJsonData = JSON.toJSONString(respObj);
					String sortRespJson = buildSortJson(respJsonData);
					logger.info("resp sort data:{}", sortRespJson);
					try {
						String authorization = getMd5String32(configAuthCode.concat(sortRespJson));
						responseContext.getHeaders().add(HTTP_HEADERS_AUTHORIZATION, authorization);
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error("resp error:configAuthCode:{},sortParam:{}生成MD5失败", configAuthCode, sortRespJson);
					}
				}
			}
		}
	}

	/** 获得32位的MD5加密算法 **/
	private static String getMd5String32(String str) throws NoSuchAlgorithmException {
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
}

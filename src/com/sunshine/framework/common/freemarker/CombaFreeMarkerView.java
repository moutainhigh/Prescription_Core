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
package com.sunshine.framework.common.freemarker;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import com.sunshine.framework.common.CommonConstant;
import com.sunshine.framework.config.SystemConfig;
import com.sunshine.framework.utils.DateUtils;

/**
 * 自定义freemarker视图解析器,增加模板全局变量
 * 
 * @Package: com.sunshine.framework.common.freemarker
 * @ClassName: CombaFreeMarkerView
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
public class CombaFreeMarkerView extends FreeMarkerView {
	public static Logger logger = LoggerFactory.getLogger(CombaFreeMarkerView.class);
	public static String DEFAULT_VERSION_NUMBER = "1.0";
	private static String SYSTEM_REQUEST_PATH = "";
	private static final String RUL_PARAM_RESOURCEID = "resourceId";

	@Override
	protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
		String path = request.getContextPath();
		String basePath = SYSTEM_REQUEST_PATH.concat(request.getContextPath());
		if (logger.isDebugEnabled()) {
			logger.debug("CombaFreeMarkerView exposeHelpers's basePath:{}", basePath);
		}
		String imgPath = SystemConfig.getStringValue("file_virtual_path");
		if (StringUtils.isBlank(imgPath)) {
			logger.warn("file_virtual_path is not found.use default /pic");
		} else {
			imgPath = basePath.concat("/pic");
		}
		DEFAULT_VERSION_NUMBER = DateUtils.getCurrentTime();
		String css_version = SystemConfig.getStringValue(CommonConstant.CSS_VERSION, DEFAULT_VERSION_NUMBER);
		String js_version = SystemConfig.getStringValue(CommonConstant.JS_VERSION, DEFAULT_VERSION_NUMBER);

		String hostIP = SystemConfig.Host_IP;
		// 设置项目路径为全局变量

		model.put(CommonConstant.PATH, path);
		model.put(CommonConstant.WEB_BASE_PATH, basePath);
		model.put(CommonConstant.WEB_HOST_IP, hostIP);
		model.put(CommonConstant.WEB_IMG_PATH, imgPath);
		model.put(CommonConstant.CSS_VERSION, css_version);
		model.put(CommonConstant.JS_VERSION, js_version);
		super.exposeHelpers(model, request);
	}
}

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

/**
 * @Project YiChenTong_Server
 * @Package com.alibaba.dubbo.rpc.protocol.rest.auth
 * @ClassName RestAuthorizationService.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年9月5日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public interface RestAuthorizationService {
	/**
	 * @Description 根据机构查找为机构设置的密钥
	 * @param orgCode
	 * @return
	 * @date 2017年9月5日
	 */
	public String findAuthorization(String orgCode);
}

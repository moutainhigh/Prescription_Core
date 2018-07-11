/**
 * <html>
 * <body>
 *  <P> Copyright 2014-2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年8月29日</p>
 *  <p> Created by wumingzi/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.shiro.matcher;

import java.security.NoSuchAlgorithmException;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

import com.sunshine.framework.utils.PWDEncryptUtil;

/**
 * @Project Prescription_Clinic
 * @Package com.sunshine.framework.shiro.matcher
 * @ClassName SunshineCredentialsMatcher.java
 * @Description 自定义shiro的密码验证
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年8月29日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class SunshineCredentialsMatcher extends SimpleCredentialsMatcher {

	@Override
	public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo authInfo) {
		// TODO Auto-generated method stub
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		Object accountCredentials = getCredentials(authInfo);
		String userInputPWD = String.valueOf(token.getPassword());
		Boolean flag = false;
		try {
			flag = PWDEncryptUtil.verify(userInputPWD, accountCredentials.toString());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
}

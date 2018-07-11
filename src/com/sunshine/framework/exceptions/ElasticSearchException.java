package com.sunshine.framework.exceptions;

/**
 * @Project: YiChenTong_Server
 * @Package: exceptions
 * @ClassName: ElasticSearchException.java
 * @Description:
 *               <p>
 *               </p>
 * @JDK version used: 1.8
 * @Author: 无名子/yu.ce@foxmail.com
 * @Create Date: 2017年4月5日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class ElasticSearchException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1656100812700116022L;

	public ElasticSearchException(String message) {
		super(message);
	}

	public ElasticSearchException(Throwable e) {
		super(e);
	}

	public ElasticSearchException(String message, Throwable cause) {
		super(message, cause);
	}

}

/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年5月9日</p>
 *  <p> Created by 51397</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.elasticsearch;

import java.io.Serializable;

/**
 * @Project: yct
 * @Package: elasticsearch
 * @ClassName: SearchResponse
 * @Description: <p>
 *               </p>
 * @JDK version used:
 * @Author: 天竹子
 * @Create Date: 2017年5月9日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class EsSearchResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5284360138930043557L;

	/**
	 * 交易结果代码
	 */
	private String resultCode;

	/**
	 * 错误信息,该字段返回错误信息,否则返回空字符串
	 */
	private String resultMessage;

	/**
	 * 输出参数
	 */
	private Object result;

	/**
	 * 数量
	 */
	private Integer totalNum;

	/**
	 * 页大小
	 */
	private Integer pageSize = 10;

	/**
	 * 总页数
	 */
	private Integer totalPages;

	/**
	 * 当前页
	 */
	private Integer curPage = 0;

	/**
	 * 首页
	 */
	private Integer homePage = 0;

	/**
	 * 末页
	 */
	private Integer lastPage = 0;

	/**
	 * 上一页
	 */
	private Integer prePage;

	/**
	 * 下一页
	 */
	private Integer nextPage;

	public EsSearchResponse() {
		super();
	}

	/**
	 * @param resultCode
	 * @param resultMessage
	 * @param result
	 * @param totalNum
	 * @param pageSize
	 * @param totalPages
	 * @param curPage
	 * @param homePage
	 * @param lastPage
	 * @param prePage
	 * @param nextPage
	 */
	public EsSearchResponse(String resultCode, String resultMessage, Object result, Integer totalNum, Integer pageSize, Integer totalPages,
			Integer curPage, Integer homePage, Integer lastPage, Integer prePage, Integer nextPage) {
		super();
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
		this.result = result;
		this.totalNum = totalNum;
		this.pageSize = pageSize;
		this.totalPages = totalPages;
		this.curPage = curPage;
		this.homePage = homePage;
		this.lastPage = lastPage;
		this.prePage = prePage;
		this.nextPage = nextPage;
	}

	/**
	 * @return the resultCode
	 */
	public String getResultCode() {
		return resultCode;
	}

	/**
	 * @param resultCode
	 *            the resultCode to set
	 */

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * @return the resultMessage
	 */
	public String getResultMessage() {
		return resultMessage;
	}

	/**
	 * @param resultMessage
	 *            the resultMessage to set
	 */
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	/**
	 * @return the totalNum
	 */
	public Integer getTotalNum() {
		return totalNum;
	}

	/**
	 * @param totalNum
	 *            the totalNum to set
	 */
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize
	 *            the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the totalPages
	 */
	public Integer getTotalPages() {
		return totalPages;
	}

	/**
	 * @param totalPages
	 *            the totalPages to set
	 */
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * @return the curPage
	 */
	public Integer getCurPage() {
		return curPage;
	}

	/**
	 * @param curPage
	 *            the curPage to set
	 */
	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}

	/**
	 * @return the homePage
	 */
	public Integer getHomePage() {
		return homePage;
	}

	/**
	 * @param homePage
	 *            the homePage to set
	 */
	public void setHomePage(Integer homePage) {
		this.homePage = homePage;
	}

	/**
	 * @return the lastPage
	 */
	public Integer getLastPage() {
		return lastPage;
	}

	/**
	 * @param lastPage
	 *            the lastPage to set
	 */
	public void setLastPage(Integer lastPage) {
		this.lastPage = lastPage;
	}

	/**
	 * @return the prePage
	 */
	public Integer getPrePage() {
		return prePage;
	}

	/**
	 * @param prePage
	 *            the prePage to set
	 */
	public void setPrePage(Integer prePage) {
		this.prePage = prePage;
	}

	/**
	 * @return the nextPage
	 */
	public Integer getNextPage() {
		return nextPage;
	}

	/**
	 * @param nextPage
	 *            the nextPage to set
	 */
	public void setNextPage(Integer nextPage) {
		this.nextPage = nextPage;
	}
}

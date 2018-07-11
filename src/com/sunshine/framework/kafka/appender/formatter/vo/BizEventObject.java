/**
 * <html>
 * <body>
 *  <P> Copyright 2017 阳光康众</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年3月10日</p>
 *  <p> Created by 于策/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.sunshine.framework.kafka.appender.formatter.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import ch.qos.logback.classic.spi.ILoggingEvent;

import com.sunshine.framework.kafka.appender.logback.LogBackEventObject;

/**
 * @Project ChuFangLiuZhuan_PlatForm
 * @Package com.sunshine.dataanalysis.vo
 * @ClassName BizEventObject.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年5月4日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class BizEventObject extends LogBackEventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3476923503290405688L;
	private static String INFO_SPLIT_CHAR = ",";
	private static String INFO_PARAM_CHAR = ":";
	private static String SPE_STRING = "event";
	protected String happenTime;
	protected String appCode;
	protected String userAccount;

	protected String deptName;
	protected String doctorName;
	protected String regDate;
	protected String doctorTime;

	protected String orderNo;
	protected String hisOrderNo;
	protected String tradeMode;
	protected String agtOrderNo;
	protected String agtTraceNo;
	protected String refundOrderNo;
	protected String agtRefundOrderNo;

	protected String regStatus;
	protected String payStatus;

	protected String event;

	public BizEventObject() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BizEventObject(ILoggingEvent eventObject) {
		super(eventObject);
		// TODO Auto-generated constructor stub
		if (StringUtils.isNotBlank(infoData)) {
			// 解析infoData数据
			// 提取event数据
			int index = infoData.indexOf(SPE_STRING);
			String copyInfoData = infoData;
			if (index > -1) {
				event = copyInfoData.substring(index + 6);
				copyInfoData = copyInfoData.substring(0, index >= 1 ? index - 1 : 0);
			}

			String[] infos = copyInfoData.split(INFO_SPLIT_CHAR);
			index = -1;
			if (infos.length > 1) {
				Map<String, String> infoDataMap = new HashMap<String, String>();
				for (int i = 0; i < infos.length; i++) {
					// 避免出现数据中有1个以上:号出现的情况
					index = infos[i].indexOf(INFO_PARAM_CHAR);
					if (index > -1 && infos[i].split(INFO_PARAM_CHAR).length > 1) {
						try {
							infoDataMap.put(infos[i].substring(0, index), infos[i].substring(index + 1));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							infoDataMap.put(infos[i].substring(0, index), infos[i].substring(index));
						}
					}
				}

				happenTime = infoDataMap.get("happenTime");
				appCode = infoDataMap.get("appCode");
				userAccount = infoDataMap.get("userAccount");
				deptName = infoDataMap.get("deptName");
				doctorName = infoDataMap.get("doctorName");
				regDate = infoDataMap.get("regDate");
				doctorTime = infoDataMap.get("doctorTime");
				orderNo = infoDataMap.get("orderNo");
				tradeMode = infoDataMap.get("tradeMode");
				agtOrderNo = infoDataMap.get("agtOrderNo");
				agtTraceNo = infoDataMap.get("agtTraceNo");
				refundOrderNo = infoDataMap.get("refundOrderNo");
				agtRefundOrderNo = infoDataMap.get("agtRefundOrderNo");
				regStatus = infoDataMap.get("regStatus");
				payStatus = infoDataMap.get("payStatus");
			}
		}
	}

	public String getHappenTime() {
		return happenTime;
	}

	public void setHappenTime(String happenTime) {
		this.happenTime = happenTime;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getDoctorTime() {
		return doctorTime;
	}

	public void setDoctorTime(String doctorTime) {
		this.doctorTime = doctorTime;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getHisOrderNo() {
		return hisOrderNo;
	}

	public void setHisOrderNo(String hisOrderNo) {
		this.hisOrderNo = hisOrderNo;
	}

	public String getTradeMode() {
		return tradeMode;
	}

	public void setTradeMode(String tradeMode) {
		this.tradeMode = tradeMode;
	}

	public String getAgtOrderNo() {
		return agtOrderNo;
	}

	public void setAgtOrderNo(String agtOrderNo) {
		this.agtOrderNo = agtOrderNo;
	}

	public String getAgtTraceNo() {
		return agtTraceNo;
	}

	public void setAgtTraceNo(String agtTraceNo) {
		this.agtTraceNo = agtTraceNo;
	}

	public String getRefundOrderNo() {
		return refundOrderNo;
	}

	public void setRefundOrderNo(String refundOrderNo) {
		this.refundOrderNo = refundOrderNo;
	}

	public String getAgtRefundOrderNo() {
		return agtRefundOrderNo;
	}

	public void setAgtRefundOrderNo(String agtRefundOrderNo) {
		this.agtRefundOrderNo = agtRefundOrderNo;
	}

	public String getRegStatus() {
		return regStatus;
	}

	public void setRegStatus(String regStatus) {
		this.regStatus = regStatus;
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
}

/**
 * <html>
 * <body>
 *  <P> Copyright 2014-2017 广东天泽阳光康众医疗投资管理有限公司. 粤ICP备09007530号-15</p>
 *  <p> All rights reserved.</p>
 *  <p> Created on 2017年9月6日</p>
 *  <p> Created by wumingzi/yu.ce@foxmail.com</p>
 *  </body>
 * </html>
 */
package com.alibaba.dubbo.rpc.protocol.rest.support;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Project YiChenTong_Server
 * @Package com.alibaba.dubbo.rpc.protocol.rest.support
 * @ClassName OutputStreamWrapper.java
 * @Description
 * @JDK version used 1.8
 * @Author 于策/yu.ce@foxmail.com
 * @Create Date 2017年9月6日
 * @modify By
 * @modify Date
 * @Why&What is modify
 * @Version 1.0
 */
public class OutputStreamWrapper extends OutputStream {
	private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	private final OutputStream output;

	public OutputStreamWrapper(OutputStream output) {
		this.output = output;
	}

	@Override
	public void write(int i) throws IOException {
		buffer.write(i);
		output.write(i);
	}

	@Override
	public void write(byte[] b) throws IOException {
		buffer.write(b);
		output.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		buffer.write(b, off, len);
		output.write(b, off, len);
	}

	@Override
	public void flush() throws IOException {
		output.flush();
	}

	@Override
	public void close() throws IOException {
		output.close();
	}

	public byte[] getBytes() {
		return buffer.toByteArray();
	}
}

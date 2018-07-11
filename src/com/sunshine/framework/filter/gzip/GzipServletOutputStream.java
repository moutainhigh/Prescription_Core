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
package com.sunshine.framework.filter.gzip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/**
 * GZIP压缩传输.
 * 
 * @Package: com.sunshine.framework.filter.gzip
 * @ClassName: GzipFilter
 * @Statement:
 *             <p>
 *             GZipServletOutputStream继承自ServletOutputStream 该类的对象用于替换HttpServletResponse.getOutputStream()方法返回的ServletOutputStream对象
 *             其内部使用GZipServletOutputStream的write(int b)方法实现ServletOutputStream类的write(int b)方法 以达到压缩数据的目的
 *             </p>
 * @JDK version used:
 * @Author: 于策
 * @Create Date: 2016-4-2
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class GzipServletOutputStream extends ServletOutputStream {
	private ByteArrayOutputStream buffer;

	public GzipServletOutputStream(ByteArrayOutputStream buffer) {
		this.buffer = buffer;
	}

	@Override
	public void write(int b) throws IOException {
		buffer.write(b);
	}

	public byte[] toByteArray() {
		return buffer.toByteArray();
	}

	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setWriteListener(WriteListener writeListener) {
		// TODO Auto-generated method stub

	}
}

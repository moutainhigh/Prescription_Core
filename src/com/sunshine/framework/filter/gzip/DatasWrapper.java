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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 
 * @Package: com.sunshine.framework.config
 * @ClassName: SystemConstants
 * @Statement:
 *             <p>
 *             CompressionResponseWrapper类从HttpServletWrapper类继承 重写了getWriter()和getOutputStream()方法 用GZIPServletOutputStream替换了ServletOutputStream对象
 *             </p>
 * @JDK version used:
 * @Author: 于策
 * @Create Date: 2016-4-2
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class DatasWrapper extends HttpServletResponseWrapper {
	public static final int OUT_NONE = 0;
	public static final int OUT_WRITER = 1;
	public static final int OUT_STREAM = 2;
	private int outputType = OUT_NONE;
	private ServletOutputStream output = null;
	private PrintWriter writer = null;
	private ByteArrayOutputStream buffer = null;

	public DatasWrapper(HttpServletResponse response) {
		super(response);
		buffer = new ByteArrayOutputStream();
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (outputType == OUT_STREAM) {
			throw new IllegalStateException();
		} else if (outputType == OUT_WRITER) {
			return writer;
		} else {
			outputType = OUT_WRITER;
			writer = new PrintWriter(new OutputStreamWriter(buffer, getCharacterEncoding()));
			return writer;
		}
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (outputType == OUT_WRITER) {
			throw new IllegalStateException();
		} else if (outputType == OUT_STREAM) {
			return output;
		} else {
			outputType = OUT_STREAM;
			output = new DatasWrappedOutputStream(buffer);
			return output;
		}
	}

	@Override
	public void flushBuffer() throws IOException {
		if (outputType == OUT_WRITER) {
			writer.flush();
		}
		if (outputType == OUT_STREAM) {
			output.flush();
		}
	}

	@Override
	public void reset() {
		outputType = OUT_NONE;
		buffer.reset();
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
		output.close();
		writer.close();

	}

	public byte[] getResponseData() throws IOException {
		flushBuffer();
		return buffer.toByteArray();
	}

	class DatasWrappedOutputStream extends ServletOutputStream {
		private ByteArrayOutputStream buffer;

		public DatasWrappedOutputStream(ByteArrayOutputStream buffer) {
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

		public void setWriteListener(WriteListener arg0) {
			// TODO Auto-generated method stub

		}
	}
}

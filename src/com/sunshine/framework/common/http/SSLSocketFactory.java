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

package com.sunshine.framework.common.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

/**
 * 自签名ssl,信任任何证书
 * 
 * @Package: com.sunshine.framework.common.http
 * @ClassName: SSLSocketFactory
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
public class SSLSocketFactory implements ProtocolSocketFactory {
	private SSLContext sslcontext = null;

	private SSLContext createSSLContext() {
		SSLContext sslcontext = null;
		try {
			sslcontext = SSLContext.getInstance("SSL");
			sslcontext.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		return sslcontext;
	}

	private SSLContext getSSLContext() {
		if (this.sslcontext == null) {
			this.sslcontext = createSSLContext();
		}
		return this.sslcontext;
	}

	public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(host, port);
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params)
			throws IOException, UnknownHostException, ConnectTimeoutException {
		if (params == null) {
			throw new IllegalArgumentException("Parameters may not be null");
		}
		int timeout = params.getConnectionTimeout();
		SocketFactory socketfactory = getSSLContext().getSocketFactory();
		if (timeout == 0) {
			return socketfactory.createSocket(host, port, localAddress, localPort);
		} else {
			Socket socket = socketfactory.createSocket();
			SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
			SocketAddress remoteaddr = new InetSocketAddress(host, port);
			socket.bind(localaddr);
			socket.connect(remoteaddr, timeout);
			return socket;
		}
	}

	private static class TrustAnyTrustManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}
}

/**
 * Copyright 1999-2014 dangdang.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.rpc.protocol.rest.support;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This logging filter
 *
 * @author Yuce
 */
@Priority(Integer.MIN_VALUE)
public class LoggingFilter
		implements ContainerRequestFilter, ClientRequestFilter, ContainerResponseFilter, ClientResponseFilter, WriterInterceptor, ReaderInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

	@Override
	public void filter(ClientRequestContext context) throws IOException {
		logHttpHeaders(context.getStringHeaders());
	}

	@Override
	public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
		logHttpHeaders(responseContext.getHeaders());
	}

	@Override
	public void filter(ContainerRequestContext context) throws IOException {
		logHttpHeaders(context.getHeaders());
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		logHttpHeaders(responseContext.getStringHeaders());
	}

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
		byte[] buffer = IOUtils.toByteArray(context.getInputStream());
		logger.info("request:" + new String(buffer, "UTF-8"));
		context.setInputStream(new ByteArrayInputStream(buffer));
		return context.proceed();
	}

	@Override
	public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
		OutputStreamWrapper wrapper = new OutputStreamWrapper(context.getOutputStream());
		context.setOutputStream(wrapper);
		context.proceed();
		logger.info("response:" + new String(wrapper.getBytes(), "UTF-8"));
	}

	protected void logHttpHeaders(MultivaluedMap<String, String> headers) {
		StringBuilder msg = new StringBuilder("headers:{");
		for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
			msg.append(entry.getKey()).append(": ");
			for (int i = 0; i < entry.getValue().size(); i++) {
				msg.append(entry.getValue().get(i));
				if (i < entry.getValue().size() - 1) {
					msg.append(", ");
				}
			}
		}
		msg.append("}");
		logger.info(msg.toString());
	}

}

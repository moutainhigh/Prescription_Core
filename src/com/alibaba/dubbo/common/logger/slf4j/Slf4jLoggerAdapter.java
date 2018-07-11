package com.alibaba.dubbo.common.logger.slf4j;

import java.io.File;

import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.logger.Level;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerAdapter;

public class Slf4jLoggerAdapter implements LoggerAdapter {

	public Logger getLogger(String key) {
		return new Slf4jLogger(LoggerFactory.getLogger(key));
	}

	public Logger getLogger(Class<?> key) {
		return new Slf4jLogger(LoggerFactory.getLogger(key));
	}

	private Level level;

	private File file;

	public void setLevel(Level level) {
		this.level = level;
	}

	public Level getLevel() {
		return level;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}

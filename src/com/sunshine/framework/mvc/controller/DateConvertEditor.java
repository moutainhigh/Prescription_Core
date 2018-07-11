package com.sunshine.framework.mvc.controller;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.util.StringUtils;

/**
 * 
 * @author 张代浩
 *
 */
public class DateConvertEditor extends PropertyEditorSupport {
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		FastDateFormat datetimeFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
		FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
		if (StringUtils.hasText(text)) {
			try {
				if (text.indexOf(":") == -1 && text.length() == 10) {
					setValue(dateFormat.parse(text));
				} else if (text.indexOf(":") > 0 && text.length() == 19) {
					setValue(datetimeFormat.parse(text));
				} else if (text.indexOf(":") > 0 && text.length() == 21) {
					text = text.replace(".0", "");
					setValue(datetimeFormat.parse(text));
				} else {
					throw new IllegalArgumentException("Could not parse date, date format is error ");
				}
			} catch (ParseException ex) {
				IllegalArgumentException iae = new IllegalArgumentException("Could not parse date: " + ex.getMessage());
				iae.initCause(ex);
				throw iae;
			}
		} else {
			setValue(null);
		}
	}
}

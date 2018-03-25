package com.cassi.spandmb.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class String2DateConverter implements Converter<String,Date>{

	private String datePattern;
	
	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}
	
	@Override
	public Date convert(String arg0) {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat(datePattern);
		try {
			return format.parse(arg0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}

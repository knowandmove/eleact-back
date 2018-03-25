package com.cassi.spandmb.helper;

import org.springframework.core.convert.converter.Converter;

import com.cassi.spandmb.entity.User;

public class String2UserConverter implements Converter<String,User>{

	@Override
	public User convert(String arg0) {
		// TODO Auto-generated method stub
		User user = new User();
		user.setName(arg0);
		return user;
	}

	
	
}

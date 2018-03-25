package com.cassi.spandmb.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AnotherController {

	@RequestMapping("/another/{hello}")
	public String another1(@PathVariable(value="hello") String pathVariable,
			@CookieValue(value="JSESSIONID",defaultValue="123123") String sessionId) {
		System.out.println(sessionId);
		return "hello";
	}
	
	
	@RequestMapping("/another/setCookie")
	public String another2(HttpServletRequest request,HttpServletResponse response) {
		Cookie[] cookies= request.getCookies();
		for(int i = 0;i < cookies.length;i++) {
			System.out.println(cookies[i].getValue());
			System.out.println(cookies[i].getDomain());
			System.out.println(cookies[i].getMaxAge());
			cookies[i].setMaxAge(1000000);
			response.addCookie(cookies[i]);
		}
		return "hello";
	}
	
	@RequestMapping("/another/newCookie")
	public String another3(HttpServletRequest request,HttpServletResponse response) {
		Cookie cookie = new Cookie("name","wangqchf");
		cookie.setMaxAge(1000000);
		response.addCookie(cookie);
		return "hello";
	}
}

package com.cassi.spandmb.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.cassi.spandmb.entity.User;

@Controller

@SessionAttributes("name1")
public class HelloController {
	
	@ModelAttribute
	public void userModel(Model model) {
		model.asMap().put("name", "name");
		System.out.println("IN Model Attribute Methods!");
	}

	@RequestMapping("/hello1")
	public String helloWorld() {
		return "hello";
	}
	
	@RequestMapping("/hello")
	public String hello(Model model,
			@RequestParam(name = "name",defaultValue = "User Name : hahhah ") String userName) {
		if(model.asMap().get("name").equals("name"))
			System.out.println("123123123");
		System.out.println(userName);
		return "hello";
	}
	
	@RequestMapping("/hello2")
	public String hello2() {
		return "hello";
	}
	
	@RequestMapping("/hello3")
	public String hello3() {
		return "hello2";
	}
	
	@RequestMapping("hello4")
	public String hello4() {
		return "hello3";
	}
	
	@RequestMapping("/testValue/{value1}")
	public String testPathVariable(@PathVariable int value1,Model model,@RequestHeader("User-Agent") String userAgen,
			@CookieValue(value="JSESSIONID",defaultValue="") String sessionId) {
		//只有addAttribute添加的属性
		model.addAttribute("value", value1);
		model.addAttribute("agent", userAgen);
		model.addAttribute("sessionId", sessionId);
		System.out.println(sessionId);
		return "test";
	}
	
	@RequestMapping("/testValue/hello")
	public String testSession(HttpSession session,Model model) {
		String name1 = "Hello sessionValue";
		model.addAttribute("name1", session.getAttribute("name1") == null ? name1 : session.getAttribute("name1"));
		System.out.println(session.getId());
		System.out.println("Inactive time is : " + session.getMaxInactiveInterval());
		return "hello";
	}
	
	@RequestMapping("/testValue/testSessionValue")
	public String testSessionValue(HttpSession session,Model model) {
		String name1 = (String) session.getAttribute("name1");
		model.addAttribute("name1", name1);
		System.out.println(session.getId());
		return "testSession";
	}
	
	@RequestMapping("/testJson")
	@CrossOrigin("http://localhost:3000")
	@ResponseBody
	public JsonObj  getJson(HttpServletResponse response) {
		JsonObj obj = new JsonObj();
		obj.name1 = "123213";
		//response.setHeader("Access-Control-Allow-Origin", "*");
		return obj;
	}
	
	class JsonObj {
		public String name1;
	}
	
	public void bubleSort(int[] array1) {
		int temp = 0;
		for(int i = array1.length - 1;i>=0;i--) {
			temp = 0;
			for(int j = 1;j < i;j++) {
				if(array1[j] >  array1[temp]) {
					temp = j;
				}
			}
			int temp_number = array1[temp];
			array1[temp] = array1[i];
			array1[i] = temp_number;
		}
	}
	
}

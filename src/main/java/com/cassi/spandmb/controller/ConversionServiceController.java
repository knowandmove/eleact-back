package com.cassi.spandmb.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cassi.spandmb.entity.User;

@Controller
public class ConversionServiceController {

	@RequestMapping("/inputDate")
	public String inputDate(@RequestParam("date") Date date) {
		System.out.println(date);
		return "hello";
	}
	
	@RequestMapping("/inputObj")
	public String inputObj(@RequestParam("name") User obj ) {
		System.out.println(obj.getName());
		return "hello";
	}
	
}

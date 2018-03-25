package com.cassi.spandmb.controller;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cassi.spandmb.entity.RequestResult;
import com.cassi.spandmb.entity.SimpleTable;
import com.cassi.spandmb.entity.User;

@Controller
@CrossOrigin("http://localhost:3000")
public class DatabaseController {
	
	private static SqlSessionFactory factoryBean = null;
	
	private static Logger logger = LoggerFactory.getILoggerFactory().getLogger("fileLog");
	
	static {
		InputStream input = null;
		try {
			input = Resources.getResourceAsStream("mybatis-config.xml");
			System.out.println("DataController classLoader is " + DatabaseController.class.getClassLoader());;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		factoryBean = new SqlSessionFactoryBuilder().build(input);
	}

	@RequestMapping("/insertSimpleTable/{username}")
	@ResponseBody
	public String insertSimpleTable(@PathVariable("username") String userName) {
		SqlSession session = factoryBean.openSession();
		SimpleTable simpleTable = new SimpleTable();
		simpleTable.setUsername(userName);
		session.insert("com.cassi.spandmb.entity.simpletablemapper.save",simpleTable);
		session.commit();
		session.close();
		return "successful!";
	}
	
	@RequestMapping("/getSimpleTable/{id}")
	@ResponseBody
	public SimpleTable getSimpleTable(@PathVariable("id") int id) {
		SqlSession session = factoryBean.openSession();
		SimpleTable table = null;
		try {
			table = session.selectOne("com.cassi.spandmb.entity.simpletablemapper.getSimpleTableById",id);
			session.commit();
		} finally {
			session.close();
		}
		return table;
	}
	
	@RequestMapping("/getSimpleTables/{id}")
	@ResponseBody
	public List<SimpleTable> getSimpleTables(@PathVariable("id") int id) {
		logger.info("id is " + id);
		SqlSession session = factoryBean.openSession(); 
		List<SimpleTable> tables = null;
		try {
			ArrayList<Integer> idList = new ArrayList<Integer>();
			for(int i = 0;i < 10;i++)
				idList.add(i);
			tables = session.selectList("com.cassi.spandmb.entity.simpletablemapper.getSimpleTableByIds",idList);
			session.commit();
		} finally {
			session.close();
		}
		return tables;
	}
	
	@RequestMapping("/login")
	@ResponseBody
	public RequestResult login(@RequestBody User user,HttpServletResponse response) {
		RequestResult result = new RequestResult();
		SqlSession session = factoryBean.openSession();
		User failedUser = new User();
		failedUser.setId(-1);
		try {
			HashMap<String,String> parameters = new HashMap<String,String>();
			parameters.put("userName", user.getName());
			String salt = session.selectOne("com.cassi.spandmb.entity.simpletablemapper.getSalt", user.getName());
			if(salt == null || salt.length() == 0) {
				result.setReturnCode(0);
				result.setReturnObj(failedUser);
				return result;
			}
			String[] returnInfo = null;
			try {
				returnInfo = passwordEncode(user.getPassword(), salt);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			parameters.put("password", returnInfo[0]);
			User returnUser = session.selectOne("com.cassi.spandmb.entity.simpletablemapper.loginCheck", parameters);
			returnUser.setPassword(null);
			returnUser.setSalt(null);
			if(returnUser != null && returnUser.getName() != null && returnUser.getName().length() > 0) {
				result.setReturnCode(1);
				result.setReturnObj(returnUser);
			}
			else {
				result.setReturnCode(0);
				result.setReturnObj(failedUser);
				result.setMessage("UserName or password is wrong!");
			}
		} finally {
			session.close();
		}
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		return result;
	}
	
	@RequestMapping("/register")
	@ResponseBody
	public RequestResult register(@RequestBody User user) {
		RequestResult result = new RequestResult();
		//System.out.println(user.getName() + user.getPassword());
		//result.setReturnCode(1);
		SqlSession session = factoryBean.openSession();
		try {
			HashMap<String,String> parameters = new HashMap<String,String>();
			parameters.put("userName", user.getName());
			
			String[] returnInfo = null;
			try {
				returnInfo = passwordEncode(user.getPassword(),null);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			parameters.put("password", returnInfo[0]);
			parameters.put("salt",returnInfo[1]);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			parameters.put("signuptime", format.format(new Date()));
			User returnUser = session.selectOne("com.cassi.spandmb.entity.simpletablemapper.register", parameters);
			if(returnUser != null && returnUser.getName() != null && returnUser.getName().length() > 0)
				result.setReturnCode(1);
		} finally {
			session.close();
		}
		
		return result;
	}
	
	private String[] passwordEncode(String password, String salt) throws NoSuchAlgorithmException {
		byte[] saltByte = null;
		if(salt == null) {
			saltByte = new byte[16];
			SecureRandom random = new SecureRandom();
			random.nextBytes(saltByte);
		}
		else {
			saltByte = toByte(salt);
		}
		
		byte[] passwordByte = toByte(password);
		saltByte = Arrays.copyOf(saltByte, 32);
		System.arraycopy(passwordByte, 0, saltByte, 16, passwordByte.length);
		MessageDigest disgest = MessageDigest.getInstance("SHA-256");
		disgest.update(saltByte);
		byte[] finalPasswordByte = disgest.digest();
		System.out.println(finalPasswordByte.length);
		String finalPasswordString = toHexString(finalPasswordByte);
		String[] returnString = new String[2];
		returnString[0] = finalPasswordString;
		returnString[1] = salt == null ? toHexString(saltByte) : salt;
		
		return returnString;
	}
	
	private String toHexString(byte[] inputArray) {
		if(inputArray == null || inputArray.length == 0)
			return null;
		StringBuilder builder = new StringBuilder();
		for(int i = 0;i < inputArray.length;i++) {
			builder.append(String.format("%02x", inputArray[i]));
		}
		
		return builder.toString();
	}
	
	private byte[] toByte(String inputStr) {
		if(inputStr == null || inputStr.length() == 0)
			return null;
		byte[] result = new byte[inputStr.length() / 2 + inputStr.length() % 2];
		for(int i = 0;i < inputStr.length() / 2;i++) {
			result[i] = (byte) Integer.parseInt(inputStr.substring(i * 2, i * 2 + 2), 16);
		}
		
		if(inputStr.length() % 2 == 1) {
			result[result.length - 1] = (byte) Integer.parseInt(inputStr.substring(inputStr.length() - 1,inputStr.length()),16);
		}
		
		return result;
	}
}

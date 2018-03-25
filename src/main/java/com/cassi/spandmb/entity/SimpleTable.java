package com.cassi.spandmb.entity;

public class SimpleTable {

	static {
		System.out.println("SimpleTable.classloader" + SimpleTable.class.getClassLoader());
	}
	
	private int id;
	private String username;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}

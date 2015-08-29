package com.ranga.threads;

import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("taskData")
@Scope("prototype")
public class TaskData{
	private String message;
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public void print()
	{
		System.out.println(new Date() + "==> "+message);
	}
}
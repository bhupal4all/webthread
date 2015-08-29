package com.ranga.threads;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.ranga.ApplicationConfig;
import com.ranga.threads.TaskData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

@Component("taskExecutorExample")
@Scope("prototype")
public class TaskExecutorExample implements Runnable {
	@Autowired
	private ApplicationConfig applicationConfig;
	
	public TaskExecutorExample() {
	}
	
	public void run() {
		for(int i=0;i<10;i++){
			TaskData taskData = (TaskData) applicationConfig.getAppCtx().getBean("taskData");
			
			try {
				if (taskData != null) {
					taskData.setMessage(Thread.currentThread().getThreadGroup().getName() + "/" + Thread.currentThread().getName() + " Itr : " + i);
					taskData.print();
				}
				else
				{
					System.out.println("null");
				}
				
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
	}
}
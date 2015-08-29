package com.ranga.threads;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ranga.ApplicationConfig;

@Component("workRunnable")
@Scope("prototype")
public class WorkRunnable implements Runnable {
	@Autowired
	private ApplicationConfig applicationConfig;

	@PreDestroy
	public void cleanUp() {
		System.out.println(Thread.currentThread().getName() + "Cleanup");
	}
	
	public void stop(){
		Thread.currentThread().stop();
	}

	public void run() {
		// for(int i=0;i<10;i++){
		// TaskData taskData = (TaskData)
		// applicationConfig.getAppCtx().getBean("taskData");
		//			
		// try {
		// if (taskData != null) {
		// taskData.setMessage(Thread.currentThread().getThreadGroup().getName()
		// + "/" + Thread.currentThread().getName() + " Itr : " + i);
		// taskData.print();
		// }
		// else
		// {
		// System.out.println("null");
		// }
		//				
		// Thread.sleep(5000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		TaskData taskData = (TaskData) applicationConfig.getAppCtx().getBean(
				"taskData");
		if (taskData != null) {
			taskData.setMessage(Thread.currentThread().getThreadGroup()
					.getName()
					+ "/" + Thread.currentThread().getName());
			taskData.print();
		} else {
			System.out.println("null");
		}
	}
}
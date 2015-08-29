package com.ranga;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component("applictionConfig")
@Scope("application")
public class ApplicationConfig implements
		org.springframework.context.ApplicationContextAware {
	static final long serialVersionUID = 02L;

	ApplicationContext applicationContext = null;
	ThreadPoolTaskExecutor taskExecutor = null;

	@PostConstruct
	protected void init() {
		System.out.println("---init---");
		initializeThreadExecutor();
	}

	@PreDestroy
	protected void cleanUp() throws Exception {
		System.out.println("---clean up---");
		if (taskExecutor != null)
			taskExecutor.shutdown();
	}

	protected void initializeThreadExecutor() {
		taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(2);
		taskExecutor.setMaxPoolSize(5);
		taskExecutor.setAllowCoreThreadTimeOut(true);
		taskExecutor.setThreadGroupName("Admin Group");
		taskExecutor.setThreadNamePrefix("Worker Thread ");
		taskExecutor.initialize();
	}

	public void setApplicationContext(
			final ApplicationContext applicationContext)
			throws org.springframework.beans.BeansException {
		System.out.println("setting context");

		this.applicationContext = applicationContext;
	}

	public ApplicationContext getAppCtx() {
		return applicationContext;
	}

	public ThreadPoolTaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

}
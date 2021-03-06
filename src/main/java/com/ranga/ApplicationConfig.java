package com.ranga;

import java.util.Date;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.ranga.threads.WorkRunnable;

@Component("applictionConfig")
@Scope("application")
public class ApplicationConfig implements
		org.springframework.context.ApplicationContextAware,
		ApplicationListener<ContextRefreshedEvent> {
	static final long serialVersionUID = 02L;

	ApplicationContext applicationContext = null;
	ThreadPoolTaskScheduler taskScheduler = null;
	ThreadPoolTaskExecutor taskExecutor = null;

	@PostConstruct
	protected void init() {
		System.out.println("---init---");
		initializeScheduler();
		initializeThreadExecutor();
	}

	@PreDestroy
	protected void cleanUp() throws Exception {
		System.out.println("---clean up---");
		if (taskScheduler != null)
			taskScheduler.shutdown();
		if (taskExecutor != null)
			taskExecutor.shutdown();
	}

	protected void initializeScheduler() {
		taskScheduler = new ThreadPoolTaskScheduler();

		ThreadFactory threadFactory = new ThreadFactory() {
			@Override
			public Thread newThread(Runnable obj) {
				return new Thread(obj);
			}
		};

		RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable thread,
					ThreadPoolExecutor executor) {
				System.out.println("Handler.... Thread = "
						+ ((Thread) thread).getName());
			}
		};

		taskScheduler.setThreadFactory(threadFactory);
		taskScheduler.setRejectedExecutionHandler(rejectedExecutionHandler);
		taskScheduler.setThreadNamePrefix("TaskScheduler");
		taskScheduler.initialize();
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

	public ThreadPoolTaskScheduler getTaskScheduler() {
		return taskScheduler;
	}

	public ThreadPoolTaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	// //////////////////////////////////////////////////////////////////////
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println("Application is initialized");

		WorkRunnable swr = (WorkRunnable) getAppCtx().getBean("workRunnable");
		getTaskScheduler().schedule(swr, new CronTrigger("*/2 * * * * *"));
	}

}
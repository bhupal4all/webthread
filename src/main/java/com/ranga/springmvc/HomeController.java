package com.ranga.springmvc;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ranga.ApplicationConfig;
import com.ranga.threads.WorkRunnable;

/**
 * Handles requests for the application home page.
 */
@Controller
@Scope(value = "application")
public class HomeController {
	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	@Autowired
	ApplicationConfig applicationConfig;

	ScheduledFuture<WorkRunnable> scheduledTask = null;

	/**
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		int count = applicationConfig.getTaskExecutor().getActiveCount();

		if (count < 1) {
			for (int i = 0; i < 5; i++) {
				WorkRunnable wr1 = (WorkRunnable) applicationConfig.getAppCtx()
						.getBean("workRunnable");
				applicationConfig.getTaskExecutor().execute(
						new java.lang.Thread(wr1));
			}
		}

		if (scheduledTask == null || scheduledTask.isDone() || scheduledTask.isCancelled()) {
			WorkRunnable swr = (WorkRunnable) applicationConfig.getAppCtx()
					.getBean("workRunnable");
			scheduledTask = applicationConfig.getTaskScheduler()
					.scheduleWithFixedDelay(swr, 2000);
		}
		else
		{
			scheduledTask.cancel(true);
		}

		return "home";
	}
}

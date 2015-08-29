package com.ranga;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("applictionConfig")
@Scope("application")
public class ApplicationConfig implements org.springframework.context.ApplicationContextAware  {
	static final long serialVersionUID = 02L;
 
    ApplicationContext applicationContext = null;
	
	public ApplicationContext getAppCtx() {
		return applicationContext;
	}
  
    public void setApplicationContext(final ApplicationContext applicationContext) throws org.springframework.beans.BeansException {
        System.out.println("setting context");
        this.applicationContext = applicationContext;
    }
}
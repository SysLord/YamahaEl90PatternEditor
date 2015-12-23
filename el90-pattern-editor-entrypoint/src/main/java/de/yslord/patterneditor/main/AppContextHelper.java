package de.yslord.patterneditor.main;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import util.LogUtil;

@Component
public class AppContextHelper implements ApplicationContextAware {

	private static ApplicationContext CONTEXT;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		CONTEXT = applicationContext;

		LogUtil.log("GOT SPRING CONTEXT!");
	}

	public static <T> T getBean(Class<T> clazz) {
		return CONTEXT.getBean(clazz);
	}

}

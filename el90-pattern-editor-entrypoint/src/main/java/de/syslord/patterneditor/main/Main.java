package de.syslord.patterneditor.main;

import javafx.application.Application;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import de.syslord.electonePattern.application.PatternFxApplication;
import de.syslord.patterneditor.spring.EntrypointSpringConfig;

public class Main {

	/**
	 * Pattern Editor main entrypoint.
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				EntrypointSpringConfig.class);

		Application.launch(PatternFxApplication.class);
		annotationConfigApplicationContext.close();
	}
}

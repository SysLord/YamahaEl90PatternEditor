package de.yslord.patterneditor.main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import parser.ParserMain;
import util.LogUtil;
import de.syslord.electonePattern.main.PatternWindow;
import de.syslord.patterneditor.spring.EntrypointSpringConfig;
import electone.constants.PatternVariation;
import electone.dataobjects.Pattern;
import electone.dataobjects.PatternIdent;
import electone.dataobjects.Patterns;

public class Main {

	public static void main(String[] args) {

		if (args.length < 1) {
			LogUtil.log("Expect B00 file as first parameter.");
			return;
		}

		@SuppressWarnings("resource")
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				EntrypointSpringConfig.class);

		String b00path = args[0];
		ParserMain parserMain = annotationConfigApplicationContext.getBean(ParserMain.class);

		Patterns patterns = parserMain.parsePatternsFromFile(b00path);

		LogUtil.logDebug(patterns, "RESULT");

		PatternWindow patternWindow = new PatternWindow();
		// patternWindow.getPresenter().setModel(Pattern.createEmptyPattern());

		LogUtil.log(patterns.getAvailablePatterns(), "availble patterns");

		Pattern fallback = patterns.get(PatternIdent.of(1, PatternVariation.A));
		Pattern orElse = patterns.getAvailablePatterns().stream()
				.map(ident -> patterns.get(ident))
				.filter(p -> p.getNumberOfSounds() > 0)
				.findFirst()
				.orElse(fallback);

		LogUtil.log(orElse.getNumberOfSounds(), "NUMBER OF SOUNDS");

		patternWindow.getPresenter().setModel(orElse);

		patternWindow.setVisible(true);
	}
}

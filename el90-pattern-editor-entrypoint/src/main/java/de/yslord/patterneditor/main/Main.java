package de.yslord.patterneditor.main;

import javafx.application.Application;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import parser.ParserMain;
import util.LogUtil;
import de.syslord.electonePattern.fxui.PatternFxApplication;
import de.syslord.electonePattern.main_OLD.PatternWindow;
import de.syslord.patterneditor.spring.EntrypointSpringConfig;
import electone.constants.PatternVariation;
import electone.dataobjects.Pattern;
import electone.dataobjects.PatternIdent;
import electone.dataobjects.Patterns;

// TODO clean up
public class Main {

	public static void main(String[] args) {

		// if (args.length < 1) {
		// LogUtil.log("Expect B00 file as first parameter.");
		// return;
		// }

		@SuppressWarnings({ "resource", "unused" })
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				EntrypointSpringConfig.class);

		// String b00path = args[0];
		// ParserMain parserMain = annotationConfigApplicationContext.getBean(ParserMain.class);

		// parseForFun(b00path, parserMain);
		Application.launch(PatternFxApplication.class);

		// oldWindow(patterns);
	}

	private static void parseForFun(String b00path, ParserMain parserMain) {
		Patterns patterns = parserMain.parsePatternsFromFile(b00path);
		LogUtil.logDebugDump(patterns, "RESULT");
	}

	// TODO remove
	private static void oldWindow(Patterns patterns) {
		PatternWindow patternWindow = new PatternWindow();
		// patternWindow.getPresenter().setModel(Pattern.createEmptyPattern());

		LogUtil.logDump(patterns.getAvailablePatterns(), "availble patterns");

		Pattern fallback = patterns.get(PatternIdent.of(1, PatternVariation.A));
		Pattern orElse = patterns.getAvailablePatterns().stream()
				.map(ident -> patterns.get(ident))
				.filter(p -> p.getNumberOfSounds() > 0)
				.findFirst()
				.orElse(fallback);

		LogUtil.logDump(orElse.getNumberOfSounds(), "NUMBER OF SOUNDS");

		patternWindow.getPresenter().setModel(orElse);

		patternWindow.setVisible(true);
	}
}

package de.syslord.electonePattern.fxui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import parser.ParserMain;
import util.LogUtil;
import de.syslord.electonePattern.Audio.Player;
import de.syslord.electonePattern.spring.AppContextHelper;
import electone.constants.PatternVariation;
import electone.dataobjects.Pattern;
import electone.dataobjects.PatternIdent;
import electone.dataobjects.Patterns;

public class PatternFxApplication extends Application {

	private Player player;

	@Override
	public void start(Stage primaryStage) {

		player = new Player(pos -> handlePos(pos));
		Pattern pattern = parseForFun();
		player.setModel(pattern);
		PatternModel patternModel = PatternModelFactory.INSTANCE.createModel(pattern);

		createStage(primaryStage, patternModel);

	}

	@Override
	public void stop() throws Exception {
		player.stop();
		super.stop();
	}

	private void handlePos(int listener) {
		// TODO
	}

	private void createStage(Stage primaryStage, PatternModel patternModel) {
		primaryStage.setTitle("EL90 Pattern Editor");

		BorderPane mainLayout = new BorderPane();
		Scene scene = new Scene(mainLayout, 700, 800);

		createLayout(mainLayout, patternModel);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void createLayout(BorderPane mainLayout, PatternModel patternModel) {

		PatternControl patternControl = new PatternControl(patternModel);
		patternControl.setBackground(new Background(new BackgroundFill(Color.AZURE, null, null)));

		mainLayout.setCenter(patternControl);

		ComboBox<Integer> speed = new ComboBox<Integer>();
		List<Integer> collect = IntStream.range(0, (250 - 40) / 10)
				.boxed()
				.map(val -> (10 * val) + 40)
				.collect(Collectors.toList());

		speed.setItems(FXCollections.observableArrayList(collect));
		speed.setValue(120);

		Button play = new Button("play");
		play.setOnMouseClicked(e -> player.start(speed.getValue()));
		Button stop = new Button("stop");
		stop.setOnMouseClicked(e -> player.stop());

		mainLayout.setTop(new HBox(play, stop, speed));
	}

	private Pattern parseForFun() {
		ParserMain parserMain = AppContextHelper.getBean(ParserMain.class);
		Patterns patterns = parserMain.parsePatternsFromFile("C:\\JEEworkspace\\RHY_MDR_00.B00");

		LogUtil.logDump(patterns.getAvailablePatterns(), "availble patterns");

		Pattern fallback = patterns.get(PatternIdent.of(1, PatternVariation.A));
		Pattern orElse = patterns.getAvailablePatterns().stream()
				.map(ident -> patterns.get(ident))
				.filter(p -> p.getNumberOfSounds() > 15)
				.findFirst()
				.orElse(fallback);

		LogUtil.logDump(orElse.getNumberOfSounds(), "NUMBER OF SOUNDS");
		return orElse;
	}
}

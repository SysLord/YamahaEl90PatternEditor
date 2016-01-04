package de.syslord.electonePattern.application;

import javafx.application.Application;
import javafx.application.Platform;
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

import de.syslord.electonePattern.Audio.AudioPlayer;
import de.syslord.electonePattern.fxui.PatternControl;
import de.syslord.electonePattern.spring.AppContextHelper;
import de.syslord.electonePattern.util.IntStreamUtil;
import electone.constants.PatternVariation;
import electone.dataobjects.Pattern;
import electone.dataobjects.PatternIdent;
import electone.dataobjects.Patterns;
import parser.ParserMain;
import util.LogUtil;

public class PatternFxApplication extends Application {

	private AudioPlayer player;
	private PatternControl patternControl;

	@Override
	public void start(Stage primaryStage) {

		//////////////

		String path = "../exampleB00File/EXAMPLE__0A-0FILL__1A-1FILL__MDR_03.B00";
		Patterns patterns = parsePatternsAndChooseOne(path);
		Pattern pattern = patterns.get(PatternIdent.of(1, PatternVariation.A));

		//////////////

		player = new AudioPlayer();
		player.setPositionListener(pos -> handlePos(pos));
		player.setModel(pattern);

		createStage(primaryStage);

		patternControl.patternProperty().set(pattern);
	}

	private void createStage(Stage primaryStage) {
		primaryStage.setTitle("EL90 Pattern Editor");

		BorderPane mainLayout = new BorderPane();
		Scene scene = new Scene(mainLayout, 700, 800);

		createLayout(mainLayout);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		player.stop();
		player.close();
		super.stop();
	}

	private void handlePos(int positionValue) {
		Platform.runLater(() -> patternControl.highlightProperty().set(positionValue));
	}

	private void createLayout(BorderPane mainLayout) {

		patternControl = new PatternControl();
		patternControl.setBackground(new Background(new BackgroundFill(Color.AZURE, null, null)));

		mainLayout.setCenter(patternControl);

		ComboBox<Integer> speed = new ComboBox<Integer>();
		List<Integer> collect = IntStreamUtil.intStream(10, 10, 250).collect(Collectors.toList());

		speed.setItems(FXCollections.observableArrayList(collect));
		speed.setValue(120);

		Button play = new Button("play");
		play.setOnMouseClicked(e -> player.start(speed.getValue()));
		Button stop = new Button("stop");
		stop.setOnMouseClicked(e -> player.stop());

		mainLayout.setTop(new HBox(play, stop, speed));
	}

	private Patterns parsePatternsAndChooseOne(String path) {
		ParserMain parserMain = AppContextHelper.getBean(ParserMain.class);
		Patterns patterns = parserMain.parsePatternsFromFile(path);

		LogUtil.logDump(patterns.getAvailablePatterns(), "availble patterns");
		return patterns;
	}
}

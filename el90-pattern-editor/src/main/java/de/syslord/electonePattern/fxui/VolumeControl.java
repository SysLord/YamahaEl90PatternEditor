package de.syslord.electonePattern.fxui;

import javafx.beans.binding.DoubleBinding;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class VolumeControl extends Pane {

	private Rectangle rectangle;
	private VolumeModel volume;
	private int steps;

	public VolumeControl(VolumeModel volume, int steps) {
		this.volume = volume;
		this.steps = steps;

		createLayout();
		createListeners();
		setupProperties();
	}

	private void createLayout() {
		setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, null, null)));

		rectangle = new Rectangle();
		rectangle.setFill(Color.BLACK);
		rectangle.setStroke(Color.RED);
		rectangle.setStrokeType(StrokeType.INSIDE);

		getChildren().add(rectangle);
	}

	private void createListeners() {
		setOnMouseClicked(event -> updateFromMouse(event.getY()));
		setOnMouseDragged(event -> handleMouseDrag(event));
	}

	private void handleMouseDrag(MouseEvent event) {
		if (event.isPrimaryButtonDown()) {
			updateFromMouse(event.getY());
		}
	}

	private void setupProperties() {
		rectangle.setX(0);
		rectangle.widthProperty().bind(this.widthProperty());

		DoubleBinding verticalSize = this.heightProperty().multiply(this.volume.getVolumeProperty())
				.divide(steps - 1);
		rectangle.yProperty().bind(this.heightProperty().subtract(verticalSize));
		rectangle.heightProperty().bind(verticalSize);
	}

	private void updateFromMouse(double y) {
		// TODO buggy, may lead to -1 or maybe too large values?
		double stepheight = getHeight() / steps;
		int value = steps - (int) (y / stepheight);
		volume.setVolume(value - 1);
	}

}

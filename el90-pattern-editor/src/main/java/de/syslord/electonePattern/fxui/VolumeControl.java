package de.syslord.electonePattern.fxui;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import util.DebugUtil;
import electone.dataobjects.Volume;

public class VolumeControl extends Pane {

	private Volume volume;

	private IntegerProperty highlightProperty = new SimpleIntegerProperty(0);

	private BooleanProperty highlightVisibleProperty = new SimpleBooleanProperty(false);
	private int steps;

	public VolumeControl(Volume volume) {
		this.volume = volume;
		steps = Volume.MAX_VOLUME;

		createLayout();
		createListeners();
	}

	private void createLayout() {
		setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, null, null)));

		Rectangle rectangle = createRectangle();
		Line highlight = createHighlightLine();

		getChildren().add(rectangle);
		getChildren().add(highlight);

		DebugUtil.onDebug(() -> addDebugLine());
	}

	private void addDebugLine() {
		Line line = new Line();
		line.setStartX(0);
		line.setEndX(0);
		line.setStartY(0);
		line.setEndY(4);
		getChildren().add(line);
	}

	private Line createHighlightLine() {
		Line highlight = new Line();
		highlight.startXProperty().bind(highlightProperty);
		highlight.endXProperty().bind(highlightProperty);
		highlight.setStartY(4);
		highlight.setEndY(8);
		highlight.visibleProperty().bind(highlightVisibleProperty);
		return highlight;
	}

	private Rectangle createRectangle() {
		Rectangle rectangle = new Rectangle();
		rectangle.setFill(Color.BLACK);
		rectangle.setStroke(Color.RED);
		rectangle.setStrokeType(StrokeType.INSIDE);

		rectangle.setX(0);
		rectangle.widthProperty().bind(widthProperty());

		DoubleBinding verticalSize = heightProperty().multiply(volume.volumeProperty()).divide(steps - 1);
		rectangle.yProperty().bind(this.heightProperty().subtract(verticalSize));
		rectangle.heightProperty().bind(verticalSize);
		return rectangle;
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

	private void updateFromMouse(double y) {
		// TODO buggy, may lead to -1 or maybe too large values?
		double stepheight = getHeight() / steps;
		int value = steps - (int) (y / stepheight);
		volume.setVolume(value - 1);
	}

	public void updateHighlight(int i) {
		highlightProperty.set(i);
		highlightVisibleProperty.set(true);
	}

	public BooleanProperty highlightVisibleProperty() {
		return highlightVisibleProperty;
	}

}

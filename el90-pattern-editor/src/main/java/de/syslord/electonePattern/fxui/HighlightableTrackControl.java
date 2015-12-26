package de.syslord.electonePattern.fxui;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

public class HighlightableTrackControl extends StackPane {

	private TrackControl control;

	private DoubleProperty highlightStepProperty = new SimpleDoubleProperty(this, "highlight", 0);

	private BooleanProperty highlightVisibleProperty = new SimpleBooleanProperty(this, "highlightVisibility", false);

	public HighlightableTrackControl(TrackModel track) {
		control = new TrackControl(track);
		initControls();
	}

	private void initControls() {
		getChildren().clear();

		control.prefHeightProperty().bind(control.heightProperty());
		control.prefWidthProperty().bind(control.widthProperty());

		getChildren().add(control);

		addLine();
	}

	private void addLine() {
		Line highlight = new Line();
		highlight.startYProperty().set(0);
		highlight.endYProperty().bind(heightProperty().subtract(1));

		// TODO constants
		DoubleBinding stepX = highlightStepProperty.multiply(widthProperty()).divide(2 * 4 * 24);
		highlight.startXProperty().bind(stepX);
		highlight.endXProperty().bind(stepX);

		highlight.visibleProperty().bind(highlightVisibleProperty);
		getChildren().add(highlight);

		highlightVisibleProperty.set(true);
		highlightStepProperty.set(17);
	}

	public void setHighlight(int highlightStep, boolean highlightVisible) {
		highlightVisibleProperty.set(highlightVisible);
		highlightStepProperty.set(highlightStep);
	}

}

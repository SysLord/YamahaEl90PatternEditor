package de.syslord.electonePattern.fxui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.VBox;

import electone.dataobjects.Pattern;
import electone.dataobjects.Track;

public class PatternControl extends VBox {

	private IntegerProperty highlightProperty = new SimpleIntegerProperty(0);

	private ObjectProperty<Pattern> patternProperty = new SimpleObjectProperty<>(this, "pattern", null);

	public PatternControl() {
		patternProperty.addListener((x, y, z) -> initControls());
	}

	private void initControls() {
		this.getChildren().clear();

		Pattern pattern = patternProperty.get();
		int controlsCount = pattern.getTracks().size();

		for (Track track : pattern.getTracks()) {
			TrackControl trackControl = new TrackControl(track);
			// HighlightableTrackControl trackControl = new HighlightableTrackControl(track);
			trackControl.maxHeightProperty().bind(this.heightProperty().divide(controlsCount));
			trackControl.prefHeightProperty().bind(this.heightProperty().divide(controlsCount));

			trackControl.maxWidthProperty().bind(this.widthProperty());
			trackControl.prefWidthProperty().bind(this.widthProperty());

			trackControl.highlightProperty().bind(highlightProperty);

			this.getChildren().add(trackControl);
		}
	}

	public IntegerProperty highlightProperty() {
		return highlightProperty;
	}

	public ObjectProperty<Pattern> patternProperty() {
		return patternProperty;
	}
}

package de.syslord.electonePattern.fxui;

import javafx.scene.layout.VBox;

public class PatternControl extends VBox {

	private PatternModel pattern;

	public PatternControl(PatternModel pattern) {
		this.pattern = pattern;
		initControls();
	}

	private void initControls() {
		this.getChildren().clear();

		int controlsCount = pattern.getTracks().size();

		for (TrackModel track : pattern.getTracks()) {
			TrackControl trackControl = new TrackControl(track);
			// HighlightableTrackControl trackControl = new HighlightableTrackControl(track);
			trackControl.maxHeightProperty().bind(this.heightProperty().divide(controlsCount));
			trackControl.prefHeightProperty().bind(this.heightProperty().divide(controlsCount));

			trackControl.maxWidthProperty().bind(this.widthProperty());
			trackControl.prefWidthProperty().bind(this.widthProperty());

			this.getChildren().add(trackControl);
		}
	}
}

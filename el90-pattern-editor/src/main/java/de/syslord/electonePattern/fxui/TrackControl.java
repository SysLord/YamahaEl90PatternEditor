package de.syslord.electonePattern.fxui;

import javafx.scene.layout.HBox;

public class TrackControl extends HBox {

	private TrackModel track;

	public TrackControl(TrackModel track) {
		this.track = track;
		initControls();
	}

	private void initControls() {
		this.getChildren().clear();

		int controlsCount = track.size();

		for (VolumeModel volume : track) {
			VolumeControl volumeControl = new VolumeControl(volume, VolumeModel.VOL_STEPS);
			volumeControl.maxHeightProperty().bind(this.heightProperty());
			volumeControl.prefHeightProperty().bind(this.heightProperty());

			volumeControl.maxWidthProperty().bind(this.widthProperty().divide(controlsCount));
			volumeControl.prefWidthProperty().bind(this.widthProperty().divide(controlsCount));

			this.getChildren().add(volumeControl);
		}
	}
}

package de.syslord.electonePattern.fxui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

import electone.dataobjects.Track;
import electone.dataobjects.Volume;

//TODO wrap TrackControl in another HBox for instrument name and track user controls.
public class TrackControl extends HBox {

	private static final int TRACK_DESCRIPTION_WIDTH = 200;
	private Track track;
	private IntegerProperty highlightProperty = new SimpleIntegerProperty(0);

	public TrackControl(Track track) {
		this.track = track;
		initControls();
	}

	private void initControls() {
		this.getChildren().clear();

		Label label = new Label();
		this.getChildren().add(label);
		label.textProperty().bind(
				track.trackIndexProperty().asString().concat(track.instrumentNameProperty()).concat(highlightProperty));

		label.maxHeightProperty().bind(this.heightProperty());
		label.prefHeightProperty().bind(this.heightProperty());
		label.setMaxWidth(TRACK_DESCRIPTION_WIDTH);
		label.setMinWidth(TRACK_DESCRIPTION_WIDTH);
		label.setPrefWidth(TRACK_DESCRIPTION_WIDTH);

		int controlsCount = track.size();

		List<VolumeControl> controls = new ArrayList<>();

		for (Volume volume : track.getVolumes()) {
			VolumeControl volumeControl = new VolumeControl(volume);
			volumeControl.maxHeightProperty().bind(this.heightProperty());
			volumeControl.prefHeightProperty().bind(this.heightProperty());

			volumeControl.maxWidthProperty().bind(this.widthProperty().divide(controlsCount));
			volumeControl.prefWidthProperty().bind(this.widthProperty().divide(controlsCount));

			controls.add(volumeControl);
			this.getChildren().add(volumeControl);
		}

		// TODO maybe give children a certain range responsibility?
		highlightProperty.addListener(e -> {

			controls.forEach(c -> c.highlightVisibleProperty().set(false));

			int i = highlightProperty.get();
			// TODO mess
				int abdeckungEinControl = (2 * 4 * 24) / controlsCount;
				int child = i / abdeckungEinControl;
				controls.get(child).updateHighlight(i % abdeckungEinControl);
			});
	}

	public IntegerProperty highlightProperty() {
		return highlightProperty;
	}
}

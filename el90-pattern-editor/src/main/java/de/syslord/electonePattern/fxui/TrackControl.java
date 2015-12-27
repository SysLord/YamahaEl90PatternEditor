package de.syslord.electonePattern.fxui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

//TODO wrap TrackControl in another HBox for instrument name and track user controls.
public class TrackControl extends HBox {

	private TrackModel trackModel;
	private IntegerProperty highlightProperty = new SimpleIntegerProperty(0);

	public TrackControl(TrackModel track) {
		this.trackModel = track;
		initControls();
	}

	private void initControls() {
		this.getChildren().clear();

		Label label = new Label();
		this.getChildren().add(label);
		label.textProperty().bind(trackModel.instrumentNameProperty().concat(highlightProperty));

		label.maxHeightProperty().bind(this.heightProperty());
		label.prefHeightProperty().bind(this.heightProperty());
		label.setMaxWidth(200);
		label.setMinWidth(200);
		label.setPrefWidth(200);

		int controlsCount = trackModel.size();

		List<VolumeControl> controls = new ArrayList<>();

		for (VolumeModel volume : trackModel) {
			VolumeControl volumeControl = new VolumeControl(volume, VolumeModel.VOL_STEPS);
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
				System.out.println(i);
				System.out.println(child);
				controls.get(child).updateHighlight(i % abdeckungEinControl);
			});
	}

	public IntegerProperty highlightProperty() {
		return highlightProperty;
	}
}

package de.syslord.electonePattern.fxui;

import java.util.Iterator;
import java.util.List;

public class TrackModel implements Iterable<VolumeModel> {

	private List<VolumeModel> track;

	// ObservableList<VolumeModel> track;

	public TrackModel(List<VolumeModel> track) {
		this.track = track;
		// track = FXCollections.observableArrayList(model);
	}

	@Override
	public Iterator<VolumeModel> iterator() {
		return track.iterator();
	}

	public int size() {
		return track.size();
	}

}

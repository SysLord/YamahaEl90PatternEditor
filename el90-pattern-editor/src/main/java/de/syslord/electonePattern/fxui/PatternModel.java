package de.syslord.electonePattern.fxui;

import java.util.List;

public class PatternModel {

	private List<TrackModel> tracks;

	public PatternModel(List<TrackModel> tracks) {
		this.tracks = tracks;
	}

	public List<TrackModel> getTracks() {
		return tracks;
	}

}

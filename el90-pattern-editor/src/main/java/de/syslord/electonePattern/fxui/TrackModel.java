package de.syslord.electonePattern.fxui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Iterator;
import java.util.List;

import electone.dataobjects.Instrument;

public class TrackModel implements Iterable<VolumeModel> {

	private List<VolumeModel> track;

	private StringProperty instrumentNameProperty;

	private int channelIndex;

	public TrackModel(int channelIndex, Instrument instrument, List<VolumeModel> track) {
		this.channelIndex = channelIndex;
		this.track = track;
		// TODO properly handle and sort by channelIndex. In TrackControl class.
		instrumentNameProperty = new SimpleStringProperty(this.channelIndex + " " + instrument.getName());
	}

	@Override
	public Iterator<VolumeModel> iterator() {
		return track.iterator();
	}

	public int size() {
		return track.size();
	}

	public StringProperty instrumentNameProperty() {
		return instrumentNameProperty;
	}

}

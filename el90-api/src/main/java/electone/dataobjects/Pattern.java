package electone.dataobjects;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import electone.constants.DrumInstrument;

public class Pattern {

	private Track[] tracks;
	private PatternIdent patternIdent;
	private TimeSignature timeSignature;

	public Pattern(PatternIdent patternIdent, TimeSignature timeSignature, Track[] tracks) {
		this.patternIdent = patternIdent;
		this.timeSignature = timeSignature;
		this.tracks = tracks;
	}

	public Map<DrumInstrument, Volume> getNotes(int measure) {
		Map<DrumInstrument, Volume> map = new HashMap<>();

		for (Track trackPattern : tracks) {
			Volume volume = trackPattern.getVolume(measure);
			DrumInstrument instrument = trackPattern.getInstrument();

			if (volume.isSounding()) {
				map.put(instrument, volume);
			}
		}
		return map;
	}

	public PatternIdent getPatternIdent() {
		return patternIdent;
	}

	public List<Track> getTracks() {
		List<Track> trackList = Arrays.asList(tracks);
		Collections.sort(trackList, Track.getOrderedTracksComparator());

		return ImmutableList.copyOf(trackList);
	}

	public void setTracks(Track[] tracks) {
		this.tracks = tracks;
	}

	public TimeSignature getTimeSignature() {
		return timeSignature;
	}
}

package electone.dataobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import electone.constants.DrumInstrument;

public class Pattern {

	private List<TrackPattern> trackPatterns = new ArrayList<>();

	public Pattern() {
		//
	}

	public Pattern(List<TrackPattern> trackPatterns) {
		this.trackPatterns = trackPatterns;
	}

	public void addTrack(TrackPattern track) {
		trackPatterns.add(track);
	}

	public TrackPattern getTrackPattern(int index) {
		return trackPatterns.get(index);
	}

	public Map<DrumInstrument, Volume> getCount(int count) {
		Map<DrumInstrument, Volume> map = new HashMap<>();

		for (TrackPattern trackPattern : trackPatterns) {
			Volume volume = trackPattern.getVolume(count);
			DrumInstrument instrument = trackPattern.getInstrument();

			if (volume.isSounding()) {
				map.put(instrument, volume);
			}
		}
		return map;
	}

	@Override
	public String toString() {
		return trackPatterns.stream().map(x -> x.toString()).collect(Collectors.joining("\n"));
	}

	public Long getNumberOfSounds() {
		return trackPatterns.stream()
				.map(track -> track.getNumberOfSounds())
				.mapToLong(i -> i)
				.sum();
	}

	// public static Pattern createEmptyPattern() {
	// Pattern pattern = new Pattern();
	//
	// for (int trackCount = 0; trackCount < Pattern.MAX_TRACKS; trackCount++) {
	//
	// CountQuantization countQuantization = trackCount % 2 == 0 ? CountQuantization.WHOLE
	// : CountQuantization.THIRDS;
	//
	// List<Volume> trackPattern = TrackPattern.createEmptyPattern();
	// TrackPattern track = new TrackPattern(DrumInstrument.values()[trackCount], countQuantization, trackPattern);
	// pattern.addTrack(track);
	// }
	//
	// return pattern;
	// }

	public List<TrackPattern> getTrackPatterns() {
		return trackPatterns;
	}
}

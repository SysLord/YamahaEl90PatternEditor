package de.syslord.electonePattern.application;


public class PatternModelFactory {

	// public static final PatternModelFactory INSTANCE = new PatternModelFactory();
	//
	// private PatternModelFactory() {
	// //
	// }
	//
	// public PatternModel createModel(Pattern pattern) {
	// List<TrackModel> tracks = pattern.getTrackPatterns().stream()
	// .map(trackPattern -> createTrackModel(trackPattern))
	// .collect(Collectors.toList());
	//
	// PatternModel patternModel = new PatternModel(tracks);
	// return patternModel;
	// }
	//
	// private TrackModel createTrackModel(Track trackPattern) {
	// // we create all models for finest quantization, so we have to do nothing on quantization changes.
	// int modelsToCreate = PatternConstants.TRACK_QUANTIZATION;
	//
	// List<VolumeModel> volumes = IntStream.range(0, modelsToCreate)
	// .boxed()
	// .map(measure -> createVolumeModel(trackPattern, measure))
	// .collect(Collectors.toList());
	//
	// TrackModel trackModel = new TrackModel(trackPattern.getChannelIndex(), trackPattern.getInstrument(),
	// trackPattern.getSingleBarQuantization(), volumes);
	//
	// return trackModel;
	// }
	//
	// private VolumeModel createVolumeModel(Track trackPattern, Integer measure) {
	// Volume volume = trackPattern.getVolume(measure);
	// if (volume == null) {
	// return new VolumeModel(7);
	// }
	// return new VolumeModel(volume.getVolume());
	// }
}
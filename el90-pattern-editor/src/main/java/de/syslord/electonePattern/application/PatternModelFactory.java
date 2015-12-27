package de.syslord.electonePattern.application;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.syslord.electonePattern.fxui.PatternModel;
import de.syslord.electonePattern.fxui.TrackModel;
import de.syslord.electonePattern.fxui.VolumeModel;
import electone.dataobjects.Pattern;
import electone.dataobjects.Quantization;
import electone.dataobjects.Volume;

public class PatternModelFactory {

	public static final PatternModelFactory INSTANCE = new PatternModelFactory();

	private PatternModelFactory() {
		//
	}

	public PatternModel createModel(Pattern p) {
		List<TrackModel> tracks = p.getTrackPatterns().stream()
				.map(trackPattern -> {

					Quantization quantization = trackPattern.getQuantization();
					// TODO assume 4/4
					// int num = 2 * quantization.getCountsPerBar();
					int num = 2 * 4 * 24;
					// int num = 2 * 4 * 8;
					System.out.println("Quantization " + num);

					List<VolumeModel> volumes = IntStream.range(0, num)
							.boxed()
							.map(i1 -> {
								int count = i1;
								Volume volume = trackPattern.getVolume(count);
								if (volume == null) {
									return new VolumeModel(7);
								}

								// TODO either use int and calc relative later (good), or user float everywhere (meh).
								int volume2 = volume.getVolume();

								return new VolumeModel(volume2);
							}).collect(Collectors.toList());
					return new TrackModel(trackPattern.getChannelIndex(), trackPattern.getInstrument(), volumes);
				}).collect(Collectors.toList());

		// List<TrackModel> tracks = IntStream.range(0, 16).boxed().map(i -> {
		//
		// List<VolumeModel> volumes = IntStream.range(0, 24 * 8)
		// .boxed()
		// .map(i1 -> new VolumeModel(i1 % 8))
		// .collect(Collectors.toList());
		// return new TrackModel(volumes);
		//
		// }).collect(Collectors.toList());

		PatternModel patternModel = new PatternModel(tracks);
		return patternModel;
	}

	// // TODO for debugging
	// public PatternModel createExampleModel() {
	// List<TrackModel> tracks = IntStream.range(0, 16).boxed().map(i -> {
	//
	// List<VolumeModel> volumes = IntStream.range(0, 24 * 8)
	// .boxed()
	// .map(i1 -> new VolumeModel(i1 % 8))
	// .collect(Collectors.toList());
	// return new TrackModel(volumes);
	//
	// }).collect(Collectors.toList());
	//
	// PatternModel patternModel = new PatternModel(tracks);
	// return patternModel;
	// }

}

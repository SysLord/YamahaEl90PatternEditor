package de.syslord.electonePattern.Audio;

import java.util.Map;

import com.google.common.collect.Maps;

import electone.constants.DrumInstrument;
import electone.dataobjects.Instrument;

public class SoundLibrary {

	private static final String RESOURCE_PATH_FORMAT = "sounds/%s";

	public static Map<Instrument, String> getAudioFiles() {
		Map<Instrument, String> map = Maps.newHashMap();
		for (DrumInstrument instrument : DrumInstrument.values()) {
			String fileName = instrument.getFileName();
			String relativePath = getRelativePath(fileName);
			map.put(instrument, relativePath);
		}
		return map;
	}

	private static String getRelativePath(String fileName) {
		return String.format(RESOURCE_PATH_FORMAT, fileName);
	}

}

package de.syslord.electonePattern.Audio;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import util.LogUtil;
import electone.constants.DrumInstrument;
import electone.dataobjects.Instrument;

public class AudioClipSoundLibrary implements SoundLibrary {

	@Override
	public Map<Instrument, String> getAudioFiles() {

		List<DrumInstrument> instruments = Arrays.asList(DrumInstrument.values());
		Map<Instrument, String> map = instruments.stream()
				.collect(Collectors.toMap(
						instrument -> instrument,
						instrument -> getAbsolutePath(instrument.getFileName())));

		return map;
	}

	private String getAbsolutePath(String fileName) {
		String resourcePath = String.format("sounds/%s", fileName);
		String audioClipUsableUrl = getAudioClipUsableUrl(resourcePath);
		LogUtil.logDebug("audio resourcePath/fullpath %s -- %s", resourcePath, audioClipUsableUrl);
		return audioClipUsableUrl;
	}

	/**
	 * Returns file:/C:/.....
	 *
	 * Every other format seems invalid for use with AudioClip.
	 */
	private String getAudioClipUsableUrl(String resourcePath) {
		ClassLoader loader = AudioClipSoundLibrary.class.getClassLoader();
		return loader.getResource(resourcePath).toExternalForm();
	}
}

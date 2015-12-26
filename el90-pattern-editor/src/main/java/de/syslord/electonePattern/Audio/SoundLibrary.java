package de.syslord.electonePattern.Audio;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import util.LogUtil;
import electone.constants.DrumInstrument;
import electone.dataobjects.Instrument;

public class SoundLibrary {

	public static Map<Instrument, String> getAudioFiles() {

		List<DrumInstrument> instruments = Arrays.asList(DrumInstrument.values());
		Map<Instrument, String> map = instruments.stream()
				.collect(Collectors.toMap(
						instrument -> instrument,
						instrument -> getAbsolutePath(instrument.getFileName())));

		return map;
	}

	private static String getAbsolutePath(String fileName) {
		String resourcePath = String.format("sounds/%s", fileName);
		String audioClipUsableUrl = getAudioClipUsableUrl(resourcePath);
		LogUtil.logDebug("audio resourcePath/fullpath %s -- %s", resourcePath, audioClipUsableUrl);
		return audioClipUsableUrl;
	}

	/**
	 * Returns file:/C:/.....
	 *
	 * Every other format seems invalid.
	 */
	private static String getAudioClipUsableUrl1(String resourcePath) {
		try {
			return new File(resourcePath).toURI().toURL().toExternalForm();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	private static String getAudioClipUsableUrl(String resourcePath) {
		// try {
		ClassLoader loader = SoundLibrary.class.getClassLoader();
		return loader.getResource(resourcePath).toExternalForm();
		// return new File(resourcePath).toURI().toURL().toExternalForm();
		// } catch (MalformedURLException e) {
		// throw new RuntimeException(e);
		// }
	}
}

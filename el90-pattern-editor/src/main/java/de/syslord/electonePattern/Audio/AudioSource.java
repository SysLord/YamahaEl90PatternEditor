package de.syslord.electonePattern.Audio;

import javafx.scene.media.AudioClip;

import java.util.HashMap;
import java.util.Map;

import util.LogUtil;
import electone.dataobjects.Instrument;

public class AudioSource {

	private Map<Instrument, AudioClip> clipMap = new HashMap<>();

	public void init(Map<Instrument, String> audioFiles) {
		audioFiles.entrySet().stream()
		.forEach(entry -> createClip(entry.getKey(), entry.getValue()));

		LogUtil.log("%d audio files loaded.", clipMap.size());
	}

	private void createClip(Instrument instrument, String absolutePath) {
		LogUtil.logDebug("Creating AudioClip from: %s", absolutePath);
		// String valueOf = String.valueOf(Files.exists(Paths.get(absolutePath)));
		// LogUtil.logDebug("Creating AudioClip from %s. File exists: %s", absolutePath, valueOf);

		AudioClip audioClip = new AudioClip(absolutePath);
		clipMap.put(instrument, audioClip);
	}

	public void play(Instrument instrument, float volume) {
		clipMap.get(instrument).play(volume);
	}

}

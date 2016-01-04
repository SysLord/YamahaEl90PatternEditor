package de.syslord.electonePattern.Audio;

import javafx.scene.media.AudioClip;

import java.util.HashMap;
import java.util.Map;

import electone.dataobjects.Instrument;
import util.LogUtil;

public class AudioClipAudioSource implements AudioSource {

	private Map<Instrument, AudioClip> clipMap = new HashMap<>();

	@Override
	public void init(Map<Instrument, String> audioFiles) {
		audioFiles.entrySet().stream()
				.forEach(entry -> createClip(entry.getKey(), entry.getValue()));

		LogUtil.log("%d audio files loaded.", clipMap.size());
	}

	private void createClip(Instrument instrument, String absolutePath) {
		LogUtil.logDebug("Creating AudioClip from: %s", absolutePath);
		AudioClip audioClip = new AudioClip(absolutePath);
		clipMap.put(instrument, audioClip);
	}

	@Override
	public void play(Instrument instrument, float volume) {
		clipMap.get(instrument).play(volume);
	}

	@Override
	public void close() {
		//
	}

}

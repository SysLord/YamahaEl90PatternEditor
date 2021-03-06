package de.syslord.electonePattern.Audio;

import java.util.Map;
import java.util.Map.Entry;

import electone.constants.DrumInstrument;
import electone.dataobjects.Pattern;
import electone.dataobjects.PatternConstants;
import electone.dataobjects.Volume;
import util.LogUtil;

public class AudioPlayer {

	private static final int OPENAL_MAX_AUDIO_SOURCES = 250;

	private static final int MILLISECONDS_PER_SECOND = 1000;

	private static final int MINUTE_SECONDS = 60;

	private AudioSource audioSource;

	private PlayerPositionListener listener;

	private boolean playing = false;

	private Pattern model;

	public interface PlayerPositionListener {

		void updatePosition(int position);

	}

	public AudioPlayer() {
		boolean openAlSuccessfull = createOpenAlAudioSource();

		if (!openAlSuccessfull) {
			LogUtil.logWarn(
					"====== OpenAl could not be initiated. Using javaFx AudioClip as fallback. It will sound awful. ======");

			AudioClipSoundLibrary audioClipSoundLibrary = new AudioClipSoundLibrary();
			audioSource = new AudioClipAudioSource();
			audioSource.init(audioClipSoundLibrary.getAudioFiles());
		}
	}

	private boolean createOpenAlAudioSource() {
		try {
			OpenAlSoundLibrary openAlSoundLibrary = new OpenAlSoundLibrary();
			audioSource = new GamingLibOpenAlAudioSource(OPENAL_MAX_AUDIO_SOURCES);
			audioSource.init(openAlSoundLibrary.getAudioFiles());

			LogUtil.log("===== OpenAl initialization successful. Realtime sound possible. =====");

			return true;
		} catch (UnsatisfiedLinkError err) {
			LogUtil.log("Failed to create OpenAl audio. Native libraries are missing.\n%s", err);
			return false;
		}
	}

	public void setModel(Pattern model) {
		this.model = model;
	}

	public void stop() {
		this.playing = false;
	}

	// TODO Bpm as a property
	public void start(int beatsPerMinute) {
		if (playing) {
			return;
		}

		this.playing = true;
		final int pause = (MILLISECONDS_PER_SECOND * MINUTE_SECONDS)
				/ (beatsPerMinute * PatternConstants.QUARTER_QUANTIZATION);
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				playLoop(pause);
			}

		});
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}

	private void playLoop(final int pause) {
		while (playing) {
			playPattern(pause);
		}
	}

	private void playPattern(final int pause) {
		for (int position = 0; position < PatternConstants.TRACK_QUANTIZATION; position++) {
			if (listener != null) {
				listener.updatePosition(position);
			}
			playCount(position);

			if (!playing) {
				return;
			}
			try {
				Thread.sleep(pause);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	private void playCount(int position) {
		Map<DrumInstrument, Volume> map = model.getNotes(position);
		for (Entry<DrumInstrument, Volume> entry : map.entrySet()) {
			if (!playing) {
				return;
			}

			DrumInstrument instrument = entry.getKey();
			Volume volume = entry.getValue();
			audioSource.play(instrument, volume.getRelative());
		}
	}

	public AudioSource getAudioSource() {
		return audioSource;
	}

	public void setPositionListener(PlayerPositionListener listener) {
		this.listener = listener;
	}

	public void close() {
		audioSource.close();
	}
}

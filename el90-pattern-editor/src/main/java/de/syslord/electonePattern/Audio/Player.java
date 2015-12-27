package de.syslord.electonePattern.Audio;

import java.util.Map;
import java.util.Map.Entry;

import electone.constants.DrumInstrument;
import electone.dataobjects.Pattern;
import electone.dataobjects.PatternConstants;
import electone.dataobjects.Volume;

public class Player {

	private static final int MINUTE_SECONDS = 60;

	private AudioSource audioSource;

	private PlayerPositionListener listener;

	private boolean playing = false;

	private Pattern model;

	public interface PlayerPositionListener {

		void updatePosition(int position);

	}

	public Player(PlayerPositionListener listener) {
		this.listener = listener;
		audioSource = new AudioSource();
		audioSource.init(SoundLibrary.getAudioFiles());
	}

	public void setModel(Pattern model) {
		this.model = model;
	}

	public void stop() {
		this.playing = false;
	}

	// TODO Bpm als property
	public void start(int beatsPerMinute) {
		if (playing) {
			return;
		}

		this.playing = true;
		final int pause = (1000 * MINUTE_SECONDS) / (beatsPerMinute * PatternConstants.QUARTER_QUANTIZATION);
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
			listener.updatePosition(position);
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
		Map<DrumInstrument, Volume> map = model.getCount(position);
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
}

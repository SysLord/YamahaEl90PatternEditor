package de.syslord.electonePattern.Audio;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import com.google.common.collect.Maps;

import electone.dataobjects.Instrument;
import util.LogUtil;

// TODO one should probably put a synchronize here and there
public class GamingLibOpenAlAudioSource implements AudioSource {

	public class AudioSourceException extends RuntimeException {

		private static final long serialVersionUID = -6620191515245979213L;

		public AudioSourceException(String msg) {
			super(msg);
		}

		public AudioSourceException(Throwable ex) {
			super(ex);
		}
	}

	private IntBuffer buffer;

	private IntBuffer source;

	private FloatBuffer sourcePos;

	private FloatBuffer sourceVel;

	private FloatBuffer listenerPos;

	private FloatBuffer listenerVel;

	private FloatBuffer listenerOrientation;

	private Map<Instrument, Integer> bufferMap;

	private Map<Instrument, List<Integer>> sourceMap;

	private int freeSources;

	private int bufferIndex = 0;

	private int maxParallelSources;

	public GamingLibOpenAlAudioSource(int maxParallelSources) {
		this.maxParallelSources = maxParallelSources;
		freeSources = maxParallelSources - 1;
	}

	public boolean testSourceNumber() {
		int sourceCounter = -1;
		try {
			createAudio();
			source = BufferUtils.createIntBuffer(1);
			for (sourceCounter = 1; sourceCounter < 2048; sourceCounter++) {
				AL10.alGenSources(source);
				onErrorThrow();

				int position = source.position();
				source.limit(position + 1);
			}
		} catch (Exception ex) {
			System.out.println("Max Sources: " + String.valueOf(sourceCounter - 1));
			ex.printStackTrace();
		}

		return true;
	}

	@Override
	public void init(Map<Instrument, String> audioFiles) {
		int sourceCount = audioFiles.size();
		buffer = BufferUtils.createIntBuffer(sourceCount);
		source = BufferUtils.createIntBuffer(maxParallelSources);

		bufferMap = Maps.newHashMap();
		sourceMap = Maps.newHashMap();

		initConstants();
		createAudio();
		initSourceAndBuffer();
		for (Entry<Instrument, String> entry : audioFiles.entrySet()) {
			Instrument instrument = entry.getKey();
			String absolutePath = entry.getValue();
			loadInstrumentIntoBuffer(instrument, absolutePath);
		}

		initListeners();
	}

	private void loadInstrumentIntoBuffer(Instrument instrument, String path) {
		addWave(bufferIndex, path);
		bufferMap.put(instrument, bufferIndex);
		sourceMap.put(instrument, new LinkedList<Integer>());
		bufferIndex += 1;
	}

	private void addWave(int bufferIndex, String path) {
		// Loads resource from classpath
		WaveData waveFile = WaveData.create(path);
		if (waveFile == null) {
			throw new AudioSourceException("Wave file could not be loaded: " + path);
		}
		AL10.alBufferData(buffer.get(bufferIndex), waveFile.format, waveFile.data, waveFile.samplerate);
		waveFile.dispose();
	}

	@Override
	public void play(Instrument instrument, float volume) {
		List<Integer> instrumentSourceIdentifiers = sourceMap.get(instrument);
		if (instrumentSourceIdentifiers != null) {
			for (Integer sourceIdentifier : instrumentSourceIdentifiers) {
				if (!isPlaying(sourceIdentifier)) {
					playSource(sourceIdentifier, volume);
					onErrorThrow();
					return;
				}
			}
		}

		if (freeSources >= 0) {
			int sourceIdentifier = source.get(freeSources);
			freeSources -= 1;
			initSource(sourceIdentifier, instrument);
			onErrorThrow();
			playSource(sourceIdentifier, volume);
			onErrorThrow();
			return;
		}

		for (Entry<Instrument, List<Integer>> entry : sourceMap.entrySet()) {
			Instrument sourceInstrument = entry.getKey();
			List<Integer> sourceIdentifiers = entry.getValue();

			if (instrument.equals(sourceInstrument)) {
				continue;
			}

			for (Iterator<Integer> iterator = sourceIdentifiers.iterator(); iterator.hasNext();) {
				Integer sourceIdentifier = iterator.next();
				if (!isPlaying(sourceIdentifier)) {
					iterator.remove();

					reinitSource(sourceIdentifier, instrument);
					onErrorThrow();
					playSource(sourceIdentifier, volume);
					onErrorThrow();
					return;
				}
			}
		}
		LogUtil.log("No free sources to play sound!");
	}

	private void reinitSource(Integer sourceIdentifier, Instrument instrument) {
		AL10.alDeleteSources(sourceIdentifier);
		onErrorThrow();
		AL10.alGenSources();
		onErrorThrow();

		initSource(sourceIdentifier, instrument);
	}

	private void initSource(Integer sourceIdentifier, Instrument instrument) {
		setupSourceBufferMapping(sourceIdentifier, bufferMap.get(instrument));
		sourceMap.get(instrument).add(sourceIdentifier);
	}

	private void playSource(int sourceIdentifier, float volume) {
		AL10.alSourcef(sourceIdentifier, AL10.AL_GAIN, volume);
		AL10.alSourcePlay(sourceIdentifier);
	}

	private void initConstants() {
		sourcePos = (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
		sourceVel = (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
		listenerPos = (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
		listenerVel = (FloatBuffer) BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();
		listenerOrientation = (FloatBuffer) BufferUtils.createFloatBuffer(6)
				.put(new float[] { 0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f }).rewind();
	}

	private boolean isPlaying(int sourceKey) {
		boolean playing = AL10.alGetSourcei(sourceKey, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
		return playing;
	}

	private void setupSourceBufferMapping(int sourceIdentifier, int bufferIndex) {
		AL10.alSourcei(sourceIdentifier, AL10.AL_BUFFER, buffer.get(bufferIndex));
		AL10.alSourcef(sourceIdentifier, AL10.AL_PITCH, 1.0f);
		AL10.alSourcef(sourceIdentifier, AL10.AL_GAIN, 1.0f);
		AL10.alSource(sourceIdentifier, AL10.AL_POSITION, sourcePos);
		AL10.alSource(sourceIdentifier, AL10.AL_VELOCITY, sourceVel);
		onErrorThrow();
	}

	private void initSourceAndBuffer() {
		AL10.alGenBuffers(buffer);
		onErrorThrow();
		AL10.alGenSources(source);
		onErrorThrow();
	}

	private void initListeners() {
		AL10.alListener(AL10.AL_POSITION, listenerPos);
		AL10.alListener(AL10.AL_VELOCITY, listenerVel);
		AL10.alListener(AL10.AL_ORIENTATION, listenerOrientation);
	}

	private void createAudio() {
		try {
			AL.create();
		} catch (LWJGLException ex) {
			throw new AudioSourceException(ex);
		}
		AL10.alGetError();
	}

	private void onErrorThrow() {
		int error = AL10.alGetError();
		if (error != AL10.AL_NO_ERROR) {
			throw new AudioSourceException(AL10.alGetString(error));
		}
	}

	public void cleanUp() {
		AL10.alDeleteSources(source);
		AL10.alDeleteBuffers(buffer);
		AL.destroy();
	}

}

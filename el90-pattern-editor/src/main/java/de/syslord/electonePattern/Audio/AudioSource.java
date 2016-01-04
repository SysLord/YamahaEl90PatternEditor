package de.syslord.electonePattern.Audio;

import java.util.Map;

import electone.dataobjects.Instrument;

public interface AudioSource {

	void init(Map<Instrument, String> audioFiles);

	void play(Instrument instrument, float relativeVolume);

	void close();

}

package de.syslord.electonePattern.Audio;

import java.util.Map;

import electone.dataobjects.Instrument;

public interface SoundLibrary {

	Map<Instrument, String> getAudioFiles();

}

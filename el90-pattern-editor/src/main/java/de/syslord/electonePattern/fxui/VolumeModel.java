package de.syslord.electonePattern.fxui;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class VolumeModel {

	public static final int MAX_VOL = 7;
	public static final int MIN_VOL = 0;
	public static final int VOL_STEPS = 8;

	IntegerProperty volumeProperty = new SimpleIntegerProperty(this, "intvol", 0);

	public VolumeModel(int volume) {
		setVolume(volume);
	}

	public int getVolume() {
		return volumeProperty.get();
	}

	public void setVolume(int volume) {
		if (volume < MIN_VOL || volume > MAX_VOL) {
			throw new IllegalArgumentException(String.format("Volume out of range: %d < %d < %d", MIN_VOL, volume,
					MAX_VOL));
		}
		volumeProperty.set(volume);
	}

	public IntegerProperty getVolumeProperty() {
		return volumeProperty;
	}

	// TODO REMOVE - too funky
	public void funky() {
		System.out.println("FUNKY!!");

		Integer value = volumeProperty.getValue();
		if (value < 8) {
			volumeProperty.setValue(value + 1);
		} else {
			volumeProperty.setValue(0);
		}
	}
}

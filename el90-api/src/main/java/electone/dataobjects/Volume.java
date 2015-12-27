package electone.dataobjects;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Volume {

	public static final int MAX_VOLUME = 8;

	private IntegerProperty volumeProperty = new SimpleIntegerProperty(0);

	public Volume(int volume) {
		if (volume < 0 || volume > 7) {
			throw new IllegalArgumentException(String.format("Volume out of range: %d < %d < %d", 0, volume, 7));
		}
		setVolume(volume);
	}

	public static Volume createSilent() {
		return new Volume(0);
	}

	private int getVolume() {
		return volumeProperty.get();
	}

	public void setVolume(int volume) {
		volumeProperty.set(volume);
	}

	public boolean isSounding() {
		return getVolume() > 0;
	}

	/**
	 * @return 0.0 to 1.0
	 */
	public float getRelative() {
		return getVolume() / (float) MAX_VOLUME;
	}

	@Override
	public String toString() {
		return String.valueOf(getVolume());
	}

	public IntegerProperty volumeProperty() {
		return volumeProperty;
	}

}

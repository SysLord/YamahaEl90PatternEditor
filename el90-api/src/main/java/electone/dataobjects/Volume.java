package electone.dataobjects;

public class Volume {

	private static final int MAX_VOLUME = 8;

	private int volume;

	public Volume(int volume) {
		if (volume < 0 || volume > 7) {
			throw new IllegalArgumentException(String.format("Volume out of range: %d < %d < %d", 0, volume, 7));
		}
		this.volume = volume;
	}

	public static Volume createSilent() {
		return new Volume(0);
	}

	public static Volume of(int volume) {
		return new Volume(volume);
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public boolean isSounding() {
		return volume > 0;
	}

	/**
	 * @return 0.0 to 1.0
	 */
	public float getRelative() {
		return volume / (float) MAX_VOLUME;
	}

	@Override
	public String toString() {
		return String.valueOf(volume);
	}
}

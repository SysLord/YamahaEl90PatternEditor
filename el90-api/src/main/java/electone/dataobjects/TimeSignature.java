package electone.dataobjects;

public enum TimeSignature {

	TIME_4_4(4),
	TIME_3_4(3);

	private int quartersPerBar;

	private TimeSignature(int quartersPerBar) {
		this.quartersPerBar = quartersPerBar;
	}

	public int getQuartersPerBar() {
		return quartersPerBar;
	}

	public int getFinestQuantizationPerBar() {
		return quartersPerBar * BarConstants.QUARTER_QUANTIZATION;
	}
}

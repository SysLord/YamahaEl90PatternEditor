package electone.dataobjects;

import java.util.Arrays;

public enum TimeSignature {

	TIME_4_4(4),
	TIME_3_4(3),
	TIME_2_4(2);

	private int quartersPerBar;

	private TimeSignature(int quartersPerBar) {
		this.quartersPerBar = quartersPerBar;
	}

	public int getQuartersPerBar() {
		return quartersPerBar;
	}

	public int getFinestQuantizationPerBar() {
		return quartersPerBar * PatternConstants.QUARTER_QUANTIZATION;
	}

	public static boolean isValid(int quartersPerBar) {
		boolean match = Arrays.asList(TimeSignature.values()).stream()
				.filter(timeSig -> timeSig.getQuartersPerBar() == quartersPerBar)
				.findFirst()
				.isPresent();
		return match;
	}
}

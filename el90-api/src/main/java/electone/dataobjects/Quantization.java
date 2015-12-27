package electone.dataobjects;

import java.util.Arrays;
import java.util.List;

// TODO actually... it depends on TimeSignature. Fix later.
public enum Quantization {

	WHOLE(1),
	HALF(2),
	QUARTER(4),
	EIGHTH(8),
	SIXTEENTH(16),
	THIRDS(3 * 4),
	FINEST(BarConstants.QUARTER_QUANTIZATION * BarConstants.MAX_QUARTERS_PER_BAR);

	private int countsPerBar;

	private Quantization(int countsPerBar) {
		this.countsPerBar = countsPerBar;
	}

	public int getCountsPerBar() {
		return countsPerBar;
	}

	public static Quantization getMostCoarseQuantization(List<Integer> measures) {
		List<Quantization> orderedQuantization = Arrays.asList(values());
		Quantization quantization = orderedQuantization.stream()
				.filter(q -> isValidQuantization(q, measures))
				.findFirst().orElse(FINEST);

		return quantization;
	}

	private static boolean isValidQuantization(Quantization quantization, List<Integer> measures) {
		int validCountsFactor = FINEST.getCountsPerBar() / quantization.getCountsPerBar();
		return measures.stream()
				.allMatch(measure -> measure % validCountsFactor == 0);
	}
}

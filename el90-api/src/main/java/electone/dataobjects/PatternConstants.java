package electone.dataobjects;

public interface PatternConstants {

	int QUARTER_QUANTIZATION = 24;

	int QUARTERS_PER_BAR = 4;

	int BARS = 2;

	int MAX_TRACKS = 16;

	int TRACK_QUANTIZATION = BARS * QUARTERS_PER_BAR * QUARTER_QUANTIZATION;
}

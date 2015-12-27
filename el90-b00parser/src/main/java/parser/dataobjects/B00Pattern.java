package parser.dataobjects;

import java.util.List;

import parser.util.HexPrintUtil;
import electone.dataobjects.PatternIdent;

public class B00Pattern {

	private PatternIdent patternIdent;

	private int measureCount;

	private BinaryData channelInstruments;

	private int offsetMeasure1;

	private int offsetMeasure2;

	private List<B00Measure> parsedMeasures1;

	private List<B00Measure> parsedMeasures2;

	public B00Pattern(PatternIdent patternIdent, int measureCount, BinaryData channelInstruments, int offsetMeasure1,
			int offsetMeasure2) {
		this.patternIdent = patternIdent;
		this.measureCount = measureCount;
		this.channelInstruments = channelInstruments;
		this.offsetMeasure1 = offsetMeasure1;
		this.offsetMeasure2 = offsetMeasure2;
	}

	@Override
	public String toString() {
		// TODO what a mess...
		String instruments = HexPrintUtil.getHumandReadable(channelInstruments.getData());
		String format = String.format("pattern %s\n" + "measures %d\noffsets %d %d\n" + "channelInstruments\n" + "%s",
				patternIdent, measureCount, offsetMeasure1, offsetMeasure2, instruments);

		// String collect = channelInstruments.getData().stream()
		// .map(x -> DrumInstrument.getName(x))
		// .collect(Collectors.joining(", "));

		String measu = String.format("measures1:\n%s\nmeasures2\n%s\n", parsedMeasures1.toString(),
				parsedMeasures2.toString());
		return format + "\n" + "\n" + measu;
	}

	// TODO REMOVE later - debugging
	public String debugPrintparsed() {
		StringBuilder debugPrint = debugPrint(parsedMeasures1);
		StringBuilder debugPrint2 = debugPrint(parsedMeasures2);

		return patternIdent.toString() + "\n" + debugPrint.toString() + "\n" + debugPrint2.toString();
	}

	private StringBuilder debugPrint(List<B00Measure> pp) {
		int[][] x = new int[16][4 * 24];

		for (B00Measure m : pp) {
			int measure = m.getMeasure();
			List<B00Note> notes = m.getNotes();
			for (B00Note note : notes) {
				int channel = note.getChannel();
				int accent = note.getAccent();
				x[channel][measure] = accent;
			}
		}

		StringBuilder b = new StringBuilder();
		for (int[] chan : x) {
			for (int i : chan) {
				b.append(i);
			}
			b.append("\n");
		}
		return b;
	}

	public PatternIdent getPatternIdent() {
		return patternIdent;
	}

	public int getMeasures() {
		return measureCount;
	}

	public int getOffsetMeasure1() {
		return offsetMeasure1;
	}

	public int getOffsetMeasure2() {
		return offsetMeasure2;
	}

	public void setMeasures1(List<B00Measure> parsedMeasures1) {
		this.parsedMeasures1 = parsedMeasures1;
	}

	public void setMeasures2(List<B00Measure> parsedMeasures2) {
		this.parsedMeasures2 = parsedMeasures2;
	}

	public List<B00Measure> getParsedMeasures1() {
		return parsedMeasures1;
	}

	public List<B00Measure> getParsedMeasures2() {
		return parsedMeasures2;
	}

	public List<Integer> getInstrumentsIds() {
		return channelInstruments.getData();
	}

}

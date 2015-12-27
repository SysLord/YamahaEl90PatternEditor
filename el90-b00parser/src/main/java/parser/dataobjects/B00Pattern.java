package parser.dataobjects;

import java.util.List;

import parser.util.HexPrintUtil;
import electone.dataobjects.PatternIdent;

public class B00Pattern {

	private PatternIdent patternIdent;

	private int quarterTime;

	private BinaryData channelInstruments;

	private int offsetMeasureBar1;

	private int offsetMeasureBar2;

	private List<B00Measure> parsedMeasuresBar1;

	private List<B00Measure> parsedMeasuresBar2;

	public B00Pattern(PatternIdent patternIdent, int quarterTime, BinaryData channelInstruments, int offsetMeasure1,
			int offsetMeasure2) {
		this.patternIdent = patternIdent;
		this.quarterTime = quarterTime;
		this.channelInstruments = channelInstruments;
		this.offsetMeasureBar1 = offsetMeasure1;
		this.offsetMeasureBar2 = offsetMeasure2;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("Dump B00Pattern: " + patternIdent + "\n");
		builder.append(HexPrintUtil.getHumandReadable(channelInstruments.getData()));

		builder.append("\n");
		builder.append("Time: " + quarterTime + "/4");

		builder.append("\n");
		builder.append("measures offsets: " + offsetMeasureBar1 + " " + offsetMeasureBar2);

		builder.append("\n");
		builder.append("Measures1:\n");
		builder.append(parsedMeasuresBar1.toString());
		builder.append("\n");
		builder.append("Measures2:\n");
		builder.append(parsedMeasuresBar2.toString());

		builder.append("\n");
		builder.append("Different dumpformat:");
		builder.append("\n");
		builder.append(debugPrint(parsedMeasuresBar1).toString());
		builder.append("\n");
		builder.append(debugPrint(parsedMeasuresBar2).toString());
		builder.append("\n");

		return builder.toString();
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

	public int getOffsetMeasure1() {
		return offsetMeasureBar1;
	}

	public int getOffsetMeasure2() {
		return offsetMeasureBar2;
	}

	public void setMeasures1(List<B00Measure> parsedMeasures1) {
		this.parsedMeasuresBar1 = parsedMeasures1;
	}

	public void setMeasures2(List<B00Measure> parsedMeasures2) {
		this.parsedMeasuresBar2 = parsedMeasures2;
	}

	public List<B00Measure> getParsedMeasures1() {
		return parsedMeasuresBar1;
	}

	public List<B00Measure> getParsedMeasures2() {
		return parsedMeasuresBar2;
	}

	public List<Integer> getInstrumentsIds() {
		return channelInstruments.getData();
	}

}

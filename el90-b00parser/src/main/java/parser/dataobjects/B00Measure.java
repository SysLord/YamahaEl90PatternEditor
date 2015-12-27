package parser.dataobjects;

import java.util.List;

import parser.constants.Constants;

import com.google.common.collect.Lists;

public class B00Measure {

	private static final int MAX_VALID_MEASURE = Constants.QUARTER_QUANTIZATION * Constants.MAX_QUARTERS_PER_BAR;

	private static final int MIN_VALID_MEASURE = 0;

	private int measureIn24thQuantizationPerQuarter;

	private List<B00Note> notes = Lists.newArrayList();

	public B00Measure(int measureIn24thQuantizationPerQuarter) {
		if (measureIn24thQuantizationPerQuarter < MIN_VALID_MEASURE
				|| measureIn24thQuantizationPerQuarter > MAX_VALID_MEASURE) {
			throw new IllegalArgumentException(String.format("measure out of range: %d < %d < %d", MIN_VALID_MEASURE,
					measureIn24thQuantizationPerQuarter, MAX_VALID_MEASURE));
		}

		this.measureIn24thQuantizationPerQuarter = measureIn24thQuantizationPerQuarter;
	}

	public void addNote(int channel, int accent) {
		B00Note note = new B00Note(channel, accent);
		notes.add(note);
	}

	@Override
	public String toString() {
		return String.format("%d:%s", measureIn24thQuantizationPerQuarter, notes.toString());
	}

	public List<B00Note> getNotes() {
		return notes;
	}

	public int getMeasure() {
		return measureIn24thQuantizationPerQuarter;
	}
}

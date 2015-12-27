package parser.dataobjects;

import java.util.List;

import com.google.common.collect.Lists;

public class B00Measure {

	private int measureIn24thQuantizationPerQuarter;

	private List<B00Note> notes = Lists.newArrayList();

	public B00Measure(int measureIn24thQuantizationPerQuarter) {
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

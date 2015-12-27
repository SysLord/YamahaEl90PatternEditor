package electone.dataobjects;

import javafx.beans.binding.StringExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.util.Assert;

import com.google.common.collect.ImmutableList;

import electone.constants.DrumInstrument;

public class Track {

	private StringProperty instrumentNameProperty = new SimpleStringProperty(this, "instrumentName", "");

	private DrumInstrument instrument;

	private Volume[] trackVolumes;

	// TODO not used yet
	@SuppressWarnings("unused")
	private Quantization singleBarQuantization;

	private IntegerProperty trackIndexProperty = new SimpleIntegerProperty(this, "trackIndex", 0);

	public Track(int trackIndex, DrumInstrument instrument, Quantization singleBarQuantization) {
		setTrackIndex(trackIndex);
		this.singleBarQuantization = singleBarQuantization;
		setInstrument(instrument);
	}

	public void setTrackIndex(int trackIndex) {
		trackIndexProperty.set(trackIndex);
	}

	private int getTrackIndex() {
		return trackIndexProperty.get();
	}

	public void setInstrument(DrumInstrument instrument) {
		this.instrument = instrument;
		instrumentNameProperty.set(instrument.getName());
	}

	public Volume getVolume(int measure) {
		Volume volume = trackVolumes[measure];
		Assert.notNull(volume);

		return volume;
	}

	public void init(Volume[] trackVolumes) {
		this.trackVolumes = trackVolumes;
		assertAllVolumesPresent(trackVolumes);
	}

	private void assertAllVolumesPresent(Volume[] trackVolumes) {
		IntStream.range(0, trackVolumes.length)
				.boxed()
				.filter(index -> trackVolumes[index] == null)
				.forEach(index -> trackVolumes[index] = Volume.createSilent());
	}

	public DrumInstrument getInstrument() {
		return instrument;
	}

	public static Comparator<Track> getOrderedTracksComparator() {
		return (track1, track2) -> Integer.compare(track1.getTrackIndex(), track2.getTrackIndex());
	}

	public int size() {
		return trackVolumes.length;
	}

	public StringExpression instrumentNameProperty() {
		return instrumentNameProperty;
	}

	public IntegerProperty trackIndexProperty() {
		return trackIndexProperty;
	}

	public List<Volume> getVolumes() {
		return ImmutableList.copyOf(trackVolumes);
	}

}

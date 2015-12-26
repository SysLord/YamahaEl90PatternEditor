package electone.dataobjects;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import electone.constants.DrumInstrument;

public class TrackPattern {

	private DrumInstrument instrument;

	private CountQuantization quantization;

	private Map<Measure, Volume> countVolumes;

	public TrackPattern(DrumInstrument instrument, CountQuantization quantization, Map<Measure, Volume> countVolumes) {
		this.instrument = instrument;
		this.quantization = quantization;
		this.countVolumes = countVolumes;
		assureCounts();
	}

	private void assureCounts() {
		IntStream.range(0, PatternConstants.MAX_PATTERN_LENGTH)
				.boxed()
				.map(measureValue -> Measure.of(measureValue))
				.filter(measure -> !countVolumes.containsKey(measure))
				.forEach(measure -> countVolumes.put(measure, Volume.createSilent()));
	}

	public DrumInstrument getInstrument() {
		return instrument;
	}

	public String getInstrumentRepresentation() {
		return instrument.getName();
	}

	public void setInstrument(DrumInstrument instrument) {
		this.instrument = instrument;
	}

	public CountQuantization getQuantization() {
		return quantization;
	}

	public Volume getVolume(int count) {
		Volume volume = countVolumes.get(Measure.of(count));
		// TODO REM
		// if (volume == null) {
		// throw new RuntimeException(String.format("Measure: %d wurde nicht gefunden.", count));
		// }
		return volume;
	}

	@Override
	public String toString() {
		return "TrackPattern [instrument=" + instrument + ", quantization=" + quantization + ", countVolumes="
				+ countVolumes + "]";
	}

	public Long getNumberOfSounds() {
		return countVolumes.values().stream()
				.filter(v -> v.isSounding())
				.collect(Collectors.counting());
	}
}

package parser.main;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import parser.constants.B00DrumMapper;
import parser.dataobjects.B00Data;
import parser.dataobjects.B00Measure;
import parser.dataobjects.B00Pattern;
import parser.dataobjects.BinaryData;
import electone.constants.DrumInstrument;
import electone.dataobjects.Pattern;
import electone.dataobjects.PatternConstants;
import electone.dataobjects.Patterns;
import electone.dataobjects.TimeSignature;
import electone.dataobjects.Track;
import electone.dataobjects.Volume;

/**
 * Convert B00File related pattern to more usable UI-related Pattern object.
 */
public class PatternConverter {

	public Patterns fromB00ToPattern(B00Data b00Data) {
		Patterns patterns = new Patterns();

		for (B00Pattern b00Pattern : b00Data.getPatterns()) {

			List<DrumInstrument> channelInstruments = b00Pattern.getInstrumentsIds().stream()
					.map(id -> B00DrumMapper.mapB00InstrumentToKeyboardKey(id))
					.map(keyboardkey -> DrumInstrument.getInstrumentByKeyIndex(keyboardkey))
					.collect(Collectors.toList());

			TimeSignature timeSignature = TimeSignature.fromQuarterCount(b00Pattern.getQuarterTime());
			PatternBuilder builder = new PatternBuilder(channelInstruments, b00Pattern.getPatternIdent(), timeSignature);

			b00Pattern.getParsedMeasures1().forEach(
					b00measure -> builder.addNotes(b00measure.getMeasure(), b00measure.getNotes()));

			int secondBarOffset = PatternConstants.QUARTERS_PER_BAR * PatternConstants.QUARTER_QUANTIZATION;
			b00Pattern.getParsedMeasures2().forEach(
					b00measure -> builder.addNotes(b00measure.getMeasure() + secondBarOffset, b00measure.getNotes()));

			patterns.addPattern(b00Pattern.getPatternIdent(), builder.build());
		}

		return patterns;
	}

	public B00Pattern fromPatternToB00(Pattern pattern) {
		List<Track> tracks = pattern.getTracks();

		Map<DrumInstrument, Integer> instrumentsChannel = IntStream.range(0, tracks.size())
				.boxed()
				.collect(Collectors.toMap(
						index -> tracks.get(index).getInstrument(),
						index -> index
						));

		List<Integer> instruments = instrumentsChannel.keySet().stream()
				.map(inst -> B00DrumMapper.mapInstrumentB00Id(inst))
				.collect(Collectors.toList());

		BinaryData b00instruments = new BinaryData(instruments, "instruments");

		int quartersPerBar = pattern.getTimeSignature().getQuartersPerBar();
		B00Pattern b00Pattern = new B00Pattern(pattern.getPatternIdent(), quartersPerBar, b00instruments);

		int oneBarQuantization = PatternConstants.QUARTERS_PER_BAR * PatternConstants.QUARTER_QUANTIZATION;

		List<B00Measure> bar1 = IntStream
				.range(0, oneBarQuantization)
				.boxed()
				.map(measure -> measuresFromPattern(pattern, instrumentsChannel, measure))
				.collect(Collectors.toList());

		List<B00Measure> bar2 = IntStream
				.range(oneBarQuantization, 2 * oneBarQuantization)
				.boxed()
				.map(measure -> measuresFromPattern(pattern, instrumentsChannel, measure))
				.collect(Collectors.toList());

		b00Pattern.setMeasures1(bar1);
		b00Pattern.setMeasures2(bar2);

		return b00Pattern;
	}

	private B00Measure measuresFromPattern(Pattern pattern, Map<DrumInstrument, Integer> instrumentsChannelMap,
			Integer measure) {
		B00Measure b00Measure = new B00Measure(measure);

		pattern.getNotes(measure)
				.entrySet()
				.forEach(entry -> {
					DrumInstrument instrument = entry.getKey();
					Volume volume = entry.getValue();

					int channel = instrumentsChannelMap.get(instrument);
					b00Measure.addNote(channel, volume.getVolume());
				});
		return b00Measure;
	}
}

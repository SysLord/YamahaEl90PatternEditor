package parser.main;

import java.util.List;
import java.util.stream.Collectors;

import parser.constants.B00DrumMapper;
import parser.dataobjects.B00Data;
import parser.dataobjects.B00Pattern;
import electone.constants.DrumInstrument;
import electone.dataobjects.PatternConstants;
import electone.dataobjects.Patterns;

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

			PatternBuilder builder = new PatternBuilder(channelInstruments, b00Pattern.getPatternIdent());

			b00Pattern.getParsedMeasures1().forEach(
					b00measure -> builder.addNotes(b00measure.getMeasure(), b00measure.getNotes()));

			int secondBarOffset = PatternConstants.QUARTERS_PER_BAR * PatternConstants.QUARTER_QUANTIZATION;
			b00Pattern.getParsedMeasures2().forEach(
					b00measure -> builder.addNotes(b00measure.getMeasure() + secondBarOffset, b00measure.getNotes()));

			patterns.addPattern(b00Pattern.getPatternIdent(), builder.build());
		}

		return patterns;
	}
}

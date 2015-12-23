package parser.main;

import java.util.List;
import java.util.stream.Collectors;

import parser.dataobjects.B00Data;
import parser.dataobjects.B00Pattern;
import electone.constants.DrumInstrument;
import electone.dataobjects.PatternConstants;
import electone.dataobjects.Patterns;

/**
 * Convert B00File pattern to more usable Pattern object.
 */
public class PatternConverter {

	public Patterns toBusiness(B00Data b00Data) {
		Patterns patterns = new Patterns();

		for (B00Pattern b00Pattern : b00Data.getPatterns()) {

			List<DrumInstrument> channelInstruments = b00Pattern.getInstrumentsIds().stream()
					.map(id -> DrumInstrument.getInstrument(id))
					.collect(Collectors.toList());

			// TODO support 3/4, there is only 4/4 and 3/4, right?
			// int measures = b00Pattern.getMeasures();
			int measures = 4;

			PatternBuilder builder = new PatternBuilder(channelInstruments);

			b00Pattern.getParsedMeasures1().forEach(
					b00measure -> builder.addNotes(b00measure.getMeasure(), b00measure.getNotes()));

			int offset = measures * PatternConstants.MAX_SUBCOUNT;
			b00Pattern.getParsedMeasures2().forEach(
					b00measure -> builder.addNotes(b00measure.getMeasure() + offset, b00measure.getNotes()));

			patterns.addPattern(b00Pattern.getPatternIdent(), builder.build());
		}

		return patterns;
	}

}

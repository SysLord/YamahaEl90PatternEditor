package parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import parser.constants.Constants;
import parser.dataobjects.B00Measure;
import parser.dataobjects.B00Pattern;
import parser.dataobjects.BinaryData;
import parser.dataobjects.BinaryData.Condition;
import parser.util.ParserUtil;
import util.AssertUtil;
import util.LogUtil;

import com.google.common.collect.Lists;

import electone.dataobjects.PatternConstants;
import electone.dataobjects.PatternIdent;
import electone.dataobjects.TimeSignature;

public class PatternParser {

	public List<B00Pattern> parsePatternBlock(BinaryData patternBlock) {
		// very weak test
		patternBlock.assertMinSize(Constants.ALL_PATTERNS_LENGTH + Constants.PATTERN_UNKNOWN_OFFSET_BY_2);

		int headerLength = Constants.ALL_PATTERNS_COUNT * Constants.SINGLE_PATTERN_LENGTH;
		BinaryData patterns8with5VariationsHead = patternBlock.getHead(headerLength).as("patterns common header");
		BinaryData dataLengthAndMeasures = patternBlock.getTail(headerLength).as("patterns length byte and measures");

		int dataLength = dataLengthAndMeasures.get(0) + 256 * dataLengthAndMeasures.get(1);
		BinaryData measures = dataLengthAndMeasures.getTail(2 + Constants.PATTERN_UNKNOWN_WEIRD_GAP)
				.as("pattern measures");
		measures.assertSize(dataLength + Constants.PATTERN_UNKNOWN_OFFSET_BY_2);

		List<B00Pattern> patterns = parsePatternsHeaders(patterns8with5VariationsHead);
		parseMeasures(patterns, measures);
		return patterns;
	}

	private void parseMeasures(List<B00Pattern> patterns, BinaryData rawMeasureData) {
		for (B00Pattern pattern : patterns) {

			LogUtil.logTraceDump(pattern.getPatternIdent(), "Parsing measures of pattern");

			int offsetMeasure1 = pattern.getOffsetMeasureBar1();

			BinaryData rawMeasures1 = rawMeasureData.getFromIndexUntilExcluding(offsetMeasure1,
					Constants.PATTERN_MEASURE_END).as("rawMeasures bar1");
			List<B00Measure> measures1 = parseMeasures(rawMeasures1);
			pattern.setMeasures1(measures1);

			int offsetMeasure2 = pattern.getOffsetMeasureBar2();

			BinaryData rawMeasures2 = rawMeasureData.getFromIndexUntilExcluding(offsetMeasure2,
					Constants.PATTERN_MEASURE_END).as("rawMeasures bar2");
			List<B00Measure> measures2 = parseMeasures(rawMeasures2);
			pattern.setMeasures2(measures2);
		}
	}

	/**
	 * rawMeasures layout:
	 * [1 byte time code] [1 byte instrument and volume ]* [0xFF]
	 * time code: 80=1 88=1.08 88=2
	 * instrument and volume: 0VVVCCCC V=Volume/Accent C=Channel
	 * end marker: 0xFF;
	 */
	private List<B00Measure> parseMeasures(BinaryData rawMeasures) {
		LogUtil.logTraceDump(rawMeasures, "rawMeasures");

		Condition isMeasureData = x -> (x & Constants.PATTERN_MEASURES_IS_MEASURE_MASK) > 0;
		List<BinaryData> measuresAndInstrumentsList = rawMeasures.chunksStartingWith(isMeasureData);
		measuresAndInstrumentsList = BinaryData.nameListAs(measuresAndInstrumentsList,
				"one measure byte and its sounding channels");

		LogUtil.logTraceDump(measuresAndInstrumentsList, "rawMeasures splitted");

		List<B00Measure> measures = Lists.newArrayList();
		for (BinaryData measureByteAndInstrumentsBytes : measuresAndInstrumentsList) {
			ParserUtil.assertFalse(measureByteAndInstrumentsBytes.isEmpty(), "Empty measure should not happen.");
			ParserUtil.assertFalse(measureByteAndInstrumentsBytes.get(0) == Constants.PATTERN_MEASURE_END,
					"Measure end byte should have been stripped off.");

			B00Measure measure = parseMeasure(measureByteAndInstrumentsBytes);
			measures.add(measure);
		}

		return measures;
	}

	private B00Measure parseMeasure(BinaryData measureByteAndInstrumentsBytes) {
		if (measureByteAndInstrumentsBytes.size() < 1) {
			throw new ParserException("Got empty measure data!");
		} else if (measureByteAndInstrumentsBytes.size() < 2) {
			LogUtil.logWarn(
					"Strange: This measure contains no instrument data. We expect at least one instrument to play for this measure. measure byte: %d",
					measureByteAndInstrumentsBytes.get(0));
		}

		int rawMeasure = measureByteAndInstrumentsBytes.get(0);
		int measure24s = ParserUtil.and7F(rawMeasure);
		AssertUtil.assertValueRange(measure24s, 0, PatternConstants.QUARTERS_PER_BAR
				* PatternConstants.QUARTER_QUANTIZATION);

		B00Measure measure = new B00Measure(measure24s);
		for (int i = 1; i < measureByteAndInstrumentsBytes.size(); i++) {
			int ChannelAndAccent = measureByteAndInstrumentsBytes.get(i);
			int channel = ChannelAndAccent & Constants.PATTERN_NOTE_CHANNEL_MASK;

			if (channel < 0 || channel > 15) {
				throw new ParserException("invalid channel " + channel);
			}

			int accent = (ChannelAndAccent & Constants.PATTERN_NOTE_ACCENT_MASK) >>> Constants.PATTERN_NOTE_ACCENT_SHIFT_RIGHT;
		AssertUtil.assertValueRange(accent, 0, 7);
		measure.addNote(channel, accent);
		LogUtil.logDebug("@%d=%d(%d)", measure24s, channel, accent);
		}

		LogUtil.logTraceDump(measure, "measure");
		return measure;
	}

	private List<B00Pattern> parsePatternsHeaders(BinaryData patterns8with5Variations) {
		List<BinaryData> patternHeaders = patterns8with5Variations
				.getEquallySizedChunks(Constants.SINGLE_PATTERN_LENGTH);

		List<PatternIdent> orderedPatternIdents = createOrderedPatternIdents();

		if (patternHeaders.size() != Constants.ALL_PATTERNS_COUNT
				|| orderedPatternIdents.size() != Constants.ALL_PATTERNS_COUNT) {
			throw new ParserException(
					"We got %d pattern headers and expected %d and got %d patternIdents and expected %d.",
					patternHeaders.size(), Constants.ALL_PATTERNS_COUNT, orderedPatternIdents.size(),
					Constants.ALL_PATTERNS_COUNT);
		}

		List<B00Pattern> patterns = Lists.newArrayList();
		for (int patternIndex = 0; patternIndex < Constants.ALL_PATTERNS_COUNT; patternIndex++) {
			PatternIdent patternIdent = orderedPatternIdents.get(patternIndex);
			BinaryData patternHeader = patternHeaders.get(patternIndex);

			int quarterTime = patternHeader.get(0) + 1;
			Assert.isTrue(TimeSignature.isValid(quarterTime));

			BinaryData channelInstruments = patternHeader.getRangeIncl(1, 16);
			int offsetMeasure1 = patternHeader.get(17) + 256 * patternHeader.get(18);
			int offsetMeasure2 = offsetMeasure1 + patternHeader.get(19);

			B00Pattern pattern = new B00Pattern(patternIdent, quarterTime, channelInstruments);
			pattern.setOffsetMeasureBar1(offsetMeasure1);
			pattern.setOffsetMeasureBar2(offsetMeasure2);

			patterns.add(pattern);
		}
		return patterns;
	}

	private List<PatternIdent> createOrderedPatternIdents() {
		List<PatternIdent> variationsIdent = PatternIdent.createVariations(Constants.PATTERNS_COUNT);
		List<PatternIdent> fillInsIdent = PatternIdent.createFillIns(Constants.PATTERNS_COUNT);

		ArrayList<PatternIdent> orderedPatternIdents = Lists.newArrayList();
		orderedPatternIdents.addAll(variationsIdent);
		orderedPatternIdents.addAll(fillInsIdent);
		return orderedPatternIdents;
	}

}

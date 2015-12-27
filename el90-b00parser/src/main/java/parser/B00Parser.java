package parser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import parser.constants.BlockKind;
import parser.constants.Constants;
import parser.constants.DataKind;
import parser.dataobjects.B00Data;
import parser.dataobjects.B00Measure;
import parser.dataobjects.B00Pattern;
import parser.dataobjects.BinaryData;
import parser.dataobjects.BinaryData.Condition;
import parser.util.ByteUtil;
import util.AssertUtil;
import util.LogUtil;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import electone.constants.ElectoneModel;
import electone.dataobjects.PatternConstants;
import electone.dataobjects.PatternIdent;
import electone.dataobjects.TimeSignature;

public class B00Parser {

	public B00Data parse(BinaryData binaryData) {

		BinaryData header = binaryData.getRange(0, 6);
		BinaryData data = binaryData.getTail(6);

		header.assertMatch(0, 0xf0, "EL90 B00file header magic number");
		header.assertMatch(1, 0x43, "EL90 B00file header magic number");
		header.assertMatch(2, 0x70, "EL90 B00file header magic number");
		header.assertMatch(3, "EL90 B00file header magic number", 0x70, 0x78);

		int modelByte = ByteUtil.and7F(header.get(5));
		ElectoneModel model = ElectoneModel.getModel(modelByte);
		LogUtil.log(model.toString());

		int dataKindByte = ByteUtil.and7F(header.get(4));
		DataKind dataKind = DataKind.get(dataKindByte);

		B00Data parseBulkdump = null;
		if (DataKind.BULKDUMP.equals(dataKind)) {
			LogUtil.log("BULKDUMP");
			parseBulkdump = parseBulkdump(data);
		}

		if (DataKind.REGISTRATION.equals(dataKind)) {
			LogUtil.log("REG (NIY)");
		}
		return parseBulkdump;
	}

	private B00Data parseBulkdump(BinaryData data) {
		B00Data b00data = new B00Data();

		Optional<BinaryData> nextBulkdumpBlock = Optional.of(data);
		while (nextBulkdumpBlock.isPresent()) {
			nextBulkdumpBlock = handleBulkdump(nextBulkdumpBlock.get(), b00data);
		}
		LogUtil.log("bulkdump end");
		return b00data;
	}

	private Optional<BinaryData> handleBulkdump(BinaryData data, B00Data b00data) {
		BlockKind blindTestKind = BlockKind.get(data.get(0));
		if (blindTestKind == BlockKind.END_BULK_BLOCK) {
			return Optional.absent();
		}
		LogUtil.log("bulkdump block");

		BinaryData head = data.getHead(Constants.BULK_DUMP_BLOCK_HEADER_LENGTH);
		assertBulkdumpBlockHeader(head);
		int blockLength = getBlockLengthFromblockHeader(head);
		BlockKind blockKind = getBlockKindFromblockHeader(head);

		BinaryData remainingData = data.getTail(Constants.BULK_DUMP_BLOCK_HEADER_LENGTH);

		BinaryData blockData = remainingData.getHead(blockLength);
		BinaryData nextblock = remainingData.getTail(blockLength + 1);
		int checksum = remainingData.get(blockLength);

		blockData.matchChecksum(checksum);

		parseBlocks(blockData, blockKind, b00data);
		return Optional.of(nextblock);
	}

	private void assertBulkdumpBlockHeader(BinaryData blockHeader) {
		blockHeader.nameValue(0, "blockkind");
		blockHeader.nameValue(1, "unknown");
		blockHeader.nameValue(2, "blocklength least sign bit");
		blockHeader.nameValue(3, "blocklength most sign bit");
		blockHeader.assertMatch(4, 0x00, "should be zero");
		blockHeader.assertMatch(5, 0x00, "should be zero");
	}

	private BlockKind getBlockKindFromblockHeader(BinaryData head) {
		return BlockKind.get(head.get(0));
	}

	private int getBlockLengthFromblockHeader(BinaryData blockHeader) {
		return blockHeader.get(2) + 128 * blockHeader.get(3);
	}

	private void parseBlocks(BinaryData bulkdumpBlock, BlockKind kind, B00Data b00data) {
		if (BlockKind.REG_BLOCK.equals(kind)) {
			LogUtil.log("REG BLOCK");
		} else if (BlockKind.VOICE_BLOCK.equals(kind)) {
			LogUtil.log("VOICE BLOCK");
		} else if (BlockKind.FLUTE_BLOCK.equals(kind)) {
			LogUtil.log("FLUTE BLOCK");
		} else if (BlockKind.PATTERN_BLOCK.equals(kind)) {
			LogUtil.log("PATTERN BLOCK");
			List<B00Pattern> parsePatternBlock = parsePatternBlock(bulkdumpBlock);
			b00data.setPatterns(parsePatternBlock);

		} else if (BlockKind.SEQUENCE_BLOCK.equals(kind)) {
			LogUtil.log("SEQUENCE BLOCK");
		} else {
			LogUtil.log("!! UNKNOWN BLOCK");
		}
	}

	private List<B00Pattern> parsePatternBlock(BinaryData patternBlock) {
		patternBlock.assertMinSize(Constants.PATTERN_LENGTH + Constants.PATTERN_UNKNOWN_OFFSET_BY_2);

		int headerLength = Constants.ALL_PATTERNS_COUNT * Constants.SINGLE_PATTERN_LENGTH;
		BinaryData patterns8with5VariationsHead = patternBlock.getHead(headerLength);
		BinaryData dataLengthAndMeasures = patternBlock.getTail(headerLength);

		int dataLength = dataLengthAndMeasures.get(0) + 256 * dataLengthAndMeasures.get(1);
		BinaryData measures = dataLengthAndMeasures.getTail(2 + Constants.PATTERN_UNKNOWN_WEIRD_GAP);
		measures.assertSize(dataLength + Constants.PATTERN_UNKNOWN_OFFSET_BY_2);

		List<B00Pattern> patterns = parsePatternsHeaders(patterns8with5VariationsHead);
		parseMeasures(patterns, measures);
		return patterns;
	}

	private void parseMeasures(List<B00Pattern> patterns, BinaryData rawMeasureData) {
		for (B00Pattern pattern : patterns) {

			LogUtil.logTraceDump(pattern.getPatternIdent(), "Parsing measures of pattern");

			int offsetMeasure1 = pattern.getOffsetMeasure1();

			BinaryData rawMeasures1 = rawMeasureData.getFromIndexUntilExcluding(offsetMeasure1,
					Constants.PATTERN_MEASURE_END);
			List<B00Measure> measures1 = parseMeasures(rawMeasures1);
			pattern.setMeasures1(measures1);

			int offsetMeasure2 = pattern.getOffsetMeasure2();

			BinaryData rawMeasures2 = rawMeasureData.getFromIndexUntilExcluding(offsetMeasure2,
					Constants.PATTERN_MEASURE_END);
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

		LogUtil.logTraceDump(measuresAndInstrumentsList, "rawMeasures splitted");

		List<B00Measure> measures = Lists.newArrayList();
		for (BinaryData measureByteAndInstrumentsBytes : measuresAndInstrumentsList) {
			ByteUtil.assertFalse(measureByteAndInstrumentsBytes.isEmpty(), "Empty measure should not happen.");
			ByteUtil.assertFalse(measureByteAndInstrumentsBytes.get(0) == Constants.PATTERN_MEASURE_END,
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
		int measure24s = ByteUtil.and7F(rawMeasure);
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

			B00Pattern pattern = new B00Pattern(patternIdent, quarterTime, channelInstruments, offsetMeasure1,
					offsetMeasure2);
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

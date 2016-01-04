package parser;

import java.util.ArrayList;
import java.util.List;

import electone.constants.ElectoneModel;
import parser.constants.BlockKind;
import parser.constants.ByteConstants;
import parser.constants.DataKind;
import parser.constants.ElectoneChecksumHelper;
import parser.dataobjects.B00Data;
import parser.dataobjects.B00Pattern;
import parser.dataobjects.BinaryData;
import parser.dataobjects.BlockAndRemaining;
import parser.util.ParserUtil;
import util.LogUtil;

public class B00Parser {

	private static final int ASSUMED_LIMIT_OF_BLOCKS = 100;

	public B00Data parse(BinaryData binaryData) {

		BinaryData header = binaryData.getRange(0, 6).as("B00 file header");
		BinaryData data = binaryData.getTail(6).as("B00 file data");

		header.assertMatch(0, 0xf0, "EL90 B00file header magic number");
		header.assertMatch(1, 0x43, "EL90 B00file header magic number");
		header.assertMatch(2, 0x70, "EL90 B00file header magic number");
		header.assertMatch(3, "EL90 B00file header magic number", 0x70, 0x78);

		int modelByte = ParserUtil.and7F(header.get(5));
		ElectoneModel model = ElectoneModel.getModel(modelByte);
		LogUtil.log(model.toString());

		int dataKindByte = ParserUtil.and7F(header.get(4));
		DataKind dataKind = DataKind.get(dataKindByte);

		B00Data parseBulkdump = null;
		if (DataKind.BULKDUMP.equals(dataKind)) {
			LogUtil.log("BULKDUMP");
			parseBulkdump = parseBulkdump(data);
		}

		if (DataKind.REGISTRATION.equals(dataKind)) {
			LogUtil.log("REG -> DO NOTHING");
		}
		return parseBulkdump;
	}

	private B00Data parseBulkdump(BinaryData data) {
		List<BinaryData> bulkDumpBlocks = collectBulkDumpBlocks(data);

		B00Data b00data = new B00Data();

		bulkDumpBlocks
				.forEach(bulkDumpBlock -> parseBlocks(bulkDumpBlock, b00data));

		return b00data;
	}

	private List<BinaryData> collectBulkDumpBlocks(BinaryData data) {
		List<BinaryData> bulkDumpBlocks = new ArrayList<>();

		BinaryData nextBulkdumpBlock = data;
		for (int loopCounter = 0; loopCounter < ASSUMED_LIMIT_OF_BLOCKS; loopCounter++) {
			BlockKind blockKind = getBlockKindFromBulkDumpBlock(nextBulkdumpBlock);
			if (blockKind == BlockKind.END_BULK_BLOCK) {
				break;
			}

			BlockAndRemaining blockAndRemaining = handleBulkdump(nextBulkdumpBlock);
			bulkDumpBlocks.add(blockAndRemaining.getBlock());

			nextBulkdumpBlock = blockAndRemaining.getRemainingData();
		}
		return bulkDumpBlocks;
	}

	private BlockAndRemaining handleBulkdump(BinaryData bulkDumpBlock) {
		BinaryData head = bulkDumpBlock.getHead(ByteConstants.BULK_DUMP_BLOCK_HEADER_LENGTH).as(
				"bulk dump inner block header");
		assertBulkdumpBlockHeader(head);
		int blockLength = getBlockLengthFromblockHeader(head);

		BinaryData remainingData = bulkDumpBlock.getTail(ByteConstants.BULK_DUMP_BLOCK_HEADER_LENGTH).as(
				"bulk dump block tail also following blocks");

		BinaryData blockData = remainingData.getHead(blockLength).as("bulk dump inner block");
		int checksum = remainingData.get(blockLength);
		BinaryData nextblock = remainingData.getTail(blockLength + 1).as("bulk dump next inner block");

		ElectoneChecksumHelper.matchChecksum(blockData, checksum);

		BlockAndRemaining blockAndRemaining = new BlockAndRemaining(blockData, nextblock);
		return blockAndRemaining;
	}

	private void assertBulkdumpBlockHeader(BinaryData blockHeader) {
		blockHeader.nameValue(0, "blockkind");
		blockHeader.nameValue(1, "unknown");
		blockHeader.nameValue(2, "blocklength least sign bit");
		blockHeader.nameValue(3, "blocklength most sign bit");
		blockHeader.assertMatch(4, 0x00, "should be zero");
		blockHeader.assertMatch(5, 0x00, "should be zero");
	}

	private BlockKind getBlockKindFromBulkDumpBlock(BinaryData bulkDumpBlock) {
		return BlockKind.get(bulkDumpBlock.get(0));
	}

	private int getBlockLengthFromblockHeader(BinaryData blockHeader) {
		return blockHeader.get(2) + 128 * blockHeader.get(3);
	}

	private void parseBlocks(BinaryData bulkdumpBlock, B00Data b00data) {
		BlockKind kind = getBlockKindFromBulkDumpBlock(bulkdumpBlock);

		if (BlockKind.REG_BLOCK.equals(kind)) {
			LogUtil.log("REG BLOCK -> DO NOTHING");
		} else if (BlockKind.VOICE_BLOCK.equals(kind)) {
			LogUtil.log("VOICE BLOCK -> DO NOTHING");
		} else if (BlockKind.FLUTE_BLOCK.equals(kind)) {
			LogUtil.log("FLUTE BLOCK -> DO NOTHING");
		} else if (BlockKind.PATTERN_BLOCK.equals(kind)) {
			LogUtil.log("PATTERN BLOCK");
			PatternParser patternParser = new PatternParser();

			List<B00Pattern> parsePatternBlock = patternParser.parsePatternBlock(bulkdumpBlock);
			b00data.setPatterns(parsePatternBlock);

		} else if (BlockKind.SEQUENCE_BLOCK.equals(kind)) {
			LogUtil.log("SEQUENCE BLOCK -> DO NOTHING");
		} else {
			LogUtil.log("!! UNKNOWN BLOCK");
		}
	}

}

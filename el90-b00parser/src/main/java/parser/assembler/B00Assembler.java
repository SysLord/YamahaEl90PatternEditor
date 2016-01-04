package parser.assembler;

import java.util.List;

import electone.constants.ElectoneModel;
import parser.constants.BlockKind;
import parser.constants.ByteConstants;
import parser.constants.DataKind;
import parser.constants.ElectoneChecksumHelper;
import parser.dataobjects.B00Pattern;
import parser.dataobjects.BinaryData;

public class B00Assembler {

	public BinaryData assemble(List<B00Pattern> b00patterns) {

		BinaryDataBuilder b00Head = assembleB00Head();

		BinaryDataBuilder pattern = buildPattern(b00patterns);

		BinaryDataBuilder endBlockMarker = new BinaryDataBuilder();
		endBlockMarker.append(BlockKind.END_BULK_BLOCK.getByteValue());

		BinaryDataBuilder res = b00Head.appendAll(b00Head, pattern, endBlockMarker);
		return res.build();
	}

	private BinaryDataBuilder buildPattern(List<B00Pattern> b00patterns) {

		BinaryDataBuilder patternBlockBody = createPattern(b00patterns);
		int blocksize = patternBlockBody.size();
		int checksumValue = ElectoneChecksumHelper.calcChecksum(patternBlockBody.getData());
		BinaryDataBuilder checksum = BinaryDataBuilder.from(checksumValue);

		BinaryDataBuilder patternHead = createPatternHead(blocksize);

		BinaryDataBuilder appendAll = patternHead.appendAll(patternHead, patternBlockBody, checksum);
		return appendAll;
	}

	private BinaryDataBuilder createPattern(List<B00Pattern> b00patterns) {
		return null;
	}

	private BinaryDataBuilder createPatternHead(int blocksize) {
		BinaryDataBuilder builder = new BinaryDataBuilder();
		builder.append(BlockKind.PATTERN_BLOCK.getByteValue());
		builder.append(0x00, "unknown");
		// blocksize = get(2) + 128 * get(3);
		builder.append(blocksize / 128);
		builder.append(blocksize % 128);
		// blockHeader.nameValue(2, "blocklength least sign bit");
		// blockHeader.nameValue(3, "blocklength most sign bit");
		builder.append(0x00, "expected 0x00");
		builder.append(0x00, "expected 0x00");

		return builder;
	}

	private BinaryDataBuilder assembleB00Head() {
		BinaryDataBuilder builder = new BinaryDataBuilder();

		builder.append(ByteConstants.B00_FILE_HEADER_0);
		builder.append(ByteConstants.B00_FILE_HEADER_1);
		builder.append(ByteConstants.B00_FILE_HEADER_2);
		builder.append(ByteConstants.B00_FILE_HEADER_3_A);

		builder.appendAdd80(ElectoneModel.EL90.getByteValue());
		builder.appendAdd80(DataKind.BULKDUMP.getValue());

		return builder;
	}
}

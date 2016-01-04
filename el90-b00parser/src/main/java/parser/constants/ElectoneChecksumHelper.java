package parser.constants;

import static parser.util.ParserUtil.and7F;

import parser.ParserException;
import parser.dataobjects.BinaryData;

public class ElectoneChecksumHelper {

	public static void matchChecksum(BinaryData data, int expectedSum) {
		Integer actualSum = sum(data);
		int num = and7F(expectedSum + actualSum);
		if (num != 0x7F) {
			throw new ParserException("Checksum bad");
		}
	}

	// from matchChecksum we know:
	// 0x7F = 0x7F & (expectedSum + actualSum)
	// which yields:
	// 0x7F - actualsum = expectedSum
	// overflow to 0x80 cannot happen
	public static int calcChecksum(BinaryData data) {
		Integer actualSum = sum(data);
		return 0x7F - actualSum;
	}

	private static Integer sum(BinaryData data) {
		Integer actualSum = 0;
		for (Integer value : data.getData()) {
			actualSum = and7F(actualSum + value);
		}
		return actualSum;
	}
}

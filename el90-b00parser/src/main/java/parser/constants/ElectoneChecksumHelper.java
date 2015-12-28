package parser.constants;

import static parser.util.ParserUtil.and7F;
import parser.ParserException;
import parser.dataobjects.BinaryData;

public class ElectoneChecksumHelper {

	public static void matchChecksum(BinaryData data, int expectedSum) {
		Integer actualSum = 0;
		for (Integer value : data.getData()) {
			actualSum = and7F(actualSum + value);
		}
		int num = and7F(expectedSum + actualSum);
		if (num != 0x7F) {
			throw new ParserException("Checksum bad");
		}
	}
}

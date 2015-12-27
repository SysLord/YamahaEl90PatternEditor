package util;

public class AssertUtil {

	public static void assertValueRange(int value, int minIncl, int maxIncl) {
		if (value < minIncl || value > maxIncl) {
			throw new RuntimeException(String.format("Value out of range: %d < %d < %d", minIncl, value, maxIncl));
		}
	}
}

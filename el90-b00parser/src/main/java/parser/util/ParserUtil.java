package parser.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

import com.google.common.primitives.Bytes;

public class ParserUtil {

	public static int and7F(int value) {
		return (value & 0x7f);
	}

	public static void assertFalse(boolean expression, String msg) {
		Assert.isTrue(!expression, msg);
	}

	public static List<Integer> toUnsignedInts(byte[] array) {
		return Bytes.asList(array)
				.stream()
				.map(b -> Byte.toUnsignedInt(b))
				.collect(Collectors.toList());
	}

	public static byte[] toSignedJavaBytes(List<Integer> ints) {
		return Bytes.toArray(ints);
	}

}

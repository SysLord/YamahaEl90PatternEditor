package parser.util;

import static org.junit.Assert.assertEquals;
import static parser.util.ParserUtil.and7F;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ParserUtilTest {

	@Test
	public void testAnd7F() throws Exception {
		assertEquals(0b01111111, and7F(0b11111111));
		assertEquals(0b01111111, and7F(0b11111111111));
		assertEquals(0b00001111, and7F(0b00001111));
	}

	@Test
	public void testMyWorkingImplVsGuava() throws Exception {
		byte[] allBytesArray = createAllBytesArray();
		List<Integer> ints = ParserUtil.toUnsignedInts(allBytesArray);

		byte[] bytes1 = myWorkingImplIntsToBytes(ints);
		byte[] bytes2 = ParserUtil.toSignedJavaBytes(ints);

		Assert.assertArrayEquals(bytes1, bytes2);

		Assert.assertArrayEquals(allBytesArray, bytes1);
		Assert.assertArrayEquals(allBytesArray, bytes2);
	}

	@Test
	public void assertMyIntsEqualsGuavaImpl() {
		List<Integer> myInts = myWorkingImplBytesToInts(createAllBytesArray());
		List<Integer> guavaInts = ParserUtil.toUnsignedInts(createAllBytesArray());

		Assert.assertEquals(myInts, guavaInts);
	}

	private byte[] createAllBytesArray() {
		byte[] arr = new byte[256];
		for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {
			arr[i + 128] = (byte) i;
		}
		return arr;
	}

	private List<Integer> myWorkingImplBytesToInts(byte[] array) {
		List<Integer> data = Lists.newArrayList();
		for (byte b : array) {
			data.add(Byte.toUnsignedInt(b));
		}
		return data;
	}

	private byte[] myWorkingImplIntsToBytes(List<Integer> data) {
		List<Byte> collect = data.stream().map(i -> i.byteValue()).collect(Collectors.toList());

		byte[] bytes = new byte[data.size()];
		for (int j = 0; j < collect.size(); j++) {
			bytes[j] = collect.get(j);
		}
		return bytes;
	}
}

package parser.constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import parser.dataobjects.BinaryData;

public class ElectoneChecksumHelperTest {

	@Test
	public void checksum() throws Exception {

		BinaryData data = new BinaryData(getValues());

		int calcChecksum = ElectoneChecksumHelper.calcChecksum(data);
		ElectoneChecksumHelper.matchChecksum(data, calcChecksum);
	}

	private ArrayList<Integer> getValues() {
		List<Integer> collect = IntStream.rangeClosed(0, 0xFF).boxed()
				.collect(Collectors.toList());
		ArrayList<Integer> list = new ArrayList<>(collect);
		Collections.shuffle(list, new Random(10));
		return list;
	}
}

package de.syslord.electonePattern.util;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IntStreamUtil {

	public static Stream<Integer> intStream(int low, int step, int max) {
		return IntStream.range(0, (max - low) / step)
				.boxed()
				.map(val -> (step * val) + low);
	}

}

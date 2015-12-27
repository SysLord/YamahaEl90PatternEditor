package electone.dataobjects;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class QuantizationTest {

	@Test
	public void testName() throws Exception {
		Assert.assertEquals(Quantization.WHOLE, Quantization.getMostCoarseQuantization(Arrays.asList(0)));

		Assert.assertEquals(Quantization.THIRDS, Quantization.getMostCoarseQuantization(Arrays.asList(96 / 3)));
		Assert.assertEquals(Quantization.QUARTER, Quantization.getMostCoarseQuantization(Arrays.asList(96 / 4)));
		Assert.assertEquals(Quantization.EIGHTH, Quantization.getMostCoarseQuantization(Arrays.asList(96 / 8)));
		Assert.assertEquals(Quantization.SIXTEENTH,
				Quantization.getMostCoarseQuantization(Arrays.asList(96 / 16)));

		Assert.assertEquals(Quantization.FINEST, Quantization.getMostCoarseQuantization(Arrays.asList(1)));

		Assert.assertEquals(Quantization.QUARTER,
				Quantization.getMostCoarseQuantization(Arrays.asList(0, 24, 48, 72)));
		Assert.assertEquals(Quantization.FINEST,
				Quantization.getMostCoarseQuantization(Arrays.asList(0, 1, 24, 48, 72)));
		Assert.assertEquals(Quantization.FINEST,
				Quantization.getMostCoarseQuantization(Arrays.asList(0, 24, 48, 72, 95)));

	}
}

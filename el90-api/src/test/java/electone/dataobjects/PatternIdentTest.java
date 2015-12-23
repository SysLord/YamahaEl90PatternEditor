package electone.dataobjects;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import electone.constants.PatternVariation;

public class PatternIdentTest {
	@Test
	public void compare() throws Exception {

		PatternIdent a0 = PatternIdent.of(0, PatternVariation.A);
		PatternIdent b0 = PatternIdent.of(0, PatternVariation.B);
		PatternIdent c0 = PatternIdent.of(0, PatternVariation.C);
		PatternIdent d0 = PatternIdent.of(0, PatternVariation.D);
		PatternIdent fill0 = PatternIdent.of(0, PatternVariation.FILL_IN);

		PatternIdent a6 = PatternIdent.of(6, PatternVariation.A);
		PatternIdent b3 = PatternIdent.of(3, PatternVariation.B);
		PatternIdent c4 = PatternIdent.of(4, PatternVariation.C);
		PatternIdent d2 = PatternIdent.of(2, PatternVariation.D);
		PatternIdent fill5 = PatternIdent.of(5, PatternVariation.FILL_IN);

		List<PatternIdent> actual = Arrays.asList(d0, fill0, a6, b3, a0, b0, c4, d2, c0, fill5);

		Collections.sort(actual);

		List<PatternIdent> expected = Arrays.asList(a0, b0, c0, d0, fill0, d2, b3, c4, fill5, a6);
		Assert.assertEquals(expected, actual);
	}
}

package parser.constants;

public class B00DrumMapper {

	// TODO we have instruments up to key 127 not just 124???
	// TODO map DrumInstruments, ditch key ids
	private static int mapB00InstrumentIdToKeyId[] = new int[]
	{
		/* key ids in [] are not used for drums */
		/* LK number 39, [40,41], 42 -> 84, perc numero 0 -> 42 */		
		/* 00 */60, 62, 57, 59, 55, 53, 48, 52, 49, 51,
		/* 10 */58, 56, 54, 46, 44, 42, 45, 82, 83, 81,
		/* 20 */80, 78, 47, 79, 77, 76, 74, 72, 71, 70,
		/* 30 */75, 73, 61, 64, 43, 67, 69, 66, 68, 84,
		/* 40 */39, 63, 65,
		/* UK note numero 86 -> [102,104,106, 109, 111, 114, 116] 124, perc numero 43 -> 75 */
		/* continue 40 ---- */86, 88, 93, 89, 999, 105, 110, /* [47]=?? map 47 to 39 */
		/* 50 */98, 99, 101, 121, 123, 115, 119, 117, 113, 108,
		/* 60 */94, 92, 90, 87, 112, 103, 107, 100, 118, 120,
		/* 70 */124, 122, 97, 95, 96, 91,
	};

	public static int mapB00InstrumentToKeyboardKey(int instrumentId) {
		return mapB00InstrumentIdToKeyId[instrumentId];
	}
}

package electone.constants;

import electone.dataobjects.Instrument;

// TODO finish el90 names
public enum DrumInstrument implements Instrument {

	/* LowerKeyboard left to right */
	X1(1, 39, "Claves", "Holzblock Höher", "01.wav"),
	X2(2, 42, "Synth.Tom3", "Synth Tom Tief", "02.wav"),
	X3(3, 43, "Concert Bd", "Pauke", "03.wav"),
	X4(4, 44, "Synth.Tom2", "Synth Tom Mitte", "04.wav"),
	X5(5, 45, "BD heavy", "Bass gedämpft", "05.wav"),
	X6(6, 46, "Synth.Tom1", "Synth Tom Hoch", "06.wav"),
	X7(7, 47, "BD light", "Bass härter", "07.wav"),
	X8(8, 48, "SD heavy", "Snare2", "08.wav"),
	X9(9, 49, "SD BR Roll", "Besen ?", "09.wav"),
	X10(10, 50, "SD heavy", "snare1", "10.wav"),
	X11(11, 51, "SD brShot1", "Besen Hit", "11.wav"),
	X12(12, 52, "SD reverb1", "Snare mit Teppich", "12.wav"),
	X13(13, 53, "SD light", "Snare hoch weich", "13.wav"),
	X14(14, 54, "Tom3", "Tom tief", "14.wav"),
	X15(15, 55, "SD rim", "Snare Rim", "15.wav"),
	X16(16, 56, "Tom2", "Tom Mitte", "16.wav"),
	X17(17, 57, "HH Chose", "Hihat geschlossen", "17.wav"),
	X18(18, 58, "Tom1", "Tom Hoch", "18.wav"),
	X19(19, 59, "HH open", "Hihat offenx19", "19.wav"),
	X20(20, 60, "Ride cym1", "Becken Rand", "20.wav"),
	X21(21, 61, "Orch.sd", "Snare mitte", "21.wav"),
	X22(22, 62, "Crash cym1", "Crash", "22.wav"),
	X23(23, 63, "SD Roll", "Trommelwirbel ?", "23.wav"),
	X24(24, 64, "Orch cym1", "Hand Becken", "24.wav"),
	X25(25, 65, "Orch cym2", "Becken Wirbel ?", "25.wav"),
	X26(26, 66, "Triangle C", "x26", "26.wav"),
	X27(27, 67, "Tambourine", "Tambourine", "27.wav"),
	X28(28, 68, "Triangle O", "Triangel?", "28.wav"),
	X29(29, 69, "Castanet", "Holzblock Tiefer", "29.wav"),
	X30(30, 70, "Cowbell1", "Kuhglocke", "30.wav"),
	X31(31, 71, "Timbale l", "Blechtrommel? Tief", "31.wav"),
	X32(32, 72, "Timbale h", "Blechtrommel? Hoch", "32.wav"),
	X33(33, 73, "Wood low", "x33", "33.wav"),
	X34(34, 74, "Conga low", "Standtrommel? Tief", "34.wav"),
	X35(35, 75, "Wood high", "x35", "35.wav"),
	X36(36, 76, "Conga high", "Bongo tief", "36.wav"),
	X37(37, 77, "Bongo low", "Bongo hoch", "37.wav"),
	X38(38, 78, "Agogo low", "x38", "38.wav"),
	X39(39, 79, "Bongo high", "x39", "39.wav"),
	X40(40, 80, "Agogo high", "x40", "40.wav"),
	X41(41, 81, "Cuica low", "x41", "41.wav"),
	X42(42, 82, "Hand Claps", "Klatschen", "42.wav"),
	X43(43, 83, "Cuica high", "x43", "43.wav"),
	X44(44, 84, "Shaker", "x44", "44.wav"),

	/* UpperKeyboard left to right */
	X45(45, 86, "Synth BD", "Große Trommel?", "45.wav"),
	X46(46, 87, "Tom Br Shot4", "Synth Base?", "46.wav"),
	X47(47, 88, "BD Attack", "Synth Base?", "47.wav"),
	X48(48, 89, "BD March", "Pauke", "48.wav"),
	X49(49, 90, "Tom Br Shot3", "x48", "49.wav"),
	X50(50, 91, "Synth SD", "Synth Tom?", "50.wav"),
	X51(51, 92, "Tom Br Shot2", "x50", "51.wav"),
	X52(52, 93, "SD reverb2", "x51", "52.wav"),
	X53(53, 94, "Tom Br Shot1", "x52", "53.wav"),
	X54(54, 95, "SD rim2", "Snare Rim", "54.wav"),
	X55(55, 96, "SD accent2", "Snare ?", "55.wav"),
	X56(56, 97, "TODO", "Besen?", "56.wav"),
	X57(57, 98, "SD accent1", "x56", "57.wav"),
	X58(58, 99, "Tom4", "x57", "58.wav"),
	X59(59, 100, "HH pedal2", "x58", "59.wav"),
	X60(60, 101, "HH pedal1", "x59", "60.wav"),
	X61(61, 103, "Tom3", "x60", "61.wav"),
	X62(62, 105, "Ride Cym2", "Ride Cymbal", "62.wav"),
	X63(63, 107, "Tom2", "Tom", "63.wav"),
	X64(64, 108, "Ride CymCup", "Ride Cymbal Glocke", "64.wav"),
	X65(65, 110, "Tom1", "Tom Hoch", "65.wav"),
	X66(66, 112, "Crash Cym 2", "Cymbal Crash?", "66.wav"),
	X67(67, 113, "Crash Cym 1", "x66", "67.wav"),
	X68(68, 115, "Cym Br Shot", "x67", "68.wav"),
	X69(69, 117, "Cym March", "x68", "69.wav"),
	X70(70, 118, "Orch TODO m", "x69", "70.wav"),
	X71(71, 119, "Conga Muff", "x70", "71.wav"),
	X72(72, 120, "Conga TODO", "x71", "72.wav"),
	X73(73, 121, "Cowbell2", "x72", "73.wav"),
	X74(74, 122, "Conga Slide", "x73", "74.wav"),
	X75(75, 123, "Cabasa", "x74", "75.wav"),
	X76(76, 124, "Quiro long", "Guiro langsam", "76.wav"),
	X77(77, 125, "Scratch", "Scratch", "77.wav"),
	X78(78, 126, "Quiro Short", "Guiro schnell?", "78.wav"),
	X79(79, 127, "Noise per.", "Synth Laser?", "79.wav"),

	UNKNOWN(999, 999, "UNKNOWN", "UNKNOWN INSTRUMENT", "79.wav");

	// TODO readable names
	@SuppressWarnings("unused")
	private String name;

	private String fileName;

	// TODO seems now unnecessary
	@SuppressWarnings("unused")
	private int idx;

	private int key;

	private String shortName;

	private DrumInstrument(int idx, int key, String shortName, String name, String fileName) {
		this.idx = idx;
		this.key = key;
		this.shortName = shortName;
		this.name = name;
		this.fileName = fileName;
	}

	@Override
	public String getName() {
		return shortName;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	public static DrumInstrument getInstrumentByKeyIndex(int keyIndex) {
		for (DrumInstrument v : values()) {
			if (v.key == keyIndex) {
				return v;
			}
		}
		throw new RuntimeException(" INVALID KEY INDEX ======================================================== "
				+ keyIndex);
	}
}

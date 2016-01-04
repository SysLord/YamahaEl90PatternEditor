package parser.constants;

public interface ByteConstants {

	int BULK_DUMP_BLOCK_HEADER_LENGTH = 6;

	int B00_FILE_HEADER_0 = 0xf0;
	int B00_FILE_HEADER_1 = 0x43;
	int B00_FILE_HEADER_2 = 0x70;
	/* both are possible */
	int B00_FILE_HEADER_3_A = 0x78;
	int B00_FILE_HEADER_3_B = 0x70;

	/* block lengths */
	int LG_REGC = 0xCD;
	int LG_REG = 0xCA;
	int LG_VOI = 0x1CC;
	int LG_FLU = 8 * 8 + 0x40 + 8 * 8 + 0x40;
	int LG_SEQ = 4 * 120 * 2;

}

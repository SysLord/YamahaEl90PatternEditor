package parser.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Component;

import electone.dataobjects.Pattern;
import electone.dataobjects.Patterns;
import parser.B00Parser;
import parser.ParserMain;
import parser.assembler.B00Assembler;
import parser.dataobjects.B00Data;
import parser.dataobjects.B00Pattern;
import parser.dataobjects.BinaryData;
import parser.util.ParserUtil;
import util.LogUtil;

@Component
public class ParserMainImpl implements ParserMain {

	/**
	 * Parser main, for tests. Logs results.
	 *
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length < 1) {
			LogUtil.log("Expect B00 file as first parameter.");
			return;
		}

		String b00path = args[0];
		ParserMainImpl parserMain = new ParserMainImpl();
		Patterns ps = parserMain.parsePatternsFromFile(b00path);

		LogUtil.logDump(ps, "===== RESULT =====");
	}

	@Override
	public Patterns parsePatternsFromFile(String b00path) {
		byte[] readFile = readFile(Paths.get(b00path));
		List<Integer> intbasedBinaryData = ParserUtil.toUnsignedInts(readFile);
		BinaryData binaryData = new BinaryData(intbasedBinaryData).as("whole B00 File");

		B00Parser parser = new B00Parser();
		B00Data b00Data = parser.parse(binaryData);

		PatternConverter converter = new PatternConverter();
		return converter.fromB00ToPattern(b00Data);
	}

	private static byte[] readFile(Path path) {
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public byte[] patternsToB00(Pattern pattern) {
		PatternConverter converter = new PatternConverter();

		B00Pattern b00pattern = converter.fromPatternToB00(pattern);
		B00Assembler assembler = new B00Assembler();

		// BinaryData b = assembler.assemble(b00pattern);

		// return ParserUtil.toSignedJavaBytes(b.getData());
		return null;
	}
}

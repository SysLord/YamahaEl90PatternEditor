package parser.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Component;

import parser.B00Parser;
import parser.ParserMain;
import parser.dataobjects.B00Data;
import parser.dataobjects.BinaryData;
import util.LogUtil;

import com.google.common.collect.Lists;

import electone.dataobjects.Patterns;

@Component
public class ParserMainImpl implements ParserMain {

	public static void main(String[] args) {

		if (args.length < 1) {
			LogUtil.log("Expect B00 file as first parameter.");
			return;
		}

		String b00path = args[0];
		ParserMainImpl parserMain = new ParserMainImpl();
		Patterns ps = parserMain.parsePatternsFromFile(b00path);

		LogUtil.log(ps, "===== RESULT =====");
	}

	@Override
	public Patterns parsePatternsFromFile(String b00path) {
		byte[] readFile = readFile(Paths.get(b00path));
		BinaryData binaryData = toIntbasedBinaryData(readFile);

		B00Parser parser = new B00Parser();
		B00Data b00Data = parser.parse(binaryData);

		LogUtil.logDebug(b00Data, "B00 object");

		PatternConverter converter = new PatternConverter();

		return converter.toBusiness(b00Data);
	}

	private static byte[] readFile(Path path) {
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// Convert bytes to int. Java bytes seem kind of useless to work with for this job.
	private BinaryData toIntbasedBinaryData(byte[] array) {
		List<Integer> data = Lists.newArrayList();
		for (byte b : array) {
			data.add(Byte.toUnsignedInt(b));
		}
		BinaryData binaryData = new BinaryData(data);
		return binaryData;
	}
}

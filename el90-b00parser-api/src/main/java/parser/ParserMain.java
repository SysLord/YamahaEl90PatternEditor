package parser;

import electone.dataobjects.Pattern;
import electone.dataobjects.Patterns;

public interface ParserMain {

	Patterns parsePatternsFromFile(String path);

	byte[] patternsToB00(Pattern pattern);

}

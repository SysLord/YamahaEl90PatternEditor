package electone.dataobjects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Patterns {

	private Map<PatternIdent, Pattern> patterns = new HashMap<>();

	public void addPattern(PatternIdent patternIdent, Pattern pattern) {
		patterns.put(patternIdent, pattern);
	}

	@Override
	public String toString() {
		return patterns.toString();
	}

	public Pattern get(PatternIdent ident) {
		return patterns.get(ident);
	}

	public List<PatternIdent> getAvailablePatterns() {
		return patterns.keySet().stream().collect(Collectors.toList());
	}
}

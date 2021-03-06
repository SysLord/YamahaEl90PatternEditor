package parser.dataobjects;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.util.Assert;

import parser.ParserException;
import parser.util.HexPrintUtil;
import util.LogUtil;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class BinaryData {

	private static final String UNKNOWN_DATA = "'unknown data'";

	public interface Condition {
		boolean condition(int value);
	}

	private List<Integer> data;

	private String debugInfo = UNKNOWN_DATA;

	public BinaryData(List<Integer> data) {
		this.data = ImmutableList.copyOf(data);
	}

	public BinaryData(List<Integer> data, String debugInfo) {
		this(data);
		this.debugInfo = debugInfo;
	}

	public static BinaryData create(Integer... values) {
		return new BinaryData(Arrays.asList(values), UNKNOWN_DATA);
	}

	public BinaryData as(String debugInfo) {
		return new BinaryData(data, debugInfo);
	}

	/**
	 * Returns BinaryData from <tt>from</tt> inclusive, until <tt>to</tt> exclusive.
	 */
	public BinaryData getRange(int from, int to) {
		return new BinaryData(data.subList(from, to));
	}

	public BinaryData getRangeIncl(int from, int to) {
		return getRange(from, to + 1);
	}

	public BinaryData getTail(int startingAt) {
		List<Integer> tail = data.subList(startingAt, data.size());
		return new BinaryData(tail);
	}

	public int get(int position) {
		return data.get(position);
	}

	public BinaryData getHead(int exclUpperBound) {
		List<Integer> head = head(exclUpperBound);
		return new BinaryData(head);
	}

	protected List<Integer> head(int exclUpperBound) {
		return data.subList(0, exclUpperBound);
	}

	public void assertMatch(int index, int value, String reason) {
		if (get(index) != value) {
			throw new RuntimeException(reason);
		}
	}

	public void assertMatch(int index, String reason, Integer... values) {
		int valueToTest = get(index);
		boolean anyMatch = Arrays.asList(values).stream()
				.anyMatch(possibleValidValue -> valueToTest == possibleValidValue);

		if (!anyMatch) {
			throw new ParserException(reason);
		}
	}

	public List<Integer> getData() {
		return data;
	}

	/**
	 * Does nothing meaningful. Logs the value with its name.
	 */
	public void nameValue(int index, String valueName) {
		LogUtil.logDump(data.get(index), valueName);
	}

	public void assertMinSize(int minSize) {
		if (data.size() < minSize) {
			throw new ParserException("%s is smaller %d than expected minsize %d.", getDebugInfo(), data.size(),
					minSize);
		}
	}

	public void assertSize(int size) {
		Assert.isTrue(data.size() == size,
				String.format("%s %d is not equal to expected size %d.", getDebugInfo(), data.size(), size));
	}

	@Override
	public String toString() {
		return HexPrintUtil.getHumandReadable(data);
	}

	public int size() {
		return data.size();
	}

	public List<BinaryData> getEquallySizedChunks(int chunkSize) {
		if (data.size() % chunkSize != 0) {
			throw new ParserException("%s cannot be split in %d-sized chunks, because size %d is not devisible.",
					getDebugInfo(), chunkSize, data.size());
		}

		List<List<Integer>> partition = Lists.partition(data, chunkSize);
		List<BinaryData> collect = partition.stream().map(x -> new BinaryData(x)).collect(Collectors.toList());
		return collect;
	}

	public BinaryData getFromIndexUntilExcluding(int startIndex, int untilValue) {
		BinaryData tail = getTail(startIndex);
		int indexOf = tail.indexOf(untilValue);
		if (indexOf >= 0) {
			return tail.getRange(0, indexOf);
		}
		throw new ParserException("%s not found in %s", HexPrintUtil.toHex(untilValue), getDebugInfo());
	}

	public BinaryData getFromIndexUntilIncluding(int startIndex, int untilValue) {
		BinaryData tail = getTail(startIndex);
		int indexOf = tail.indexOf(untilValue);
		if (indexOf >= 0) {
			return tail.getRange(0, indexOf + 1);
		}
		throw new ParserException("%s not found in %s", HexPrintUtil.toHex(untilValue), getDebugInfo());
	}

	private int indexOf(int value) {
		return data.indexOf(Integer.valueOf(value));
	}

	public List<BinaryData> chunksStartingWith(Condition condition) {
		List<Integer> positions = Lists.newArrayList();

		for (int i = 0; i < data.size(); i++) {
			Integer value = data.get(i);
			if (condition.condition(value)) {
				positions.add(i);
			}
		}

		if (positions.isEmpty()) {
			return Lists.newArrayList();
		}

		List<BinaryData> splitData = Lists.newArrayList();
		for (int index = 0; index < positions.size(); index++) {
			Integer position = positions.get(index);

			int nextIndex = index + 1;
			boolean positionsIndexInBounds = nextIndex < positions.size();
			Integer nextPosition = positionsIndexInBounds ? positions.get(nextIndex) : data.size();

			BinaryData range = getRange(position, nextPosition);
			splitData.add(range);
		}
		return splitData;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BinaryData other = (BinaryData) obj;
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!data.equals(other.data)) {
			return false;
		}
		return true;
	}

	public boolean isEmpty() {
		return data.isEmpty();
	}

	public String getDebugInfo() {
		return debugInfo;
	}

	public static List<BinaryData> nameListAs(List<BinaryData> dataList, String debugString) {
		Function<BinaryData, BinaryData> mapper = binaryData -> binaryData.as(debugString);
		return dataList.stream()
				.map(mapper)
				.collect(Collectors.toList());
	}

}

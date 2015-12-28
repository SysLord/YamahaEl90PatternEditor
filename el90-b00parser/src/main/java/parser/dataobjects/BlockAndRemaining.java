package parser.dataobjects;

public class BlockAndRemaining {

	private BinaryData block;
	private BinaryData remainingData;

	public BlockAndRemaining(BinaryData block, BinaryData remainingData) {
		this.block = block;
		this.remainingData = remainingData;
	}

	public BinaryData getBlock() {
		return block;
	}

	public BinaryData getRemainingData() {
		return remainingData;
	}

}

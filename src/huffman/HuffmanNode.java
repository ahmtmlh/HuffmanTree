package huffman;

public class HuffmanNode extends Node{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4647257638545194461L;

	public HuffmanNode(Node left, Node right) {
		super(EMPTY, left.getFreq()+right.getFreq(), left, right);
	}
}

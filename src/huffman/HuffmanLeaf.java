package huffman;

import java.util.Map;

public class HuffmanLeaf extends Node{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1998253131153332059L;

	public HuffmanLeaf(char data, int freq) {
		super(data, freq);
	}
	
	public HuffmanLeaf(Map.Entry<Character, Integer> entry) {
		super(entry.getKey(), entry.getValue());
	}
}

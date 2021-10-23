package huffman;

import java.io.Serializable;

public class Node implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6066430594853018755L;

	// STATIC FINAL FIELDS
	
	protected static final char EMPTY = '\0';
	protected static final Node BLANK_NODE = new Node(EMPTY, 0);
	// INSTANCE FIELDS
	
	private char data;
	private int freq;
	private Node left;
	private Node right;
	
	// CONSTRUCTORS

	public Node(char data) {
		this.data = data;
	}
	
	public Node(char data, int freq) {
		this(data);
		this.freq = freq;
	}
	
	public Node(char data, int freq, Node left, Node right) {
		this(data,freq);
		this.left = left;
		this.right = right;
	}
	
	// GETTERS SETTERS
	
	public char getData() {
		return data;
	}
	public void setData(char data) {
		this.data = data;
	}
	public int getFreq() {
		return freq;
	}
	public void setFreq(int freq) {
		this.freq = freq;
	}
	public Node getLeft() {
		return left;
	}
	public void setLeft(Node left) {
		this.left = left;
	}
	public Node getRight() {
		return right;
	}
	public void setRight(Node right) {
		this.right = right;
	}
	
	public boolean isEmptyData() {
		return this.data == EMPTY;
	}
	
}

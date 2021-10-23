package huffman;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import huffman.exception.IllegalInputException;

public class HuffmanTree implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6230183299955938091L;
	private HuffmanNode root;
	// Don't save the characterCodeMap and the actual text to a file.
	private transient Map<Character, String> characterCodeMap;
	private transient String code;
	
	public HuffmanTree(HuffmanNode root, String code) {
		this(root);
		this.setCode(code);
	}

	public HuffmanTree(HuffmanNode root) {
		this.root = root;
		characterCodeMap = new HashMap<>();
		generateMap("", root);
	}
	
	public HuffmanNode getRoot() {
		return root;
	}

	public void setRoot(HuffmanNode root) {
		this.root = root;
	}
	
	/**
	 * THIS FUNCTION ENCODES TEXT AS ANOTHER TEXT OF 1 AND 0
	 * THIS IS FOR DEBUGGING ONLY, HAS NO COMPRESSION PURPOSES
	 * @return String of encoded text. Only 1 and 0
	 */
	
	public String encode() {
		StringBuilder sb = new StringBuilder();
		for(char c : code.toCharArray()) {
			sb.append(characterCodeMap.get(c));
		}	
		return sb.toString();
	}

	public int[] encodeBinary() {
		List<Integer> list = new ArrayList<>();
		int count = 0;
		int chunk = 0;
		for(char c : code.toCharArray()) {
			String current = characterCodeMap.get(c);
			for(char ch : current.toCharArray()) {
				if(count == 31) {
					list.add(chunk);
					chunk = 0;
					count = 0;
				}
				int bit = Integer.parseInt(Character.toString(ch));
				chunk |= bit;
				chunk = chunk << 1;
				count++;
			}
		}
		if(count != 0) {
			list.add(chunk);
		}
		list.add(count);
		int[] ret = new int[list.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = list.get(i);
		}
		return ret;
	}
	
	/**
	 * THIS FUNCTION ONLY DECODES INPUT GIVEN AS STRING
	 * STRING MUST BE IN 1 & 0 FORM. DEBUGGING ONLY
	 * @param inputStr Input String as the encoded text. 
	 * @return Decoded text.
	 */
	
	public String decode(String inputStr) throws IllegalInputException{
		StringBuilder sb = new StringBuilder();
		Node temp = root;
		for (char c : inputStr.toCharArray()) {
			if(c == '1') {
				temp = temp.getRight();
			} else if (c == '0'){
				temp = temp.getLeft();
			}
			else {
				throw new IllegalInputException("Input string must only have 1 or 0");
			}
			if(!temp.isEmptyData()) {
				sb.append(Character.toString(temp.getData())); 
				temp = root;
			}
		}
		return sb.toString();
	}
	
	public String decodeBinary(int[] input) {
		StringBuilder sb = new StringBuilder();
		Node temp = root;
		for (int i = 0; i < input.length-1; i++) {
			int bitCounter = 0;
			// For the last number taht will go to the array.
			// Number of bits that are used by the last number is contained in the next position of the array
			// Doing this prevents data corruption at the end of the message.
			if(i == input.length-2) {
				bitCounter = 31 - input[input.length-1];
			}
			while(bitCounter < 31) {
				int bit = (input[i] >> (31-bitCounter)) & 1;
				bitCounter++;
				if(bit == 1) {
					temp = temp.getRight();
				} else {
					temp = temp.getLeft();
				}
				if(!temp.isEmptyData()) {
					sb.append(temp.getData());
					temp = root;
				}
			}
		}
		return sb.toString();
	}
	
	private void generateMap(String path, Node node) {
		
		if(node == null) {
			return;
		}
		
		if(node instanceof HuffmanLeaf) {
			characterCodeMap.put(node.getData(), path);
		}
		
		generateMap(path+"0", node.getLeft());
		generateMap(path+"1", node.getRight());
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * Saves the tree object that this function was called on
	 * to a file in order to read it another time. Standart Serializable
	 * Interface is used.
	 * @param fileName Name of the file that the tree will be save to.
	 */
	
	public void saveToFile(String fileName) {
		//Change file extension.
		if(!fileName.endsWith(".huff")) {
			fileName = fileName + ".huff";
		}
		//Try within sources method. No need to close streams 
		try(FileOutputStream fOut = new FileOutputStream(fileName);
				ObjectOutputStream out = new ObjectOutputStream(fOut)){
			out.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveToFile() {
		saveToFile("tree.huff");
	}

}

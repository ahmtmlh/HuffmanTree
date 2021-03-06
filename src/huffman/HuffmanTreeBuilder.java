package huffman;

import java.util.Map;
import java.util.PriorityQueue;
import exception.IllegalInputException;

import java.util.Queue;
import java.util.Comparator;
import java.util.LinkedHashMap;

public class HuffmanTreeBuilder {
	
	private Queue<Node> nodeQueue;
	private final Map<Character, Integer> frequencyMap;
	private String code;
	
	public HuffmanTreeBuilder() {
		frequencyMap = new LinkedHashMap<>();
	}
	
	public HuffmanTree buildTree() throws IllegalInputException {
		buildFrequencyList();
		return buildTreeInternal();
	}
	
	public void buildFrequencyMap(String inputStr) {
		for(char c : inputStr.toCharArray()) {
			if(frequencyMap.containsKey(c))
				frequencyMap.put(c, frequencyMap.get(c)+1);
			else 
				frequencyMap.put(c, 1);
		}
		code = inputStr;
	}
	
	private void buildFrequencyList() {
		nodeQueue = new PriorityQueue<>(1, Comparator.comparingInt(Node::getFreq));
		frequencyMap.entrySet().forEach(item -> nodeQueue.add(new HuffmanLeaf(item)));
	}
	
	private HuffmanTree buildTreeInternal() throws IllegalInputException{
		HuffmanTree tree;
		
		if (nodeQueue.isEmpty()) {
			throw new IllegalInputException("Input stream is corrupted or empty!");
		}
		
		while(nodeQueue.size() > 1) {
			Node n1 = getNodeFromQueue();
			Node n2 = getNodeFromQueue();
			
			// Left node needs to have the smaller frequency of the two
			Node left = n1.getFreq() < n2.getFreq() ? n1 : n2;
			Node right = n1.getFreq() < n2.getFreq() ? n2 : n1;
			
			HuffmanNode newNode = new HuffmanNode(left, right);
			nodeQueue.add(newNode);
		}
		
		Node lastNode = nodeQueue.remove();
		HuffmanNode rootNode;
		
		// This should only happen if the input stream is one character long
		if (lastNode instanceof HuffmanLeaf) {
			rootNode = new HuffmanNode(lastNode, HuffmanNode.BLANK_NODE);
		} else {
			rootNode = (HuffmanNode) lastNode;
		}
		
		tree = new HuffmanTree(rootNode, code);
		
		return tree;
	}
	
	
	private Node getNodeFromQueue() {
		if (nodeQueue.isEmpty())
			return Node.BLANK_NODE;
		
		return nodeQueue.remove();
	}
	
}

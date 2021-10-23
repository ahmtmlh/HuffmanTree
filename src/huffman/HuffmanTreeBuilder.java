package huffman;

import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import huffman.exception.IllegalInputException;

import java.util.Queue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.LinkedHashMap;

public class HuffmanTreeBuilder {
	
	private Queue<Node> nodeQueue;
	private Map<Character, Integer> frequencyMap;
	private StringBuilder codeStringBuilder;
	
	public HuffmanTreeBuilder() {
		frequencyMap = new LinkedHashMap<>();
		codeStringBuilder = new StringBuilder();
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
		codeStringBuilder.append(inputStr);
	}
	
	private void buildFrequencyList() {
		nodeQueue = new PriorityQueue<>(1, Comparator.comparingInt(Node::getFreq));
		
		List<Entry<Character, Integer>> sortedList = new ArrayList<>(frequencyMap.entrySet());
		sortedList.sort(Entry.comparingByValue());
		
		sortedList.forEach(item -> nodeQueue.add(new HuffmanLeaf(item)));
	}
	
	private HuffmanTree buildTreeInternal() throws IllegalInputException{
		HuffmanTree tree = null;
		
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
		
		if (lastNode instanceof HuffmanLeaf) {
			// This should only happen if the input stream is one character long
			rootNode = new HuffmanNode(lastNode, HuffmanNode.BLANK_NODE);
		} else {
			rootNode = (HuffmanNode) lastNode;
		}
		
		tree = new HuffmanTree(rootNode, codeStringBuilder.toString());
		
		return tree;
	}
	
	
	private Node getNodeFromQueue() {
		if (nodeQueue.isEmpty())
			return Node.BLANK_NODE;
		
		return nodeQueue.remove();
	}
	
}

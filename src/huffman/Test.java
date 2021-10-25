package huffman;

import java.io.*;
import java.util.Scanner;

import exception.IllegalInputException;

public class Test {

	private static final int OPERATION_CREATE_FILE = 1;
	private static final int OPERATION_READ_FILE = 2;


	public static void main(String[] args) {
		//codeTest();
		ioTest(Integer.parseInt(args[0]), args[1]);
	}

	public static void ioTest(int operation, String filename){

		switch (operation){
			case OPERATION_CREATE_FILE:
				if (!createHuffmanFile(filename)) {
					System.err.println("Operation failed!");
				}
				break;
			case OPERATION_READ_FILE:
				readHuffmanFile(filename);
				break;
			default:
				System.err.println("Unknown operation!");
				System.exit(1);
				break;
		}
	}

	private static void readHuffmanFile(String filename){
		try {
			HuffmanFile file = HuffmanFile.readFromFile(filename);
			String newFileName = filename.replace(".huff", ".orig");

			try(BufferedWriter bw = new BufferedWriter(new FileWriter(newFileName))){
				bw.write(file.getText());
			}

			System.out.println("File contents are dumped to: " + newFileName);

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static boolean createHuffmanFile(String filename){
		long old = System.currentTimeMillis();
		int c;
		StringBuilder sb = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new FileReader(filename))){

			while ((c = br.read()) != -1){
				sb.append((char) c);
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		System.out.println("Read file time: "  + (System.currentTimeMillis() - old) + "ms");
		old = System.currentTimeMillis();
		HuffmanTreeBuilder builder = new HuffmanTreeBuilder();
		builder.buildFrequencyMap(sb.toString());
		System.out.println("Frequency Map build time: "  + (System.currentTimeMillis() - old) + "ms");
		old = System.currentTimeMillis();

		HuffmanTree tree;
		try {
			tree = builder.buildTree();
		} catch (IllegalInputException e) {
			e.printStackTrace();
			return false;
		}

		System.out.println("Huffman Tree build time: "  + (System.currentTimeMillis() - old) + "ms");
		old = System.currentTimeMillis();

		int[] encodedBinary = tree.encodeBinary();

		System.out.println("Binary encoding time: "  + (System.currentTimeMillis() - old) + "ms");
		old = System.currentTimeMillis();

		HuffmanFile file = new HuffmanFile(tree, encodedBinary);

		try {
			file.saveFile(filename + ".huff");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		System.out.println("Huffman File save time: "  + (System.currentTimeMillis() - old) + "ms");

		return true;
	}

	public static void codeTest() {
		HuffmanTreeBuilder builder = new HuffmanTreeBuilder();
		String inputString = "Test Quarry for the next level of testing with blanks and punctuation.Test Quarry for" +
				" the next level of testing with blanks and punctuation.Test Quarry for the next level of testing" +
				" with blanks and punctuation.Test Quarry for the next level of testing with blanks and punctuation" +
				".Test Quarry for the next level of testing with blanks and punctuation.Test Quarry for the next " +
				"level of testing with blanks and punctuation.Test Quarry for the next level of testing with blanks" +
				" and punctuation.Test Quarry for the next level of testing with blanks and punctuation.Test Quarry" +
				" for the next level of testing with blanks and punctuation.Test Quarry for the next level of testing" +
				" with blanks and punctuation.Test Quarry for the next level of testing with blanks and punctuation." +
				"Test Quarry for the next level of testing with blanks and punctuation.";
		builder.buildFrequencyMap(inputString);
		HuffmanTree tree;
		try {
			tree = builder.buildTree();
		} catch (IllegalInputException e1) {
			e1.printStackTrace();
			return;
		}
		
		int[] encodedBinary = tree.encodeBinary();
		
		try (FileOutputStream fOut = new FileOutputStream("b-out.bin");
				ObjectOutputStream out = new ObjectOutputStream(fOut)) {
			out.writeObject(encodedBinary);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tree.saveToFile();
		
		String s = tree.decodeBinary(tree.encodeBinary());
		System.out.println("Decoded string: " + s);
		System.out.println("Decoding success: " + s.equals(inputString));

		System.out.printf("Original string size: ~%d, huffman decoded byte size: ~%d%n", inputString.length(), encodedBinary.length * 4);
	}

	public static void readCodeTest() {
		int[] code = null;

		try (ObjectInputStream input = new ObjectInputStream(
				new BufferedInputStream(new FileInputStream("b-out.bin")))){
			code = (int[])input.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}

		try (ObjectInputStream input = new ObjectInputStream(
				new BufferedInputStream(new FileInputStream("tree.huff")))) {
			HuffmanTree tree = (HuffmanTree) input.readObject();
			String decoded = tree.decodeBinary(code);
			System.out.println(decoded);
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}

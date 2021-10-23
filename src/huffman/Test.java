package huffman;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import huffman.exception.IllegalInputException;

public class Test {

	public static void main(String[] args) {
		codeTest();
	}

	public static void codeTest2() {
		HuffmanTreeBuilder builder = new HuffmanTreeBuilder();
		HuffmanTree tree = null;
		StringBuilder test = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(new File("input2.txt")))) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
				builder.buildFrequencyMap(line);
				test.append(line);
			}
			tree = builder.buildTree();
		} catch (IllegalInputException | IOException e) {
			e.printStackTrace();
			return;
		}
		// Save tree to file in order to read it before. Java Serializable is used
		tree.saveToFile();
		int[] encoded = tree.encodeBinary();
		String codeB = tree.decodeBinary(encoded);
		System.out.println(codeB.equals(test.toString()));
		saveTextFile(codeB, "b-out.txt");
		// Try within sources method. No need to close streams
		try (FileOutputStream fOut = new FileOutputStream("b-out.bin");
				ObjectOutputStream out = new ObjectOutputStream(fOut)) {
			out.writeObject(encoded);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void saveTextFile(String str, String filename) {
		try (BufferedWriter out = new BufferedWriter(new FileWriter(filename))) {
			out.write(str);
			out.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void codeTest() {
		HuffmanTreeBuilder builder = new HuffmanTreeBuilder();
		String inputString = "Test Quarry for the next level of testing with blanks and punctuation.Test Quarry for the next level of testing with blanks and punctuation.Test Quarry for the next level of testing with blanks and punctuation.Test Quarry for the next level of testing with blanks and punctuation.Test Quarry for the next level of testing with blanks and punctuation.Test Quarry for the next level of testing with blanks and punctuation.Test Quarry for the next level of testing with blanks and punctuation.Test Quarry for the next level of testing with blanks and punctuation.Test Quarry for the next level of testing with blanks and punctuation.Test Quarry for the next level of testing with blanks and punctuation.Test Quarry for the next level of testing with blanks and punctuation.Test Quarry for the next level of testing with blanks and punctuation.";
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
		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
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

package huffman;

import java.io.*;

public class HuffmanFile implements Serializable {

    private static final long serialVersionUID = 4125L;

    private HuffmanTree tree;
    private int[] bytes;
    private transient String text;
    private transient String name;

    private static <T> T safeCast(Class<T> clazz, Object obj){
        return clazz.cast(obj);
    }

    public static HuffmanFile readFromFile(String filename) throws IOException, ClassNotFoundException{
        return readFromFile(new File(filename));
    }

    public static HuffmanFile readFromFile(File _file) throws IOException, ClassNotFoundException{
        HuffmanFile file = null;

        try (ObjectInputStream input = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(_file)))){
            Object obj = input.readObject();
            file = safeCast(HuffmanFile.class, obj);

            file.name = _file.getName();

            return file;
        }
    }

    private HuffmanFile(){

    }

    public HuffmanFile(HuffmanTree tree, int[] bytes){
        this.tree = tree;
        this.bytes = bytes;
        this.text = null;
    }

    public String getText() {
        if (text == null){
            text = tree.decodeBinary(bytes);
        }
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void saveFile(String filename) throws IOException{
        this.name = filename;

        try(FileOutputStream fOut = new FileOutputStream(this.name);
            ObjectOutputStream out = new ObjectOutputStream(fOut)){
            out.writeObject(this);
        }
    }
}

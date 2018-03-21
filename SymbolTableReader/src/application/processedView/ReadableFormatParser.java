package application.processedView;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


public class ReadableFormatParser {
	private final static String INDENT_STRING = "  ";
	private static BufferedWriter writer;
	
	public static File parseStructure(ArrayList<Structure> structs, int baseIndent, File file) throws IOException{
		writer = new BufferedWriter(new FileWriter(file));
		String indentString = "";
		for (int i=0;i<baseIndent; i++) {
			indentString = indentString + INDENT_STRING;
		}
		parseList(structs,indentString);
		return file;
	}
	
	private static void parseSingle(Structure struct, String indent) throws IOException{
		writer.write(indent + struct.getName() + "\n");
		for (Field f:struct.getFields()) {
			writer.write(indent + INDENT_STRING + f.toString() + "\n");
		}
		if(struct.getChildren().size()>0) {
			parseList(struct.getChildren(), indent + INDENT_STRING);
		}
	}
	
	private static void parseList(ArrayList<Structure> structs, String indent) throws IOException {
		for (Structure s:structs) {
			parseSingle(s,indent);
		}
	}
}
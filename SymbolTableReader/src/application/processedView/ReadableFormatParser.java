package application.processedView;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * Class used to parse processed structures into a readable format to a file.
 * 
 * @author Russell L. Binaco
 * @version 2018.03.23
 */

public class ReadableFormatParser {
	private final static String INDENT_STRING = "  ";
	private static BufferedWriter writer;
	
	/*
	 * Parses a list of structures to a file
	 * 
	 * @param	structs	 The list of structures to parse.
	 * @param	baseIndent	The starting indentation level.
	 * @param	filePath	The name of the filePath.
	 * 
	 */
	public static boolean parseStructure(ArrayList<Structure> structs, int baseIndent, String filePath) throws IOException{
		File file = new File(filePath);
		boolean notFound = false;
		if(!file.exists())
		{
			notFound = true;
			writer = new BufferedWriter(new FileWriter(file));
			String indentString = "";
			for (int i=0;i<baseIndent; i++) {
				indentString = indentString + INDENT_STRING;
			}
			parseList(structs,indentString);
			writer.close();
		}
		return notFound;
	}
	
	/*
	 * Parses selected list of structures to a file.
	 * 
	 * @param	selected	 The list of structures to parse.
	 * @param	filePath	The name of the filePath.
	 * 
	 */
	public static boolean parseSelected(ObservableList<TreeItem<Structure>> selected, String filePath) throws IOException{
		ArrayList<Structure> structs = new ArrayList<Structure>();
		for(TreeItem<Structure> item : selected) {
			structs.add(item.getValue());
		}
		return parseStructure(structs,0, filePath);
	}
	
	/*
	 * Helper method to parse a structure into a file.
	 *  
	 * @param	struct	 The structure to parse.
	 * @param	indent	The starting indentation level.
	 */
	private static void parseSingle(Structure struct, String indent) throws IOException{
		writer.write(indent + struct.getName() + "\r\n");
		for (Field f:struct.getFields()) {
			writer.write(indent + INDENT_STRING + f.toString() + "\r\n");
		}
		if(struct.getChildren().size()>0) {
			parseList(struct.getChildren(), indent + INDENT_STRING);
		}
	}
	
	/*
	 * Helper method to parse a lsit of structures to a file.
	 * 
	 * @param	structs	 The list of structures to parse.
	 * @param	indent	The starting indentation level.
	 * 
	 */
	private static void parseList(ArrayList<Structure> structs, String indent) throws IOException {
		for (Structure s:structs) {
			parseSingle(s,indent);
		}
	}
}

package application.processedView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class MATLABFormatParser {
	private final static String SPACE_PIPE = " |";
	private final static String INDENT_STRING = " |  ";
	private final static String DASHED_INDENT = " |--";
	private static BufferedWriter writer;
	
	/*
	 * Parses a list of structures to a file
	 * 
	 * @param	structs	 The list of structures to parse.
	 * @param	baseIndent	The starting indentation level, DOES NOTHING HERE
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
//			for (int i=0;i<baseIndent; i++) {
//				indentString = indentString + INDENT_STRING;
//			}
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
		File file = new File(filePath);
		boolean notFound = false;
		if(!file.exists())
		{
			notFound = true;
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for(TreeItem<Structure> item : selected)
			{
				TreeItem<Structure> parent = item;
				int indentCount = -1;
				do{
					indentCount++;
					parent = parent.getParent();
				}while(parent.getParent()!=null);
				String indentString = "";
				for(int i =0; i<indentCount; i++){
					indentString = indentString + "  ";
				}
//				writer.write(indentString + item.getValue().toString() + "\r\n");
//				for (TreeItem<String> child: item.getChildren()){
//					writer.write(indentString + INDENT_STRING + child.getValue().toString() + "\r\n");
//				}
				Structure struct = item.getValue();
				writer.write(indentString + struct.getName() + "\r\n");
				writer.write(indentString + SPACE_PIPE + "\r\n");
				if(struct instanceof Child) {
					writer.write(indentString+ DASHED_INDENT + "Name : '" + ((Child)struct).getFieldName() + "'\r\n");
					writer.write(indentString+ DASHED_INDENT + "Starting_Word : '" + ((Child)struct).getWord() + "'\r\n");
					writer.write(indentString+ DASHED_INDENT + "Starting_Byte : '" + ((Child)struct).getStartByte() + "'\r\n");
				}
				
				
				for (Field f:struct.getFields()) {
					writer.write(indentString + SPACE_PIPE);
					writer.write(indentString + INDENT_STRING + f.getName() + "\r\n");
					writer.write(indentString + INDENT_STRING + SPACE_PIPE);
					writer.write(indentString + INDENT_STRING + DASHED_INDENT + "Type : '" + f.getType() + "'\r\n");
					writer.write(indentString + INDENT_STRING + DASHED_INDENT + "Starting_Word : '" + f.getWord() + "'\r\n");
					writer.write(indentString + INDENT_STRING + DASHED_INDENT + "Starting_Byte : '" + f.getStartByte() + "'\r\n");
				}
				if(struct.getChildren().size()>0) {
					//parseList(struct.getChildren(), indentString + INDENT_STRING);
					for(Structure child:struct.getChildren()) {
						writer.write(indentString + SPACE_PIPE);
						writer.write(indentString + DASHED_INDENT+ child.getName() + "\r\n");
						writer.write(indentString + INDENT_STRING + SPACE_PIPE + "\r\n");
						if(child instanceof Child) {
							writer.write(indentString + INDENT_STRING+ DASHED_INDENT + "Name : '" + ((Child)child).getFieldName() + "'\r\n");
							writer.write(indentString + INDENT_STRING+ DASHED_INDENT + "Starting_Word : '" + ((Child)child).getWord() + "'\r\n");
							writer.write(indentString + INDENT_STRING+ DASHED_INDENT + "Starting_Byte : '" + ((Child)child).getStartByte() + "'\r\n");
						}
					}
				}
				writer.write(indentString + SPACE_PIPE + "\r\n");
				
				
				
				writer.write("\r\n");
			}
			writer.close();
		}
		return notFound;
	}
	
	/*
	 * Helper method to parse a structure into a file.
	 *  
	 * @param	struct	 The structure to parse.
	 * @param	indent	The starting indentation level.
	 */
	private static void parseSingle(Structure struct, String indent) throws IOException{
		//writer.write(indent + struct.getName() + "\r\n");
		writer.write(indent + struct.getName() + "\r\n");
		writer.write(indent + SPACE_PIPE + "\r\n");
		if(struct instanceof Child) {
			writer.write(indent+ DASHED_INDENT + "Name : '" + ((Child)struct).getFieldName() + "'\r\n");
			writer.write(indent+ DASHED_INDENT + "Starting_Word : '" + ((Child)struct).getWord() + "'\r\n");
			writer.write(indent+ DASHED_INDENT + "Starting_Byte : '" + ((Child)struct).getStartByte() + "'\r\n");
		}
		
		
		for (Field f:struct.getFields()) {
			writer.write(indent + SPACE_PIPE);
			writer.write(indent + INDENT_STRING + f.getName() + "\r\n");
			writer.write(indent + INDENT_STRING + SPACE_PIPE);
			writer.write(indent + INDENT_STRING + DASHED_INDENT + "Type : '" + f.getType() + "'\r\n");
			writer.write(indent + INDENT_STRING + DASHED_INDENT + "Starting_Word : '" + f.getWord() + "'\r\n");
			writer.write(indent + INDENT_STRING + DASHED_INDENT + "Starting_Byte : '" + f.getStartByte() + "'\r\n");
		}
		if(struct.getChildren().size()>0) {
			parseList(struct.getChildren(), indent + INDENT_STRING);
		}
		writer.write(indent + SPACE_PIPE + "\r\n");
	}
	
	/*
	 * Helper method to parse a list of structures to a file.
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

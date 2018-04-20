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
private final static String INDENT_STRING = " |  ";
private final static String DASHED_INDENT = " |--";
private static BufferedWriter writer;
/*
 * Parses a list of structures to a file
 * 
 * @param structs  The list of structures to parse.
 * @param baseIndent The starting indentation level, DOES NOTHING HERE
 * @param filePath The name of the filePath.
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
parseList(structs,indentString);
writer.close();
}
return notFound;
}
/*
 * Parses selected list of structures to a file.
 * 
 * @param selected  The list of structures to parse.
 * @param filePath The name of the filePath.
 * 
 */
public static boolean parseSelected(ObservableList<TreeItem<Structure>> selected, String filePath) throws IOException{
File file = new File(filePath);
boolean notFound = false;
if(!file.exists())
{
notFound = true;
BufferedWriter writer = new BufferedWriter(new FileWriter(file));
ArrayList<Structure> structs = new ArrayList<Structure>();
for(TreeItem<Structure> item : selected)
{
structs.add(item.getValue());
}
String indentString = "";
parseList(structs, indentString);
writer.close();
}
return notFound;
}
/*
 * Helper method to parse a structure into a file.
 *  
 * @param struct  The structure to parse.
 * @param indent The starting indentation level.
 */
private static void parseSingle(Structure struct, String indent) throws IOException{
//writer.write(indent + struct.getName() + "\r\n");
writer.write(indent + struct.getType() + "\r\n");
writer.write(indent + SPACE_PIPE + "\r\n");
writer.write(indent + INDENT_STRING + DASHED_INDENT + "Name : '" + struct.getName() + "'\r\n");

for (Field f:struct.getFields()) {
writer.write(indent + SPACE_PIPE + "\r\n");
writer.write(indent + INDENT_STRING + f.getType() + "\r\n");
writer.write(indent + INDENT_STRING + SPACE_PIPE + "\r\n");
writer.write(indent + INDENT_STRING + DASHED_INDENT + "Name : '" + f.getName() + "'\r\n");
writer.write(indent + INDENT_STRING + DASHED_INDENT + "Starting_Word : '" + f.getStart() + "'\r\n");
writer.write(indent + INDENT_STRING + DASHED_INDENT + "Byte_Size : '" + f.getByteSize() + "'\r\n");
writer.write(indent + INDENT_STRING + DASHED_INDENT + "Bit_Size : '" + f.getBitSize() + "'\r\n"); 
}
if(struct.getChildren().size()>0) {
parseList(struct.getChildren(), indent + INDENT_STRING);
}
writer.write(indent + SPACE_PIPE + "\r\n");
}
/*
 * Helper method to parse a list of structures to a file.
 * 
 * @param structs  The list of structures to parse.
 * @param indent The starting indentation level.
 * 
 */
private static void parseList(ArrayList<Structure> structs, String indent) throws IOException {
for (Structure s:structs) {
parseSingle(s,indent);
}
writer.write(indent+"\r\n");
}
}


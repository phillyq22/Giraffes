package application.processedView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import com.jmatio.io.MatFileWriter;
import com.jmatio.types.MLArray;
import com.jmatio.types.MLChar;
import com.jmatio.types.MLStructure;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class MATLABFormatParser {

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
			ArrayList<MLArray> list = new ArrayList<MLArray>();
			for(Structure s:structs) {
				list.add(makeStruct(s));
			}
			new MatFileWriter(file, list);
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
			ArrayList<MLArray> list = new ArrayList<MLArray>();
			for (TreeItem<Structure> item : selected) {
				list.add(makeStruct(item.getValue()));
			}
			new MatFileWriter(file, list);
		}
		return notFound;
	}
	
	public static MLArray makeStruct(Structure struct) {
		MLStructure mlStruct = new MLStructure(validateName(struct.getName()), new int[] {1,1});
		mlStruct.setField("Type", new MLChar(null,validateMAT(struct.getType())));
		for(Structure s: struct.getChildren()) {
			MLStructure temp = (MLStructure) makeStruct(s);
			mlStruct.setField(validateName(s.getName()), temp);
		}
		for (Field f: struct.getFields()) {
			MLStructure fieldStruct = (MLStructure) makeField(f);
			mlStruct.setField(validateName(f.getName()), fieldStruct);
		}
		return mlStruct;
	}
	
	public static MLArray makeField(Field field) {
		MLStructure mlField = new MLStructure(validateName(field.getName()),new int[] {1,1});
		mlField.setField("Type", new MLChar(null,validateMAT(field.getType())));
		mlField.setField("StartingWord", new MLChar(null,validateMAT(field.getStart())));
		mlField.setField("ByteSize", new MLChar(null,validateMAT(Integer.toString(field.getByteSize()))));
		mlField.setField("BitSize", new MLChar(null,validateMAT(Integer.toString(field.getBitSize()))));
		return mlField;
	}

    private static String validateMAT(String input) {
    	if(input == null ||input.isEmpty()) {
    		input = " ";
    	}
    	return input;
    }
    private static String validateName(String input) {
    	input = input.replaceAll(" ", "_");
    	input = input.replaceAll("\\*", "ptr");
    	input = input.replaceAll("[^A-Za-z0-9]", "");
    	if(input.isEmpty()) {
    		input = "empty_name";
    	}

    	return input;
    }
    
}

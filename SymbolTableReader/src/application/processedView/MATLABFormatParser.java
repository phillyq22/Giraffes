package application.processedView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.jmatio.io.*;
import com.jmatio.types.*;

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
		MLStructure mlStruct = new MLStructure(struct.getName(), new int[] {1,1});
		mlStruct.setField("Type", new MLChar(null,struct.getType()));
		for(Structure s: struct.getChildren()) {
			MLStructure temp = (MLStructure) makeStruct(s);
			mlStruct.setField(s.getName(), temp);
		}
		for (Field f: struct.getFields()) {
			MLStructure fieldStruct = (MLStructure) makeField(f);
			mlStruct.setField(f.getName(), fieldStruct);
		}
		return mlStruct;
	}
	
	public static MLArray makeField(Field field) {
		MLStructure mlField = new MLStructure(field.getName(),new int[] {1,1});
		mlField.setField("Type", new MLChar(null,field.getType()));
		mlField.setField("StartingWord", new MLChar(null,field.getStart()));
		mlField.setField("ByteSize", new MLChar(null,Integer.toString(field.getByteSize())));
		mlField.setField("BitSize", new MLChar(null,Integer.toString(field.getBitSize())));
		return mlField;
	}
	/*
	 * Helper method to parse a structure into a file.
	 *  
	 * @param	struct	 The structure to parse.
	 * @param	indent	The starting indentation level.
	 */
}

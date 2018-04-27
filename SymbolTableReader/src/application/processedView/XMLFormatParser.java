package application.processedView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

public class XMLFormatParser {	
	/*
	 * Parses a list of structures to a file
	 * 
	 * @param	structs	 The list of structures to parse.
	 * @param	filePath	The name of the filePath.
	 * 
	 */
	
	public static boolean parseStructure(ArrayList<Structure> structs, String filePath) throws IOException{
		File file = new File(filePath);
		boolean notFound = false;
		if(!file.exists())
		{
			notFound = true;
			generateAllXML(structs, file);
		}
		return notFound;
	}
	
	/*
	 * Parses selected list of structures to a file.
	 * 
	 * @param	selected	The list of structures to parse.
	 * @param	filePath	The name of the filePath.
	 * 
	 */
	public static boolean parseSelected(ObservableList<TreeItem<Structure>> selected, String filePath) throws IOException{
		File file = new File(filePath);
		boolean notFound = false;
		if(!file.exists())
		{
			notFound = true;
			generateSelectedXML(selected, file);
		}
		return notFound;
	}
	
	/*
	 * Helper method to generate an XML file	 
	 * @param	structs	 The list of structures to parse.
	 * @param	file	The file destination.
	 */
    private static void generateAllXML(ArrayList<Structure> structs, File f) {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            Element mainRootElement = doc.createElementNS("XML_Format", "All_Structures");
            doc.appendChild(mainRootElement);
           for(Structure s:structs) {
            	mainRootElement.appendChild(getStructure(doc,s));
            }
            // output DOM XML to file 
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
            DOMSource source = new DOMSource(doc);
            StreamResult console = new StreamResult(f);
            transformer.transform(source, console);
 
            System.out.println("\nXML DOM Created Successfully..");
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	/*
	 * Helper method to generate an XML file from selected structures 
	 * @param	selected The list of selected TreeItem<Structure> objects to parse.
	 * @param	file	 The file destination.
	 */
    private static void generateSelectedXML(ObservableList<TreeItem<Structure>> selected, File f) {
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            Element mainRootElement = doc.createElementNS("XML_Format", "Selected_Structures");
            doc.appendChild(mainRootElement);
            for(TreeItem<Structure> item : selected) {
            	Structure struct = item.getValue();
            	mainRootElement.appendChild(getStructure(doc,struct));
            }
            // output DOM XML to file 
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
            DOMSource source = new DOMSource(doc);
            StreamResult console = new StreamResult(f);
            transformer.transform(source, console);
         }catch (Exception e) {
        	e.printStackTrace();
        }
    }
	
    /*
     * Helper method to create a Structure node
     * @param doc The document that is the XML DOM
     * @param struct The structure to be made into a node
     */
    private static Node getStructure(Document doc,Structure struct) {
    	Element structElement = doc.createElement(validateXML(struct.getType()));
    	structElement.setAttribute("name", struct.getName());
    	for(Structure s:struct.getChildren()) {
    		structElement.appendChild(getStructure(doc,s));
    	}
    	for(Field f: struct.getFields()) {
    		structElement.appendChild(getFieldElement(doc,f));
    	}
    	return structElement;
    }
    
    /*
     * Helper method to create a field node
     * @param doc The document that is the XML DOM
     * @param struct The field to be made into a node
     */
    private static Node getFieldElement(Document doc, Field field) {
    	Element fieldElement = doc.createElement(validateXML(field.getName()));

    	fieldElement.setAttribute("type", field.getType());
    	fieldElement.setAttribute("Starting_Byte", field.getStart());
    	fieldElement.setAttribute("Byte_Size", Integer.toString(field.getByteSize()));
    	fieldElement.setAttribute("Bit_Size", Integer.toString(field.getBitSize()));
    	return fieldElement;
    }
	
    private static String validateXML(String input) {
    	input = input.replace(" ", "_");
    	input = input.replaceAll("\\*", "ptr");
    	input = input.replaceAll("\\p{Cntrl}|;", "");
    	input = input.replaceAll("\\[|\\]", "-");
    	
    	if(input.isEmpty()) {
    		input = "_";
    	}
    	return input;
    }
    
}

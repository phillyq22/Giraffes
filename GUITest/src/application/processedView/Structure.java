package application.processedView;

import java.util.*;

/**
 * Class that represents a data structure given other structures (children) and fields
 * @author Zac Capell
 * @version 3/1/18
 */
public class Structure 
{
	private String name;
	ArrayList<Structure> children = new ArrayList<Structure>();
	ArrayList<Field> fields = new ArrayList<Field>();
	
	public Structure(String name)
	{
		this.name = name;
	}
	
	/**
	 * Constructor for Structure
	 * @param name
	 * @param children
	 * @param fields
	 */
	public Structure(String name, ArrayList<Structure> children, ArrayList<Field> fields)
	{
		this.children = children;
		this.fields = fields;
		this.name = name;
	}
	
	public ArrayList<Structure> getChildren()
	{
		return children;
	}
	
	public ArrayList<Field> getFields()
	{
		return fields;
	}
	
	public String toString()
	{
		String s = name + ":\n";
		for(Field f : fields)
			s += f.toString() + "\n";
		for(Structure c : children)
			s += c.toString() + "\n";
		return s;
	}

	/**
	 * Adds a field to the the ArrayList of fields
	 * @param field
	 */
	public void addField(Field field) {
		 fields.add(field);
	}

	/**
	 * Adds a child structure to this structure 
	 * @param structure
	 */
	public void addStructure(Structure structure) {
		children.add(structure);
	}

}

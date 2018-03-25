package application.processedView;

import java.util.*;

/*
 * Class to represent a data structure. 
 * 
 * @author Zac Capell
 * @version 2018.03.23
 */
public class Structure {
	
	private String name;
	private ArrayList<Structure> children = new ArrayList<Structure>();
	private ArrayList<Field> fields = new ArrayList<Field>();
	
	/*
	 * @param name the name of the structure.
	 * @param children the Structures within this structure.
	 * @param fields the list of this Structure's fields.
	 */
	public Structure(String name, ArrayList<Structure> children, ArrayList<Field> fields)
	{
		this.name = name;
		this.children = children;
		this.fields = fields;
	}
	
	public Structure()
	{
		
	}
	
	public String getName()
	{
		return name;
	}
	
	public ArrayList<Structure> getChildren()
	{
		return children;
	}
	
	/** public ArrayList<Attribute> getAttributes()
	* {
	* 	return attributes;
	* }
	*/
	
	public ArrayList<Field> getFields()
	{
		return fields;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setChildren(ArrayList<Structure> children)
	{
		this.children = children;
	}
	
	public void setFields(ArrayList<Field> fields)
	{
		this.fields = fields;
	}
	
	public String toString()
	{
		String s = "STRUCTURE: " +name ;
		return s;
	}

}

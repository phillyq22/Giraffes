package swengStructure;

import java.util.*;

// Zac Capell, 2/26/18

public class Structure {
	
	String name;
	ArrayList<Structure> children = new ArrayList<Structure>();
	ArrayList<Field> fields = new ArrayList<Field>();
	
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
		String s = "STRUCTURE: " +name + "\n";
		for(Field f : fields)
			s += f.toString() + "\n";
		for(Structure c : children)
			s += c.toString() + "\n";
		return s;
	}

}

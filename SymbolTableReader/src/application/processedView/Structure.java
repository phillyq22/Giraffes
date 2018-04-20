package application.processedView;

import java.util.ArrayList;

/**
 * @author Zac Capell
 * @version 2/26/18
 * 
 * Contains the name of a structure,
 * then array lists for each of its variables
 * and children
 */

public class Structure {
	
	private String name, type;
	private ArrayList<Structure> children = new ArrayList<Structure>();
	private ArrayList<Field> fields = new ArrayList<Field>();
	
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
	
	public ArrayList<Field> getFields()
	{
		return fields;
	}
	
	public String getType()
	{
		return type;
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
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String toString()
	{
		return type + ": " + name;
	}
	
	public boolean equals(Object obj)
	{
		if(obj instanceof Structure)
		{
			Structure s = (Structure) obj;
			if(this.name.equals(s.getName()))
				return true;
		}
		return false;
	}

}


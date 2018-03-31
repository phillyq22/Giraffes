package swengStructure;

import java.util.*;

// Zac Capell, 2/26/18

public class Structure {
	
	String name;
	ArrayList<Child> children = new ArrayList<Child>();
	ArrayList<Field> fields = new ArrayList<Field>();
	
	public Structure(String name, ArrayList<Child> children, ArrayList<Field> fields)
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
	
	public ArrayList<Child> getChildren()
	{
		return children;
	}
	
	public ArrayList<Field> getFields()
	{
		return fields;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setChildren(ArrayList<Child> children)
	{
		this.children = children;
	}
	
	public void setFields(ArrayList<Field> fields)
	{
		this.fields = fields;
	}
	
	public String toString()
	{
		return "STRUCTURE: " + name;
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

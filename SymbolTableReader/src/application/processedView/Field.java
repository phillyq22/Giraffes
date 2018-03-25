package application.processedView;
/*
 * Class to represent the fields of a structure. 
 * 
 * @author Zac Capell
 * @version 2018.03.23
 */
public class Field {
	
	String type, name, starting, size;
	
	public Field()
	{
	}
	
	/*
	 * Constructor of Field.
	 * 
	 * @param	type		The datatype of the field.
	 * @param	name		The name of the field.
	 * @param	starting	The starting bit of the field.
	 * @param	size		The bit size of the field. 
	 */
	public Field(String type, String name, String starting, String size)
	{
		this.type = type;
		this.name = name;
		this.starting = starting;
		this.size = size;
	}
	
	public String getType()
	{
		return type;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getStarting()
	{
		return starting;
	}
	
	public String getSize()
	{
		return size;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setStarting(String starting)
	{
		this.starting = starting;
	}
	
	public void setSize(String size)
	{
		this.size = size;
	}
	
	public String toString()
	{
		return "FIELD: " + name + ", " + type + ", " + starting + ", " + size;
	}

}

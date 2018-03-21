package application.processedView;

// Zac Capell, 2/27

public class Field {
	
	String type, name, starting, size;
	
	public Field()
	{
	}
	
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

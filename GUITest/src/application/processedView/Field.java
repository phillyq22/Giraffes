package application.processedView;

/**
 * Class that represents the fields or attributes of the processed structures.
 * @author Zac Capell
 * @version 3/1/18
 */
public class Field extends Structure {
	
	String type, name, size;
	
	/**
	 * Constructor for Field
	 * @param type
	 * @param name
	 * @param size
	 */
	public Field(String type, String name, String size)
	{
		super(name);
		this.type = type;
		this.name = name;
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
	
	public String getSize()
	{
		return size;
	}
	
	public String toString()
	{
		return " FIELDS: " + name + ", " + type + ", " + size;
	}

}

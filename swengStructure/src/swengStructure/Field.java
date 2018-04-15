package swengStructure;

/**
 * @author Zac Capell 
 * @version 2/27/18
 *
 * Contains the variable type, name, starting word, and byte within that word of a single variable in a structure
 */

public class Field extends Structure{
	
	private int bitSize, byteSize;
	private String start;
	
	public Field()
	{
	}
	
	public Field(String type, String name, int bitSize, int byteSize, String start)
	{
		super.setType(type);
		super.setName(name);
		this.bitSize = bitSize;
		this.byteSize = byteSize;
		this.start = start;
		
	}
	
	public String getType()
	{
		return super.getType();
	}
	
	
	public void setType(String type)
	{
		super.setType(type);
	}
	
	
	public String toString()
	{
		return "FIELD: " + getType() + ", " + super.getName() + " STARTING WORD: " + start + " BIT SIZE: " + bitSize + " BYTE SIZE: " + byteSize;
	}

}

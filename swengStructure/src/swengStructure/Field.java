package swengStructure;

 

/**
 * @author Zac Capell 
 * @version 2/27/18
 *
 * Contains the variable type, name, starting word, and byte within that word of a single variable in a structure
 */

public class Field extends Structure{
	
	private String type;
	private int word, startByte;
	
	public Field()
	{
	}
	
	public String getType()
	{
		return type;
	}
	
	
	public int getWord()
	{
		return word;
	}
	
	public int getStartByte()
	{
		return startByte;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public void setWord(int word)
	{
		this.word = word;
	}
	
	public void setStartByte(int startByte)
	{
		this.startByte = startByte;
	}
	
	public String toString()
	{
		return "FIELD: " + type + ", " + super.getName() + " STARTING WORD: " + word + " STARTING BYTE: " + startByte;
	}

}

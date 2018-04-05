package application.processedView;

/**
 * @author Zac Capell 
 * @version 3/29/18
 *
 * A version of a structure that is the same as a structure, but also contains its name as a variable and its starting word and byte within that word
 */

public class Child extends Structure{
	
	private String fieldName;
	private int word, startByte;
	
	public Child()
	{
		
	}
		
	public void setWord(int word)
	{
		this.word = word;
	}
	
	public void setStartByte(int startByte)
	{
		this.startByte = startByte;
	}
	
	public void setFieldName(String fieldName)
	{
		this.fieldName = fieldName;
	}
	
	public String getFieldName()
	{
		return fieldName;
	}
	
	public int getWord()
	{
		return word;
	}
	
	public int getStartByte()
	{
		return startByte;
	}
	
	public String toString()
	{
		return super.toString() + ", " + fieldName + " STARTING WORD: " + word + ", STARTING BYTE: " + startByte;
	}

}
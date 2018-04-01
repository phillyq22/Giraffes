package swengStructure;

//Zac Capell 3/29

public class Child extends Structure{
	
	String fieldName;
	int word, startByte;
	
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

package swengStructure;

// Zac Capell, 2/27

public class Field {
	
	String type, name;
	int word, startByte;
	
	public Field()
	{
	}
	
	public Field(String type, String name, int word, int startByte)
	{
		this.type = type;
		this.name = name;
		this.word = word;
		this.startByte = startByte;
	}
	
	public String getType()
	{
		return type;
	}
	
	public String getName()
	{
		return name;
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
	
	public void setName(String name)
	{
		this.name = name;
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
		return "FIELD: " + type + ", " + name + " STARTING WORD: " + word + " STARTING BYTE: " + startByte;
	}

}

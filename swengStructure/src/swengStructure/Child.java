package swengStructure;

//Zac Capell 3/29

public class Child extends Structure{
	
	String name;
	int word, startByte;
	
	public Child()
	{
		
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
		return super.toString() + " STARTING WORD: " + word + ", STARTING BYTE: " + startByte;
	}

}

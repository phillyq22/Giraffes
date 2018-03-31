package swengStructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import javax.xml.crypto.dsig.TransformException;

// Zac Capell, 3/8

public class Parsing {
	
	
	public Parsing()
	{
	}
	/**
	 * @param file - designed to take in a pahole text file...
	 * @return - ... and turn it into a list of structures with proper fields and children that can then be displayed
	 * @throws FileNotFoundException - the operation should be safe, but just in case, also throws an exception if the file path doesn't lead anywhere
	 * @throws TransformException 
	 */
	public static ArrayList<Structure> parse(File file) throws FileNotFoundException, TransformException
	{
		try {
		ArrayList<Structure> structures = new ArrayList<Structure>();
		
		Scanner scanner = new Scanner(file);
		
		while(scanner.hasNextLine())
		{
			String[] temp = scanner.nextLine().split("\\s+");
			// When it finds a new structure...
			if(temp[0].equals("struct"))
			{
				Structure s = new Structure();
				s.setName(temp[1]); // ... it makes a new Structure... 
				ArrayList<Field> fields = new ArrayList<Field>();
				ArrayList<Child> children = new ArrayList<Child>();
				while(!scanner.hasNext("};")) // ... and goes until the ending bracket of that structure
				{
					String line = scanner.nextLine(); // Only reading first word of line?? works fine in structure 
					if(!line.isEmpty())
					{
						String[] t2 = line.split("\\s+");
						// Creates a new structure and adds it to the list
						// self: have it search for existing children first and add those?
						
						String[] methodTemp = t2[2].split("\\("); // checks to find methods and avoid adding them for now
						if(methodTemp.length < 2 || !methodTemp[1].equals("class"))
						{
							if(t2[1].equals("class")) // if the field is a class, that means it's a child, and adds it do the children for later
							{
								Child child = new Child();
								child.setName(t2[2]);
								child.setName(t2[4]);
								child.setWord(Integer.parseInt(t2[6]) / 8);
								child.setStartByte(Integer.parseInt(t2[7]) % 8);
							}	
							else if(!t2[1].equals("/*")) // denotes a pahole information line
							{
								Field field = new Field();		
								field.setType(t2[1]);
								if(!(t2[2].equals("*") || t2[2].equals("int") || t2[2].equals("char") || t2[1].equals("signed") || t2[2].equals("unsigned"))) // checks to see what type the field is so the type and bits can be set correctly based on how many words are in the array
								{
									field.setName(t2[2]);
									field.setWord(Integer.parseInt(t2[4]) / 8 );
									field.setStartByte(Integer.parseInt(t2[5]) % 8);
								}
							
								else if(t2[1].equals("signed")) // does the same as if above, but different spacing for signed fields
								{
									field.setType(t2[2]);
									field.setName(t2[3]);
									field.setWord(Integer.parseInt(t2[5]) / 8 );
									field.setStartByte(Integer.parseInt(t2[6]) % 8);
								
								}
							
								else if(t2[2].equals("unsigned") || t2[1].equals("const")) // does the same as if above, but different spacing for unsigned fields
								{
									field.setName(t2[4]);
									field.setWord(Integer.parseInt(t2[6]) / 8 );
									field.setStartByte(Integer.parseInt(t2[7]) % 8);
								}
								
								else // if it requires no special spacing, do this instead
								{
									field.setName(t2[3]);
									field.setWord(Integer.parseInt(t2[5]) / 8 );
									field.setStartByte(Integer.parseInt(t2[6]) % 8);
								}
								fields.add(field);
							}
						}
					}
					
				}
				
			s.setChildren(children); // add the list of children generated at the end of the structure
			s.setFields(fields); // add the list of fields generated at the end of the structure
			structures.add(s); // add the structure to the list of structures to be returned
			}
			
			
		}
		
		assignChildren(structures);
		scanner.close();
		return structures;
		}
		
		catch(IndexOutOfBoundsException oob)
		{
			throw new TransformException("Parsing hit a line that did not fit in expected format");
		}
		
	}

	private static void assignChildren(ArrayList<Structure> structures)
	{
		for(Structure struct : structures)
		{
			ArrayList<Child> children = struct.getChildren();
			for(Child child : children)
			{
				Iterator<Structure> it = structures.iterator();
				while(it.hasNext())
				{
					Structure s = it.next();
					if(s.equals(child))
					{
						child.setChildren(s.getChildren());
						child.setFields(s.getFields());
					}
				}
			}
		}
	}
	
}

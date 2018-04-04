package application.processedView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import javax.xml.crypto.dsig.TransformException;

/**
 * @author Zac Capell
 * @version 3/8/18
 * 
 * Takes in pahole files from c++ currently
 * Creates a list of structures that can then be manipulated
 */

public class Parsing {
	
	
	public Parsing()
	{
	}
	/**
	 * @param file - designed to take in a pahole text file...
	 * @return - ... and turn it into a list of structures with proper fields and children that can then be displayed
	 * @throws FileNotFoundException - the operation should be safe, but just in case, also throws an exception if the file path doesn't lead anywhere
	 * @throws TransformException - throws an exception if the processing hits an error, such as if something in the wrong format is fed in
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
				ArrayList<Structure> children = new ArrayList<Structure>();
				while(!scanner.hasNext("};")) // ... and goes until the ending bracket of that structure
				{
					String line = scanner.nextLine(); // Only reading first word of line?? works fine in structure 
					if(!line.isEmpty())
					{
						String[] t2 = line.split("\\s+");
						// Creates a new structure and adds it to the list						
						String[] methodTemp = t2[2].split("\\("); // checks to find methods and avoid adding them for now
						if(methodTemp.length < 2 || !methodTemp[1].equals("class"))
						{
							if(t2[1].equals("class")) // if the field is a class, that means it's a child, and adds it do the children for later
							{
								Field field = new Field();
								field.setType(t2[1] + " " + t2[2] + " " + t2[3]);
								field.setName(t2[4]);
								field.setWord(Integer.parseInt(t2[6]) / 8);
								field.setStartByte(Integer.parseInt(t2[7]) % 8);
								fields.add(field);
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
							
								else if(t2[2].equals("unsigned") || t2[1].equals("const")) // does the same as if above, but different spacing for unsigned fields or const characters
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
		
		//assignChildren(structures); // finds the top level children and assigns the proper fields and children to them
		scanner.close();
		return structures;
		}
		
		catch(Exception e)
		{
			throw new TransformException("Parsing hit a line that did not fit in expected format");
		}
		
	}

	private static void assignChildren(ArrayList<Structure> structures)
	{
		for(Structure struct : structures)
		{
			ArrayList<Structure> children = struct.getChildren();
			for(Structure child : children) // for each child in the top level...
			{
				Iterator<Structure> it = structures.iterator();
				while(it.hasNext()) // ... look at each structure in the list...
				{
					Structure s = it.next();  
					if(s.equals(child)) // ... and if the structure and child have the same name (not field name)...
					{
						child.setChildren(s.getChildren()); // ... the child has that structure's children...
						child.setFields(s.getFields()); // ... and that structure's fields
					}
				}
			}
		}
	}
	
}
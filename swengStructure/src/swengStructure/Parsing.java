package swengStructure;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

// Zac Capell, 3/8

public class Parsing {
	
	
	public Parsing()
	{
	}
	
	public static ArrayList<Structure> parse(File file) throws FileNotFoundException
	{
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
				if(getClass(s.getName(), structures) != null) // ... finds if there's a matching child...
				{
					s = getClass(s.getName(), structures); // ... makes that the Structure it's working with...
				}
				ArrayList<Field> fields = new ArrayList<Field>();
				ArrayList<Structure> children = new ArrayList<Structure>();
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
							if(t2[1].equals("class"))
							{
								Structure child = new Structure();
								child.setName(t2[2]);
								children.add(child);
							}	
							else if(!t2[1].equals("/*"))
							{
								Field field = new Field();		
								field.setType(t2[1]);
								if(!(t2[2].equals("*") || t2[2].equals("int") || t2[2].equals("char") || t2[1].equals("signed") || t2[2].equals("unsigned")))
								{
									field.setName(t2[2]);
									field.setStarting(t2[4]);
									field.setSize(t2[5]);
								}
							
								else if(t2[1].equals("signed"))
								{
									field.setType(t2[2]);
									field.setName(t2[3]);
									field.setStarting(t2[5]);
									field.setSize(t2[6]);
								
								}
							
								else if(t2[2].equals("unsigned"))
								{
									field.setName(t2[4]);
									field.setStarting(t2[6]);
									field.setSize(t2[7]);
								}
								
								else
								{
									field.setName(t2[3]);
									field.setStarting(t2[5]);
									field.setSize(t2[6]);
								}
								fields.add(field);
							}
						}
					}
					
				}
				
			s.setChildren(children);
			s.setFields(fields);
			structures.add(s);
			}
			
			
		}
		
		scanner.close();
		return structures;
	}
	
	private static Structure getClass(String name, ArrayList<Structure> structures)
	{
		Iterator<Structure> it = structures.iterator();
		while(it.hasNext())
		{
			ArrayList<Structure> children = it.next().getChildren();
			for(Structure child : children)
				if(child.getName().equalsIgnoreCase(name))
					return child;
		}
		return null;
		
	}

}

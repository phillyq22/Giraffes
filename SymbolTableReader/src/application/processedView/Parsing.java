package application.processedView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.xml.crypto.dsig.TransformException;

/**
 * @author Zac Capell
 * @version 4/18/18
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
	public static ArrayList<Structure> parse(File file) throws TransformException
	{
		
		try {
				ArrayList<Structure> result = new ArrayList<Structure>();
				
				Scanner scanner = new Scanner(file);
				
				while(scanner.hasNextLine())
				{
					String[] temp = scanner.nextLine().split("\\s+");
					if(temp[temp.length - 1].equals("{"))
					{
						Structure struct = new Structure();
						struct.setName(temp[1]);
						struct.setType(temp[0]);
						struct = process(struct, scanner);
						result.add(struct);
					}
					
					
				}
			
				scanner.close();
				return result;
			}
		catch(Exception e)
		{
			throw new TransformException("Parsing hit a line that did not fit in expected format");
		}
	}
	
	private static Structure process(Structure struct, Scanner scanner)
	{
		ArrayList<Field> fields = new ArrayList<Field>();
		ArrayList<Structure> children = new ArrayList<Structure>();
		while(scanner.hasNextLine())
		{
			String line = scanner.nextLine();
			if(line.contains("}"))
				break;
			else if(!(line.isEmpty() || line.trim().equals("public:") || line.trim().equals("protected:") || line.trim().equals("private:")))
			{
				String[] temp = line.split("\\s+");
				// When it finds a new structure...
				if(!(temp[1].equals("/*") || temp[temp.length - 1].contains(";")))
				{
					if(temp[temp.length-1].equals("{"))
					{
						Structure result = new Structure();
						result.setName(temp[2]);
						result.setType(temp[1]);
						process(result, scanner);
						children.add(result);
					}
					else if(temp[2].equals("=") && !temp[1].contains("[")) 
					{
						Field field = new Field();
						field.setType("int");
						field.setName(temp[1]);
						fields.add(field);
					}
					else
					{							
						for(int i = 0; i < temp.length-1; i++)
						{
							if(temp[i].contains(";"))
							{
								boolean isUnion = false;
								if(struct.getType().equals("union"))
								{
									isUnion = true;
								}
								fields.add(setField(i, temp, isUnion));
								break;								}
							}
						}
					}
				}
			}
		
		struct.setChildren(children);
		struct.setFields(fields);
		return struct;
	}
	
	private static Field setField(int i, String[] line, boolean isUnion)
	{
		String type = "", name = "", start = "";
		int bitSize, byteSize;
		for(int j = 0; j < i; j++)
			type += line[j] + " ";
		type = type.trim();
		if(line[i].contains(":"))
		{
			String[] temp = line[i].split(":");
			name = temp[0];
			bitSize = Integer.parseInt(temp[1].substring(0, temp[1].length()-1));
			byteSize = bitSize / 8;
			temp = line[i + 2].split(":");
			start = temp[0] +", STARTING BIT: " + temp[1];
		}
		
		else
		{
			int n = 3;
			if(isUnion)
				n = 2;
			
			name = line[i].substring(0, line[i].length()-1);
			byteSize = Integer.parseInt(line[i + n]);
			bitSize = byteSize * 8;
			if(isUnion)
				start = "0";
			else
				start = line[i + 2];
		}
		
		return new Field(type, name, bitSize, byteSize, start);
		
	}

}
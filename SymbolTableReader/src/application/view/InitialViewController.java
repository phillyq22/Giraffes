package application.view;
/*
 * object:
 * produce archive (hardcode name)
 * produce exe from that name (hardcode new name)
 * produce pahole txt file
 *
 *
 * archive:
 * produce exe (hardcode name)
 * produce pahole txt file
 *
 *
 * exe:
 * produce pahole txt file
 *
 *
 * afterwards:
 * load pahole txt file into lf
 * run Parsing code on lf
 * load new view
 */
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
 
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.xml.crypto.dsig.TransformException;
 
import application.Main;
import application.processedView.Parsing;
import application.processedView.ProcessViewController;
import application.processedView.Structure;
 
/*
 * Controller for the initial view of the Symbol Table Reader.
 * Handles any actions done within the InitialView.
 *
 * @author Philip S. Quinn
 * @version 3/1/2018
 */
public class InitialViewController {
    /*Fields*/
    @FXML private TextField filename;
    @FXML private TableView<LoadedFile> loadedFilesView;
    @FXML private TableColumn<LoadedFile, CheckBox> loadedFiles;
    @FXML private Button removeFileButton;
    @FXML private Button processButton;
    @FXML private RadioButton dwarf2;
    @FXML private RadioButton dwarf3;
    @FXML private RadioButton dwarf4;
    @FXML private RadioButton dwarf5;
    @FXML private Label fileError;
    @FXML private Label processError;
    private ArrayList<Structure> structs;
    /*
     * Searches in the current working directory(cwd) for the file name entered by the user.
     * If the File is found in cwd, checks if file has already been imported. If not, add
     *  
     * @param e   Event of clicking the import button
     */
    public void searchFile(ActionEvent e)
    {
        Path currentRelativePath = Paths.get("");//getting the cwd path as an object
        String s = currentRelativePath.toAbsolutePath().toString();//cwd as a string
        String fileName = filename.getText();//getting the file name entered into the filename textfield
        String filePath = (s + File.separator + fileName);//creating the full file path
        File file = new File(filePath);
        if(file.exists() && !fileName.equals("") && file.isFile())
        {
            ObservableList<LoadedFile> list = loadedFilesView.getItems();
            if(!fileExist(file) && (Pattern.matches(".*[.o]", fileName) || Pattern.matches(".*[.a]", fileName) || !Pattern.matches(".*[.]", fileName)))
            {
                fileError.setText("");
                CheckBox checkBox = new CheckBox(fileName);
                LoadedFile lf =  new LoadedFile(file, checkBox);
                //Makes sure that the checkboxes are displayed by checking for the field called checkBox in LoadedFile object
                loadedFiles.setCellValueFactory(new PropertyValueFactory<LoadedFile, CheckBox>("checkBox"));
                list.add(lf);
                loadedFilesView.setItems(list);
            }
            else
            {
                fileError.setText("File already loaded in, or is of the wrong extension.");
            }
        }      
        else
        {
            fileError.setText("Error loading file.");
        }
    }
   
    /*
     * Removes all of the files that are selected.
     *
     * @param e Clicking of the remove files button
     */
    public void removeFile(ActionEvent e)
    {
        ObservableList<LoadedFile> list = loadedFilesView.getItems();
        int listSize = list.size();
        for(int i = 0; i < listSize; i++)
        {
            LoadedFile curr = list.get(i);
            if(curr.getCheckBox().isSelected())
            {
                list.remove(i);
                i--;
                listSize--;
            }
        }
        loadedFilesView.setItems(list);
    }
   
    /*
     * Processes selected files
     *
     * @param   e   The pressing of the process button
     */
    public void process(ActionEvent e) throws IOException, InterruptedException, FileNotFoundException, TransformException
    {
        boolean error = true;
        ObservableList<LoadedFile> list = loadedFilesView.getItems();
 
        Path currentRelativePath = Paths.get("");//getting the cwd path as an object
        String s = currentRelativePath.toAbsolutePath().toString();//cwd as a string
        String fileName = ("paholeOutput.txt");//getting the file name
        String filePath = (s + File.separator);
        String fullFilePath = (s + File.separator + fileName);//creating the full file path
        File file = new File(fullFilePath);
       
        String runTimeString = new String();
	String tester = new String("tester tester.a -lpthread");
        String exeLinked = new String("cd " + filePath + " ; /usr/bin/g++ -o " + tester);
        String pahole = new String("cd " + filePath + " ; pahole -A tester.a > " + fullFilePath);
 
        if(!list.isEmpty())
        {
            LoadedFile lf = list.get(0);
           
                if(lf.getCheckBox().isSelected())
                {
           
 
            if (Pattern.matches(".*[.o]", lf.getFile().getName()))
            {
               
                for (int i = 0; i < list.size(); i++)
                {
                    lf = list.get(i);
                    if (Pattern.matches(".*[.o]", lf.getFile().getName()))
                    {                      
                runTimeString += lf.getFile().getName() + " ";
		/**String dwarfName = ("dwarfDump" + i +".txt");//getting the file name
        	String dwarfPath = (s + File.separator);
        	String dwarfFullFilePath = (s + File.separator + dwarfName);//creating the full file path
        	File dwarf = new File(dwarfFullFilePath);
		String dwarfDump = new String("cd " + filePath + " ; dwarfdump -a " + runTimeString + "| grep 'pthread' > " + dwarfName);
		String[] arcCmd = new String[] {"/bin/bash", "-c", dwarfDump};
                    Process process = Runtime.getRuntime().exec(arcCmd);
                    process.destroy();
		if (!dwarf.getName().isEmpty())
			{
			tester = new String("tester tester.a -lpthread");
			exeLinked = new String("cd " + filePath + " ; /usr/bin/g++ -o " + tester);
			}*/
                    }
                   
                }
               
                try {
                    String archive = new String("cd " + filePath + " ; ar -cr tester.a " + runTimeString);
                    String[] arcCmd = new String[] {"/bin/bash", "-c", archive};
                    Process process = Runtime.getRuntime().exec(arcCmd);
                    process.destroy();
                    String[] exeCmd = new String[] {"/bin/bash", "-c", exeLinked};
                    Process processor = Runtime.getRuntime().exec(exeCmd);
                    processor.destroy();
                    String[] paholeCmd = new String[] {"/bin/bash","-c", pahole};
                    Process ppp = Runtime.getRuntime().exec(paholeCmd);
                    ppp.destroy();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                   
            }
            else if (Pattern.matches(".*[.a]", lf.getFile().getName()))
            {
                //call runtime for filename with flags to process .a into .exe
                //then process executable
                try {
                    String[] exeCmd = new String[] {"/bin/bash", "-c", exeLinked};
                    Process p = Runtime.getRuntime().exec(exeCmd);
                    p.destroy();
                    String[] paholeCmd = new String[] {"/bin/bash","-c", pahole};
                    Process pp = Runtime.getRuntime().exec(paholeCmd);
                    pp.destroy();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
               
            }
            else if (!Pattern.matches(".*[.]", lf.getFile().getName()))
            {
                //already .exe, process executable
                try {
                    String[] paholeCmd = new String[] {"/bin/bash","-c", pahole};
                    Process pp = Runtime.getRuntime().exec(paholeCmd);
                    pp.destroy();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
       
           
            error = false;
            processError.setText("");
   
            //lf.setFile(file);
           
            structs = Parsing.parse(file);
            ProcessViewController.setStructs(structs);
 
            Main.buildProcessStage();
            Main.showProcessView();
            }
        }
           
        if(error)
        {
            processError.setText("Select a File to process!");
        }
       
       
    }
       
    /*
     * Allows file selection from your operating system's file browser.
     *
     * @param   e   The clicking of the selectFiles button
     */
    public void importMultiFiles(ActionEvent e)
    {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(null);
        if(file != null)
        {
            String filepath = file.getAbsolutePath();
            if(!fileExist(file) && (Pattern.matches(".*[.o]", filepath) || Pattern.matches(".*[.a]", filepath) || !Pattern.matches(".*[.]", filepath)))
            {
                fileError.setText("");
                ObservableList<LoadedFile> list = loadedFilesView.getItems();
                CheckBox checkBox = new CheckBox(file.getName());
                LoadedFile lf =  new LoadedFile(file, checkBox);
                //Makes sure that the checkboxes are displayed by checking for the field called checkBox in LoadedFile object
                loadedFiles.setCellValueFactory(new PropertyValueFactory<LoadedFile, CheckBox>("checkBox"));
                list.add(lf);
                loadedFilesView.setItems(list);
            }
            else
            {
                fileError.setText("File either already loaded in, or is of the wrong extension.");
            }
        }
    }
   
    /*
     * Checks whether or not the selected file already has been selected.
     *
     * @param   file    The file to be checked
     */
    public boolean fileExist(File file)
    {
        boolean found = false;
        ObservableList<LoadedFile> list = null;
        if(loadedFilesView != null)
        {
            list = loadedFilesView.getItems();
        }
        if(list != null)
        {
            int size = list.size();
            for(int i = 0; i < size; i++)
            {
                if(list.get(i).getFile().getAbsolutePath().equals(file.getAbsolutePath()))
                {
                    //found = true;//file was already imported
		return true;
                }
            }
        }
        return found;
    }
   
   
    public File getFile()
    {
        return loadedFilesView.getItems().get(0).getFile();
    }
}

package application.view;

import java.io.File;

import javafx.scene.control.CheckBox;

/*
 * Class that stores instances of loaded files. Knows the loaded files full file file and the checkbox associated with it.
 * 
 * @author Philip S. Quinn
 * @version 3/1/2018
 */
public class LoadedFile {
	private File file;//full file file
	private CheckBox checkBox;
	
	public LoadedFile(File file, CheckBox checkBox)
	{
		this.file = file;
		this.checkBox = checkBox;
	}
	
	public LoadedFile(File file)
	{
		this.file = file;
		this.checkBox = null;
	}
	
	public LoadedFile()
	{
		this.file = null;
		this.checkBox = null;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public CheckBox getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}
}

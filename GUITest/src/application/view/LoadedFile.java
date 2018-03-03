package application.view;

import javafx.scene.control.CheckBox;

/*
 * Class that stores instances of loaded files. Knows the loaded files full file path and the checkbox associated with it.
 * 
 * @author Philip S. Quinn
 * @version 3/1/2018
 */
public class LoadedFile {
	private String path;//full file path
	private CheckBox checkBox;
	
	public LoadedFile(String path, CheckBox checkBox)
	{
		this.path = path;
		this.checkBox = checkBox;
	}
	
	public LoadedFile(String path)
	{
		this.path = path;
		this.checkBox = null;
	}
	
	public LoadedFile()
	{
		this.path = null;
		this.checkBox = null;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public CheckBox getCheckBox() {
		return checkBox;
	}

	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}
}

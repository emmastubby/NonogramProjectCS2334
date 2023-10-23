package edu.ou.cs2334.project5.handlers;

import javafx.stage.Window;
import javafx.stage.FileChooser;
/**
 * Represents a general handler involving file selection.
 * 
 * @author emmas
 * @version 0.1
 */
public abstract class AbstractBaseHandler {
	
	protected Window window;
	protected FileChooser fileChooser;
	
	protected AbstractBaseHandler(Window window, FileChooser fileChooser) {
		this.window = window;
		this.fileChooser = fileChooser;
	}

}

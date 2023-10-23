package edu.ou.cs2334.project5.handlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.stage.Window;
import edu.ou.cs2334.project5.interfaces.Openable;
/**
 * Extends AbstractBaseHandler and implements EventHandler interface to implement
 * the handle method for the open button.
 * 
 * @author emmas
 * @version 0.1
 */
public class OpenHandler extends AbstractBaseHandler implements EventHandler<ActionEvent> {
	
	private Openable opener;
	
	/**
	 * Constructs an OpenHandler using a window, fileChooser, and opener.
	 * 
	 * @param window	window of the program
	 * @param fileChooser	allows user to choose a file to open
	 * @param opener	helps to implement opening process
	 */
	public OpenHandler(Window window, FileChooser fileChooser, Openable opener) {
		super(window, fileChooser);
		this.opener = opener;
	}

	/**
	 * Handles the opening of the file if it is not null.
	 */
	public void handle(ActionEvent event) {
		try {
			File file = fileChooser.showOpenDialog(window);
			
			if (file != null) {
				opener.open(file);
			}
		}
		catch (Exception e) {
			e.getMessage();
		}
	}

}

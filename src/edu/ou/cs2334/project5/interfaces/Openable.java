package edu.ou.cs2334.project5.interfaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 * Interface that allows the OpenHandler to handle opening files.
 * 
 * @author emmas
 * @version 0.1
 */
public interface Openable {
	
	/**
	 * Allows OpenHandler to handle opening files.
	 * 
	 * @param file	file to be opened
	 * @throws FileNotFoundException	if the file is not found
	 */
	void open(File file) throws FileNotFoundException, IOException;

}
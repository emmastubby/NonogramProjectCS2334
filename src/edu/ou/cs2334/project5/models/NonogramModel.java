package edu.ou.cs2334.project5.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Encapsulates the state and rules of the game.
 * 
 * @author emmas
 * @version 0.1
 */
public class NonogramModel {

	private static final String DELIMITER = " ";
	private static final int IDX_NUM_ROWS = 0;
	private static final int IDX_NUM_COLS = 1;

	private int[][] rowClues;
	private int[][] colClues;
	private CellState[][] cellStates;
	
	/**
	 * Constructs a NonogramModel using row clues and column clues.
	 * 
	 * @param rowClues	2D array representing row projections of the solved puzzle
	 * @param colClues	2D array representing col projections of the solved puzzle
	 */
	public NonogramModel(int[][] rowClues, int[][] colClues) {
		this.rowClues = deepCopy(rowClues);
		this.colClues = deepCopy(colClues);

		cellStates = initCellStates(getNumRows(), getNumCols());
	}
	
	/**
	 * Constructs a NonogramModel using a file containing row and column clues.
	 * 
	 * @param file`File containing puzzle information
	 * @throws IOException	if I/O exception occurs
	 */
	public NonogramModel(File file) throws IOException {
		// Number of rows and columns
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String header = reader.readLine();
		String[] fields = header.split(DELIMITER);
		int numRows = Integer.parseInt(fields[IDX_NUM_ROWS]);
		int numCols = Integer.parseInt(fields[IDX_NUM_COLS]);

		// Initialize cellStates.
		// This should be a simple one-liner. Please do ask about this on Discord.
		this.cellStates = initCellStates(numRows, numCols);
		
		// Read in row clues.
		// This should be a simple one-liner. Please do ask about this on Discord.
		this.rowClues = readClueLines(reader, numRows);
		
		// Read in column clues.
		// This should be a simple one-liner. Please do ask about this on Discord.
		this.colClues = readClueLines(reader, numCols);
		
		// Close reader
		reader.close();
	}

	/**
	 * Constructs a NonogramModel using a file name.
	 * 
	 * @param filename	name of the File containing the puzzle information
	 * @throws IOException	if I/O exception
	 */
	public NonogramModel(String filename) throws IOException {
		// This should be a simple one-liner. Please do ask about this on Discord.
		this(new File(filename));
	}
	
	/**
	 * Returns the number of rows in the nonogram.
	 * 
	 * @return	number of rows
	 */
	public int getNumRows() {
		return rowClues.length;
	}
	
	/**
	 * Returns the number of columns in the nonogram.
	 * 
	 * @return	number of columns
	 */
	public int getNumCols() {
		return colClues.length;
	}
	
	/**
	 * Returns the state of the cell with the given row and column indices.
	 * 
	 * @param rowIdx	row index
	 * @param colIdx	column index
	 * @return	CellState of the cell
	 */
	public CellState getCellState(int rowIdx, int colIdx) {
		return this.cellStates[rowIdx][colIdx];
	}
	
	/**
	 * Returns the boolean state of the cell with the given row and column indices.
	 * 
	 * @param rowIdx	row index
	 * @param colIdx	column index
	 * @return	boolean cell state
	 */
	public boolean getCellStateAsBoolean(int rowIdx, int colIdx) {
		CellState state = getCellState(rowIdx, colIdx);
		return CellState.toBoolean(state);
	}
	
	/**
	 * Sets the state of the cell with the given indices with the given state.
	 * 
	 * @param rowIdx	row index
	 * @param colIdx	column index
	 * @param state		desired cell state
	 * @return			true if the cell state changed, false otherwise
	 */
	public boolean setCellState(int rowIdx, int colIdx, CellState state) {
		// check if the puzzle is already solved or if the enum is null
		if (this.isSolved() || state == null || 
				state == getCellState(rowIdx, colIdx)) {
			return false;
		}
		
		// set the cell to the given state
		this.cellStates[rowIdx][colIdx] = state;
		
		return true;
	}
	
	/**
	 * Returns a deep copy of the row clues.
	 * 
	 * @return	deep copy of the row clues
	 */
	public int[][] getRowClues() {
		return deepCopy(this.rowClues);
	}
	
	/**
	 * Returns a deep copy of column clues.
	 * 
	 * @return	deep copy of column clues
	 */
	public int[][] getColClues() {
		return deepCopy(this.colClues);
	}
	
	/**
	 * Returns a deep copy of the row clue with the given index.
	 * 
	 * @param rowIdx	row index
	 * @return	copy of the row clue
	 */
	public int[] getRowClue(int rowIdx) {
		return deepCopy(this.rowClues)[rowIdx];
	}
	
	/**
	 * Returns a deep copy of the column clue with the given index.
	 * 
	 * @param colIdx	column index
	 * @return	copy of the column clue
	 */
	public int[] getColClue(int colIdx) {
		return deepCopy(this.colClues)[colIdx];
	}
	
	/**
	 * Indicates whether the row with the given index has been solved.
	 * 
	 * @param rowIdx	row index
	 * @return	true if the projected row matches the row's clue, false otherwise
	 */
	public boolean isRowSolved(int rowIdx) {
		// get the row from the puzzle and its corresponding clue
		int[] row = projectCellStatesRow(rowIdx);
		int[] clues = getRowClue(rowIdx);
		
		// return true if they're equal
		if (Arrays.equals(row, clues)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Indicates whether the column with the given index has been solved.
	 * 
	 * @param colIdx	column index
	 * @return	true if the projected column matches the column's clue, false otherwise
	 */
	public boolean isColSolved(int colIdx) {
		// get the column and its corresponding clue
		int[] col = projectCellStatesCol(colIdx);
		int[] clues = getColClue(colIdx);
		
		// return true if they're equal
		if (Arrays.equals(col, clues)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Indicates whether the puzzle is solved.
	 * 
	 * @return	true if the puzzle is solved, false otherwise
	 */
	public boolean isSolved() {
		boolean solved = true;
		
		// check if each row is solved
		for (int i = 0; i < getNumRows(); ++i) {
			if (!isRowSolved(i)) {
				solved = false;
			}
		}
		
		// check if each col is solved
		for (int i = 0; i < getNumCols(); ++i) {
			if (!isColSolved(i)) {
				solved = false;
			}
		}
		
		return solved;
	}
	
	/**
	 * Changes the states of all cells to empty.
	 */
	public void resetCells() {
		for (int i = 0; i < this.cellStates.length; ++i) {
			Arrays.fill(this.cellStates[i], CellState.EMPTY);
		}
	}
	
	/**
	 * Returns the projection of the cellStates row with the given index.
	 * 
	 * @param rowIdx	row index
	 * @return	int array of the row projection
	 */
	public int[] projectCellStatesRow(int rowIdx) {
		// get the row to be projected
		CellState[] row = this.cellStates[rowIdx];
		
		// create boolean array of cell states
		boolean[] cells = new boolean[row.length];
		for (int i = 0; i < cells.length; ++i) {
			cells[i] = CellState.toBoolean(row[i]);
		}
		
		// project the boolean array
		List<Integer> projection = project(cells);
		
		// convert the ArrayList to an int array
		int[] result = new int[projection.size()];
		for (int i = 0; i < result.length; ++i) {
			result[i] = (int) projection.get(i);
		}
		
		return result;
	}
	
	/**
	 * Returns the projection of the cellStates column with the given index.
	 * 
	 * @param colIdx	column index
	 * @return	int array of the column projection
	 */
	public int[] projectCellStatesCol(int colIdx) {
		// get the column to be projected
		CellState[] col = new CellState[getNumRows()];
		for (int i = 0; i < col.length; ++i) {
			col[i] = this.cellStates[i][colIdx];
		}
		
		// create boolean array of cell states
		boolean[] cells = new boolean[col.length];
		for (int i = 0; i < cells.length; ++i) {
			cells[i] = CellState.toBoolean(col[i]);
		}
		
		// project boolean array
		List<Integer> projection = project(cells);
		
		// convert the ArrayList to an int array
		int[] result = new int[projection.size()];
		for (int i = 0; i < result.length; ++i) {
			result[i] = (int) projection.get(i);
		}
		
		return result;
	}
	
	/**
	 * Returns the nonogram numbers of the given array of cells.
	 * 
	 * @param cells	boolean array of cells to be projected
	 * @return	the projection of the cells
	 */
	public static List<Integer> project(boolean[] cells) {
		ArrayList<Integer> projection = new ArrayList<Integer>();
		
		boolean current;
		int numCluster = 0;
		boolean hasFilled = false;
		for (int i = 0; i < cells.length; ++i) {
			current = cells[i];
			
			if (current) {
				++numCluster;
				hasFilled = true;
				
				if ((i == cells.length - 1) || !cells[i + 1]) {
					// end of the cluster, so add the amount to the list
					projection.add(numCluster);
					numCluster = 0;
				}
			}
		}
		// if the row has no filled cells, add 0 to the list
		if (!hasFilled) {
			projection.add(0);
		}
		
		return projection;
	}
	
	// This is implemented for you
	private static CellState[][] initCellStates(int numRows, int numCols) {
		// Create a 2D array to store numRows * numCols elements
		CellState[][] cellStates = new CellState[numRows][numCols];
		
		// Set each element of the array to empty
		for (int rowIdx = 0; rowIdx < numRows; ++rowIdx) {
			for (int colIdx = 0; colIdx < numCols; ++colIdx) {
				cellStates[rowIdx][colIdx] = CellState.EMPTY;
			}
		}
		
		// Return the result
		return cellStates;
	}
	
	private static int[][] deepCopy(int[][] array) {
		/*
		 * Title: Deep Copy of 2D Array in Java
		 * Type: Source Code
		 * Availability: https://www.knowprogram.com/java/copy-array-java/
		 */
		int[][] temp = new int[array.length][];
		
		for (int i = 0; i < array.length; ++i) {
			temp[i] = new int[array[i].length];
			
			for (int j = 0; j < array[i].length; ++j) {
				temp[i][j] = array[i][j];
			}
		}
		
		return temp;
	}
	
	// This method is implemented for you. You need to figure out how it is useful.
	private static int[][] readClueLines(BufferedReader reader, int numLines)
			throws IOException {
		// Create a new 2D array to store the clues
		int[][] clueLines = new int[numLines][];
		
		// Read in clues line-by-line and add them to the array
		for (int lineNum = 0; lineNum < numLines; ++lineNum) {
			// Read in a line
			String line = reader.readLine();
			
			// Split the line according to the delimiter character
			String[] tokens = line.split(DELIMITER);
			
			// Create new int array to store the clues in
			int[] clues = new int[tokens.length];
			for (int idx = 0; idx < tokens.length; ++idx) {
				clues[idx] = Integer.parseInt(tokens[idx]);
			}
			
			// Store the processed clues in the resulting 2D array
			clueLines[lineNum] = clues;
		}
		
		// Return the result
		return clueLines;
	}
	
}

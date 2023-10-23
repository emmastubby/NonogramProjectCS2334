package edu.ou.cs2334.project5.models;
/**
 * This enumeration represents the state of each cell in the puzzle grid,
 * which can be either empty, filled, or marked.
 * 
 * @author emmas
 * @version 0.1
 */
public enum CellState {
	
	/**
	 * Empty cell state
	 */
	EMPTY, 
	
	/**
	 * Filled cell state
	 */
	FILLED, 
	
	/**
	 * Marked cell state
	 */
	MARKED;
	
	/**
	 * Helper method returns true if the given cell state is filled and
	 * false otherwise.
	 * 
	 * @param state a given CellState
	 * @return true if the CellState is filled, false otherwise
	 */
	public static boolean toBoolean(CellState state) {
		if (state == FILLED) {
			return true;
		}
		else {
			return false;
		}
	}

}

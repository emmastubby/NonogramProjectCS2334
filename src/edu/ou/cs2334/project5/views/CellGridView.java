package edu.ou.cs2334.project5.views;

import edu.ou.cs2334.project5.models.CellState;
import javafx.scene.layout.GridPane;
/**
 * Extends GridPane and displays the cell states.
 * 
 * @author emmas
 * @version 0.1
 */
public class CellGridView extends GridPane {
	
	private static final String STYLE_CLASS = "cell-grid-view";
	private static CellView[][] cellView;
	
	/**
	 * Constructs a CellGridView using the number of rows, number of columns,
	 * and the cell length.
	 * 
	 * @param numRows	number of rows in the grid
	 * @param numCols	number of columns in the grid
	 * @param cellLength	length of each cell in pixels
	 */
	public CellGridView(int numRows, int numCols, int cellLength) {
		// use initCells
		initCells(numRows, numCols, cellLength);
		
		// add style class
		getStyleClass().add(STYLE_CLASS);
	}
	
	/**
	 * Initializes the cells of the view.
	 * 
	 * @param numRows	number of rows in the grid
	 * @param numCols	number of columns in the grid
	 * @param cellLength	length of each cell in pixels
	 */
	public void initCells(int numRows, int numCols, int cellLength) {
		// clear children of the view
		getChildren().clear();
		
		// initialize cell view array
		cellView = new CellView[numRows][numCols];
		
		for (int row = 0; row < numRows; ++row) {
			
			for (int col = 0; col < numCols; ++col) {
				
				// create cell view at each spot in the array
				cellView[row][col] = new CellView(cellLength);
				
				// add each cell view to the grid
				add(cellView[row][col], col, row);
			}
		}
	}
	
	/**
	 * Gets the cell view at the given indices.
	 * 
	 * @param rowIdx	row index of the cell view
	 * @param colIdx	column index of the cell view
	 * @return	the cell view at the given indices
	 */
	public CellView getCellView(int rowIdx, int colIdx) {
		return cellView[rowIdx][colIdx];
	}
	
	/**
	 * Updates the state of the cell view with the given indices.
	 * 
	 * @param rowIdx	row index of the cell view
	 * @param colIdx	column index of the cell view
	 * @param state		desired state of the cell
	 */
	public void setCellState(int rowIdx, int colIdx, CellState state) {
		cellView[rowIdx][colIdx].setState(state);
	}

}

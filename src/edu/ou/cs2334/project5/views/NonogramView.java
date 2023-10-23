package edu.ou.cs2334.project5.views;

import edu.ou.cs2334.project5.models.CellState;
import edu.ou.cs2334.project5.views.clues.LeftCluesView;
import edu.ou.cs2334.project5.views.clues.TopCluesView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
/**
 * Extends the BorderPane class and displays the cells grid, the row clues
 * to the left of the grid, and the column clues on top of the grid.
 * 
 * @author emmas
 * @version 0.1
 */
public class NonogramView extends BorderPane {
	
	private static final String STYLE_CLASS = "nonogram-view";
	private static final String SOLVED_STYLE_CLASS = "nonogram-view-solved";
	private LeftCluesView leftCluesView;
	private TopCluesView topCluesView;
	private CellGridView cellGridView;
	private HBox bottomHBox;
	private Button loadBtn;
	private Button resetBtn;
	
	/**
	 * Constructs a NonogramView by adding the style class "nonogram-view".
	 */
	public NonogramView() {
		// add the style class
		getStyleClass().add(STYLE_CLASS);
	}
	
	/**
	 * Initializes the view.
	 * 
	 * @param rowClues	2D array representing the row clues
	 * @param colClues	2D array representing the column clues
	 * @param cellLength	length of each cell in pixels
	 */
	public void initialize(int[][] rowClues, int[][] colClues, int cellLength) {
		// initialize left clues view
		this.leftCluesView = new LeftCluesView(rowClues, cellLength, 
				maxClueLength(rowClues));
		
		// initialize top clues view
		this.topCluesView = new TopCluesView(colClues, cellLength, 
				maxClueLength(colClues));

		
		// initialize cell grid view
		this.cellGridView = new CellGridView(rowClues.length, colClues.length,
				cellLength);
		
		// set left clues view to left part of the BorderPane
		setLeft(this.leftCluesView);
		setAlignment(this.leftCluesView, Pos.TOP_LEFT);
		
		// set top clues view to top part of the BorderPane
		setTop(this.topCluesView);
		setAlignment(this.topCluesView, Pos.TOP_RIGHT);
		
		// set cell grid view to center part of the BorderPane
		setCenter(this.cellGridView);
		setAlignment(this.cellGridView, Pos.BOTTOM_RIGHT);
		
		// initialize HBox
		initBottomHBox();
		
		// set bottom HBox to bottom part of the BorderPane
		setBottom(this.bottomHBox);
	}
	
	private void initBottomHBox() {
		// initialize HBox
		this.bottomHBox = new HBox();
		
		// set HBox alignment
		this.bottomHBox.setAlignment(Pos.CENTER);
		
		// create Load button
		this.loadBtn = new Button("Load");
		
		// create Reset button
		this.resetBtn = new Button("Reset");
		
		// add buttons to the HBox
		this.bottomHBox.getChildren().addAll(this.loadBtn, this.resetBtn);
	}
	
	/**
	 * Gets the CellView with the given indices.
	 * 
	 * @param rowIdx	row index of the CellView
	 * @param colIdx	column index of the CellView
	 * @return	the CellView with the given indices
	 */
	public CellView getCellView(int rowIdx, int colIdx) {
		return cellGridView.getCellView(rowIdx, colIdx);
	}
	
	/**
	 * Updates the state of the CellView at the given indices.
	 * 
	 * @param rowIdx	row index of the CellView
	 * @param colIdx	column index of the CellView
	 * @param state	desired state of the CellView
	 */
	public void setCellState(int rowIdx, int colIdx, CellState state) {
		cellGridView.setCellState(rowIdx, colIdx, state);
	}
	
	/**
	 * Updates the state of the row clue.
	 * 
	 * @param rowIdx	index of the row clue
	 * @param solved	desired state of the row clue
	 */
	public void setRowClueState(int rowIdx, boolean solved) {
		leftCluesView.setState(rowIdx, solved);
	}
	
	/**
	 * Updates the state of the column clue.
	 * 
	 * @param colIdx	index of the column clue
	 * @param solved	desired state of the column clue
	 */
	public void setColClueState(int colIdx, boolean solved) {
		topCluesView.setState(colIdx, solved);
	}
	
	/**
	 * Updates whether the puzzle is solved or not.
	 * 
	 * @param solved	boolean indicated if the puzzle is solved
	 */
	public void setPuzzleState(boolean solved) {
		// if puzzle is solved, add SOLVED_STYLE_CLASS
		// else remove all SOLVED_STYLE_CLASS
		if (solved) {
			getStyleClass().add(SOLVED_STYLE_CLASS);
		}
		else {
			getStyleClass().removeAll(SOLVED_STYLE_CLASS);
		}
	}
	
	/**
	 * Returns the view's load button
	 * 
	 * @return	the view's load button
	 */
	public Button getLoadButton() {
		return this.loadBtn;
	}
	
	/**
	 * Returns the view's reset button
	 * 
	 * @return	the view's reset button
	 */
	public Button getResetButton() {
		return this.resetBtn;
	}
	
	/**
	 * Shows a victory alert
	 */
	public void showVictoryAlert() {
		// create new alert of type information
		Alert victory = new Alert(AlertType.INFORMATION);
		
		// set the title
		victory.setTitle("Puzzle Solved");
		
		// set head and content text
		victory.setHeaderText("Congratulations!");
		victory.setContentText("You Win!");
		
		// show the alert and wait for the user to click the OK button
		victory.showAndWait();
	}
	
	private int maxClueLength(int[][] clues) {
		// if the clue array has a length of one, return the clue length
		if (clues.length == 1) {
			return clues[0].length;
		}
		
		int max = clues[0].length;
		
		// find clue within maximum length
		int clueLength;
		for (int i = 1; i < clues.length; ++i) {
			clueLength = clues[i].length;
			
			if (clueLength > max) {
				max = clueLength;
			}
		}
		
		return max;
	}

}

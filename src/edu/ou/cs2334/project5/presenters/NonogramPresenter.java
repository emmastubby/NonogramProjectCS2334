package edu.ou.cs2334.project5.presenters;

import edu.ou.cs2334.project5.views.CellView;
import edu.ou.cs2334.project5.views.NonogramView;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import edu.ou.cs2334.project5.handlers.OpenHandler;
import edu.ou.cs2334.project5.interfaces.Openable;
import edu.ou.cs2334.project5.models.CellState;
import edu.ou.cs2334.project5.models.NonogramModel;
/**
 * Synchronizes the graphical view and the model data of the nonogram
 * puzzle.
 * 
 * @author emmas
 * @version 0.1
 */
public class NonogramPresenter implements Openable {
	
	private NonogramView view;
	private NonogramModel model;
	private int cellLength;
	private static final String DEFAULT_PUZZLE = "puzzles/space-invader.txt";
	
	/**
	 * Constructs a NonogramPresenter using a given cell size.
	 * 
	 * @param cellLength	length of each cell in pixels
	 * @throws IOException	if I/O exception occurs
	 */
	public NonogramPresenter(int cellLength) throws IOException {
		// initialize cell length
		this.cellLength = cellLength;
		
		// initialize model
		this.model = new NonogramModel(DEFAULT_PUZZLE);
		
		// construct view
		this.view = new NonogramView();
		
		// initialize presenter
		initializePresenter();
	}
	
	private void initializePresenter() {
		// initialize view
		initializeView();
		
		// bind cell views
		bindCellViews();
		
		// synchronize model and view data
		synchronize();
		
		// configure buttons
		configureButtons();
	}
	
	private void initializeView() {
		// initialize view
		int[][] rowClues = model.getRowClues();
		int[][] colClues = model.getColClues();
		this.view.initialize(rowClues, colClues, this.cellLength);
		
		// set window size
		if (getWindow() != null) {
			getWindow().sizeToScene();
		}
	}
	
	private void bindCellViews() {
		// iterate through each cell
		CellView cellView;
		for (int row = 0; row < model.getNumRows(); ++row) {
			
			for (int col = 0; col < model.getNumCols(); ++col) {
				final int ROW = row;
				final int COL = col;
				// get the cell view for each cell in the puzzle
				cellView = this.view.getCellView(row, col);
				
				/*
				 * Title: Right click in JavaFX?
				 * Author: Paul S.
				 * Type: Source Code
				 * Availability: https://stackoverflow.com/questions/1515547/right-click-in-javafx
				 */
				cellView.setOnMouseClicked(event ->
				{
					// call corresponding click method if right or left clicked
					if (event.getButton() == MouseButton.PRIMARY) {
						handleLeftClick(ROW, COL);
					}
					else if (event.getButton() == MouseButton.SECONDARY) {
						handleRightClick(ROW, COL);
					}
				}
				);
			}
		}
	}
	
	private void handleLeftClick(int rowIdx, int colIdx) {
		CellState state = null;
		
		// get current cell state
		CellState currState = this.model.getCellState(rowIdx, colIdx);
		
		// if state is empty or marked, change to filled
		// if state is filled, change to empty
		if (currState == CellState.EMPTY || currState == CellState.MARKED) {
			state = CellState.FILLED;
		}
		else if (currState == CellState.FILLED) {
			state = CellState.EMPTY;
		}
		
		// update model and view with the desired state
		updateCellState(rowIdx, colIdx, state);
	}
	
	private void handleRightClick(int rowIdx, int colIdx) {
		CellState state = null;
		
		// get current cell state
		CellState currState = this.model.getCellState(rowIdx, colIdx);
		
		// if state is empty or filled, change to marked
		// if state is marked, change to empty
		if (currState == CellState.EMPTY || currState == CellState.FILLED) {
			state = CellState.MARKED;
		}
		else if (currState == CellState.MARKED) {
			state = CellState.EMPTY;
		}
		
		// update model and view with desired state
		updateCellState(rowIdx, colIdx, state);
	}
	
	private void updateCellState(int rowIdx, int colIdx, CellState state) {
		// update model
		boolean changed = this.model.setCellState(rowIdx, colIdx, state);
		
		// if nothing changed, don't update view
		if (!changed) {
			return;
		}
		
		// check to see if this row or column is solved
		boolean rowSolved = this.model.isRowSolved(rowIdx);
		boolean colSolved = this.model.isColSolved(colIdx);
		
		// update view
		this.view.setCellState(rowIdx, colIdx, state);
		this.view.setRowClueState(rowIdx, rowSolved);
		this.view.setColClueState(colIdx, colSolved);
		
		// check if model is solved
		boolean modelSolved = this.model.isSolved();
		
		if (modelSolved) {
			processVictory();
		}
	}
	
	private void synchronize() {
		// iterate through each cell
		for (int row = 0; row < model.getNumRows(); ++row) {
			
			for (int col = 0; col < model.getNumCols(); ++col) {
				
				// set the view state of each cell to that of the model
				this.view.setCellState(row, col, this.model.getCellState(row, col));
			}
		}
		
		// set the view's row clue to the state of the model's row state
		boolean rowSolved;
		for (int row = 0; row < model.getNumRows(); ++row) {
			rowSolved = this.model.isRowSolved(row);
			this.view.setRowClueState(row, rowSolved);
		}
		
		// set the view's col clue to the state of the model's col state
		boolean colSolved;
		for (int col = 0; col < model.getNumCols(); ++col) {
			colSolved = this.model.isColSolved(col);
			this.view.setColClueState(col, colSolved);
		}
		
		// sync puzzle state
		boolean puzzleSolved = this.model.isSolved();
		this.view.setPuzzleState(puzzleSolved);
		
		if (puzzleSolved) {
			processVictory();
		}
	}
	
	private void processVictory() {
		// remove marks
		removeCellViewMarks();
		
		// show victory alert
		this.view.showVictoryAlert();
	}
	
	private void removeCellViewMarks() {
		
		CellState state;
		for (int row = 0; row < model.getNumRows(); ++row) {
			
			for (int col = 0; col < model.getNumCols(); ++col) {
				// get the state of each cell
				state = this.model.getCellState(row, col);
				
				// if the cell is marked, change to empty
				if (state == CellState.MARKED) {
					this.view.setCellState(row, col, CellState.EMPTY);
				}
			}
		}
	}
	
	private void configureButtons() {
		// configure load button
		FileChooser fileChooser1 = new FileChooser();
		fileChooser1.setTitle("Load");
		fileChooser1.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
		fileChooser1.setInitialDirectory(new File("."));
		view.getLoadButton()
			 .setOnAction(new OpenHandler(getWindow(), fileChooser1, this));
		
		// configure reset button
		view.getResetButton().setOnAction((ActionEvent event) -> {
			resetPuzzle();
		});
	}
	
	private void resetPuzzle() {
		// reset model
		this.model.resetCells();
		
		// sync model and view
		synchronize();
	}
	
	/**
	 * Returns the Pane associated with the presenter.
	 * 
	 * @return	the Pane associated with the presenter
	 */
	public Pane getPane() {
		return this.view;
	}
	
	/**
	 * Returns the Window associated with the presenter.
	 * 
	 * @return	the window associated with the presenter
	 */
	public Window getWindow() {
		try {
			Pane pane = this.view;
			Scene scene = pane.getScene();
			Window window = scene.getWindow();
			return window;
		}
		catch(NullPointerException e) {
			return null;
		}
	}
	
	/**
	 * Re-initializes the model with the given puzzle file.
	 */
	public void open(File file) throws FileNotFoundException, IOException {
		// initialize model
		this.model = new NonogramModel(file);
		
		// initialize presenter
		initializePresenter();
	}

}

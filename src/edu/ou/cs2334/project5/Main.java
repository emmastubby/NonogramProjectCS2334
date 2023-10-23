package edu.ou.cs2334.project5;
import java.io.IOException;
import java.util.List;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import edu.ou.cs2334.project5.presenters.NonogramPresenter;
/**
 * Extends Application and starts the nonogram puzzle.
 * 
 * @author emmas
 * @version 0.1
 */
public class Main extends Application {
	
	private static int IDX_CELL_SIZE = 0;
	private static int DEFAULT_CELL_SIZE = 30;
	
	/**
	 * Launches the puzzle.
	 * 
	 * @param args	arguments of the program
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Sets the stage to start the program.
	 * 
	 * @param primaryStage	stage of the program
	 */
	public void start(Stage primaryStage) throws IOException {
		List<String> parameters = getParameters().getUnnamed();
		
		// extract cell size
		int cellSize;
		if (parameters.size() != 0) {
			cellSize = Integer.parseInt(parameters.get(IDX_CELL_SIZE));			
		}
		else {
			cellSize = DEFAULT_CELL_SIZE;
		}

		// construct presenter
		NonogramPresenter presenter = new NonogramPresenter(cellSize);
		
		// create scene
		Pane pane = presenter.getPane();
		Scene scene = new Scene(pane);
		scene.getStylesheets().add("/style.css");
		
		// set scene on primary stage
				primaryStage.setScene(scene);
				primaryStage.setTitle("Nonogram Puzzle");
				primaryStage.setResizable(false);
				primaryStage.show();
	}

}

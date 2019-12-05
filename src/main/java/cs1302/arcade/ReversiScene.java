package cs1302.arcade;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * The class {@code ReversiScene} represents the custom Scene class for the
 * Reversi game. It contains the actual Reversi game board and the scoring
 * section.
 *
 * @author Vincent Bui
 */
public class ReversiScene extends Scene {

    private GridPane reversiGrid;
    private StackPane stackPane;
    private CustomHBox chBox1;
    private HBox reversiHomeBox;

    private Group cellGroup;
    private Rectangle square;
    private Circle circle;
    private StackPane stackCell;

    private int[][] reversiArray = new int[GRID_SIZE][GRID_SIZE];

    private final static int SQUARE_SIZE = 55;
    private final static int GRID_SIZE = 8;


    /**
     * Constructor of the class
     *
     * @param pStage  the primary stage for the application
     * @param hScreen the scene for the home page
     * @param layout  the root layout control for the scene
     * @param width   the preferred width for the scene
     */
    public ReversiScene(Stage pStage, Scene hScreen, VBox layout, double width) {

        super(layout, width, 725, Color.LIGHTBLUE);
        layout.setSpacing(10);
        this.getStylesheets().add("application.css");

        chBox1 = new CustomHBox(pStage, hScreen, "Reversi");

        reversiHomeBox = new HBox(20);
        reversiHomeBox.setPadding(new Insets(10, 20, 10, 20));

        // setup the Reversi Grid and Score Grid
        scoreGridSetup();
        reversiGridSetup();

        // Allows to overlays layouts for pausing the game
        stackPane = new StackPane();
        stackPane.getChildren().add(reversiGrid);

        reversiHomeBox.getChildren().addAll(stackPane);
        layout.getChildren().addAll(chBox1, reversiHomeBox);

    }

    /**
     * Implements the handler for the Start button
     *
     * @param e the object of type ActionEvent
     */
    public void startButtonHandler(ActionEvent e) {

    }

    /**
     * This method sets up the scoring Grid.
     */
    public void scoreGridSetup() {

    }

    /**
     * This method initializes the reversiGrid
     */
    public void reversiGridSetup() {

        reversiGrid = new GridPane();
        reversiGrid.getStyleClass().add("rGrid");
        reversiGrid.getStyleClass().add("rBackground");

        // setting row and column dimensions (height and width)
        for (int i = 0; i < GRID_SIZE; i++) {
            reversiGrid.getColumnConstraints().add(new ColumnConstraints(SQUARE_SIZE));
        }

        for (int i = 0; i < GRID_SIZE; i++) {
            reversiGrid.getRowConstraints().add(new RowConstraints(SQUARE_SIZE));
        }

        // starting position
        reversiArray[3][3] = 1;
        reversiArray[3][4] = 2;
        reversiArray[4][4] = 1;
        reversiArray[4][3] = 2;

        draw();

    }

    public void draw() {

        // Iterate through each cell in the reversiGrid
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                cellGroup = new Group();
                // Shapes used for each cell
                square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);

                // Sets the color of the square according to its point type
                square.setFill(Color.GREEN);

                if(reversiArray[i][j] == 1  || reversiArray[i][j] == 2) {
                    stackCell = new StackPane();
                    circle = new Circle(22);
                    if (reversiArray[i][j] == 1 ) {
                        circle.setFill(Color.WHITE);
                    } else {
                        circle.setFill(Color.BLACK);
                    }

                    stackCell.getChildren().addAll(square, circle);
                    cellGroup.getChildren().addAll(stackCell);
                }
                else {
                    cellGroup.getChildren().addAll(square);
                }

                reversiGrid.add(cellGroup, j, i);
            }
        }
    }


    /**
     * Creates or resets variables needed to start a new game
     */
    public void startNewGame() {

    }

}

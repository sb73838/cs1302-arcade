package cs1302.arcade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The class {@code TetrisScene} represents the custom Scene class for the
 * Tetris game. It contains the actual Tetris game board and the scoring
 * section.
 * 
 * @author Sahil Balhara
 */
public class TetrisScene extends Scene {

    private Label statisticsLabel;
    private Label scoreLabel;
    private Label scoreValue;
    private Label lineLabel;
    private Label lineValue;
    private Label levelLabel;
    private Label levelValue;

    private Button btnStart;
    private Button btnReset;
    private GridPane tetrisGrid;
    private GridPane scoreGrid;

    private TetrisBoard tetrisBoard;
    private boolean pressed;
    private boolean running;
    private boolean gameOver;
    private BorderPane pausePane;
    private BorderPane gameOverPane;
    private Group cellGroup;
    private int shapeSpeed;
    private Label gameOverTitle;
    private Label gamePauseTitle;
    private Label gameOverScore;
    private List<Point> points;
    private Map<Integer, Color> shapeColors;
    private PauseTransition pauseTransition;
    private Point currentPoint;
    private Polygon topShade;
    private Polygon bottomShade;
    private Rectangle tetrisBoardShade;
    private Rectangle square;
    private SequentialTransition shapeTransition;
    private StackPane stackPane;
    private VBox gameOverVbox;
    private CustomHBox chBox1;
    private HBox tetrisHomeBox;

    private final static int PIXEL = 25;

    /**
     * Constructor of the class
     * 
     * @param pStage  the primary stage for the application
     * @param hScreen the scene for the home page
     * @param layout  the root layout control for the scene
     * @param width   the preferred width for the scene
     */
    public TetrisScene(Stage pStage, Scene hScreen, VBox layout, double width) {

        super(layout, width, 725, Color.LIGHTBLUE);
        layout.setSpacing(10);
        this.getStylesheets().add("application.css");

        chBox1 = new CustomHBox(pStage, hScreen, "Tetris");

        initializeMemberVariables();

        tetrisHomeBox = new HBox(20);
        tetrisHomeBox.setPadding(new Insets(10, 20, 10, 20));

        // setup the Tetris Grid and Score Grid
        scoreGridSetup();
        tetrisGridSetup();

        // Allows to overlays layouts for pausing the game
        stackPane = new StackPane();
        stackPane.getChildren().add(tetrisGrid);

        tetrisHomeBox.getChildren().addAll(scoreGrid, stackPane);
        layout.getChildren().addAll(chBox1, tetrisHomeBox);

        this.setOnKeyReleased(e -> {
            pressed = true;
        });

        this.setOnKeyPressed((e) -> {
            if (running) {
                if (e.getCode().equals(KeyCode.LEFT) || e.getCode().equals(KeyCode.A)) {
                    tetrisBoard.moveLeft();
                    draw();
                } else if (e.getCode().equals(KeyCode.DOWN) || e.getCode().equals(KeyCode.S)) {
                    tetrisBoard.moveDown();
                    draw();
                } else if (e.getCode().equals(KeyCode.RIGHT) || e.getCode().equals(KeyCode.D)) {
                    tetrisBoard.moveRight();
                    draw();
                } else if ((e.getCode().equals(KeyCode.UP) || e.getCode().equals(KeyCode.W)) 
                        && pressed) {
                    pressed = false;
                    tetrisBoard.rotate();
                    draw();
                }
            }
        });

        btnStart.setOnAction(e -> startButtonHandler(e));

        initializeBoard();
    }

    /**
     * Implements the event handler for the Start button
     * 
     * @param e the object of type ActionEvent
     */
    public void startButtonHandler(ActionEvent e) {

        // flip the label between Start and Pause
        if (btnStart.getText().equalsIgnoreCase("Start")) {
            btnStart.setText("Pause");
        } else {
            btnStart.setText("Start");
        }

        // if the game is over
        if (gameOver) {
            shapeTransition.stop();
            startNewGame();
            stackPane.getChildren().removeAll(tetrisBoardShade, gameOverPane);
        } else {
            if (running) {
                running = false;
                shapeTransition.stop();
                stackPane.getChildren().addAll(tetrisBoardShade, pausePane);
            } else {
                running = true;
                if (shapeTransition != null) {
                    shapeTransition.play();
                } else {
                    startNewGame();
                }

                stackPane.getChildren().removeAll(tetrisBoardShade, pausePane);
            }
        }
        setSceneDisable(!running);
    }

    /**
     * Initializes some of the Member Variables.
     */
    public void initializeMemberVariables() {

        // initialize the colors
        shapeColors = new HashMap<Integer, Color>();

        shapeColors.put(1, Color.RED);
        shapeColors.put(2, Color.GREEN);
        shapeColors.put(3, Color.BLUE);
        shapeColors.put(4, Color.CHARTREUSE);
        shapeColors.put(5, Color.ORANGE);
        shapeColors.put(6, Color.CYAN);
        shapeColors.put(7, Color.YELLOW);

        statisticsLabel = new Label("Statistics:");
        scoreLabel = new Label("Score:");
        levelLabel = new Label("Level:");
        lineLabel = new Label("Lines cleared:");

        scoreValue = new Label("0");
        levelValue = new Label("0");
        lineValue = new Label("0");

        statisticsLabel.setFont(Font.font("verdana", FontWeight.BOLD, 16));

        // setting the style properties for the labels to make them look consistent
        scoreLabel.getStyleClass().add("arcadeScore");
        levelLabel.getStyleClass().add("arcadeScore");
        lineLabel.getStyleClass().add("arcadeScore");

        scoreValue.getStyleClass().add("arcadeScore");
        levelValue.getStyleClass().add("arcadeScore");
        lineValue.getStyleClass().add("arcadeScore");

        btnStart = new Button("Start");
        btnReset = new Button("Reset");

        btnStart.getStyleClass().add("arcadeButton");
        btnReset.getStyleClass().add("arcadeButton");
    }

    /**
     * This method sets up the scoring Grid.
     */
    public void scoreGridSetup() {

        // initializing the scoring grid
        scoreGrid = new GridPane();
        scoreGrid.setPrefWidth(210);
        scoreGrid.setPrefHeight(600);
        scoreGrid.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" 
                + "-fx-border-width: 2;" + "-fx-border-insets: 5;" 
                + "-fx-border-radius: 5;" + "-fx-border-color: green;");

        // setting row and column dimensions (height and width)
        scoreGrid.getColumnConstraints().add(new ColumnConstraints(130));
        scoreGrid.getColumnConstraints().add(new ColumnConstraints(75));

        scoreGrid.getRowConstraints().add(new RowConstraints(50));
        scoreGrid.getRowConstraints().add(new RowConstraints(50));
        scoreGrid.getRowConstraints().add(new RowConstraints(50));
        scoreGrid.getRowConstraints().add(new RowConstraints(50));
        scoreGrid.getRowConstraints().add(new RowConstraints(200));
        scoreGrid.getRowConstraints().add(new RowConstraints(50));

        // adding the scoring labels and their values
        scoreGrid.add(statisticsLabel, 0, 0);

        scoreGrid.add(scoreLabel, 0, 1);
        scoreGrid.add(scoreValue, 1, 1);

        scoreGrid.add(levelLabel, 0, 2);
        scoreGrid.add(levelValue, 1, 2);

        scoreGrid.add(lineLabel, 0, 3);
        scoreGrid.add(lineValue, 1, 3);

        scoreGrid.add(btnStart, 0, 4);
        scoreGrid.add(btnReset, 0, 5);
    }

    /**
     * This method initializes the tetrisGrid, game pause and game over controls
     */
    public void tetrisGridSetup() {

        tetrisGrid = new GridPane();
        tetrisGrid.getStyleClass().add("tGrid");
        tetrisGrid.getStyleClass().add("tBackground");

        // setting row and column dimensions (height and width)
        for (int i = 0; i < TetrisBoard.WIDTH; i++) {
            tetrisGrid.getColumnConstraints().add(new ColumnConstraints(PIXEL));
        }

        for (int i = 0; i < TetrisBoard.HEIGHT; i++) {
            tetrisGrid.getRowConstraints().add(new RowConstraints(PIXEL));
        }

        // initialize the game pause controls
        gamePauseTitle = new Label("II");
        gamePauseTitle.setTextFill(Color.WHITE);
        gamePauseTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 72));
        gamePauseTitle.setTextAlignment(TextAlignment.CENTER);

        pausePane = new BorderPane();
        pausePane.setCenter(gamePauseTitle);

        tetrisBoardShade = new Rectangle(TetrisBoard.WIDTH * PIXEL + 2 * (TetrisBoard.WIDTH - 1),
                TetrisBoard.HEIGHT * PIXEL + 2 * (TetrisBoard.HEIGHT - 1));
        tetrisBoardShade.setOpacity(0.5);
        tetrisBoardShade.setFill(Color.BLACK);
        tetrisBoardShade.toFront();

        // initialize the game over controls
        gameOverTitle = new Label("GAME OVER");
        gameOverTitle.setTextFill(Color.WHITE);
        gameOverTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 36));
        gameOverTitle.setTextAlignment(TextAlignment.CENTER);

        gameOverScore = new Label("");
        gameOverScore.setTextFill(Color.WHITE);
        gameOverScore.setFont(Font.font("Segoe UI", 24));
        gameOverScore.setTextAlignment(TextAlignment.CENTER);

        gameOverVbox = new VBox();
        gameOverVbox.getChildren().addAll(gameOverTitle, gameOverScore);
        gameOverVbox.setAlignment(Pos.CENTER);

        gameOverPane = new BorderPane();
        gameOverPane.setCenter(gameOverVbox);
    }

    /**
     * Creates or resets variables needed to start a new game
     */
    public void startNewGame() {

        tetrisBoard = new TetrisBoard();
        running = true;
        gameOver = false;
        pressed = true;
        shapeSpeed = tetrisBoard.getTimeToMove();

        draw();
        createTransition();
    }

    /**
     * Creates a new transition for the moving shape. Called when the speed of the
     * moving shape changes.
     */
    public void createTransition() {

        shapeSpeed = tetrisBoard.getTimeToMove();
        shapeTransition = new SequentialTransition();
        pauseTransition = new PauseTransition(Duration.millis(shapeSpeed));

        // move the current Tetris shape one step down
        pauseTransition.setOnFinished(e -> {
            tetrisBoard.moveDown();
            draw();
        });

        shapeTransition.getChildren().add(pauseTransition);
        shapeTransition.setCycleCount(Timeline.INDEFINITE);
        shapeTransition.play();
    }

    public void initializeBoard() {

        // Iterates through each cell in the tetrisGrid
        for (int i = 0; i < TetrisBoard.HEIGHT; i++) {
            for (int j = 0; j < TetrisBoard.WIDTH; j++) {
                cellGroup = new Group();
                // Shapes used for each cell
                square = new Rectangle(PIXEL, PIXEL);
                // Colors the square in a gradient style
                square.setFill(Color.GRAY);
                square.setOpacity((1.0 - ((double) i + 35.0) / 
                        ((double) TetrisBoard.HEIGHT + 50)));
                cellGroup.getChildren().addAll(square);

                tetrisGrid.add(cellGroup, j, i);
            }
        }
    }

    /**
     * Draws the game board to reflect the current state of the Tetris game.
     */
    public void draw() {
        // if the game is still going on
        if (!tetrisBoard.isGameOver()) {
            // Updates the speed of the falling block if the speed changed
            if (shapeSpeed != tetrisBoard.getTimeToMove()) {
                shapeTransition.stop();
                createTransition();
            }
            // Updates the game score
            scoreValue.setText(String.valueOf(tetrisBoard.getScore()));
            lineValue.setText(String.valueOf(tetrisBoard.getLinesCleared()));
            levelValue.setText(String.valueOf(tetrisBoard.getLevel()));
            // Gets points of fallen shapes and remove everything from the tetrisGrid
            points = tetrisBoard.getPoints();
            tetrisGrid.getChildren().clear();

            // Iterates through each cell in the tetrisGrid
            for (int i = 0; i < TetrisBoard.HEIGHT; i++) {
                for (int j = 0; j < TetrisBoard.WIDTH; j++) {
                    currentPoint = new Point(j, i);
                    cellGroup = new Group();
                    // Shapes used for each cell
                    square = new Rectangle(PIXEL, PIXEL);
                    topShade = new Polygon();
                    bottomShade = new Polygon();
                    // If the current point was or is part of a shape, color the square
                    if (points.contains(currentPoint)) {
                        topShade.setOpacity(0.5);
                        topShade.setFill(Color.WHITE);
                        bottomShade.setOpacity(0.5);
                        bottomShade.setFill(Color.BLACK);

                        // Sets the color of the square according to its point type
                        square.setFill(shapeColors.get(points.get(
                                points.indexOf(currentPoint)).getType()));
                        cellGroup.getChildren().addAll(square, topShade, bottomShade);
                    } else {
                        // Colors the square in a gradient style
                        square.setFill(Color.GRAY);
                        square.setOpacity((1.0 - ((double) i + 35.0) /
                                ((double) TetrisBoard.HEIGHT + 50)));
                        cellGroup.getChildren().addAll(square);
                    }
                    tetrisGrid.add(cellGroup, j, i);
                }
            }
        } else {
            // Stop the game and the moving shape
            btnStart.setText("Start");
            gameOver = true;
            running = false;
            setSceneDisable(true);
            shapeTransition.stop();
            gameOverScore.setText("\nYou scored " + tetrisBoard.getScore() 
                    + " points\nand reached level "    + tetrisBoard.getLevel() + "!");
            stackPane.getChildren().addAll(tetrisBoardShade, gameOverPane);
        }
    }

    /**
     * Disables the elements in the scene. Used to pause the game or when the game
     * is over.
     */
    public void setSceneDisable(boolean value) {

        scoreLabel.setDisable(value);
        levelLabel.setDisable(value);
        lineLabel.setDisable(value);

        scoreValue.setDisable(value);
        levelValue.setDisable(value);
        lineValue.setDisable(value);
    }

}

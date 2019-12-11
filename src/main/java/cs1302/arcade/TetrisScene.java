package cs1302.arcade;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The class {@code TetrisScene} represents the custom Scene class for the
 * Tetris game. It contains the actual Tetris game board and the scoring
 * section.
 * 
 * @author Sahil Balhara & Vincent Bui
 */
public class TetrisScene extends Scene {

    private Label statisticsLabel;
    private Label highScoresLabel;
    private Label scoreLabel;
    private Label scoreValue;
    private Label lineLabel;
    private Label lineValue;
    private Label levelLabel;
    private Label levelValue;

    private GridPane menuGrid;
    private Button btnHighScores;
    private Button btnHelp;
    private Button btnQuit;
    private Button btnExit;

    private Button btnStart;
    private GridPane tetrisGrid;
    private GridPane scoreGrid;
    private GridPane highScoresGrid;
    private String playerName;

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
    private HBox tetrisHomeBox;

    private Stage primaryStage;
    private Scene homeScreen;

    private static final int PIXEL = 25;
    private static final int HIGH_SCORES = 3;

    private int[] topScores = new int[HIGH_SCORES];
    private String[] topScorers = new String[HIGH_SCORES];

    /**
     * Constructor of the class.
     * 
     * @param pStage  the primary stage for the application
     * @param hScreen the scene for the home page
     * @param layout  the root layout control for the scene
     * @param width   the preferred width for the scene
     */
    public TetrisScene(Stage pStage, Scene hScreen, VBox layout, double width) {

        super(layout, width, 720, Color.LIGHTBLUE);
        layout.setSpacing(5);
        this.getStylesheets().add("application.css");
        primaryStage = pStage;
        homeScreen = hScreen;
        playerName = "guest";

        // initialize member variables
        initializeMemberVariables();
        initializeTopScores();
        initializeMenuBar();

        tetrisHomeBox = new HBox(20);
        tetrisHomeBox.setPadding(new Insets(10, 20, 10, 20));

        // setup the Tetris Grid and Score Grid
        scoreGridSetup();
        tetrisGridSetup();
        updateHighScoresGrid();

        // Allows to overlays layouts for pausing the game
        stackPane = new StackPane();
        stackPane.getChildren().add(tetrisGrid);

        tetrisHomeBox.getChildren().addAll(scoreGrid, stackPane);
        layout.getChildren().addAll(menuGrid, tetrisHomeBox);

        this.setOnKeyReleased(e -> {
            pressed = true;
        });
        // handling on-key pressed event
        this.setOnKeyPressed((e) -> {
            if (running) {
                if (e.getCode().equals(KeyCode.LEFT)) {
                    tetrisBoard.moveLeft();
                    draw();
                } else if (e.getCode().equals(KeyCode.DOWN)) {
                    tetrisBoard.moveDown();
                    draw();
                } else if (e.getCode().equals(KeyCode.RIGHT)) {
                    tetrisBoard.moveRight();
                    draw();
                } else if ((e.getCode().equals(KeyCode.UP)) && pressed) {
                    pressed = false;
                    tetrisBoard.rotate();
                    draw();
                }
            }
        });

        btnStart.setOnAction(e -> startButtonHandler());
        initializeBoard();
        btnStart.requestFocus();
    }

    /**
     * Sets the player name to be used while recording high scores.
     * 
     * @param name the input name to be used
     */
    public void setPlayerName(String name) {

        // trim name and then take out part till the first white space
        name = name.trim();
        if (name.contains(" ")) {
            name = name.substring(0, name.indexOf(" "));
        }

        // use maximum 7 characters
        if (name.length() >= 1) {
            if (name.length() > 7) {
                name = name.substring(0, 7);
            }
            playerName = name;
        } else {
            playerName = "guest";
        }
    }

    /**
     * Implements the event handler for the Start button.
     */
    public void startButtonHandler() {

        // flip the label between Start, Pause and Resume
        if (btnStart.getText().equalsIgnoreCase("Start Game")
                || btnStart.getText().equalsIgnoreCase("Start New Game")) {
            btnStart.setText("Pause");
        } else if (btnStart.getText().equalsIgnoreCase("Pause")) {
            btnStart.setText("Resume");
        } else if (btnStart.getText().equalsIgnoreCase("Resume")) {
            btnStart.setText("Pause");
        }

        // if the game is over
        if (gameOver) {
            if (shapeTransition != null) {
                if (shapeTransition.getStatus() == Animation.Status.RUNNING) {
                    shapeTransition.stop();
                }
            }
            startNewGame();
            stackPane.getChildren().removeAll(tetrisBoardShade, gameOverPane);
        } else {
            if (running) {
                running = false;
                if (shapeTransition != null) {
                    if (shapeTransition.getStatus() == Animation.Status.RUNNING) {
                        shapeTransition.stop();
                    }
                }
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
    }

    /**
     * Initializes some of the Member Variables like colors and scoring labels.
     */
    public void initializeMemberVariables() {

        // initialize the colors
        shapeColors = new HashMap<Integer, Color>();

        shapeColors.put(1, Color.DARKORANGE);
        shapeColors.put(2, Color.CYAN);
        shapeColors.put(3, Color.MEDIUMPURPLE);
        shapeColors.put(4, Color.LIMEGREEN);
        shapeColors.put(5, Color.RED);
        shapeColors.put(6, Color.BLUE);
        shapeColors.put(7, Color.YELLOW);

        // initializing scoring labels
        statisticsLabel = new Label("Statistics:");
        highScoresLabel = new Label("High Scores:");
        highScoresLabel.setVisible(false);

        scoreLabel = new Label("Score:");
        levelLabel = new Label("Level:");
        lineLabel = new Label("Lines cleared:");

        scoreValue = new Label("0");
        levelValue = new Label("0");
        lineValue = new Label("0");

        // formatting scoring labels
        statisticsLabel.setFont(Font.font("segoe ui", FontWeight.NORMAL, 20));
        statisticsLabel.setTextFill(Color.BROWN);
        highScoresLabel.setFont(Font.font("segoe ui", FontWeight.NORMAL, 20));
        highScoresLabel.setTextFill(Color.BROWN);

        // setting the style properties for the labels to make them look consistent
        scoreLabel.getStyleClass().add("arcadeScore");
        levelLabel.getStyleClass().add("arcadeScore");
        lineLabel.getStyleClass().add("arcadeScore");

        scoreValue.getStyleClass().add("arcadeScore");
        levelValue.getStyleClass().add("arcadeScore");
        lineValue.getStyleClass().add("arcadeScore");

        btnStart = new Button("Start Game");
        btnStart.getStyleClass().add("startButton");
    }

    /**
     * This method sets up the menu bar at the top.
     */
    public void initializeMenuBar() {

        menuGrid = new GridPane();
        menuGrid.setPadding(new Insets(12, 10, 12, 20));
        menuGrid.getStyleClass().add("tBackground");

        // setting column dimensions (width) for menuGrid
        menuGrid.getColumnConstraints().add(new ColumnConstraints(150));
        menuGrid.getColumnConstraints().add(new ColumnConstraints(150));
        menuGrid.getColumnConstraints().add(new ColumnConstraints(235));
        menuGrid.getColumnConstraints().add(new ColumnConstraints(150));

        btnHighScores = new Button("High Scores");
        btnHelp = new Button("Instructions");
        btnQuit = new Button("Quit Tetris");
        btnExit = new Button("Exit Arcade");
        // setting styling for buttons
        btnHighScores.getStyleClass().add("arcadeButton");
        btnHelp.getStyleClass().add("arcadeButton");
        btnQuit.getStyleClass().add("arcadeButton");
        btnExit.getStyleClass().add("exitButton");

        // setting event handlers for the menu bar buttons
        btnHighScores.setOnAction(e -> {
            if (btnHighScores.getText().equalsIgnoreCase("High Scores")) {
                btnHighScores.setText("Hide Scores");
                highScoresGrid.setVisible(true);
                highScoresLabel.setVisible(true);
            } else {
                btnHighScores.setText("High Scores");
                highScoresGrid.setVisible(false);
                highScoresLabel.setVisible(false);
            }
        });
        btnQuit.setOnAction(e -> quitButtonHandler());
        btnHelp.setOnAction(e -> displayHelp());
        btnExit.setOnAction(e -> Platform.exit());

        // adding buttons to the menu grid
        menuGrid.add(btnHelp, 0, 0);
        menuGrid.add(btnHighScores, 1, 0);
        menuGrid.add(btnQuit, 2, 0);
        menuGrid.add(btnExit, 3, 0);
    }

    /**
     * This method resets the game before taking user to home screen.
     */
    public void quitButtonHandler() {

        // stop the animation if it's running
        if (shapeTransition != null) {
            if (shapeTransition.getStatus() == Animation.Status.RUNNING) {
                shapeTransition.stop();
            }
        }
        if (tetrisGrid != null) {

            tetrisGrid.getChildren().clear();
            initializeBoard();
            running = false;
            gameOver = true;
            stackPane.getChildren().removeAll(tetrisBoardShade, gameOverPane, pausePane);

            btnStart.setText("Start Game");

            // reset the scores
            scoreValue.setText("0");
            levelValue.setText("0");
            lineValue.setText("0");
        }

        primaryStage.setTitle("CS1302-Arcade!");
        primaryStage.setScene(homeScreen);
    }

    /**
     * This method displays the Tetris instructions.
     */
    public void displayHelp() {

        Stage helpWindow = new Stage();

        helpWindow.initModality(Modality.APPLICATION_MODAL);
        helpWindow.initOwner(primaryStage);
        helpWindow.setTitle("Instructions");
        helpWindow.setMaxWidth(600);
        helpWindow.setMaxHeight(500);

        // Set position of help window, relative to primary window/stage.
        helpWindow.setX(primaryStage.getX() + 60);
        helpWindow.setY(primaryStage.getY() + 100);
        helpWindow.setResizable(false);

        Label topLabel = new Label("Tetris Instructions: \n\n");

        Label label = new Label("You can use left, right, or down arrow keys to move the "
                + "pieces, \nwhereas the up arrow key is used to rotate the pieces. \n\n"
                + "The game is over if your pieces reach the top of the screen. \n"
                + "You can remove pieces from the screen by filling all the squares \n"
                + "in a line, which is known as clearing a line. \n\n"
                + "For every 10 lines cleared, the game level increases by 1. When the \n"
                + "level goes up, the time taken by pieces to drop one spot reduces \n"
                + "by 10% thus increasing the difficulty level. \n\n"
                + "You earn points when you clear a line. The points earned depend on \n"
                + "the number of lines cleared at once and the current game level. \n\n"
                + "You can pause the game and then resume from the same place. \n"
                + "The game also keeps track of high scores (top three scores). \n");

        Label bottomLabel = new Label("  ");
        label.setWrapText(true);

        // formatting the labels
        topLabel.setFont(Font.font("segoe ui", FontWeight.NORMAL, 20));
        topLabel.setTextFill(Color.BROWN);

        label.setFont(Font.font("segoe ui", FontWeight.NORMAL, 16));
        label.setTextFill(Color.DARKBLUE);

        // adding all the labels in to a VBox
        VBox helpVBox = new VBox(topLabel, label, bottomLabel);
        helpVBox.setAlignment(Pos.CENTER);
        helpVBox.setPadding(new Insets(20, 20, 20, 20));

        Scene helpScene = new Scene(helpVBox, 600, 500);
        helpWindow.setScene(helpScene);
        helpWindow.sizeToScene();
        helpWindow.show();
    }

    /**
     * This method sets up the scoring Grid.
     */
    public void scoreGridSetup() {

        // initializing the scoring grid
        scoreGrid = new GridPane();
        scoreGrid.setPrefWidth(300);
        scoreGrid.setPrefHeight(600);
        scoreGrid.getStyleClass().add("greenBorder");

        highScoresGrid = new GridPane();
        highScoresGrid.setVisible(false);

        // setting row and column dimensions (height and width) for highScoresGrid
        highScoresGrid.getColumnConstraints().add(new ColumnConstraints(110));
        highScoresGrid.getColumnConstraints().add(new ColumnConstraints(90));

        highScoresGrid.getRowConstraints().add(new RowConstraints(35));
        highScoresGrid.getRowConstraints().add(new RowConstraints(35));
        highScoresGrid.getRowConstraints().add(new RowConstraints(35));

        // setting row and column dimensions (height and width) for scoreGrid
        scoreGrid.getColumnConstraints().add(new ColumnConstraints(200));
        scoreGrid.getColumnConstraints().add(new ColumnConstraints(100));

        scoreGrid.getRowConstraints().add(new RowConstraints(50));
        scoreGrid.getRowConstraints().add(new RowConstraints(50));
        scoreGrid.getRowConstraints().add(new RowConstraints(50));
        scoreGrid.getRowConstraints().add(new RowConstraints(50));
        scoreGrid.getRowConstraints().add(new RowConstraints(200));

        scoreGrid.getRowConstraints().add(new RowConstraints(50));
        scoreGrid.getRowConstraints().add(new RowConstraints(100));

        // adding the scoring labels and their values
        scoreGrid.add(statisticsLabel, 0, 0);

        scoreGrid.add(scoreLabel, 0, 1);
        scoreGrid.add(scoreValue, 1, 1);
        scoreGrid.add(levelLabel, 0, 2);
        scoreGrid.add(levelValue, 1, 2);
        scoreGrid.add(lineLabel, 0, 3);
        scoreGrid.add(lineValue, 1, 3);

        scoreGrid.add(btnStart, 0, 4);

        //adding high score controls
        scoreGrid.add(highScoresLabel, 0, 5);
        scoreGrid.add(highScoresGrid, 0, 6);
    }

    /**
     * This method updates the grid for top scores.
     */
    public void updateHighScoresGrid() {

        highScoresGrid.getChildren().clear();

        for (int i = 0; i < HIGH_SCORES; i++) {
            if (!topScorers[i].equals("")) {
                Label name = new Label(topScorers[i] + ":");
                Label score = new Label(Integer.toString(topScores[i]));

                // setting the styling for labels
                name.getStyleClass().add("arcadeScore");
                score.getStyleClass().add("arcadeScore");

                highScoresGrid.add(name, 0, (0 + i));
                highScoresGrid.add(score, 1, (0 + i));
            }
        }
    }

    /**
     * This method initializes the top scores.
     */
    public void initializeTopScores() {

        for (int i = 0; i < HIGH_SCORES; i++) {
            topScores[i] = 0;
            topScorers[i] = "";
        }

        Scanner scoreScanner = null;
        // reading scores from the text file
        try {
            File scoresFile = new File("tetrisScores.txt");
            if (scoresFile.exists()) {
                scoreScanner = new Scanner(scoresFile);
                int counter = 0;
                while (scoreScanner.hasNext()) {

                    topScorers[counter] = scoreScanner.next();
                    topScores[counter] = scoreScanner.nextInt();
                    counter++;
                    if (counter >= HIGH_SCORES) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("initializeTopScores() failed");
        } finally {
            if (scoreScanner != null) {
                scoreScanner.close();
            }
        }
    }

    /**
     * Method for detecting new high score.
     * 
     * @param newScore new high score.
     * @return true if there is a new high score
     */
    public boolean gotHighScore(int newScore) {

        boolean bHighScore = false;
        int i = 0;
        for (i = 0; i < HIGH_SCORES; i++) {
            if (newScore > topScores[i]) {
                bHighScore = true;
                break;
            }
        }
        return bHighScore;
    }

    /**
     * This method updates the top scores.
     *
     * @param newScore new high score. 
     */
    public void updateHighScores(int newScore) {

        boolean updateScore = false;
        int i = 0;
        for (i = 0; i < HIGH_SCORES; i++) {
            if (newScore > topScores[i]) {
                updateScore = true;
                break;
            }
        }

        // if we got a new high score
        if (updateScore) {

            for (int j = HIGH_SCORES - 1; j >= i; j--) {
                if (j == i) {
                    topScorers[j] = playerName;
                    topScores[j] = newScore;
                } else {
                    topScorers[j] = topScorers[j - 1];
                    topScores[j] = topScores[j - 1];
                }
            }

            updateHighScoresGrid();
            try {
                // do not append, rather overwrite
                FileWriter writer = new FileWriter("tetrisScores.txt", false);
                PrintWriter printWriter = new PrintWriter(writer);

                for (int k = 0; k < HIGH_SCORES; k++) {
                    if (!topScorers[k].equals("")) {
                        printWriter.print(topScorers[k] + " ");
                        printWriter.print(topScores[k] + " ");
                    } else {
                        break;
                    }
                }
                printWriter.close();
                writer.close();
            } catch (Exception e) {
                System.out.println("updateTopScores() failed");
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * This method initializes the tetrisGrid, game pause and game over controls.
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
     * Creates or resets variables needed to start a new game.
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

    /**
     * This method initializes the blank Tetris board.
     */
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
        if (!tetrisBoard.isGameOver()) {
            // Updates the speed of the falling block if the speed changed
            if (shapeSpeed != tetrisBoard.getTimeToMove()) {
                shapeTransition.stop();
                createTransition();
            }
            // Updates the game score
            scoreValue.setText(Integer.toString(tetrisBoard.getScore()));
            lineValue.setText(Integer.toString(tetrisBoard.getLinesCleared()));
            levelValue.setText(Integer.toString(tetrisBoard.getLevel()));
            points = tetrisBoard.getPoints();
            tetrisGrid.getChildren().clear();
            // Iterates through each cell in the tetrisGrid
            for (int i = 0; i < TetrisBoard.HEIGHT; i++) {
                for (int j = 0; j < TetrisBoard.WIDTH; j++) {
                    currentPoint = new Point(j, i);
                    cellGroup = new Group();
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
            btnStart.setText("Start New Game");
            gameOver = true;
            running = false;
            shapeTransition.stop();
            gameOverScore.setText("\nYou scored " + tetrisBoard.getScore() 
                    + " points\nand reached level "    + tetrisBoard.getLevel() + "!");
            stackPane.getChildren().addAll(tetrisBoardShade, gameOverPane);
            // show congratulations message if a new high score is achieved.
            if (gotHighScore(tetrisBoard.getScore())) {
                gameOverTitle.setText("Congratulations!!");
                updateHighScores(tetrisBoard.getScore());
            } else {
                gameOverTitle.setText("GAME OVER");
            }
        }
    }
}

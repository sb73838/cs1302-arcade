package cs1302.arcade;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The class {@code ReversiScene} represents the custom Scene class for the
 * Reversi game. It contains the actual Reversi game board and the scoring
 * section.
 * 
 * @author Sahil Balhara & Vincent Bui
 */
public class ReversiScene extends Scene {

    private Label scoreLabel;
    private Text turnText;
    private Text passText;
    private Text passMessage;
    private Label player1Label;
    private Label player2Label;
    private Label player1Name;
    private Label player2Name;
    private Label player1Score;
    private Label player2Score;
    private Button btnStart;
    private Button btnPass;

    private GridPane reversiGrid;
    private GridPane scoreGrid;
    private GridPane rightGrid;
    private StackPane stackPane;
    private HBox reversiHomeBox;
    private HBox scoreHBox;

    private Group cellGroup;
    private Rectangle square;
    private Circle circle;
    private StackPane stackCell;

    private BorderPane gameOverPane;
    private VBox gameOverVbox;
    private Label gameOverTitle;
    private Label gameOverScore;
    private Rectangle reversiBoardShade;

    private GridPane menuGrid;
    private Button btnHighScores;
    private Button btnHelp;
    private Button btnQuit;
    private Button btnExit;

    private Stage primaryStage;
    private Scene homeScreen;

    // Black is the first player (1 - Black ; 2 - White )
    private int currentPlayer = 1;

    private boolean player1Pass = false;
    private boolean player2Pass = false;

    private int[][] reversiArray = new int[GRID_SIZE][GRID_SIZE];

    private static final int SQUARE_SIZE = 55;
    private static final int GRID_SIZE = 8;
    private static final int RADIUS = 22;

    /**
     * Constructor of the class.
     * 
     * @param pStage  the primary stage for the application
     * @param hScreen the scene for the home page
     * @param layout  the root layout control for the scene
     * @param width   the preferred width for the scene
     */
    public ReversiScene(Stage pStage, Scene hScreen, VBox layout, double width) {

        super(layout, width, 725, Color.LIGHTBLUE);
        layout.setSpacing(10);
        layout.getStyleClass().add("rBackground");
        this.getStylesheets().add("application.css");
        primaryStage = pStage;
        homeScreen = hScreen;

        // creating HBoxes for Reversi home and score sections
        reversiHomeBox = new HBox(20);
        reversiHomeBox.setPadding(new Insets(10, 20, 5, 20));
        scoreHBox = new HBox(20);
        scoreHBox.setPadding(new Insets(1, 20, 10, 15));

        initializeMenuBar();
        initializeGameOverControls();

        // setup the Reversi Grid and Score Grid
        scoreGridSetup();
        reversiGridSetup();
        rightGridSetup();

        // handling mouse click event on the Reversi grid
        reversiGrid.setOnMouseClicked(e -> {
            int row = (int) ((e.getY() - 4) / (SQUARE_SIZE + 2));
            if (row == GRID_SIZE) {
                row = GRID_SIZE - 1;
            }

            int col = (int) ((e.getX() - 4) / (SQUARE_SIZE + 2));
            if (col == GRID_SIZE) {
                col = GRID_SIZE - 1;
            }

            validMove(row, col, true);
        });

        btnStart.setOnAction(e -> startNewGame());
        btnPass.setOnAction(e -> passButtonHandler());

        // Stack pane allows to overlay layouts for pausing/finishing the game
        stackPane = new StackPane();
        stackPane.getChildren().add(reversiGrid);

        reversiHomeBox.getChildren().addAll(stackPane, rightGrid);
        scoreHBox.getChildren().addAll(scoreGrid);

        layout.getChildren().addAll(menuGrid, reversiHomeBox, scoreHBox);
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
        btnQuit = new Button("Quit Reversi");
        btnExit = new Button("Exit Arcade");

        // setting styling for buttons
        btnHighScores.getStyleClass().add("arcadeButton");
        btnHelp.getStyleClass().add("arcadeButton");
        btnQuit.getStyleClass().add("arcadeButton");
        btnExit.getStyleClass().add("exitButton");

        // setting event handlers for the buttons in the menu bar
        btnQuit.setOnAction(e -> {
            startNewGame();
            primaryStage.setTitle("CS1302-Arcade!");
            primaryStage.setScene(homeScreen);
        });
        btnHelp.setOnAction(e -> displayHelp());
        btnExit.setOnAction(e -> Platform.exit());

        // adding buttons to the menu grid
        menuGrid.add(btnHelp, 0, 0);
        menuGrid.add(btnQuit, 1, 0);
        menuGrid.add(btnExit, 3, 0);
    }

    /**
     * This method displays the Reversi instructions.
     */
    public void displayHelp() {

        Stage helpWindow = new Stage();
        helpWindow.initModality(Modality.APPLICATION_MODAL);
        helpWindow.initOwner(primaryStage);
        helpWindow.setTitle("Instructions");
        helpWindow.setMaxWidth(600);
        helpWindow.setMaxHeight(580);

        // Set position of help window, relative to primary window/stage.
        helpWindow.setX(primaryStage.getX() + 55);
        helpWindow.setY(primaryStage.getY() + 100);
        helpWindow.setResizable(false);

        Label topLabel = new Label("Reversi Instructions: \n\n");
        Label label = new Label("The board is made up of 64 squares in an 8x8 layout "
                + "and there are two human players that take alternate turns.\n\n"
                + "At the beginning of the game, the four starting pieces are placed "
                + "in the center of the board. Two white pieces are placed diagonally "
                + "at the left-top and right-bottom positions and similarly "
                + "two black pieces are placed along the other diagonal. \n\n"
                + "Player 1 is denoted by the black pieces and Player 2 is denoted "
                + "by the white pieces. Player 1 goes first and can put down a black piece "
                + "adjacent to one of Player 2’s pieces or a row of Player 2’s pieces so that "
                + "they are flanked by the newly placed piece and another piece of "
                + "Player 1’s color. All of the opponent’s pieces that are flanked in between "
                + "the new piece and another piece of the current player’s color are flipped "
                + "to the current player’s colors.\n\n"
                + "To place a piece down, the current player left clicks with the mouse on "
                + "the available square where the new token can be played. The game goes back "
                + "and forth between the players after each player places a new token down. "
                + "The game keeps going until neither player has any more legal moves or "
                + "when the game board is full. The player with the most occupied squares "
                + "of their color at the end of the game is the winner.\n");

        Label bottomLabel = new Label("");
        label.setWrapText(true);

        // formatting the lables
        topLabel.setFont(Font.font("segoe ui", FontWeight.NORMAL, 20));
        topLabel.setTextFill(Color.BROWN);
        label.setFont(Font.font("segoe ui", FontWeight.NORMAL, 16));
        label.setTextFill(Color.DARKBLUE);

        // adding all the lables in to a VBox
        VBox helpVBox = new VBox(topLabel, label, bottomLabel);
        helpVBox.setAlignment(Pos.CENTER);
        helpVBox.setPadding(new Insets(20, 20, 20, 20));

        Scene helpScene = new Scene(helpVBox, 600, 580);
        helpWindow.setScene(helpScene);
        helpWindow.sizeToScene();
        helpWindow.show();
    }

    /**
     * Method for determining if the the move is valid for the current player.
     * 
     * @param row         the row number for the square clicked
     * @param col         the column number for the square clicked
     * @param bTakeAction should the in-between squares be flipped
     * @return true if the move is valid
     */
    public boolean validMove(int row, int col, boolean bTakeAction) {

        if (reversiArray[row][col] != 0) {
            return false;
        }

        // using ReversiBoard class to validate the current move
        ReversiBoard reversiBoard = new ReversiBoard(reversiArray, currentPlayer);
        boolean bValid = reversiBoard.isValidMove(row, col, bTakeAction);

        if (bValid && bTakeAction) {
            reversiArray[row][col] = currentPlayer;
            drawGrid();
            player1Pass = false;
            player2Pass = false;

            // update currentPlayer if the move was successful
            if (currentPlayer == 1) {
                currentPlayer = 2;
            } else {
                currentPlayer = 1;
            }
            updateScore();
            passMessage.setText("");
            isGameOver();
        }
        return bValid;
    }

    /**
     * Implements the handler for the Pass button.
     */
    public void passButtonHandler() {
        int nValidMoves = 0;

        // check for valid moves against each entry in reversiArray
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (validMove(i, j, false)) {
                    nValidMoves++;
                }
            }
        }

        // if there are valid moves, do not accept PASS request.
        if (nValidMoves > 0) {
            if (nValidMoves == 1) {
                passMessage.setText("There is " + nValidMoves + " move.\nTry to find that.");
            } else {
                passMessage.setText("There are " + nValidMoves + " moves.\nTry to find one.");
            }
        } else {
            // update currentPlayer & currentOpponent if PASS was successful
            if (currentPlayer == 1) {
                player1Pass = true;
                currentPlayer = 2;
            } else {
                player2Pass = true;
                currentPlayer = 1;
            }
            // update score and check if the game is over
            updateScore();
            isGameOver();
        }
    }

    /**
     * This method sets up the scoring Grid.
     */
    public void scoreGridSetup() {

        // initializing the scoring grid
        scoreGrid = new GridPane();
        scoreGrid.setPrefWidth(400);
        scoreGrid.setPrefHeight(150);
        scoreGrid.getStyleClass().add("greenBorder");

        // setting row and column dimensions (height and width)
        scoreGrid.getColumnConstraints().add(new ColumnConstraints(20));
        scoreGrid.getColumnConstraints().add(new ColumnConstraints(100));
        scoreGrid.getColumnConstraints().add(new ColumnConstraints(110));
        scoreGrid.getColumnConstraints().add(new ColumnConstraints(100));
        scoreGrid.getColumnConstraints().add(new ColumnConstraints(110));

        scoreGrid.getRowConstraints().add(new RowConstraints(40));
        scoreGrid.getRowConstraints().add(new RowConstraints(40));
        scoreGrid.getRowConstraints().add(new RowConstraints(40));

        // setting the players' names and their initial scores
        player1Label = new Label("Player 1:");
        player2Label = new Label("Player 2:");

        player1Name = new Label("Black");
        player2Name = new Label("White");
        player1Score = new Label("2");
        player2Score = new Label("2");

        scoreLabel = new Label("Score");
        scoreLabel.setFont(Font.font("verdana", FontWeight.BOLD, 16));

        player1Label.getStyleClass().add("arcadeScore");
        player2Label.getStyleClass().add("arcadeScore");

        player1Name.getStyleClass().add("arcadeScore");
        player2Name.getStyleClass().add("arcadeScore");
        player1Score.getStyleClass().add("arcadeScore");
        player2Score.getStyleClass().add("arcadeScore");

        // adding the scoring and players' labels and their values
        scoreGrid.add(scoreLabel, 3, 0);

        scoreGrid.add(player1Label, 1, 1);
        scoreGrid.add(player2Label, 1, 2);
        scoreGrid.add(player1Name, 2, 1);
        scoreGrid.add(player2Name, 2, 2);
        scoreGrid.add(player1Score, 3, 1);
        scoreGrid.add(player2Score, 3, 2);
    }

    /**
     * This method sets up the Grid on the right side of Reversi game board. It
     * displays the name of the player whose turn is next. It also has a button to
     * pass the turn and a button to start the new game.
     */
    public void rightGridSetup() {

        // initializing the scoring grid
        rightGrid = new GridPane();

        // setting row and column dimensions (height and width)
        rightGrid.getRowConstraints().add(new RowConstraints(115));
        rightGrid.getRowConstraints().add(new RowConstraints(60));
        rightGrid.getRowConstraints().add(new RowConstraints(50));
        rightGrid.getRowConstraints().add(new RowConstraints(50));
        rightGrid.getRowConstraints().add(new RowConstraints(50));
        rightGrid.getRowConstraints().add(new RowConstraints(90));

        // a button to start a new game
        btnStart = new Button("Start New Game");
        turnText = new Text(player1Name.getText() + "'s turn");
        passText = new Text("If you can not move,\nthen you need to pass \nyour turn.");
        passMessage = new Text("");

        turnText.setFont(Font.font("verdana", FontWeight.SEMI_BOLD, 24));
        turnText.setFill(Color.RED);

        // a message telling the player to pass his turn if he can not move
        passText.setFont(Font.font("verdana", FontWeight.NORMAL, 16));
        passText.setFill(Color.DARKBLUE);

        passMessage.setFont(Font.font("verdana", FontWeight.NORMAL, 16));
        passMessage.setFill(Color.BROWN);

        btnStart = new Button("Start New Game");
        btnPass = new Button("Pass");

        btnStart.getStyleClass().add("startButton");
        btnPass.getStyleClass().add("arcadeButton");

        // adding all the control to the grid pane
        rightGrid.add(turnText, 0, 0);
        rightGrid.add(passText, 0, 1);
        rightGrid.add(btnPass, 0, 2);
        rightGrid.add(passMessage, 0, 3);
        rightGrid.add(btnStart, 0, 5);
    }

    /**
     * This method initializes the Reversi Grid.
     */
    public void reversiGridSetup() {

        reversiGrid = new GridPane();
        reversiGrid.getStyleClass().add("rGrid");
        reversiGrid.getStyleClass().add("tBackground");

        // set each entry in reversiArray to 0
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                reversiArray[i][j] = 0;
            }
        }

        // setting row and column dimensions (height and width)
        for (int i = 0; i < GRID_SIZE; i++) {
            reversiGrid.getColumnConstraints().add(new ColumnConstraints(SQUARE_SIZE));
        }

        for (int i = 0; i < GRID_SIZE; i++) {
            reversiGrid.getRowConstraints().add(new RowConstraints(SQUARE_SIZE));
        }

        // starting position
        reversiArray[3][3] = 2;
        reversiArray[3][4] = 1;
        reversiArray[4][4] = 2;
        reversiArray[4][3] = 1;

        drawGrid();
    }

    /**
     * Draws the Reversi grid reflecting the current state of the game.
     */
    public void drawGrid() {

        // remove all existing controls from the gridPane
        reversiGrid.getChildren().clear();

        // Iterate through each cell in the reversiGrid
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                cellGroup = new Group();
                // Shapes used for each cell
                square = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
                square.setFill(Color.GREEN);

                if (reversiArray[i][j] == 1 || reversiArray[i][j] == 2) {
                    stackCell = new StackPane();
                    circle = new Circle(RADIUS);
                    // Player 1 is Black and Player 2 is White
                    if (reversiArray[i][j] == 1) {
                        circle.setFill(Color.BLACK);
                    } else {
                        circle.setFill(Color.WHITESMOKE);
                    }

                    stackCell.getChildren().addAll(square, circle);
                    cellGroup.getChildren().addAll(stackCell);
                } else {
                    cellGroup.getChildren().addAll(square);
                }

                reversiGrid.add(cellGroup, j, i);
            }
        }
    }

    /**
     * Updates the current scores and the player whose turn is next.
     */
    public void updateScore() {

        int score1 = 0;
        int score2 = 0;

        // get the scores for both the players
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (reversiArray[i][j] == 1) {
                    score1++;
                }
                if (reversiArray[i][j] == 2) {
                    score2++;
                }
            }
        }

        player1Score.setText(Integer.toString(score1));
        player2Score.setText(Integer.toString(score2));

        // update the player whose turn is next
        if (currentPlayer == 1) {
            turnText.setText(player1Name.getText() + "'s turn");
        } else {
            turnText.setText(player2Name.getText() + "'s turn");
        }
    }

    /**
     * Creates or resets variables needed to start a new game.
     */
    public void startNewGame() {

        // set each entry in reversiArray to 0
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                reversiArray[i][j] = 0;
            }
        }

        // starting position
        reversiArray[3][3] = 2;
        reversiArray[3][4] = 1;
        reversiArray[4][4] = 2;
        reversiArray[4][3] = 1;

        // resets the current player
        currentPlayer = 1;
        passMessage.setText("");
        stackPane.getChildren().removeAll(reversiBoardShade, gameOverPane);

        drawGrid();
        updateScore();
        btnPass.setDisable(false);
    }

    /**
     * Checks if the game is over. Game is over when either all the squares are
     * filled or both players have passed their turns (no more valid moves)
     */
    public void isGameOver() {

        int score1 = 0;
        int score2 = 0;
        String winnerMsg = "\n";
        boolean blankSquares = false;

        // get the scores for both the players and check for blank squares
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (reversiArray[i][j] == 1) {
                    score1++;
                }
                if (reversiArray[i][j] == 2) {
                    score2++;
                }
                if (reversiArray[i][j] == 0) {
                    blankSquares = true;
                }
            }
        }

        // If there are no blank squares or both players have passed (no move valid
        // moves)
        if (!blankSquares || (player1Pass && player2Pass)) {

            if (score1 == score2) {
                winnerMsg += "It was a tie " + score1 + "-" + score2 + " !!";
            } else if (score1 > score2) {
                winnerMsg += player1Name.getText() + " won " + score1 + "-" + score2 + " !!";
            } else {
                winnerMsg += player2Name.getText() + " won " + score2 + "-" + score1 + " !!";
            }

            gameOverScore.setText(winnerMsg);
            stackPane.getChildren().addAll(reversiBoardShade, gameOverPane);

            turnText.setText("");
            btnPass.setDisable(true);
        }
    }

    /**
     * Initializes the controls like title, score and shade, that are used when the
     * game is over.
     */
    public void initializeGameOverControls() {

        // initialize the game over controls
        gameOverTitle = new Label("GAME OVER");
        gameOverTitle.setTextFill(Color.YELLOW);
        gameOverTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 48));
        gameOverTitle.setTextAlignment(TextAlignment.CENTER);

        gameOverScore = new Label("");
        gameOverScore.setTextFill(Color.YELLOW);
        gameOverScore.setFont(Font.font("Segoe UI", 36));
        gameOverScore.setTextAlignment(TextAlignment.CENTER);

        // adding a VBox to contain game-over tile and score
        gameOverVbox = new VBox();
        gameOverVbox.getChildren().addAll(gameOverTitle, gameOverScore);
        gameOverVbox.setAlignment(Pos.CENTER);

        gameOverPane = new BorderPane();
        gameOverPane.setCenter(gameOverVbox);

        // creates a shade over the board so that game over controls are visible
        reversiBoardShade = new Rectangle(SQUARE_SIZE * GRID_SIZE + 2 * (GRID_SIZE - 1),
                SQUARE_SIZE * GRID_SIZE + 2 * (GRID_SIZE - 1));
        reversiBoardShade.setOpacity(0.5);
        reversiBoardShade.setFill(Color.BLACK);
        reversiBoardShade.toFront();
    }

}

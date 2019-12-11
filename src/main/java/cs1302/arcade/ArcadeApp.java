package cs1302.arcade;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Application subclass for {@code ArcadeApp}.
 * This class implements the home screen for Arcade App.
 *
 * @author Sahil Balhara & Vincent Bui
 */
public class ArcadeApp extends Application {

    private static final int SCENE_WIDTH = 720;
    private static final int SCENE_HEIGHT = 650;

    private static final int HOME_BUTTON_WIDTH = 300;
    private static final int HOME_BUTTON_HEIGHT = 300;

    private BorderPane borderPane = new BorderPane();
    private VBox layout1 = new VBox();
    private VBox layout2 = new VBox();

    private Scene homeScreen;
    private TetrisScene game1Screen;
    private ReversiScene game2Screen;

    private Text welcomeText;
    private Button buttonTetris;
    private Button buttonReversi;
    private HBox mainWelcomeHBox;
    private GridPane gameGrid;

    private VBox nameBox;
    private Label information;
    private Label nameLabel;
    private TextField userName;

    /** {@inheritDoc} */
    @Override
    public void start(Stage primaryStage) {

        // initializing the various scenes
        homeScreen = new Scene(borderPane, SCENE_WIDTH, SCENE_HEIGHT);
        game1Screen = new TetrisScene(primaryStage, homeScreen, layout1, SCENE_WIDTH);
        game2Screen = new ReversiScene(primaryStage, homeScreen, layout2, SCENE_WIDTH);

        primaryStage.setTitle("CS1302-Arcade!");
        primaryStage.setMaxHeight(720);
        primaryStage.setMaxWidth(1280);
        initialize(primaryStage);

        // Home Page welcome section
        welcomeText = new Text(10, 40, "Welcome to our Arcade!");
        welcomeText.setFont(new Font(40));
        mainWelcomeHBox = new HBox(welcomeText);
        mainWelcomeHBox.setPadding(new Insets(15, 12, 15, 12));
        mainWelcomeHBox.setStyle("-fx-background-color: #336699;");

        // creating a grid pane to hold the two buttons
        gameGrid = new GridPane();
        gameGrid.setHgap(50);
        gameGrid.setVgap(10);
        gameGrid.setPadding(new Insets(50, 30, 50, 30));
        gameGrid.add(buttonTetris, 0, 0);
        gameGrid.add(buttonReversi, 1, 0);

        // adding controls to border pane, the main layout control for the home screen
        borderPane.setTop(mainWelcomeHBox);
        borderPane.setCenter(gameGrid);
        borderPane.setBottom(nameBox);

        primaryStage.setScene(homeScreen);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    /**
     * Initializes controls on the home page.
     *
     * @param pStage primary stage.
     */
    public void initialize(Stage pStage) {

        // A button with the Tetris icon.
        Image imageTetris = new Image("Tetris.jpg");
        buttonTetris = new Button("Tetris", new ImageView(imageTetris));
        buttonTetris.setContentDisplay(ContentDisplay.TOP);
        buttonTetris.setFont(new Font(24));
        buttonTetris.setPrefSize(HOME_BUTTON_WIDTH, HOME_BUTTON_HEIGHT);
        buttonTetris.setOnAction(e -> {

            game1Screen.setPlayerName(userName.getText());
            pStage.setTitle("CS1302-Arcade!   \"You are playing Tetris\"");
            pStage.setScene(game1Screen);
        });

        // A button with the Reversi icon.
        Image imageReversi = new Image("Reversi.jpg");
        buttonReversi = new Button("Reversi", new ImageView(imageReversi));
        buttonReversi.setContentDisplay(ContentDisplay.TOP);
        buttonReversi.setFont(new Font(24));
        buttonReversi.setPrefSize(HOME_BUTTON_WIDTH, HOME_BUTTON_HEIGHT);
        buttonReversi.setOnAction(e -> {

            pStage.setTitle("CS1302-Arcade!   \"You are playing Reversi\"");
            pStage.setScene(game2Screen);
        });

        // initializing controls to ask user name
        nameBox = new VBox();
        nameBox.setPadding(new Insets(10, 20, 20, 20));
        nameBox.setSpacing(10);
        information = new Label("If you want to be included in the high scores table,"
                + " then please enter \nyour name (initials) here," 
                + " otherwise 'guest' will be used by default.");
        information.setFont(Font.font("verdana", FontWeight.NORMAL, 16));
        nameLabel = new Label("Enter name (without spaces):");
        nameLabel.setFont(Font.font("verdana", FontWeight.NORMAL, 16));
        userName = new TextField("guest");
        userName.setMaxWidth(150);
        userName.setFont(Font.font("verdana", FontWeight.NORMAL, 16));
        nameBox.getChildren().addAll(information, nameLabel, userName);
    }
}

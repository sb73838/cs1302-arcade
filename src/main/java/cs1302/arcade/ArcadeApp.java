package cs1302.arcade;

import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ArcadeApp extends Application {

    private static final int SCENE_WIDTH = 700;
    private static final int SCENE_HEIGHT = 500;

    private static final int HOME_BUTTON_WIDTH = 300;
    private static final int HOME_BUTTON_HEIGHT = 300;

    BorderPane borderPane = new BorderPane();
    VBox layout1 = new VBox();
    VBox layout2 = new VBox();

    Scene homeScreen = new Scene(borderPane, SCENE_WIDTH, SCENE_HEIGHT);
    Scene game1Screen;
    Scene game2Screen;
    
    @Override
    public void start(Stage primaryStage) {

        game1Screen = new TetrisScene(primaryStage, homeScreen, layout1, SCENE_WIDTH);
        game2Screen = new ReversiScene(primaryStage, homeScreen, layout2, SCENE_WIDTH);

        primaryStage.setTitle("CS1302-Arcade!");

        // Home Page
        Text text = new Text(10, 40, "Welcome to our Arcade!");
        text.setFont(new Font(40));

        // A button with the Tetris icon.
        Image imageTetris = new Image("Tetris.jpg");
	Button button1 = new Button("Tetris", new ImageView(imageTetris));
        button1.setContentDisplay(ContentDisplay.TOP);
        button1.setFont(new Font(24));
        button1.setPrefSize(HOME_BUTTON_WIDTH, HOME_BUTTON_HEIGHT);
        button1.setOnAction(e -> primaryStage.setScene(game1Screen));

        // A button with the Reversi icon.
        Image imageReversi = new Image("Reversi.jpg");
        Button button2 = new Button("Reversi", new ImageView(imageReversi));
        button2.setContentDisplay(ContentDisplay.TOP);
        button2.setFont(new Font(24));
        button2.setPrefSize(HOME_BUTTON_WIDTH, HOME_BUTTON_HEIGHT);
        button2.setOnAction(e -> primaryStage.setScene(game2Screen));
	
        HBox mainWelcomeHBox = new HBox(text);
        mainWelcomeHBox.setPadding(new Insets(15, 12, 15, 12));
        mainWelcomeHBox.setStyle("-fx-background-color: #336699;");

        GridPane gameGrid = new GridPane();
        gameGrid.setHgap(50);
        gameGrid.setVgap(10);
        gameGrid.setPadding(new Insets(20, 10, 20, 10));

        gameGrid.add(button1, 0, 0);
        gameGrid.add(button2, 1, 0);

        VBox homeLayout = new VBox(mainWelcomeHBox, gameGrid);
        homeLayout.setSpacing(20);

        borderPane.setTop(mainWelcomeHBox);
        borderPane.setCenter(gameGrid);

        primaryStage.setScene(homeScreen);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

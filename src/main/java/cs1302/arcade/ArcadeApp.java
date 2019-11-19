package cs1302.arcade;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    private static final int SCENE_WIDTH = 600;
    private static final int SCENE_HEIGHT = 500;

    BorderPane borderPane = new BorderPane();
    VBox layout1 = new VBox();
    VBox layout2 = new VBox();

    Scene homeScreen = new Scene(borderPane, SCENE_WIDTH, SCENE_HEIGHT);
    Scene game1Screen = new Scene(layout1, SCENE_WIDTH, SCENE_HEIGHT);
    Scene game2Screen = new Scene(layout2, SCENE_WIDTH, SCENE_HEIGHT);

    @Override
    public void start(Stage primaryStage) {

        CustomHBox chBox1 = new CustomHBox(primaryStage, homeScreen, "Tetris");
        CustomHBox chBox2 = new CustomHBox(primaryStage, homeScreen, "Reversi");

        primaryStage.setTitle("CS1302-Arcade!");

        // Game 1
        Label label1 = new Label("Development of Tetris game is in progress...");
        label1.setFont(new Font(24));
        label1.setPadding(new Insets(5, 5, 5, 5));
        layout1.getChildren().addAll(chBox1, label1);
        layout1.setSpacing(30);

        // Game 2
        Label label2 = new Label("Development of Reversi game is in progress...");
        label2.setFont(new Font(24));
        label2.setPadding(new Insets(5, 5, 5, 5));
        layout2.getChildren().addAll(chBox2, label2);
        layout2.setSpacing(30);

        // Home Page
        Text text = new Text(10, 40, "Welcome to our Arcade!");
        text.setFont(new Font(40));

        // A button with the Tetris icon.
        Image imageTetris = new Image("Tetris.jpg");
        Button button1 = new Button("", new ImageView(imageTetris));
        button1.setOnAction(e -> primaryStage.setScene(game1Screen));

        // A button with the Reversi icon.
        Image imageReversi = new Image("Reversi.jpg");
        Button button2 = new Button("", new ImageView(imageReversi));
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

        Label tetrisLabel = new Label("Tetris");
        Label reversiLabel = new Label("Reversi");

        tetrisLabel.setFont(new Font(24));
        reversiLabel.setFont(new Font(24));

        gameGrid.add(tetrisLabel, 0, 1);
        gameGrid.add(reversiLabel, 1, 1);

        GridPane.setHalignment(tetrisLabel, javafx.geometry.HPos.CENTER);
        GridPane.setHalignment(reversiLabel, javafx.geometry.HPos.CENTER);

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

package cs1302.arcade;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TetrisScene extends Scene {
    
    private Label subScore;
    private Label subLine;
    private Label score;
    private Label line;
    private Label level;
    private Label emptyLabel;    
    private Button btnStart;
    private Button btnReset;
    
    public TetrisScene(Stage primaryStage, Scene homeScreen, VBox layout, 
                       double width, double height, Paint fill) {        
        super(layout, width, 610, fill);
        layout.setSpacing(10);
        
        CustomHBox chBox1 = new CustomHBox(primaryStage, homeScreen, "Tetris");
        
        score = new Label("");
        level = new Label("Skill Level:");
        level.setFont(new Font(20));
        line = new Label("");
        emptyLabel = new Label("");
        subScore = new Label("Score");
        subLine = new Label("Lines cleared");
        
        RadioButton radioButton1 = new RadioButton("Beginner");
        RadioButton radioButton2 = new RadioButton("Intermediate");
        RadioButton radioButton3 = new RadioButton("Expert");
        
        ToggleGroup radioGroup = new ToggleGroup();
        
        radioButton1.setToggleGroup(radioGroup);
        radioButton2.setToggleGroup(radioGroup);
        radioButton3.setToggleGroup(radioGroup);
        
        radioButton1.setSelected(true);
        
        VBox vbSkillLevel = new VBox(10, radioButton1, radioButton2, radioButton3);
        
        btnStart = new Button("Start");
        btnReset = new Button("Reset");
        
        btnStart.setStyle("-fx-font: 20 verdana; -fx-base: #b6e7c9;");
        btnReset.setStyle("-fx-font: 20 verdana; -fx-base: #b6e7c9;");
        
        HBox tetrisHomeBox = new HBox(20);
        tetrisHomeBox.setPadding(new Insets(10, 20, 10, 20));
        
        VBox scoreBox = new VBox(20);
        scoreBox.setPrefWidth(200);
        scoreBox.setPrefHeight(600);
        
        scoreBox.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
                          + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
                          + "-fx-border-radius: 5;" + "-fx-border-color: green;");
        
        scoreBox.getChildren().addAll(level, vbSkillLevel, score, subScore, line,
                                      subLine, btnStart, emptyLabel, btnReset);
        
        VBox gameBox = new VBox(new Label("Game Board"));                               
        gameBox.setPrefWidth(400);
        gameBox.setPrefHeight(600);
        gameBox.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
                         + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
                         + "-fx-border-radius: 5;" + "-fx-border-color: blue;");
        
        tetrisHomeBox.getChildren().addAll(scoreBox, gameBox);
        
        layout.getChildren().addAll(chBox1, tetrisHomeBox);
    }
}

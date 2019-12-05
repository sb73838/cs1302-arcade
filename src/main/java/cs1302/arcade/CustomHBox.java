package cs1302.arcade;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CustomHBox extends HBox {

    Button btnHome;
    Button btnHelp;
    Button btnQuit;
    Button btnExit;
    
    public CustomHBox(Stage stage, Scene home, String game) {
           super();
           
           // creating buttons
           btnHome = new Button("Home");
           btnHelp = new Button("Help");
           btnQuit = new Button("Quit " + game);
           btnExit = new Button("Exit");
           
           btnHome.getStyleClass().add("arcadeButton");
           btnHelp.getStyleClass().add("arcadeButton");
           btnQuit.getStyleClass().add("arcadeButton");
           btnExit.getStyleClass().add("arcadeButton");
           
           btnHome.setOnAction(e -> stage.setScene(home));       
           btnQuit.setOnAction(e -> stage.setScene(home));
           btnExit.setOnAction(e -> Platform.exit());
           
           this.setSpacing(50);
           this.setPadding(new Insets(12, 10, 12, 20));
           this.setStyle("-fx-background-color: #336699;");
           
           this.getChildren().addAll(btnHome, btnHelp, btnQuit, btnExit);
        }
}

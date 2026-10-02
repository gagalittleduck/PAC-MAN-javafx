package Model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import interfaces.GameAlert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.Optional;

import static Control.MenuControl.path;

public class GameWin implements GameAlert {
    String message;
    boolean flag = false;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void showGameAlert(BorderPane root, TileMap tilemap) {
        // Constructing Game Over Messages
        String message = "Win! Your score is " + tilemap.getPlayer().getScore();;

        // Create an Alert popup with a confirmation type
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText(null); // Clear default content
        Text winMessage = new Text(message);
        winMessage.setFont(Font.font("Arial", 15)); // Setting fonts
        winMessage.setStyle("-fx-fill: #0bb6da; -fx-font-weight: bold;");

        StackPane centerPane = new StackPane(winMessage);
        centerPane.setStyle("-fx-alignment: center; -fx-padding: 20;"); // Ensure centering and margins
        alert.getDialogPane().setContent(centerPane);

        // the overall style of DialogPane
        alert.getDialogPane().setStyle("-fx-background-color: #ffffff; -fx-border-color: #b105fa; -fx-border-width: 5px;");

        // Load and set an image as the graphic for the popup window
        Image gameOverImage;
        try {
            gameOverImage = new Image(path+"/win.png");
        } catch (Exception e) {
            // If the image fails to load, it can be set to null or the default graphic
            gameOverImage = null;
            System.err.println("Unable to load image: " + e.getMessage());
        }

        if (gameOverImage != null) {
            ImageView gameOverImageView = new ImageView(gameOverImage);
            gameOverImageView.setFitWidth(200); // Resize as needed
            gameOverImageView.setFitHeight(200);
            gameOverImageView.setPreserveRatio(true); // Maintaining ratios
            alert.setGraphic(gameOverImageView);
        }

        // Customized button types
        ButtonType restartButton = new ButtonType("Menu", ButtonBar.ButtonData.OK_DONE);
        ButtonType exitButton = new ButtonType("Exit", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Set the popup owner to ensure that the popup is above the current window
        Window owner = root.getScene().getWindow();
        alert.initOwner(owner);

        // Setting the popup button
        alert.getButtonTypes().setAll(restartButton, exitButton);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.initStyle(StageStyle.UNDECORATED);
        // Display a popup window and wait for the user to respond
        Optional<ButtonType> result = alert.showAndWait();

        // Returns a Boolean value based on the user's selection
        if (result.isPresent() && result.get() == restartButton) {
            setFlag(true);
        } else {
            setFlag(false);
        }
    }
}


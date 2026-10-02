package Control;

import Model.WavPlay;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

import static Control.MenuControl.path;
/**
 * This class controls the Tips page of the application.
 * It handles the display of tips using a ScrollPane and an ImageView,
 * and manages navigation back to the main menu.
 */
public class TipsControl implements Initializable{
    @FXML
    Button backButton;
    @FXML
    private ScrollPane imageScrollPane;
    @FXML
    ImageView tipsImage;
    // Method to update the tips image displayed
    public void changetips(){
        Image tips = new Image(path+"/tips_page.png");
        if(tipsImage!=null){
            Platform.runLater(() -> {
                tipsImage.setImage(tips);
            });
        }
    }
    /**
     * Initializes the TipsControl class.
     * Sets up the back button's hover effects, loads the tips image, and configures the ScrollPane.
     *
     * @param url The location used to resolve relative paths for the root object, or null if not applicable.
     * @param resourceBundle The resources used to localize the root object, or null if not applicable.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        backButton.setOnMouseEntered(event -> {
            // Change the button style when the mouse enters
            backButton.setStyle("-fx-background-color: #60dfb3;"
                    + "-fx-padding: 8;"
                    + "-fx-font-size: 20px;"
                    + "-fx-text-fill: #eee;");
            WavPlay menuwav=new WavPlay("src/main/resources/music/press.wav");
            menuwav.play();
        });
        backButton.setOnMouseExited(event -> {
            // Restore button style on mouse off
            backButton.setStyle("-fx-background-color: white;"
                    + "-fx-padding: 8;"
                    + "-fx-font-size: 16px;"
                    + "-fx-text-fill: black;");
        });
        changetips();
        tipsImage.setPreserveRatio(true);
        // Setting ScrollPane Properties
        imageScrollPane.setPannable(true); // Allow drag scrolling
    }
    /**
     * Handles the action for navigating back to the main menu.
     * Triggered when the back button is clicked.
     *
     * @param event The ActionEvent triggered by clicking the back button.
     */
    @FXML
    public void backmenu(ActionEvent event) {
        // Navigate back to the main menu
         Menupage.showMenu();
    }
}

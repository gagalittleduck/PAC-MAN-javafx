package Control;

import Model.WavPlay;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import static Control.MapDesignControl.self_design;

/**
 * Controls the main menu, providing navigation to various parts of the application.
 */
public class MenuControl implements Initializable {
    public static String path="icons";
    private SettingControl settingControl;
    private TipsControl tipsControl;
    private MapDesignControl mapControl;
    @FXML ImageView MainImage;
    @FXML Button GameStartButton;
    @FXML Button leftButton;
    @FXML Button rightButton;
    @FXML Button settingButton;
    @FXML Button tipsButton;
    @FXML ImageView left;
    @FXML ImageView right;
    @FXML ImageView set;
    @FXML ImageView tip;
    @FXML ImageView newmapImage;
    @FXML VBox MainVBox;
    @FXML Button newmapButton;
    /**
     * Initializes the menu controller.
     *
     * @param url the location of the FXML file.
     * @param resourceBundle the resources used by the FXML file.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        leftButton.setOnMouseEntered(event -> {
            //Mouse over to change appearance
            leftButton.setStyle("-fx-background-color: #60dfb3;"
                    + "-fx-padding: 8;"
                    + "-fx-font-size: 20px;"
                    + "-fx-text-fill: #eee;");
            WavPlay menuwav=new WavPlay("src/main/resources/music/press.wav");
            menuwav.play();
        });
        leftButton.setOnMouseExited(event -> {
            // Restore button style on mouse off
            leftButton.setStyle("-fx-background-color: transparent;"
                    + "-fx-padding: 8;"
                    + "-fx-font-size: 16px;"
                    + "-fx-text-fill: black;");
        });
        rightButton.setOnMouseEntered(event -> {
            //Mouse over to change appearance
            rightButton.setStyle("-fx-background-color: #60dfb3;"
                    + "-fx-padding: 8;"
                    + "-fx-font-size: 20px;"
                    + "-fx-text-fill: #eee;");
            WavPlay menuwav=new WavPlay("src/main/resources/music/press.wav");
            menuwav.play();
        });
        rightButton.setOnMouseExited(event -> {
            // Restore button style on mouse off
            rightButton.setStyle("-fx-background-color: transparent;"
                    + "-fx-padding: 8;"
                    + "-fx-font-size: 16px;"
                    + "-fx-text-fill: black;");
        });
        GameStartButton.setOnMouseEntered(event -> {
            //Mouse over to change appearance
            GameStartButton.setStyle("-fx-background-color: #60dfb3;"
                    + "-fx-padding: 8;"
                    + "-fx-font-size: 26px;"
                    + "-fx-text-fill: #eee;");
            WavPlay menuwav=new WavPlay("src/main/resources/music/press.wav");
            menuwav.play();
        });
        GameStartButton.setOnMouseExited(event -> {
            // Restore button style on mouse off
            GameStartButton.setStyle("-fx-background-color: #ec4b4b;"
                    + "-fx-padding: 8;"
                    + "-fx-font-size: 20px;"
                    + "-fx-text-fill: black;");
        });
        settingButton.setOnMouseEntered(event -> {
            //Mouse over to change appearance
            settingButton.setStyle("-fx-background-color: #60dfb3;"
                    + "-fx-padding: 8;"
                    + "-fx-font-size: 20px;"
                    + "-fx-text-fill: #eee;");
            WavPlay menuwav=new WavPlay("src/main/resources/music/press.wav");
            menuwav.play();
        });
        settingButton.setOnMouseExited(event -> {
            // Restore button style on mouse off
            settingButton.setStyle("-fx-background-color: white;"
                    + "-fx-padding: 8;"
                    + "-fx-font-size: 16px;"
                    + "-fx-text-fill: black;");
        });
        tipsButton.setOnMouseEntered(event -> {
            //Mouse over to change appearance
            tipsButton.setStyle("-fx-background-color: #60dfb3;"
                    + "-fx-padding: 8;"
                    + "-fx-font-size: 20px;"
                    + "-fx-text-fill: #eee;");
            WavPlay menuwav=new WavPlay("src/main/resources/music/press.wav");
            menuwav.play();
        });
        tipsButton.setOnMouseExited(event -> {
            // Restore button style on mouse off
            tipsButton.setStyle("-fx-background-color: white;"
                    + "-fx-padding: 8;"
                    + "-fx-font-size: 16px;"
                    + "-fx-text-fill: black;");
        });
        newmapButton.setOnMouseEntered(event -> {
            //Mouse over to change appearance
            newmapButton.setStyle("-fx-background-color: #60dfb3;"
                    + "-fx-padding: 8;"
                    + "-fx-font-size: 20px;"
                    + "-fx-text-fill: #eee;");
            WavPlay menuwav=new WavPlay("src/main/resources/music/press.wav");
            menuwav.play();
        });
        newmapButton.setOnMouseExited(event -> {
            // Restore button style on mouse off
            newmapButton.setStyle("-fx-background-color: white;"
                    + "-fx-padding: 8;"
                    + "-fx-font-size: 16px;"
                    + "-fx-text-fill: black;");
        });
    }
    /**
     * Handles the action of starting a new game.
     *
     * @param event the action event triggered by the user.
     */
    @FXML
    public void startgame(ActionEvent event) throws IOException {

        // Check if `settingControl` is null; if it is, create a new `SettingControl` object.
        // Otherwise, use the existing `settingControl` instance.
        SettingControl settings = this.settingControl == null ? new SettingControl() : this.settingControl;

        // (1) Loading new FXML layouts via FXMLLoader
        // Load the `Game.fxml` layout file to create the game UI.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Game.fxml"));
        Parent root = loader.load(); // Load the root node of the FXML file

        // Set the resource path and configure the background style if the path matches "images"
        if (path == "images") {
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #a206fd, #40f404);");
            // Use a gradient background with colors transitioning from purple to green
        }

        // Create a new Scene with the loaded UI and display it using the Menupage class
        Scene scene = new Scene(root);
        Menupage.showMap(scene); // Show the new scene with the game map

        // Retrieve the `GameControl` controller from the FXML loader
        GameControl gameControl = loader.getController();

        // Pass the `settings` object to the controller's initialize method to set up game configurations
        gameControl.initialize(settings);


    }
    /**
     * Sets the setting control instance.
     *
     * @param sc the SettingControl instance to set.
     */
    public void setSettingControl(SettingControl sc) {
        this.settingControl = sc;
    }
    /**
     * Switches the theme to the right-hand design.
     *
     * @param event the action event triggered by the user.
     */
    @FXML
    public void rightTheme(ActionEvent event) {
        //If the right triangle is clicked, use the new skin
        Image newImage = new Image("images/mainpage.png");
        Image rightImage = new Image("icons/right2_image.png");
        Image leftImage = new Image("icons/left_image.png");
        left.setImage(leftImage);
        right.setImage(rightImage);
        Image setImage = new Image("images/setting.png");
        set.setImage(setImage);
        Image tipsImage = new Image("images/tips.png");
        tip.setImage(tipsImage);
        Image designImage = new Image("images/design.png");
        newmapImage.setImage(designImage);
        path="images";
        MainImage.setImage(newImage);
        if(tipsControl!=null){
            tipsControl.changetips();
        }
    }
    /**
     * Navigates to the map design screen.
     *
     * @param event the action event triggered by the user.
     */
    @FXML
    public void newmap(ActionEvent event) throws IOException {
        Menupage.showMapDesign();
    }
    /**
     * Switches the theme to the left-hand design.
     *
     * @param event the action event triggered by the user.
     */
    @FXML
    public void leftTheme(ActionEvent event) {
        // If the left triangle is clicked, use the original skin
        path="icons";
        Image leftImage = new Image("icons/left2_image.png");
        left.setImage(leftImage);
        Image rightImage = new Image("icons/right_image.png");
        right.setImage(rightImage);
        Image setImage = new Image("icons/setting.png");
        set.setImage(setImage);
        Image tipsImage = new Image("icons/tips.png");
        tip.setImage(tipsImage);
        Image designImage = new Image("icons/design.png");
        newmapImage.setImage(designImage);
        Image newImage = new Image("icons/mainpage.png");
        MainImage.setImage(newImage);
        if(tipsControl!=null){
            tipsControl.changetips();
        }
    }

    /**
     * Navigates to the tips menu.
     *
     * @param event the action event triggered by the user.
     */
    @FXML
    private void tips(ActionEvent event) throws IOException {
        // If the tips button is clicked, switch to the tips screen.
        Menupage.showTips();
    }
    /**
     * Navigates to the settings menu.
     *
     * @param event the action event triggered by the user.
     */
    @FXML
    public void setting(ActionEvent event) {
        // If the setting button is clicked, switch to the setting screen.
        Boolean inited=false;
        if(self_design){
            inited=true;
        }
        Menupage.showSetting(inited);
    }

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        MenuControl.path = path;
    }

    public SettingControl getSettingControl() {
        return settingControl;
    }

    public TipsControl getTipsControl() {
        return tipsControl;
    }

    public void setTipsControl(TipsControl tipsControl) {
        this.tipsControl = tipsControl;
    }

    public MapDesignControl getMapControl() {
        return mapControl;
    }

    public void setMapControl(MapDesignControl mapControl) {
        this.mapControl = mapControl;
    }

    public ImageView getMainImage() {
        return MainImage;
    }

    public void setMainImage(ImageView mainImage) {
        MainImage = mainImage;
    }

    public Button getGameStartButton() {
        return GameStartButton;
    }

    public void setGameStartButton(Button gameStartButton) {
        GameStartButton = gameStartButton;
    }

    public Button getLeftButton() {
        return leftButton;
    }

    public void setLeftButton(Button leftButton) {
        this.leftButton = leftButton;
    }

    public Button getRightButton() {
        return rightButton;
    }

    public void setRightButton(Button rightButton) {
        this.rightButton = rightButton;
    }

    public Button getSettingButton() {
        return settingButton;
    }

    public void setSettingButton(Button settingButton) {
        this.settingButton = settingButton;
    }

    public Button getTipsButton() {
        return tipsButton;
    }

    public void setTipsButton(Button tipsButton) {
        this.tipsButton = tipsButton;
    }

    public ImageView getLeft() {
        return left;
    }

    public void setLeft(ImageView left) {
        this.left = left;
    }

    public ImageView getRight() {
        return right;
    }

    public void setRight(ImageView right) {
        this.right = right;
    }

    public ImageView getSet() {
        return set;
    }

    public void setSet(ImageView set) {
        this.set = set;
    }

    public ImageView getTip() {
        return tip;
    }

    public void setTip(ImageView tip) {
        this.tip = tip;
    }

    public VBox getMainVBox() {
        return MainVBox;
    }

    public void setMainVBox(VBox mainVBox) {
        MainVBox = mainVBox;
    }

    public Button getNewmapButton() {
        return newmapButton;
    }

    public void setNewmapButton(Button newmapButton) {
        this.newmapButton = newmapButton;
    }
}

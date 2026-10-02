package Control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * The Menupage class serves as the main controller for the application's stages and scenes.
 * It manages transitions between the menu, settings, tips, and map design scenes.
 */
public class Menupage extends Application {
    private static Stage mainStage;   // Main stage for the entire application
    private static Scene menuScene;
    private static Scene settingScene;
    private static Scene tipsScene;
    private static Scene newmapScene;
    private static SettingControl settingControl;
    private static TipsControl tipsControl;
    private static MenuControl menuControl;
    private static MapDesignControl newmapControl;
    /**
     * Displays the main menu scene.
     */
    public static void showMenu() {
        mainStage.setScene(menuScene);
    }
    /**
     * Displays the settings page scene. Updates settings based on initialization state.
     *
     * @param inited A boolean indicating whether the settings page is initialized.
     */
    public static void showSetting(Boolean inited) {
        if(inited) {
            settingControl.getCustom().setSelected(true);
            settingControl.getLevel_tip().setText("Last modified map used!");
           settingControl.getLevel_tip().setStyle("-fx-font-size: 20;-fx-text-fill: red");
        }
        mainStage.setScene(settingScene);
    }
    /**
     * Displays the tips page scene. Loads the associated FXML file.
     *
     * @throws IOException If the FXML file cannot be loaded.
     */
    public static void showTips() throws IOException {
        FXMLLoader tipsLoader = new FXMLLoader(Menupage.class.getResource("/Tips.fxml"));
        tipsScene = new Scene(tipsLoader.load());
        tipsControl = tipsLoader.getController();
        mainStage.setScene(tipsScene);
    }
    /**
     * Displays the map design page scene. Loads the associated FXML file.
     * Links the map design controller to the menu controller.
     *
     * @throws IOException If the FXML file cannot be loaded.
     */
    public static void showMapDesign() throws IOException {
        FXMLLoader newmapLoader = new FXMLLoader(Menupage.class.getResource("/MapDesign.fxml"));
        newmapScene = new Scene(newmapLoader.load());
        newmapControl = newmapLoader.getController();
        menuControl.setMapControl(newmapControl);
        mainStage.setScene(newmapScene);
    }
    /**
     * Sets the provided scene on the main stage.
     *
     * @param scene The scene to be displayed.
     */
    public static void showMap(Scene scene) {
        mainStage.setScene(scene);
    }
    /**
     * Initializes and starts the JavaFX application.
     * Preloads the menu and settings scenes for smoother transitions.
     *
     * @param primaryStage The primary stage for this application.
     * @throws Exception If there is an error during initialization.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.mainStage=primaryStage;
        // 1) Preload Menu.fxml
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/Menu.fxml"));
        menuScene = new Scene(menuLoader.load());
        menuControl = menuLoader.getController();

        // 2) Preload Setting.fxml
        FXMLLoader settingLoader = new FXMLLoader(getClass().getResource("/Setting.fxml"));
        settingScene = new Scene(settingLoader.load());
        settingControl = settingLoader.getController();

        // 3) Pass SettingScene to SettingControl.
        settingControl.setSettingScene(settingScene);

        // 4) Save the SettingControl for easy access.
        menuControl.setSettingControl(settingControl);

        primaryStage.setTitle("PAC MAN");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }
    /**
     * The main entry point of the application. Launches the JavaFX application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

}

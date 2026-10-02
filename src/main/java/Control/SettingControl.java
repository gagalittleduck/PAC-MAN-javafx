package Control;

import Model.WavPlay;
import interfaces.DirectionMap;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static Control.MapDesignControl.newmap;
import static Control.MapDesignControl.self_design;
import static Control.MenuControl.path;

public class SettingControl implements Initializable, DirectionMap {
    @FXML
    private RadioButton simple;   // fx:id="simple"
    @FXML
    private RadioButton middle;   // fx:id="middle"
    @FXML
    private RadioButton hard;     // fx:id="hard"
    @FXML
    private RadioButton custom;     // fx:id="hard"
    @FXML
    private Label level_tip;
    @FXML
    private ToggleButton soundEffectToggle; //
    @FXML
    private ToggleGroup levelToggleGroup;
    @FXML
    private Slider PlayerBar;     // fx:id="PlayerBar"
    @FXML
    private Slider GhostBar;      // fx:id="GhostBar"
    @FXML
    private Slider volumeBar;      // fx:id="musicBar"
    @FXML
    private Button backButton;

    private boolean musicopen = true;
    private double ghostspeed = 5;
    private double playerspeed = 5;
    private int musicVolume = 50;
    private int level = 3;
    private Scene settingScene;
    String filePath = "src/main/resources/music/background.wav";
    File audioFile = new File(filePath);
    AudioInputStream audioStream = null;
    Clip audioClip = null;
    Boolean self_designed=false;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the self-designed flag based on the external variable
        this.self_designed = self_design;

        // Add listeners to the difficulty level radio buttons
        simple.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Clear the level tip when "Simple" is selected
                Platform.runLater(() -> level_tip.setText(""));
            }
        });
        middle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Clear the level tip when "Middle" is selected
                Platform.runLater(() -> level_tip.setText(""));
            }
        });
        hard.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // Clear the level tip when "Hard" is selected
                Platform.runLater(() -> level_tip.setText(""));
            }
        });

        // Add hover effects to the back button
        backButton.setOnMouseEntered(event -> {
            // Change button style when the mouse hovers over it
            backButton.setStyle("-fx-background-color: #60dfb3; -fx-padding: 8; -fx-font-size: 20px; -fx-text-fill: #eee;");
            // Play hover sound effect
            WavPlay menuwav = new WavPlay("src/main/resources/music/press.wav");
            menuwav.play();
        });

        backButton.setOnMouseExited(event -> {
            // Restore button style when the mouse leaves
            backButton.setStyle("-fx-background-color: white; -fx-padding: 8; -fx-font-size: 16px; -fx-text-fill: black;");
        });

        try {
            // Initialize audio playback for background music
            audioStream = AudioSystem.getAudioInputStream(audioFile);
            audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            audioClip.start();
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);

            // Adjust the initial volume to 50%
            FloatControl volumeControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeBar.setValue(50);
            volumeControl.setValue((float) (Math.log(50 / 100.0) * 20));

            // Dynamically adjust volume when the slider value changes
            volumeBar.valueProperty().addListener((obs, oldVal, newVal) -> {
                double volume = newVal.doubleValue() / 100.0;
                float dB = (float) (Math.log(volume) * 20); // Convert volume to decibels
                volumeControl.setValue(dB);
            });
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Audio format not supported: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Audio file read failure: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.out.println("Audio resources not available: " + e.getMessage());
        }
    }

    @FXML
    public void backmenu(ActionEvent event) throws IOException {
        // Save current settings and navigate back to the main menu
        level = getSelectedLevel(); // Get selected difficulty level
        playerspeed = Playerspeed(); // Get player speed
        ghostspeed = Ghostspeed(); // Get ghost speed
        musicVolume = (int) volumeBar.getValue(); // Save the current music volume
        musicopen = soundEffectToggle.getText().equals("ON"); // Check if the sound effect toggle is ON

        Menupage.showMenu(); // Return to the menu page
    }

    private double Playerspeed() {
        // Map slider values to player speed
        switch ((int) PlayerBar.getValue()) {
            case 0: return 0.0;
            case 1: return 2.0;
            case 2: return 4.0;
            case 3: return 5.0;
            case 4: return 10.0;
            default: return 5.0;
        }
    }

    private double Ghostspeed() {
        // Map slider values to ghost speed
        switch ((int) GhostBar.getValue()) {
            case 0: return 0.0;
            case 1: return 2.0;
            case 2: return 4.0;
            case 3: return 5.0;
            case 4: return 10.0;
            default: return 5.0;
        }
    }

    public void setSettingScene(Scene scene) {
        // Set the scene for the settings menu
        this.settingScene = scene;
    }

    public Scene getSettingScene() {
        // Retrieve the scene for the settings menu
        return settingScene;
    }

    public int getSelectedLevel() {
        // Determine the selected difficulty level
        if (simple.isSelected()) {
            self_design = false; // Simple mode disables self-design
            return SIMPLE;
        } else if (middle.isSelected()) {
            self_design = false; // Middle mode disables self-design
            return MIDDLE;
        } else if (hard.isSelected()) {
            self_design = false; // Hard mode disables self-design
            return HARD;
        } else if (custom.isSelected()) {
            if (newmap != null) {
                self_design = true; // Enable self-design if custom is selected and a map exists
            }
            return HARD; // Use hard as the base for custom
        }
        return SIMPLE; // Default to SIMPLE mode
    }

    @FXML
    public void custom_selected(ActionEvent event) throws IOException {
        // Handle custom level selection
        if (newmap == null && !self_design) {
            Platform.runLater(() -> {
                custom.setSelected(false);
                hard.setSelected(true);
                level_tip.setText("");
            });
            Menupage.showMapDesign(); // Navigate to the map design page
        } else {
            self_design = true; // Enable self-design mode
            custom.setSelected(true);
            Platform.runLater(() -> {
                level_tip.setText("Last modified map used!");
                level_tip.setStyle("-fx-font-size: 20; -fx-text-fill: red;");
            });
        }
    }

    @FXML
    public void music(ActionEvent event) {
        // Toggle sound effects
        boolean isSelected = soundEffectToggle.isSelected();
        if (isSelected) {
            soundEffectToggle.setText("OFF"); // Update toggle label to OFF
            System.out.println("Sound Effect is OFF");
            if (audioClip != null) {
                audioClip.stop(); // Stop the audio clip
                audioStream = null;
                audioClip = null;
            }
        } else {
            soundEffectToggle.setText("ON"); // Update toggle label to ON
            System.out.println("Sound Effect is ON");

            // Restart audio playback
            try {
                audioStream = AudioSystem.getAudioInputStream(audioFile);
                audioClip = AudioSystem.getClip();
                audioClip.open(audioStream);
                audioClip.start();
                audioClip.loop(Clip.LOOP_CONTINUOUSLY);
            } catch (UnsupportedAudioFileException e) {
                System.out.println("Audio format not supported: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("Audio file read failure: " + e.getMessage());
            } catch (LineUnavailableException e) {
                System.out.println("Audio resources not available: " + e.getMessage());
            }
        }
    }

    public RadioButton getSimple() {
        return simple;
    }

    public void setSimple(RadioButton simple) {
        this.simple = simple;
    }

    public RadioButton getMiddle() {
        return middle;
    }

    public void setMiddle(RadioButton middle) {
        this.middle = middle;
    }

    public RadioButton getCustom() {
        return custom;
    }

    public void setCustom(RadioButton custom) {
        this.custom = custom;
    }

    public Label getLevel_tip() {
        return level_tip;
    }

    public void setLevel_tip(Label level_tip) {
        this.level_tip = level_tip;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public File getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(File audioFile) {
        this.audioFile = audioFile;
    }

    public AudioInputStream getAudioStream() {
        return audioStream;
    }

    public void setAudioStream(AudioInputStream audioStream) {
        this.audioStream = audioStream;
    }

    public Clip getAudioClip() {
        return audioClip;
    }

    public void setAudioClip(Clip audioClip) {
        this.audioClip = audioClip;
    }

    public Boolean getSelf_designed() {
        return self_designed;
    }

    public void setSelf_designed(Boolean self_designed) {
        this.self_designed = self_designed;
    }

    public RadioButton getHard() {
        return hard;
    }

    public void setHard(RadioButton hard) {
        this.hard = hard;
    }

    public ToggleButton getSoundEffectToggle() {
        return soundEffectToggle;
    }

    public void setSoundEffectToggle(ToggleButton soundEffectToggle) {
        this.soundEffectToggle = soundEffectToggle;
    }

    public ToggleGroup getLevelToggleGroup() {
        return levelToggleGroup;
    }

    public void setLevelToggleGroup(ToggleGroup levelToggleGroup) {
        this.levelToggleGroup = levelToggleGroup;
    }

    public Slider getPlayerBar() {
        return PlayerBar;
    }

    public void setPlayerBar(Slider playerBar) {
        PlayerBar = playerBar;
    }

    public Slider getGhostBar() {
        return GhostBar;
    }

    public void setGhostBar(Slider ghostBar) {
        GhostBar = ghostBar;
    }

    public Slider getVolumeBar() {
        return volumeBar;
    }

    public void setVolumeBar(Slider volumeBar) {
        this.volumeBar = volumeBar;
    }

    public boolean isMusicopen() {
        return musicopen;
    }

    public void setMusicopen(boolean musicopen) {
        this.musicopen = musicopen;
    }

    public double getGhostspeed() {
        return ghostspeed;
    }

    public void setGhostspeed(double ghostspeed) {
        this.ghostspeed = ghostspeed;
    }

    public double getPlayerspeed() {
        return playerspeed;
    }

    public void setPlayerspeed(double playerspeed) {
        this.playerspeed = playerspeed;
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(int musicVolume) {
        this.musicVolume = musicVolume;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Button getBackButton() {
        return backButton;
    }

    public void setBackButton(Button backButton) {
        this.backButton = backButton;
    }
}

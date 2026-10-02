/**
 * Handles the game logic, rendering, and user interactions during gameplay.
 */
package Control;

import Model.*;
import interfaces.DirectionMap;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import static Control.MapDesignControl.self_design;
import static javafx.application.Platform.exit;

/**
 * Controls the main gameplay, managing player, ghosts, and map interactions.
 */
public class GameControl implements DirectionMap {
    @FXML
    private Label scoreLabel;
    @FXML
    private BorderPane root;
    @FXML
    private Canvas tileMapCanvas;

    // Declare the classes and variables being used
    private GameOver gameOver;
    private GameWin gameWin;
    private TileMap tileMap;
    private int player_speed = 5;
    private int ghost_speed = 5;
    private Player player;
    private AnimationTimer gameloop;
    private boolean paused = false;
    private SettingControl settingControl;
    private Boolean self_designed = false;

    /**
     * Audio resource for the end state.
     */
    private WavPlay winWav = new WavPlay("src/main/resources/music/win.wav");
    private WavPlay gameoverWav = new WavPlay("src/main/resources/music/gameover.wav");

    /**
     * Initializes the game controller with the provided settings.
     *
     * @param settingControl The settings for player speed, ghost speed, and level.
     */
    @FXML
    public void initialize(SettingControl settingControl) {
        this.settingControl = settingControl;
        this.self_designed = self_design;
        handleStartButtonClick(settingControl.getPlayerspeed(), settingControl.getGhostspeed(), settingControl.getLevel());
        // Make the canvas focusable for key input
        tileMapCanvas.setFocusTraversable(true);
        // Add key event listener for player controls
        tileMapCanvas.setOnKeyPressed(event -> handleKeyPressed(event));
    }

    /**
     * Renders the current game map on the canvas.
     *
     * @param gImage The graphics context for rendering.
     */
    private void render(GraphicsContext gImage) {
        tileMap.drawMap(gImage);
    }

    /**
     * Draws the game map and updates the player's score.
     */
    private void drawTileMap() {
        GraphicsContext gc = tileMapCanvas.getGraphicsContext2D();
        // Clear the canvas
        gc.clearRect(0, 0, tileMapCanvas.getWidth(), tileMapCanvas.getHeight());
        // Render the current game state
        render(gc);
        // Update the player's score
        if (player != null) {
            scoreLabel.setText("Score: " + player.getScore());
        }
    }

    /**
     * Handles the start button click to initialize the game loop and map.
     *
     * @param player_speed The speed of the player.
     * @param ghost_speed The speed of the ghosts.
     * @param target_level The target level for the game.
     */
    private void handleStartButtonClick(double player_speed, double ghost_speed, int target_level) {
        tileMap = new TileMap(target_level, player_speed, ghost_speed, self_design);
        tileMap.updateTileMap();
        player = tileMap.getPlayer();

        // Initialize the game loop running at 60 FPS
        gameloop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                drawTileMap();
                player.GetBean();
                player.GetKey();
                player.GetIceBean();
                player.move();
                for (Ghost ghost : tileMap.getGhosts()) {
                    if (!ghost.getFrozen()) {
                        ghost.move();
                    }
                }
                if (player.dead()) {
                    handleGameOver();
                }
                if (player.getinGate()) {
                    handleGameWin();
                }
            }
        };
        gameloop.start();
    }

    /**
     * Handles key press events for player movement and actions.
     *
     * @param event The key event triggered by the player.
     */
    private void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case UP -> player.checkDirection(DIRECTION_UP);
            case DOWN -> player.checkDirection(DIRECTION_DOWN);
            case LEFT -> player.checkDirection(DIRECTION_LEFT);
            case RIGHT -> player.checkDirection(DIRECTION_RIGHT);
            case P -> togglePause();
            case SPACE -> handleSpecialAction();
        }
    }

    /**
     * Toggles the pause state of the game.
     */
    private void togglePause() {
        paused = !paused;
        if (paused) {
            if (tileMap.getAudioClip() != null) {
                tileMap.getAudioClip().stop();
            }
            gameloop.stop();
            showPauseMenu();
        } else {
            gameloop.start();
            if (tileMap.getAudioClip() != null) {
                tileMap.getAudioClip().start();
            }
            root.setLeft(null);
        }
    }

    /**
     * Handles special player actions, such as using a bomb or ice.
     */
    private void handleSpecialAction() {
        if (tileMap.getBombstate() == 1 && !player.isPutabomb()) {
            tileMap.Run_Bomb(player.getX(), player.getY());
        }
        if (player.isWithaice()) {
            player.putIce();
            tileMap.Froze_ghosts();
        }
    }

    /**
     * Displays the pause menu with options to resume, restart, or return to the main menu.
     */
    private void showPauseMenu() {
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: lightblue;" + "-fx-padding: 8;" + "-fx-font-size: 20px;" + "-fx-text-fill: #eee;");

        Menu menu = new Menu("Pause Menu");
        menu.setStyle("-fx-text-fill: #fff; -fx-font-weight: bold;");

        MenuItem resumeItem = new MenuItem("Continue");
        MenuItem restartItem = new MenuItem("Restart");
        MenuItem menuItem = new MenuItem("Menu");

        resumeItem.setOnAction(e -> {
            paused = false;
            gameloop.start();
            root.setLeft(null);
        });
        menuItem.setOnAction(e -> {
            paused = false;
            Menupage.showMenu();
            root.setLeft(null);
        });
        restartItem.setOnAction(e -> {
            paused = false;
            handleStartButtonClick(this.settingControl.getPlayerspeed(), this.settingControl.getGhostspeed(), this.settingControl.getLevel());
            tileMapCanvas.setFocusTraversable(true);
            tileMapCanvas.setOnKeyPressed(event -> handleKeyPressed(event));
            root.setLeft(null);
        });

        menu.getItems().addAll(resumeItem, restartItem, menuItem);
        menuBar.getMenus().add(menu);
        root.setLeft(menuBar);
    }

    /**
     * Handles the game over state by stopping the game and showing the game over menu.
     */
    private void handleGameOver() {
        gameOver = new GameOver();
        Platform.runLater(() -> {
            gameoverWav.play();
            gameloop.stop();
            gameOver.showGameAlert(root, tileMap);

            if (gameOver.isFlag()) {
                Menupage.showMenu();
            } else {
                exit();
            }
        });
    }

    /**
     * Handles the game win state by stopping the game and showing the victory menu.
     */
    private void handleGameWin() {
        gameWin = new GameWin();
        Platform.runLater(() -> {
            winWav.play();
            gameloop.stop();
            gameWin.showGameAlert(root, tileMap);

            if (gameWin.isFlag()) {
                Menupage.showMenu();
            } else {
                exit();
            }
        });
    }
}

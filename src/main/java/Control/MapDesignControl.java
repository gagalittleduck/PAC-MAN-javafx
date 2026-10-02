package Control;

import Model.WavPlay;
import interfaces.DirectionMap;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import static Control.MenuControl.path;
/**
 * Main controller class for designing maps.
 */
public class MapDesignControl implements Initializable, DirectionMap {
    @FXML TextField rownumber;
    @FXML TextField colnumber;
    @FXML Canvas tileMapCanvas;
    @FXML Label errorLabel;
    @FXML Button backButton;
    private int rows=2;
    private int cols=2;
    private static int current_row=1;
    private static int current_col=1;
    private Image WallImage=new Image(path+"/wall_image.png");
    private Image PlayerImage=new Image(path+"/player_image.png");
    private Image GhostImage=new Image(path+"/ghost1_image.png");
    private Image KeyImage=new Image(path+"/key_image.png");
    private Image GateImage=new Image(path+"/gate_image.png");
    private Image EntranceImage=new Image(path+"/in_image.png");
    private Image ExitImage=new Image(path+"/out_image.png");
    private Image BombbeanImage=new Image(path+"/bomb0_image.png");
    private Image IceImage=new Image(path+"/ice_image.png");
    private Image BeanImage=new Image(path+"/bean_image.png");
    private Image EmptyImage=new Image(path+"/empty_image.png");
    @FXML ImageView wall;
    @FXML ImageView player;
    @FXML ImageView ghost;
    @FXML ImageView key;
    @FXML ImageView gate;
    @FXML ImageView entrance;
    @FXML ImageView exit;
    @FXML ImageView bombbean;
    @FXML ImageView icebean;
    @FXML ImageView bean;
    @FXML ImageView skipImage;
    public static char[][] newmap;
    public int player_number=0;
    public static int ghosts_number=0;
    public static int bomb_number=0;
    public static int in_number=0;
    public static int out_number=0;
    public int key_number=0;
    public int gate_number=0;
    // Get GraphicsContext
    private GraphicsContext gc;
    private static Boolean inited=false;
    private static Boolean ready=false;
    public static Boolean self_design=false;
    /**
     * Initializes the map design controller.
     *
     * @param url the location of the FXML file
     * @param resourceBundle the resources used by the FXML file
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        wall.setImage(WallImage);
        player.setImage(PlayerImage);
        ghost.setImage(GhostImage);
        key.setImage(KeyImage);
        gate.setImage(GateImage);
        entrance.setImage(EntranceImage);
        exit.setImage(ExitImage);
        bombbean.setImage(BombbeanImage);
        icebean.setImage(IceImage);
        bean.setImage(BeanImage);

        if (tileMapCanvas == null) {
            tileMapCanvas=new Canvas(800,800);
        }
        gc = tileMapCanvas.getGraphicsContext2D();
        rownumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // matching number
                rownumber.setText(oldValue);
            }
        });
        backButton.setOnMouseEntered(event -> {
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
    }
    /**
     * Returns to the main menu.
     *
     * @param event the action event triggered by the user
     */
    @FXML
    public void backmenu(ActionEvent event) {
        self_design=true;
        inited=true;
        Menupage.showMenu();
    }
    /**
     * Previews the map based on the current inputs.
     */
    @FXML
    public void Preview() {
        ready=true;
        current_row=1;
        current_col=1;
        player_number=0;
        ghosts_number=0;
        bomb_number=0;
        gate_number=0;
        key_number=0;
        out_number=0;
        in_number=0;
        // Get the number of rows and columns of the input
        int rows = rownumber.getText().isEmpty() ? 0 : Integer.parseInt(rownumber.getText());
        int cols = colnumber.getText().isEmpty() ? 0 : Integer.parseInt(colnumber.getText());
        if (rows < 3 || rows > 15) {
            Platform.runLater(() -> {
                rownumber.setStyle("-fx-border-radius: 80; " +
                        "-fx-border-color: red; " +
                        "-fx-text-fill: red; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: transparent;");
                errorLabel.setText("Allowed rows must be between 3 and 15.");
                errorLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;-fx-font-size: 20");
            });
            return;
        } else{
            this.rows=rows;
            // Enter legal to restore the default style
            Platform.runLater(() -> {
                rownumber.setStyle("-fx-border-radius: 40; -fx-background-color: white;");
                colnumber.setStyle("-fx-border-radius: 40; -fx-background-color: white;");
                errorLabel.setText("");
            });
        }
        if(cols < 3 || cols > 15) {
            Platform.runLater(() -> {
                colnumber.setStyle("-fx-border-radius: 80; " +
                        "-fx-border-color: red; " +
                        "-fx-text-fill: red; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: transparent;");
                errorLabel.setText("Allowed cols must be between 3 and 15.");
                errorLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;-fx-font-size: 20");
            });
            return;
        } else{
            this.cols=cols;
            // Enter legal to restore the default style
            Platform.runLater(() -> {
                rownumber.setStyle("-fx-border-radius: 40; -fx-background-color: white;");
                colnumber.setStyle("-fx-border-radius: 40; -fx-background-color: white;");
                errorLabel.setText("");
            });
            newmap=new char[this.rows][this.cols];
        }

        // Get the width and height of the Canvas
        double canvasWidth = cols*grid_width;
        double canvasHeight = rows*grid_width;

        // Empty Canvas
        gc.clearRect(0, 0, 800, 800);

        // Drawing Grids
        gc.setStroke(javafx.scene.paint.Color.BLACK);
        for (int i = 0; i <= rows; i++) {
            gc.strokeLine(0, i * grid_width, canvasWidth, i * grid_width); // 水平线
        }
        for (int j = 0; j <= cols; j++) {
            gc.strokeLine(j * grid_width, 0, j * grid_width, canvasHeight); // 垂直线
        }
        // Make sure the edges of the map are walls
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(i==0||i==rows-1||j==0||j==cols-1) {
                    gc.drawImage(WallImage,j*grid_width,i*grid_width);
                    newmap[i][j]='W';
                }else {
                    newmap[i][j]='.';
                }
            }
        }
    }
    @FXML Button Wall;
    /**
     * Adds a wall to the map at the current position.
     *
     * @param event the action event triggered by the user
     */
    @FXML public void setWall(ActionEvent event) {
        Platform.runLater(() -> {
            errorLabel.setText("");
        });
        if(ready) {
            if (current_row < rows - 1) { // Check if the current row is in range
                if (current_col < cols - 2) { // Check if the current column is in range
                    // Draw the current grid content
                    gc.drawImage(WallImage, current_col * grid_width, current_row * grid_width);
                    newmap[current_row][current_col] = 'W'; // Update map status
                    current_col++; // Move to next column
                } else if (current_col == cols - 2) { // Checks if the last column of the current row has been reached

                    gc.drawImage(WallImage, current_col * grid_width, current_row * grid_width);
                    newmap[current_row][current_col] = 'W'; // Update map status
                    current_row++; // Move to next line
                    current_col = 1; // Reset column to first column
                }
            }
        }
    }
    @FXML Button Player;
    /**
     * Handles user actions for adding the player to the map.
     *
     * @param event the action event triggered by the user.
     */
    @FXML public void setPlayer(ActionEvent event){
        Platform.runLater(() -> {
            errorLabel.setText("");
        });
        if(player_number<1){
            player_number++;
            if(ready) {
                if (current_row < rows - 1) { // Check if the current row is in range
                    if (current_col < cols - 2) { // Check if the current column is in range
                        // Draw the current grid content
                        gc.drawImage(PlayerImage, current_col * grid_width, current_row * grid_width);
                        newmap[current_row][current_col] = 'P'; // Update map status
                        current_col++; // Move to next column
                    } else if (current_col == cols - 2) { // Checks if the last column of the current row has been reached

                        gc.drawImage(PlayerImage, current_col * grid_width, current_row * grid_width);
                        newmap[current_row][current_col] = 'P'; // Update map status
                        current_row++; // Move to next line
                        current_col = 1; // Reset column to first column
                    }
                }
            }
        }else {
            Platform.runLater(() -> {
                errorLabel.setText("Only one player can be placed");
                errorLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;-fx-font-size: 20");
            });
        }


    }
    @FXML Button Ghost;
    /**
     * Handles user actions for adding the Ghost to the map.
     *
     * @param event the action event triggered by the user.
     */
    @FXML public void setGhost(ActionEvent event){
        Platform.runLater(() -> {
            errorLabel.setText("");
        });
        ghosts_number++;
        if(ready) {
            if (current_row < rows - 1) { // Check if the current row is in range
                if (current_col < cols - 2) { // Check if the current column is in range
                    // Draw the current grid content
                    gc.drawImage(GhostImage, current_col * grid_width, current_row * grid_width);
                    newmap[current_row][current_col] = 'C'; // Update map status
                    current_col++; // Move to next column
                } else if (current_col == cols - 2) { // Checks if the last column of the current row has been reached

                    gc.drawImage(GhostImage, current_col * grid_width, current_row * grid_width);
                    newmap[current_row][current_col] = 'C'; // Update map status
                    current_row++; // Move to next line
                    current_col = 1; // Reset column to first column
                }
            }
        }
    }
    @FXML Button Key;
    /**
     * Handles user actions for adding the Key to the map.
     *
     * @param event the action event triggered by the user.
     */
    @FXML public void setKey(ActionEvent event){
        Platform.runLater(() -> {
            errorLabel.setText("");
        });
        key_number++;
        if(ready) {
            if (current_row < rows - 1) { // Check if the current row is in range
                if (current_col < cols - 2) { // Check if the current column is in range
                    // Draw the current grid content
                    gc.drawImage(KeyImage, current_col * grid_width, current_row * grid_width);
                    newmap[current_row][current_col] = 'K'; // Update map status
                    current_col++; // Move to next column
                } else if (current_col == cols - 2) { // Checks if the last column of the current row has been reached

                    gc.drawImage(KeyImage, current_col * grid_width, current_row * grid_width);
                    newmap[current_row][current_col] = 'K'; // Update map status
                    current_row++; // Move to next line
                    current_col = 1; // Reset column to first column
                }
            }
        }
    }
    @FXML Button Gate;
    /**
     * Handles user actions for adding the Gate to the map.
     *
     * @param event the action event triggered by the user.
     */
    @FXML public void setGate(ActionEvent event){
        Platform.runLater(() -> {
            errorLabel.setText("");
        });
        if(gate_number<1){
            gate_number++;
            if(ready) {
                if (current_row < rows - 1) { // Check if the current row is in range
                    if (current_col < cols - 2) { // Check if the current column is in range
                        // Draw the current grid content
                        gc.drawImage(GateImage, current_col * grid_width, current_row * grid_width);
                        newmap[current_row][current_col] = 'G'; // Update map status
                        current_col++; // Move to next column
                    } else if (current_col == cols - 2) { // Checks if the last column of the current row has been reached

                        gc.drawImage(GateImage, current_col * grid_width, current_row * grid_width);
                        newmap[current_row][current_col] = 'G'; // Update map status
                        current_row++; // Move to next line
                        current_col = 1; // Reset column to first column
                    }
                }
            }
        }else {
            Platform.runLater(() -> {
                errorLabel.setText("Only one gate can be placed");
                errorLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;-fx-font-size: 20");
            });
        }

    }
    @FXML Button Entrance;
    /**
     * Handles user actions for adding the portal Entrance to the map.
     *
     * @param event the action event triggered by the user.
     */
    @FXML public void setEntrance(ActionEvent event){
        Platform.runLater(() -> {
            errorLabel.setText("");
        });
        in_number++;
        if(ready) {
            if (current_row < rows - 1) { // Check if the current row is in range
                if (current_col < cols - 2) { // Check if the current column is in range
                    // Draw the current grid content
                    gc.drawImage(EntranceImage, current_col * grid_width, current_row * grid_width);
                    newmap[current_row][current_col] = 'I'; // Update map status
                    current_col++; // Move to next column
                } else if (current_col == cols - 2) { // Checks if the last column of the current row has been reached

                    gc.drawImage(EntranceImage, current_col * grid_width, current_row * grid_width);
                    newmap[current_row][current_col] = 'I'; // Update map status
                    current_row++; // Move to next line
                    current_col = 1; // Reset column to first column
                }
            }
        }
    }
    @FXML Button Exit;
    /**
     * Handles user actions for adding the portal Exit to the map.
     *
     * @param event the action event triggered by the user.
     */
    @FXML public void setExit(ActionEvent event){
        Platform.runLater(() -> {
            errorLabel.setText("");
        });
        if(in_number>0){
            if(out_number<1){
                out_number++;
                if(ready) {
                    if (current_row < rows - 1) { // Check if the current row is in range
                        if (current_col < cols - 2) { // Check if the current column is in range
                            // Draw the current grid content
                            gc.drawImage(ExitImage, current_col * grid_width, current_row * grid_width);
                            newmap[current_row][current_col] = 'T'; // Update map status
                            current_col++; // Move to next column
                        } else if (current_col == cols - 2) { // Checks if the last column of the current row has been reached

                            gc.drawImage(ExitImage, current_col * grid_width, current_row * grid_width);
                            newmap[current_row][current_col] = 'T'; // Update map status
                            current_row++; // Move to next line
                            current_col = 1; // Reset column to first column
                        }
                    }
                }
            }else {
                Platform.runLater(() -> {
                    errorLabel.setText("Only one portal exit can be placed");
                    errorLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;-fx-font-size: 20");
                });
            }
        }else {
            Platform.runLater(() -> {
                errorLabel.setText("At least one portal entrance is required before the portal exit placed");
                errorLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;-fx-font-size: 20");
            });
        }

    }
    @FXML Button Bombbean;
    /**
     * Handles user actions for adding the Bombbean to the map.
     *
     * @param event the action event triggered by the user.
     */
    @FXML public void setBombbean(ActionEvent event) {
        Platform.runLater(() -> {
            errorLabel.setText("");
        });
        if (bomb_number < 1){
            bomb_number++;
            if(ready) {
                if (current_row < rows - 1) { // Check if the current row is in range
                    if (current_col < cols - 2) { // Check if the current column is in range
                        // Draw the current grid content
                        gc.drawImage(BombbeanImage, current_col * grid_width, current_row * grid_width);
                        newmap[current_row][current_col] = 'B'; // Update map status
                        current_col++; // Move to next column
                    } else if (current_col == cols - 2) { // Checks if the last column of the current row has been reached

                        gc.drawImage(BombbeanImage, current_col * grid_width, current_row * grid_width);
                        newmap[current_row][current_col] = 'B'; // Update map status
                        current_row++; // Move to next line
                        current_col = 1; // Reset column to first column
                    }
                }
            }
        }else {
            Platform.runLater(() -> {
                errorLabel.setText("Only one bomb bean can be placed");
                errorLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;-fx-font-size: 20");
            });
        }
    }
    @FXML Button Bean;
    /**
     * Handles user actions for adding the Bean to the map.
     *
     * @param event the action event triggered by the user.
     */
    @FXML public void setBean(ActionEvent event){
        Platform.runLater(() -> {
            errorLabel.setText("");
        });
        if(ready) {
            if (current_row < rows - 1) { // Check if the current row is in range
                if (current_col < cols - 2) { // Check if the current column is in range
                    // Draw the current grid content
                    gc.drawImage(BeanImage, current_col * grid_width, current_row * grid_width);
                    newmap[current_row][current_col] = 'O'; // Update map status
                    current_col++; // Move to next column
                } else if (current_col == cols - 2) { // Checks if the last column of the current row has been reached

                    gc.drawImage(BeanImage, current_col * grid_width, current_row * grid_width);
                    newmap[current_row][current_col] = 'O'; // Update map status
                    current_row++; // Move to next line
                    current_col = 1; // Reset column to first column
                }
            }
        }
    }
    @FXML Button Icebean;
    /**
     * Handles user actions for adding the Icebean to the map.
     *
     * @param event the action event triggered by the user.
     */
    @FXML public void setIcebean(ActionEvent event){
        Platform.runLater(() -> {
            errorLabel.setText("");
        });
        if(ready) {
            if (current_row < rows - 1) { // Check if the current row is in range
                if (current_col < cols - 2) { // Check if the current column is in range
                    // Draw the current grid content
                    gc.drawImage(IceImage, current_col * grid_width, current_row * grid_width);
                    newmap[current_row][current_col] = 'F'; // Update map status
                    current_col++; // Move to next column
                } else if (current_col == cols - 2) { // Checks if the last column of the current row has been reached

                    gc.drawImage(IceImage, current_col * grid_width, current_row * grid_width);
                    newmap[current_row][current_col] = 'F'; // Update map status
                    current_row++; // Move to next line
                    current_col = 1; // Reset column to first column
                }
            }
        }
    }
    @FXML Button skip;
    /**
     * Handles user actions for skip a grid of the map.
     *
     * @param event the action event triggered by the user.
     */
    @FXML public void skip(ActionEvent event){
        Platform.runLater(() -> {
            errorLabel.setText("");
        });
        if(ready) {
            if (current_row < rows - 1) { // Check if the current row is in range
                if (current_col < cols - 2) { // Check if the current column is in range
                    // Draw the current grid content
                    gc.drawImage(EmptyImage, current_col * grid_width, current_row * grid_width);
                    newmap[current_row][current_col] = '.'; // Update map status
                    current_col++; // Move to next column
                } else if (current_col == cols - 2) { // Checks if the last column of the current row has been reached

                    gc.drawImage(EmptyImage, current_col * grid_width, current_row * grid_width);
                    newmap[current_row][current_col] = '.'; // Update map status
                    current_row++; // Move to next line
                    current_col = 1; // Reset column to first column
                }
            }
        }
    }
    /**
     * Saves the current map design to a file.
     *
     * @param event the action event triggered by the user
     * @return true if the map was saved successfully, false otherwise
     */
    @FXML
    public Boolean saveMap(ActionEvent event) {

        if(!ready){
            Platform.runLater(()->{
                errorLabel.setText("Init a new map first!");
                errorLabel.setStyle("-fx-text-fill: red;-fx-font-size: 20" );
            });
            return false;
        }
        // There can only be one player on the map, one bomb bean, at least one ghost, at least one key, and one door
        if (player_number == 1 &&bomb_number<=1&&ghosts_number > 0 && key_number > 0 && gate_number ==1) {
            String filePath = "src/main/resources/LELVEL1.txt"; // File save path for user-defined maps
            int[] firstLine = { this.rows, this.cols}; // First line of content
            char[][] secondLine = this.newmap; // Contents of the second line
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                // Write to the first line
                writer.write(Arrays.toString(firstLine));
                writer.newLine();

                // Write second line (write 2D array line by line)
                for (char[] row : secondLine) {
                    writer.write(row);
                    writer.newLine();
                }
                Platform.runLater(()->{
                    errorLabel.setText("Saved,back to menu to start your own map!");
                    errorLabel.setStyle("-fx-text-fill: white;-fx-font-size: 20;-fx-font-weight: bold" );
                });
                self_design=true;
                return true;
            } catch (IOException e) {
                throw new RuntimeException("File Write Failure: " + e.getMessage(), e);
            }
        }else{
            Platform.runLater(()->{
                errorLabel.setText("Only 1 player, 1 bomb, 1 gate, 0+ keys, 0+ ghost allowed!");
                errorLabel.setStyle("-fx-text-fill: red;-fx-font-size: 20;-fx-font-weight: bold" );
            });
            return false;
        }
    }
}

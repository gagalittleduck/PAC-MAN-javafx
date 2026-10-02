package Model;

import javafx.scene.canvas.GraphicsContext;
import interfaces.DirectionMap;
import interfaces.Drawable;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Control.MapDesignControl.ghosts_number;
import static Control.MapDesignControl.newmap;
import static java.lang.Math.abs;


public class TileMap implements DirectionMap{
    int level=HARD;
    public static char[][] map;
    double player_velocity=2.0;
    double ghost_velocity=2.0;
    //get by menu
    int gatestate=0;
    int bombstate=0;

    int player_number=1;
    Player player;
    Gate gate;
    BombBean bombbean;
    List<IceBean> icebean;
    List<Ghost> ghosts=new ArrayList<>();
    List<Drawable> staticObjects;
    int col_boundary ;
    int row_boundary ;
    static int out_x=0;
    static int out_y=0;
    String filePath = "src/main/resources/music/runbomb.wav";
    File audioFile = new File(filePath);
    AudioInputStream audioStream = null;
    Clip audioClip = null;

    public TileMap(int level,double player_velocity,double ghost_velocity,Boolean self_design){
        this.level=level;
        this.player_velocity=player_velocity;
        this.ghost_velocity=ghost_velocity;
        if(self_design){
            this.level=ghosts_number;
            map=Arrays.stream(newmap)
                    .map(char[]::clone)
                    .toArray(char[][]::new);
        }else {
            if(level==HARD){
                map = Arrays.stream(HARD_MAP)
                        .map(char[]::clone)
                        .toArray(char[][]::new);
            }else if(level==MIDDLE){
                map=Arrays.stream(MIDDLE_MAP)
                        .map(char[]::clone)
                        .toArray(char[][]::new);
            }else{
                map=Arrays.stream(SIMPLE_MAP)
                        .map(char[]::clone)
                        .toArray(char[][]::new);
            }
        }

        updateTileMap();
        col_boundary = map[0].length;
        row_boundary = map.length;
    }
    // Method to update the tile map based on the current state of the game
    public void updateTileMap() {
        List<Drawable> staticObjects = new ArrayList<>(); // List to store static objects like walls and beans
        icebean = new ArrayList<>(); // List to store ice beans
        int a = level; // Number of ghosts to create based on the level

        // Iterate through the map array to initialize objects
        for (int row = 0; row < this.map.length; row++) {
            for (int col = 0; col < this.map[0].length; col++) {
                int x = col * grid_width; // X-coordinate for the object
                int y = row * grid_width; // Y-coordinate for the object
                switch (this.map[row][col]) {
                    case 'W': // Wall
                        staticObjects.add(new Wall(x, y));
                        break;
                    case 'O': // Bean
                        staticObjects.add(new Bean(x, y));
                        break;
                    case 'K': // Key
                        staticObjects.add(new Key(x, y));
                        break;
                    case 'B': // Bomb Bean
                        this.bombbean = new BombBean(x, y, bombstate);
                        break;
                    case 'F': // Ice Bean
                        this.icebean.add(new IceBean(x, y, this));
                        break;
                    case 'G': // Gate
                        if (this.gatestate == 1) {
                            this.gate = new Gate(x, y, gatestate);
                        }
                        break;
                    case 'P': // Player
                        if (player == null) {
                            this.player = new Player(x, y, player_velocity, this);
                        }
                        break;
                    case 'C': // Ghost
                        if (ghosts == null || ghosts.size() != level) {
                            ghosts.add(new Ghost(x, y, ghost_velocity, this, a));
                            a--; // Reduce the ghost count for remaining levels
                        }
                        break;
                    case 'I': // Transport Gate (Entrance)
                        staticObjects.add(new Transgate(x, y, "in"));
                        break;
                    case 'T': // Transport Gate (Exit)
                        staticObjects.add(new Transgate(x, y, "out"));
                        out_x = x; // Save the exit X-coordinate
                        out_y = y; // Save the exit Y-coordinate
                        break;
                }
            }
        }
        this.staticObjects = staticObjects; // Update the static objects
    }

    // Method to check if a position collides with a wall
    public Boolean CollidedWithWall(double x, double y, int requested_direction) {
        int col = 0; // Column index for the next position
        int row = 0; // Row index for the next position
        double nextcol = 0; // Next column position
        double nextrow = 0; // Next row position

        // Determine the next position based on the requested direction
        switch (requested_direction) {
            case DIRECTION_UP:
                nextrow = y - grid_width;
                if ((x % grid_width == 0) && (y % grid_width == 0)) {
                    row = (int) (nextrow / this.grid_width);
                    col = (int) (x / this.grid_width);
                    if (checkindex(row, col)) {
                        return false; // Out of bounds
                    }
                    if (map[row][col] == 'W') {
                        return true; // Collided with a wall
                    }
                }
                break;
            case DIRECTION_DOWN:
                nextrow = y + grid_width;
                if ((x % grid_width == 0) && (y % grid_width == 0)) {
                    row = (int) (nextrow / this.grid_width);
                    col = (int) (x / this.grid_width);
                    if (checkindex(row, col)) {
                        return false; // Out of bounds
                    }
                    if (map[row][col] == 'W') {
                        return true; // Collided with a wall
                    }
                }
                break;
            case DIRECTION_LEFT:
                nextcol = x - grid_width;
                if ((x % grid_width == 0) && (y % grid_width == 0)) {
                    col = (int) (nextcol / this.grid_width);
                    row = (int) (y / this.grid_width);
                    if (checkindex(row, col)) {
                        return false; // Out of bounds
                    }
                    if (map[row][col] == 'W') {
                        return true; // Collided with a wall
                    }
                }
                break;
            case DIRECTION_RIGHT:
                nextcol = x + grid_width;
                if ((x % grid_width == 0) && (y % grid_width == 0)) {
                    col = (int) (nextcol / this.grid_width);
                    row = (int) (y / this.grid_width);
                    if (checkindex(row, col)) {
                        return false; // Out of bounds
                    }
                    if (map[row][col] == 'W') {
                        return true; // Collided with a wall
                    }
                }
                break;
            default:
                return false; // No collision
        }
        return false; // No collision
    }

    // Method to check if a position is out of bounds
    public boolean checkindex(int row, int col) {
        return row > row_boundary || row < 0 || col < 0 || col > col_boundary;
    }

    // Method to check if the player collides with a bean
    public Boolean CollidedWithBean(double x, double y) {
        int col = (int) x / this.grid_width;
        int row = (int) y / this.grid_width;
        if ((x % grid_width == 0) && (y % grid_width == 0)) {
            if (this.map[row][col] == 'O') {
                this.map[row][col] = '.'; // Remove the bean
                this.updateTileMap(); // Update the map
                return true; // Bean collected
            }
        }
        return false; // No bean collision
    }

    // Method to check if the player collides with a key
    public Boolean CollidedWithKey(double x, double y) {
        int col = (int) (x / this.grid_width);
        int row = (int) (y / this.grid_width);
        if ((x % grid_width == 0) && (y % grid_width == 0)) {
            if (this.map[row][col] == 'K') {
                this.map[row][col] = '.'; // Remove the key
                this.gatestate = 1; // Unlock the gate
                this.updateTileMap(); // Update the map
                return true; // Key collected
            }
        }
        return false; // No key collision
    }

    // Method to check if the player collides with a gate
    public Boolean CollidedWithGate(double x, double y) {
        int col = (int) (x / this.grid_width);
        int row = (int) (y / this.grid_width);
        if (this.gatestate == 1) { // Check if the gate is unlocked
            if ((x % grid_width == 0) && (y % grid_width == 0)) {
                if (this.map[row][col] == 'G') {
                    this.player_number = 0; // Reset player count
                    this.updateTileMap(); // Update the map
                    return true; // Player entered the gate
                }
            }
        }
        return false; // No gate collision
    }
    // Check if the player has entered a transport gate
    public Boolean ComeInTransportGate(double x, double y) {
        int col = (int) (x / this.grid_width); // Calculate the column index
        int row = (int) (y / this.grid_width); // Calculate the row index
        // Check if the player is exactly on a grid cell
        if ((x % grid_width == 0) && (y % grid_width == 0)) {
            if (this.map[row][col] == 'I') { // Check if the cell is an entrance transport gate
                return true; // Player has entered the transport gate
            }
        }
        return false; // Player has not entered the transport gate
    }

    // Place a bomb at the specified position
    public void Run_Bomb(double x, double y) {
        int row = (int) (y / grid_width); // Calculate the row index
        int col = (int) (x / grid_width); // Calculate the column index
        map[row][col] = 'B'; // Place the bomb on the map

        // Play bomb placement sound
        try {
            audioStream = AudioSystem.getAudioInputStream(audioFile);
            audioClip = AudioSystem.getClip();
            audioClip.open(audioStream);
            audioClip.start();
            audioClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the audio continuously
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Audio format not supported: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Audio file read failure: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.out.println("Audio resources not available: " + e.getMessage());
        }

        updateTileMap(); // Update the tile map after placing the bomb
        this.bombstate++; // Increment the bomb state
        player.setPutabomb(true); // Indicate the player has placed a bomb
        player.setWithabomb(false); // Remove the bomb from the player
    }

    // Check if the player collides with a bomb bean
    public Boolean CollidedWithBombbean(double x, double y) {
        int col = (int) (x / this.grid_width); // Calculate the column index
        int row = (int) (y / this.grid_width); // Calculate the row index
        // Check if the player is exactly on a grid cell
        if ((x % grid_width == 0) && (y % grid_width == 0)) {
            if (this.map[row][col] == 'B') { // Check if the cell contains a bomb bean
                this.map[row][col] = '.'; // Remove the bomb bean from the map
                this.player.score++; // Increment the player's score
                this.bombstate++; // Increment the bomb state
                this.bombbean.state = this.bombstate; // Update the bomb bean's state
                this.updateTileMap(); // Update the tile map
                if (audioClip != null) {
                    audioClip.stop(); // Stop the bomb placement sound
                }
                return true; // Player collided with a bomb bean
            }
        }
        return false; // No collision with a bomb bean
    }

    // Check if the player collides with an ice bean
    public Boolean CollidedWithIceBean(double x, double y) {
        int col = (int) (x / this.grid_width); // Calculate the column index
        int row = (int) (y / this.grid_width); // Calculate the row index
        // Check if the player is exactly on a grid cell
        if ((x % grid_width == 0) && (y % grid_width == 0)) {
            if (this.map[row][col] == 'F') { // Check if the cell contains an ice bean
                this.map[row][col] = '.'; // Remove the ice bean from the map
                this.player.score += 2; // Add points to the player's score
                this.updateTileMap(); // Update the tile map
                this.icebean = null; // Remove the ice bean object
                return true; // Player collided with an ice bean
            }
        }
        return false; // No collision with an ice bean
    }

    // Check if the player collides with a ghost
    public Boolean CollidedWithGhost(double x, double y) {
        for (Ghost ghost : ghosts) {
            double de_x = (int) abs(x - ghost.getX()); // Calculate the horizontal distance
            double de_y = (int) abs(y - ghost.getY()); // Calculate the vertical distance
            // Check if the player is close enough to the ghost
            if (de_y <= 2 && de_x == 0 || (de_x <= 2 && de_y == 0)) {
                this.player_number = 0; // Reset the player count
                if (audioClip != null) {
                    audioClip.stop(); // Stop any playing audio
                    audioClip = null;
                }
                return true; // Player collided with a ghost
            }
        }
        return false; // No collision with a ghost
    }

    // Check if the player's ice collides with another object
    public Boolean IceCollidedByPlayer(double x, double y) {
        double de_x = (int) abs(x - player.getX()); // Calculate the horizontal distance
        double de_y = (int) abs(y - player.getY()); // Calculate the vertical distance
        // Check if the ice is close enough to the player
        if (de_y <= 2 && de_x == 0 || (de_x <= 2 && de_y == 0)) {
            this.map[(int) x / grid_width][(int) y / grid_width] = '.'; // Remove the ice from the map
            this.flush_skill();
            this.player.setPutaice(false); // Indicate the player is no longer putting ice
            this.player.setWithaice(true); // Indicate the player now has ice
            return true; // Ice collided with the player
        }
        return false; // No collision with ice
    }

    public void Froze_ghosts(){
        for(Ghost ghost:ghosts){
            ghost.getfrozen();
        }
    }
    public void flush_skill(){
        if(player.withaice){
            player.withaice=false;
            icebean=null;
        }
        if(player.withabomb){
            player.withabomb=false;
            this.bombstate=0;
            this.bombbean=null;
        }
    }
    public void drawMap(GraphicsContext gImage) {
        this.updateTileMap();
        for (Drawable obj : this.staticObjects) {
            obj.paintSelf(gImage);
        }
        if (gate != null && gate.state==1) {
            gate.paintSelf(gImage);
        }
        if (bombbean != null && bombbean.state!=1) {
            bombbean.paintSelf(gImage);
        }

        if (icebean != null) {
            icebean.removeIf(ice -> {
                if(IceCollidedByPlayer(ice.x,ice.y)){
                    return true;
                }else {
                    ice.paintSelf(gImage);
                    return false;
                }
            });
        }
        if (player != null && player_number!=0) {
            if(player.activateBombbean()){
                updateTileMap();
            }
            player.paintSelf(gImage);
        }

        ghosts.removeIf(ghost -> {
            if(bombbean == null ||(bombbean!=null&&!ghost.dead())){
                ghost.paintSelf(gImage);
                return false;
            }else{
                this.bombstate++;
                player.score+=5;
                this.bombbean=null;
                return true;
            }
        });
        this.level=ghosts.size();
    }

    public int getCol_boundary() {
        return col_boundary;
    }

    public void setCol_boundary(int col_boundary) {
        this.col_boundary = col_boundary;
    }

    public int getRow_boundary() {
        return row_boundary;
    }

    public void setRow_boundary(int row_boundary) {
        this.row_boundary = row_boundary;
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public char[][] getMap() {
        return map;
    }

    public void setMap(char[][] map) {
        this.map = map;
    }

    public double getPlayer_velocity() {
        return player_velocity;
    }

    public void setPlayer_velocity(double player_velocity) {
        this.player_velocity = player_velocity;
    }

    public double getGhost_velocity() {
        return ghost_velocity;
    }

    public void setGhost_velocity(double ghost_velocity) {
        this.ghost_velocity = ghost_velocity;
    }

    public int getGatestate() {
        return gatestate;
    }

    public void setGatestate(int gatestate) {
        this.gatestate = gatestate;
    }

    public int getBombstate() {
        return bombstate;
    }

    public void setBombstate(int bombstate) {
        this.bombstate = bombstate;
    }

    public int getPlayer_number() {
        return player_number;
    }

    public void setPlayer_number(int player_number) {
        this.player_number = player_number;
    }

    public Gate getGate() {
        return gate;
    }

    public void setGate(Gate gate) {
        this.gate = gate;
    }


    public List<Ghost> getGhosts() {
        return ghosts;
    }

    public void setGhosts(List<Ghost> ghosts) {
        this.ghosts = ghosts;
    }

    public List<Drawable> getStaticObjects() {
        return staticObjects;
    }

    public void setStaticObjects(List<Drawable> staticObjects) {
        this.staticObjects = staticObjects;
    }
}

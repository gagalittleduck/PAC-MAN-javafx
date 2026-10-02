package Model;

import javafx.scene.canvas.GraphicsContext;
import interfaces.DirectionMap;
import interfaces.Drawable;
import javafx.scene.image.Image;

import static Control.MenuControl.path;
import static Model.TileMap.out_x;
import static Model.TileMap.out_y;

public class Player implements Drawable, DirectionMap {

    double x;
    double y;
    public int score=0;
    double velocity;
    int current_direction=100;
    int requested_direction=-1;
    Boolean putabomb=false;
    Boolean putaice=false;
    Boolean withabomb=false;
    Boolean withaice=false;
    Image plager_image= new Image(path+"/player_image.png");
    Image bomb_image=new Image(path+"/player_with_bomb_image.png");
    Image ice_image=new Image(path+"/player_with_ice_image.png");
    TileMap tilemap ;
    int state=0;
    Player(int x,int y,double velocity,TileMap tilemap){
        this.x = x;
        this.y = y;
        this.velocity=velocity*0.5;
        this.tilemap=tilemap;
    }
    // state 0 ; 1: moving ; 2: dead




    // Method to handle the player collecting a regular bean
    public void GetBean() {
        // Check if the player collides with a bean on the tile map
        if (this.tilemap.CollidedWithBean(this.x, this.y)) {
            // Play the bean collection sound effect
            WavPlay wavplay = new WavPlay("src/main/resources/music/eat_bean.wav");
            wavplay.play();
            score++; // Increment the player's score
        }
    }

    // Method to handle the player collecting an ice bean
    public void GetIceBean() {
        // Check if the player collides with an ice bean on the tile map
        if (this.tilemap.CollidedWithIceBean(this.x, this.y)) {
            // Play the ice bean collection sound effect
            WavPlay wavplay = new WavPlay("src/main/resources/music/eat_bean.wav");
            wavplay.play();
            tilemap.flush_skill(); // Activate tile map's skill flush mechanism
            score += 2; // Add bonus points for collecting an ice bean
            this.withaice = true; // Set the player to be carrying ice
        }
    }

    // Method to place an ice effect
    public void putIce() {
        // Play the ice placement sound effect
        WavPlay wavplay = new WavPlay("src/main/resources/music/eat_bean.wav");
        wavplay.play();
        this.withaice = false; // The player no longer has ice
        this.putaice = true; // The player has placed ice
    }

    // Method to check and update the player's direction based on input
    public void checkDirection(int requested_direction) {
        // Allow changing to the opposite direction immediately
        if (requested_direction == DIRECTION_UP && this.current_direction == DIRECTION_DOWN) {
            this.current_direction = requested_direction;
        }
        if (requested_direction == DIRECTION_DOWN && this.current_direction == DIRECTION_UP) {
            this.current_direction = requested_direction;
        }
        if (requested_direction == DIRECTION_LEFT && this.current_direction == DIRECTION_RIGHT) {
            this.current_direction = requested_direction;
        }
        if (requested_direction == DIRECTION_RIGHT && this.current_direction == DIRECTION_LEFT) {
            this.current_direction = requested_direction;
        }
        // Store the requested direction for future use
        this.requested_direction = requested_direction;
    }

    // Method to handle the player's movement
    public void move() {
        // Check if the player enters a transport gate
        if (this.tilemap.ComeInTransportGate(x, this.y)) {
            setX(out_x); // Move the player to the transport exit's X-coordinate
            setY(out_y); // Move the player to the transport exit's Y-coordinate
            tilemap.updateTileMap(); // Update the tile map after transportation
        }

        // If the player requests a new direction, check if it's valid
        if (this.current_direction != this.requested_direction) {
            // Ensure the player is aligned with the grid before changing direction
            if ((this.x % this.grid_width == 0) && (this.y % this.grid_width == 0)) {
                // Change direction if the requested direction does not lead to a wall
                if (!tilemap.CollidedWithWall(this.x, this.y, this.requested_direction)) {
                    this.current_direction = this.requested_direction;
                }
            }
        }

        // Move the player in the current direction if there's no collision
        if (!tilemap.checkindex((int) this.x / grid_width, (int) this.y / grid_width) &&
                !tilemap.CollidedWithWall(this.x, this.y, this.current_direction)) {
            switch (this.current_direction) {
                case DIRECTION_DOWN:
                    this.y += velocity; // Move down
                    break;
                case DIRECTION_UP:
                    this.y -= velocity; // Move up
                    break;
                case DIRECTION_LEFT:
                    this.x -= velocity; // Move left
                    break;
                case DIRECTION_RIGHT:
                    this.x += velocity; // Move right
                    break;
            }
        }
    }

    // Method to handle the player collecting a key
    public Boolean GetKey() {
        // Check if the player collides with a key on the tile map
        if (this.tilemap.CollidedWithKey(this.x, this.y)) {
            // Play the key collection sound effect
            WavPlay wavplay = new WavPlay("src/main/resources/music/key.wav");
            wavplay.play();
            return true; // Key collected
        }
        return false; // No key collected
    }

    // Method to check if the player enters the gate
    public Boolean getinGate() {
        // Return true if the player collides with the gate on the tile map
        return this.tilemap.CollidedWithGate(this.x, this.y);
    }

    // Method to check if the player is dead
    public Boolean dead() {
        // Return true if the player collides with a ghost on the tile map
        return this.tilemap.CollidedWithGhost(this.x, this.y);
    }

    // Method to activate the bomb bean
    public Boolean activateBombbean() {
        // Check if the player collides with a bomb bean and the bomb state is inactive
        if (this.tilemap.bombstate == 0 && tilemap.bombbean != null &&
                this.tilemap.CollidedWithBombbean(this.x, this.y)) {
            tilemap.flush_skill(); // Activate tile map's skill flush
            withabomb = true; // Set the player to be carrying a bomb
            score += 2; // Add bonus points for collecting a bomb bean
            return true; // Bomb bean activated
        }
        return false; // Bomb bean not activated
    }

    public void paintSelf(GraphicsContext g) {
        if(!this.getinGate()){
            if(!putabomb && withabomb){
                g.drawImage(bomb_image, x, y);
            } else if (!putaice && withaice) {
                g.drawImage(ice_image, x, y);
            }else{
                g.drawImage(plager_image, x, y);
            }
        }
    }

    public double getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public int getCurrent_direction() {
        return current_direction;
    }

    public void setCurrent_direction(int current_direction) {
        this.current_direction = current_direction;
    }

    public int getRequested_direction() {
        return requested_direction;
    }

    public void setRequested_direction(int requested_direction) {
        this.requested_direction = requested_direction;
    }

    public Boolean isPutabomb() {
        return putabomb;
    }

    public void setPutabomb(Boolean putabomb) {
        this.putabomb = putabomb;
    }

    public Boolean isPutaice() {
        return putaice;
    }

    public void setPutaice(Boolean putaice) {
        this.putaice = putaice;
    }

    public Boolean isWithabomb() {
        return withabomb;
    }

    public void setWithabomb(Boolean withabomb) {
        this.withabomb = withabomb;
    }

    public Boolean isWithaice() {
        return withaice;
    }

    public void setWithaice(Boolean withaice) {
        this.withaice = withaice;
    }

    public Image getPlager_image() {
        return plager_image;
    }

    public void setPlager_image(Image plager_image) {
        this.plager_image = plager_image;
    }

    public Image getBomb_image() {
        return bomb_image;
    }

    public void setBomb_image(Image bomb_image) {
        this.bomb_image = bomb_image;
    }

    public Image getIce_image() {
        return ice_image;
    }

    public void setIce_image(Image ice_image) {
        this.ice_image = ice_image;
    }

    public TileMap getTilemap() {
        return tilemap;
    }

    public void setTilemap(TileMap tilemap) {
        this.tilemap = tilemap;
    }


    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

package Model;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;
import interfaces.DirectionMap;
import interfaces.Drawable;
import javafx.scene.image.Image;

import java.util.*;
import java.util.List;

import static Control.MenuControl.path;
import static Model.TileMap.out_x;
import static Model.TileMap.out_y;

public class Ghost implements Drawable, DirectionMap {
    List<String> image_index = new ArrayList<>(Arrays.asList("3", "1","2"));
    int current_direction;
    int timer;
    int defult_timer;
    Image ghost_image ;
    Image frozen_image ;
    double x;
    double y;
    double velocity;
    Boolean frozen=false;
    TileMap tilemap;
    List<Integer> numbers = Arrays.asList(2,3,4);
    Random random = new Random(5);
    int level;

    Ghost(int x,int y,double velocity,TileMap tilemap,int level){
        this.x = x;
        this.y = y;
        this.velocity=velocity*0.5;
        this.tilemap=tilemap;
        this.level=level%3+1;
        this.ghost_image= new Image(String.format(path+"/ghost%d_image.png" , this.level));
        this.frozen_image= new Image(String.format(path+"/ghost%dfrozen_image.png" , this.level));
        this.current_direction = random.nextInt(4);
        int randomIndex = random.nextInt(numbers.size());
        this.defult_timer=(int)Math.floor(Math.random() * 5+numbers.get(randomIndex));
        this.timer=this.defult_timer;
        switch(this.current_direction){
            case DIRECTION_LEFT:
                this.ghost_image=new Image(String.format(path+"/ghost%d%d_image.png" , this.level,2));
                break;
            case DIRECTION_RIGHT:
                this.ghost_image=new Image(String.format(path+"/ghost%d_image.png" , this.level));
                break;
        }
    }
    public void move() {
        // Check if the ghost enters a transport gate
        if (this.tilemap.ComeInTransportGate(x, this.y)) {
            setX(out_x); // Set the ghost's position to the exit gate's X-coordinate
            setY(out_y); // Set the ghost's position to the exit gate's Y-coordinate
            tilemap.updateTileMap(); // Update the tile map after transportation
        }

        // Check if the ghost can move to the next position without hitting a wall
        if (!tilemap.checkindex((int) this.x / grid_width, (int) this.y / grid_width) &&
                !this.tilemap.CollidedWithWall(this.x, this.y, this.current_direction)) {
            // Move the ghost in the current direction based on velocity
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
        } else {
            // Change direction if the ghost collides with a wall or reaches a blocked position
            changeDirection();
        }
    }

    public Boolean dead() {
        // Check if the ghost collides with a bomb bean and is in an active bomb state
        if (tilemap.bombstate == 2 && this.tilemap.CollidedWithBombbean(this.x, this.y)) {
            tilemap.CollidedWithBombbean(this.x, this.y); // Handle collision with the bomb bean
            WavPlay wavplay = new WavPlay("src/main/resources/music/ghostdead.wav");
            wavplay.play(); // Play ghost dead sound effect
            return true; // Ghost is dead
        }
        return false; // Ghost is not dead
    }

    void changeDirection() {
        this.timer--; // Decrease the timer for changing direction
        int new_direction = -1;

        // When the timer reaches 0, calculate a new direction
        if (this.timer == 0) {
            this.timer = this.defult_timer; // Reset the timer
            switch (this.current_direction) {
                case DIRECTION_DOWN:
                    // Randomly select a new direction from possible directions opposite to DOWN
                    new_direction = INVERSE_DOWN.get(random.nextInt(INVERSE_DOWN.size()));
                    break;
                case DIRECTION_UP:
                    new_direction = INVERSE_UP.get(random.nextInt(INVERSE_UP.size()));
                    break;
                case DIRECTION_LEFT:
                    new_direction = INVERSE_LEFT.get(random.nextInt(INVERSE_LEFT.size()));
                    break;
                case DIRECTION_RIGHT:
                    new_direction = INVERSE_RIGHT.get(random.nextInt(INVERSE_RIGHT.size()));
                    break;
            }
        }

        // If a new direction is determined
        if (new_direction != -1) {
            // Ensure the ghost is aligned with the grid before changing direction
            if ((this.x % this.grid_width == 0) && (this.y % this.grid_width == 0)) {
                // Check if the new direction does not lead to a wall collision
                if (!this.tilemap.CollidedWithWall(this.x, this.y, new_direction)) {
                    this.current_direction = new_direction; // Update the ghost's direction
                }
            }

            // Update ghost image based on the new direction
            switch (this.current_direction) {
                case DIRECTION_LEFT:
                    this.ghost_image = new Image(String.format(path + "/ghost%d%d_image.png", this.level, 2));
                    break;
                case DIRECTION_RIGHT:
                    this.ghost_image = new Image(String.format(path + "/ghost%d_image.png", this.level));
                    break;
            }
        }
    }

    boolean getfrozen() {
        // Check if the player uses an ice bean and the ghost is not already frozen
        if (this.tilemap.player.putaice && !frozen) {
            this.frozen = true; // Set the ghost to frozen
            // Schedule the ghost to unfreeze after 1 second
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), e -> {
                        this.frozen = false; // Unfreeze the ghost
                    })
            );
            timeline.setCycleCount(1); // Execute the timeline only once
            timeline.play();
            return false; // Return false to indicate the ghost was just frozen
        }
        return true; // Return true if the ghost is already frozen
    }

    public void paintSelf(GraphicsContext g) {
        // Draw the ghost's current image based on its state (frozen or normal)
        if (this.frozen) {
            g.drawImage(frozen_image, this.x, this.y); // Draw frozen image
        } else {
            g.drawImage(ghost_image, this.x, this.y); // Draw normal ghost image
        }
    }


    public List<String> getImage_index() {
        return image_index;
    }

    public void setImage_index(List<String> image_index) {
        this.image_index = image_index;
    }

    public int getCurrent_direction() {
        return current_direction;
    }

    public void setCurrent_direction(int current_direction) {
        this.current_direction = current_direction;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public int getDefult_timer() {
        return defult_timer;
    }

    public void setDefult_timer(int defult_timer) {
        this.defult_timer = defult_timer;
    }

    public Image getGhost_image() {
        return ghost_image;
    }

    public void setGhost_image(Image ghost_image) {
        this.ghost_image = ghost_image;
    }

    public Image getFrozen_image() {
        return frozen_image;
    }

    public void setFrozen_image(Image frozen_image) {
        this.frozen_image = frozen_image;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public Boolean getFrozen() {
        return frozen;
    }

    public void setFrozen(Boolean frozen) {
        this.frozen = frozen;
    }

    public TileMap getTilemap() {
        return tilemap;
    }

    public void setTilemap(TileMap tilemap) {
        this.tilemap = tilemap;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }
}

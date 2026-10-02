package Model;

import javafx.scene.canvas.GraphicsContext;
import interfaces.Drawable;
import javafx.scene.image.Image;

import static Control.MenuControl.path;


public class Wall implements Drawable {
    int x;
    int y;
    Image wall_image= new Image(path+"/wall_image.png");
    Wall(int x, int y){
        this.x=x;
        this.y=y;
    }
    public void paintSelf(GraphicsContext g){
        g.drawImage(wall_image, x, y);
    }
}

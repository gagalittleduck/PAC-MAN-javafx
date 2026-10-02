package Model;

import javafx.scene.canvas.GraphicsContext;
import interfaces.Drawable;
import javafx.scene.image.Image;

import static Control.MenuControl.path;


public class Key implements Drawable {
    Image key_image= new Image(path+"/key_image.png");
    //gate state 0: not collected ; state 1: collected
    int x;
    int y;
    Key(int x,int y){
        this.x=x;
        this.y=y;
    }
    public void paintSelf(GraphicsContext g){
        g.drawImage(key_image, x, y);
    }
}

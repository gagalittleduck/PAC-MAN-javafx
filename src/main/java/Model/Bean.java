package Model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import interfaces.Drawable;

import static Control.MenuControl.path;

public class Bean implements Drawable {
    Image bean_image = new Image(path+"/bean_image.png");
    int x;
    int y;
    Bean(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void paintSelf(GraphicsContext g){
        g.drawImage(bean_image, x, y);
    }
}

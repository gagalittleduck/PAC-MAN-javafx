package Model;

import javafx.scene.canvas.GraphicsContext;
import interfaces.DirectionMap;
import interfaces.Drawable;
import javafx.scene.image.Image;
import static Control.MenuControl.path;


public class Transgate implements Drawable, DirectionMap {
    int x;
    int y;
    Image inImage=new Image(path+"/in_image.png");
    Image outImage=new Image(path+"/out_image.png");
    String type="in";
    Transgate(int x, int y,String type){
        this.x = x;
        this.y = y;
        this.type = type;
    }
    public void paintSelf(GraphicsContext g){
        if(type=="in"){
            g.drawImage(inImage, x, y);
        }else if(type=="out"){
            g.drawImage(outImage, x, y);
        }
    }
}

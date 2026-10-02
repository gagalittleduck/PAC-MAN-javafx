package Model;

import javafx.scene.canvas.GraphicsContext;
import interfaces.Drawable;
import javafx.scene.image.Image;

import static Control.MenuControl.path;

public class BombBean implements Drawable {
    int x;
    int y;
    int state=0;
    Image image ;
    BombBean(int x,int y,int state) {
        this.x = x;
        this.y = y;
        this.state = state;
    }

    @Override
    public void paintSelf(GraphicsContext g) {

        if(this.state!=1&&this.state<3){
            this.image=new Image(String.format(path+"/bomb%d_image.png",this.state));
            g.drawImage(this.image,x,y);
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}

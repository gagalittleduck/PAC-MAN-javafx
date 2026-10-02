package Model;

import javafx.scene.canvas.GraphicsContext;
import interfaces.Drawable;
import javafx.scene.image.Image;

import static Control.MenuControl.path;

public class Gate implements Drawable {

    int state=0;
    int x;
    int y;

    Image gate_image=new Image(path+"/gate_image.png");;
    //state 0: closed; state: opening

    Gate(int x,int y,int state){
        this.x=x;
        this.y=y;
        this.state=state;
    }


    public void paintSelf(GraphicsContext g){

        g.drawImage(gate_image, x, y);
    }
}

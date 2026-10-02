package Model;

import javafx.scene.canvas.GraphicsContext;
import interfaces.Drawable;
import javafx.scene.image.Image;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Control.MenuControl.path;


public class IceBean implements Drawable {
        int x;
        int y;
        TileMap tilemap;
        int state=0;
        Image image=new Image(String.format(path+"/ice_image.png"));
        IceBean(int x,int y,TileMap tilemap) {
            this.x = x;
            this.y = y;
            this.tilemap = tilemap;
        }
        public Boolean picked(){
            if(tilemap.IceCollidedByPlayer(this.x,this.y)){
                state=1;
                return true;
            }
            return false;
        }
        @Override
        public void paintSelf(GraphicsContext g) {
            g.drawImage(this.image,x,y);
        }

}

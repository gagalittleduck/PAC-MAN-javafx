package interfaces;

import Model.TileMap;
import javafx.scene.layout.BorderPane;

public interface GameAlert {
    void showGameAlert(BorderPane root, TileMap tilemap);
}

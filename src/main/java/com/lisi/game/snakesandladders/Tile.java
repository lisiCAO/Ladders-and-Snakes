package com.lisi.game.snakesandladders;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {

    public Tile(int x, int y) {
        setWidth(x);
        setHeight(y);

        setFill(Color.PINK);
        setStroke(Color.BLACK);
    }
}

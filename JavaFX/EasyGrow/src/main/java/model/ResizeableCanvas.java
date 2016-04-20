package model;

import javafx.scene.canvas.Canvas;

/**
 * Created by alex on 18.04.16.
 */
public class ResizeableCanvas extends Canvas {
    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double minWidth(double height) {
        return 0;
    }

    @Override
    public double minHeight(double width) {
        return 0;
    }

    @Override
    public void resize(double width, double height) {
        super.setWidth(width);
        super.setHeight(height);

    }
}

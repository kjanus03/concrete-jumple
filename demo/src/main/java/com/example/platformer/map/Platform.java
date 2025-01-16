package map;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Platform {
    private Rectangle platformView;
    private double x, y;

    public Platform(double x, double y, double width, double height) {
        platformView = new Rectangle(width, height, Color.BROWN);
        this.x = x;
        this.y = y;
        platformView.setTranslateX(x);
        platformView.setTranslateY(y);
    }

    public Rectangle getView() {
        return platformView;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return (int) platformView.getWidth();
    }
}

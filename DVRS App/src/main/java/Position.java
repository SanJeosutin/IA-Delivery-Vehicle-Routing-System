import java.io.Serializable;

public class Position implements Serializable {
    private double x, y;

    public Position(double posX, double posY){
        x = posX;
        y = posY;
    }

    public double getX() {
        return x;
    }

    public void setX(double posX) {
        x = posX;
    }

    public double getY() {
        return y;
    }

    public void setY(double posY) {
        y = posY;
    }
}
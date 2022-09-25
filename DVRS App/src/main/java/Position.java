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

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
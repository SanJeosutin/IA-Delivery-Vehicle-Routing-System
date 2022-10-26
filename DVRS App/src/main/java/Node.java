import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.Serializable;

public class Node implements Serializable {
    private String id;
    private Position pos;

    public Node(String id, Position pos) {
        this.id = id;
        this.pos = pos;
    }

    public Circle create() {
        Circle node = new Circle(pos.getX(), pos.getY(), 5, Color.BLACK);
        node.toBack();
        return node;
    }

    public String getId() {
        return id;
    }

    public Position getPos() {
        return pos;
    }

    public double getX() {
        return pos.getX();
    }

    public double getY() {
        return pos.getY();
    }

    public boolean amI(String id) {
        return id.equals(getId());
    }
}

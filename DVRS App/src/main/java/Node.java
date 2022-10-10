import java.io.Serializable;

public class Node implements Serializable {
    private String id;
    private Position pos;

    public Node(String id, Position pos) {
        this.id = id;
        this.pos = pos;
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

    public  double getY() {
        return pos.getY();
    }

    public boolean amI() {
        return id.equals(id);
    }
}

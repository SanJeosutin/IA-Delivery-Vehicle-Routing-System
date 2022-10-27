import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {
    private List<Node> route;
    public void setRoute(List<Node> nodeRoute) { route = nodeRoute;}
        public List<Node> getRoute() {return route;}
}

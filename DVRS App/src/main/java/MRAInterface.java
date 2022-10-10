import java.util.List;

public interface MRAInterface {
    void startRoute(List<String> DAs);
    void newNode(Node n);
    void removeNode(Node n);
    void addParcel(Parcel p);
    void removeParcel(Parcel p);
}

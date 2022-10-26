public class Parcel {
    private int weight;
    private String name, destination;

    public Parcel(int parcelWeight, String parcelDestination, String parcelName) {
        setWeight(parcelWeight);
        setName(parcelName);
        setDestination(parcelDestination);

    }

    @Override
    public String toString() {
        return getName() + " | Destination: " + getDestination() + " | Weight: " + getWeight();
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}

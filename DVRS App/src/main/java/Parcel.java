public class Parcel {
    private String destination;

    public Parcel(String parcelDestination) {
        destination = parcelDestination;
    }

    @Override
    public String toString() {
        return "Delivering to: " + destination.toLowerCase();
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}

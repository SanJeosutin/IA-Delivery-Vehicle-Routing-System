import java.util.ArrayList;
import java.util.List;

public class DataModel {
    private final int warehouse;
    private List<Integer> demands;
    private List<Integer> demandsCopy;
    private int numberOfDAs = 0;
    private final List<Integer> parcelsWeight;
    private final List<Integer> agentsCapacity;
    private final List<List<Double>> distanceMatrix;

    public DataModel(
            List<List<Double>> distanceMatrix,
            int numberOfDAs,
            List<Integer> demands,
            List<Integer> agentsCapacity,
            int warehouse,
            List<Integer> parcelsWeight) {

        this.warehouse = warehouse;
        this.demands = demands;
        this.demandsCopy = List.copyOf(demands);
        this.numberOfDAs = numberOfDAs;
        this.parcelsWeight = parcelsWeight;
        this.agentsCapacity = agentsCapacity;
        this.distanceMatrix = distanceMatrix;
    }


    public Double getDistanceBetween(int node1, int node2) {
        return distanceMatrix.get(node1).get(node2);
    }

    public int getDistanceSize(){
        return distanceMatrix.size();
    }

    public int getTotalDAs(){
        return numberOfDAs;
    }

    public List<Integer> getDemands (){
        return demands;
    }

    public Integer getAgentMaxCapacity(int agentID){
        return agentsCapacity.get(agentID);
    }

    public void setAgentsMaxCapacity(int agentID, int agentCapacity){
        agentsCapacity.set(agentID, agentCapacity);
    }

    public int getWarehouse(){
        return warehouse;
    }

    public Integer getParcelWeight(int parcelID) {
        return parcelsWeight.get(parcelID);
    }

    public void resetDemands(){
        demands = new ArrayList<>(List.copyOf(demandsCopy));
    }
}

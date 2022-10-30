import com.sun.javafx.scene.shape.ArcHelper;

import java.util.ArrayList;
import java.util.List;

public class RoutingLogic {
    static class Agent {
        public int agentID;
        public Double routeCost, loadWeight;
        public List<Integer> route, negativeDomain, positiveDomain;

        public Agent(int id, int startLocation, double payloadWeight) {
            agentID = id;
            route = new ArrayList<>();
            negativeDomain = new ArrayList<>();
            positiveDomain = new ArrayList<>();

            route.add(startLocation);
            loadWeight = payloadWeight;
        }

    }

    static class Search {
        public double distanceMean;
        public double negativeDomain, positiveDomain;
        public double negativeMultiplyer, positiveMultiplyer;
        public boolean reverseSearch;

        public Search(DataModel agentData) {
            reverseSearch = false;
            negativeDomain = 0.05;
            positiveDomain = 0.25;
            negativeMultiplyer = 0.5;
            positiveMultiplyer = 0.25;

            double tempDistanceMatrix = 0;
            for (int i = 0; i < agentData.getDistanceSize(); i++) {
                tempDistanceMatrix += agentData.getDistanceBetween(0, i);
            }
            distanceMean = tempDistanceMatrix / agentData.getDistanceSize();
        }
    }

    public static List<Agent> findOptimalRoute(DataModel agentData, Search searchLocation) {
        List<Agent> DAs = new ArrayList<>();
        //initialise routing for agents that contain 3 routes and add 1st one as the initial warehouse location
        for (int i = 0; i < agentData.getTotalDAs(); i++) {
            DAs.add(new Agent(i, agentData.getWarehouse(), agentData.getAgentMaxCapacity(i)));
        }

        //add demands for each agent
        while (agentData.getDemands().contains(1)) {
            List<Integer> currentLocation = new ArrayList<>();
            List<Integer> agentDomain = new ArrayList<>();

            for (int i = 0; i < agentData.getDemands().size(); i++) {
                agentDomain.add(i);
            }

            //populate +ve domain
            getWeightConstraints(agentData, DAs, agentDomain, searchLocation);

            //redefining each agent -ve domains
            for (int i = 0; i < DAs.size(); i++) {
                int eachDALocation = DAs.get(i).route.get(DAs.get(i).route.size() - 1);
                currentLocation.add(eachDALocation);

                DAs.get(i).negativeDomain.clear();
                DAs.get(i).negativeDomain.addAll(
                        getNearbyDomain(
                                searchLocation.negativeDomain * searchLocation.distanceMean,
                                agentData, agentDomain,
                                DAs.get(i).route.get(eachDALocation)
                        )
                );

                // should find the best optimised route for the DAs

            }

            //add final destination along with a cost to go back to initial pos
            for(int i =  0; i < DAs.size(); i++){
                int eachDALocation = DAs.get(i).route.size() - 1;
                DAs.get(i).routeCost += agentData.getDistanceBetween(eachDALocation, 0);
                DAs.get(i).route.add(0);
            }
        }

        return DAs;
    }

    public static List<Integer> getNearbyDomain(double parcelRange, DataModel parcelData, List<Integer> parcelDomain, Integer parcelLocation) {
        List<Integer> nearbyDomain = new ArrayList<>();

        for(int i = 0; i < parcelDomain.size(); i++){
            if(parcelData.getDistanceBetween(parcelLocation, parcelDomain.get(i)) < parcelRange){
                nearbyDomain.add(parcelDomain.get(i));
            }
        }

        return nearbyDomain;
    }

    public static void getWeightConstraints(
            DataModel agentData,
            List<Agent> DAs,
            List<Integer> agentDomains,
            Search search) {
        List<Integer> priority = new ArrayList<>();

        for (Agent DA : DAs) {
            DA.positiveDomain.clear();

            for (int i = 0; i < agentDomains.size(); i++) {
                if (agentData.getParcelWeight(agentDomains.get(i)) >= DA.loadWeight) {
                    priority.add(agentDomains.get(i));
                }
            }
        }
    }

    public static List<List<Integer>> routing(DataModel agentData) {
        List<List<Integer>> routes = new ArrayList<>();
        Search searchLocation = new Search(agentData);
        List<List<Agent>> DAs = new ArrayList<>();

        while (DAs.size() < agentData.getTotalDAs()) {
            List<Agent> DAsManager = findOptimalRoute(agentData, searchLocation);

            if(!agentData.getDemands().contains(1)){
                DAs.add(DAsManager);
            }

            agentData.resetDemands();
        }

        for(int i = 0 ; i < DAs.size(); i++){
            routes.add(DAs.get(i).get(i).route);
        }

        return routes;
    }
}

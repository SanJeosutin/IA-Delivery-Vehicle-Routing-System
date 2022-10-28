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

        static class Search{
            public double distanceMean;
            public double negativeDomain, positiveDomain;
            public double negativeMultiplyer, positiveMultiplyer;
            public double reverseSearch;

            public Search(DataModel agentData){
                //getMedianDistance(agentData);


            }
        }
    }
}

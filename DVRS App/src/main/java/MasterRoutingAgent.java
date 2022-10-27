import jade.core.AID;
import jade.core.Agent;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MasterRoutingAgent extends Agent implements MRAInterface {

    private Integer numOfResponses = 0;
    private List<Node> nodes = new ArrayList<>();
    private List<Parcel> parcels = new ArrayList<>();
    private List<Integer> agentCapacity = new ArrayList<>();

    private Position position = new Position(0, 0);
    private List<List<Double>> distanceMatrix = new ArrayList<>();

    public static String ONTOLOGY_CAPACITY_REQUEST = "capacity-request";
    public static String ONTOLOGY_CAPACITY_RESPONSE = "capacity-response";
    public static final String ONTOLOGY_DELIVERY_ROUTE = "delivery-route";

    protected void setup() {
        //initialised O2AInterface before calling in MRAInterface
        registerO2AInterface(MRAInterface.class, this);

        System.out.println("Hello! " + getAID().getName() + " is Ready for duty!");

        //First set-up message receiving behaviour
        CyclicBehaviour messageListeningBehaviour = new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    if (msg.getOntology().equals(ONTOLOGY_CAPACITY_RESPONSE)) {
                        synchronized (numOfResponses) {
                            numOfResponses++;

                            String[] response = msg.getContent().split(",");
                            agentCapacity.set(Integer.parseInt(response[0]), Integer.valueOf(response[1]));
                        }
                    }
                }
            }
        };
        addBehaviour(messageListeningBehaviour);
        newNode(new Node("WAREHOUSE", position));

    }

    @Override
    public void startRoute(List<String> DAs) {
        //should called in the routing class when implemented
    }

    @Override
    public synchronized void newNode(Node n) {
        // simple check when node is already on the 'list'
        if (nodes.contains(n)) {
            return;
        }

        List<Double> nodeDistances = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            // calculate distance different from pos x & y
            double distanceDiffX = Math.pow(n.getX() - nodes.get(i).getX(), 2);
            double distanceDiffY = Math.pow(n.getY() - nodes.get(i).getY(), 2);
            double distance = Math.sqrt(distanceDiffX + distanceDiffY);

            nodeDistances.add(distance);
            distanceMatrix.get(i).add(distance);
        }
        //add it to itself in matrix
        nodeDistances.add((double) 0);
        distanceMatrix.add(nodeDistances);
        nodes.add(n);
    }

    @Override
    public void removeNode(Node n) {
        int pos = nodes.indexOf(n);

        if (pos >= 0) {
            for (int i = 0; i < distanceMatrix.size(); i++) {
                if (i != pos) {
                    distanceMatrix.get(i).remove(pos);
                }
            }
            distanceMatrix.remove(pos);
            nodes.remove(pos);
        }
    }

    @Override
    public void addParcel(Parcel p) {
        parcels.add(p);
        //Send messages to two agents (hard-coded)
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent("Ping");
        for (int i = 1; i <= 2; i++) {
            msg.addReceiver(new AID("DA" + i, AID.ISLOCALNAME));
        }

        //Send message (only once)
        System.out.println(getLocalName() + ": Sending message " + msg.getContent() + " to ");
        Iterator receivers = msg.getAllIntendedReceiver();
        while (receivers.hasNext()) {
            System.out.println(((AID) receivers.next()).getLocalName());
        }
        send(msg);
    }

    @Override
    public void removeParcel(Parcel p) {
        parcels.remove(p);
    }

    private void getCapacity(List<String> DAs) throws InterruptedException {
        agentCapacity.clear();

        for (int i = 0; i < DAs.size(); i++) {
            agentCapacity.add(0);
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);

            msg.setOntology(ONTOLOGY_CAPACITY_REQUEST);
            msg.addReceiver(new AID(DAs.get(i), AID.ISLOCALNAME));
            msg.setContent(String.valueOf(i));

            send(msg);
        }

        while (numOfResponses != DAs.size()) {
            Thread.sleep(500);
        }
    }

    private void sendRoutes(List<String> DAs) throws InterruptedException {
        List<List<Integer>> newRoute;
        getCapacity(DAs);

        List<Integer> demands = new ArrayList<>();
        List<Integer> parcelsWeight = new ArrayList<>();

        distanceMatrix.forEach(doubles -> demands.add(0));
        distanceMatrix.forEach(distance -> parcelsWeight.add(0));

        for (Parcel parcel : parcels) {
            Optional<Node> destination = nodes.stream().filter(node -> node.amI(parcel.getDestination())).findFirst();

            if (destination.isPresent()) {
                int index = nodes.indexOf(destination.get());
                demands.set(index,1);
                parcelsWeight.set(index,(parcelsWeight.get(index) + parcel.getWeight()));
            }

        }

    }
}

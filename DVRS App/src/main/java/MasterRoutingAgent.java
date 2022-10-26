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

public class MasterRoutingAgent extends Agent implements MRAInterface {
    private List<Node> nodes = new ArrayList<>();
    private List<Parcel> parcels = new ArrayList<>();
    private List<Integer> agentCapacity = new ArrayList<>();

    private Position position = new Position(0, 0);
    private List<List<Double>> distanceMatrix = new ArrayList<>();


    protected void setup() {
        //initialised O2AInterface before calling in MRAInterface
        registerO2AInterface(MRAInterface.class, this);

        System.out.println("Hello! Agent " + getAID().getName() + " is Ready");

        //First set-up message receiving behaviour
        CyclicBehaviour messageListeningBehaviour = new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String[] response = msg.getContent().split(",");

                }
                block();
            }
        };
        addBehaviour(messageListeningBehaviour);

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
    public  void removeNode(Node n) {
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
}

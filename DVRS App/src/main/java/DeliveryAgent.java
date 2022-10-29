import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;


public class DeliveryAgent extends Agent {
    private int speed = 25;
    private int maxCapacity;
    private boolean outDelivering = false;
    private List<Node> route = new ArrayList<Node>();
    private Circle agentBody;

    protected void setup() {
        System.out.println("Hello! " + getAID().getName() + " is Ready for duty!");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage aclMessage = receive();
                // check whether there is a message. When there is, check if the message is for route delivery
                if (aclMessage != null) {
                    if (aclMessage.getOntology().equals(MasterRoutingAgent.ONTOLOGY_DELIVERY_ROUTE)) {
                        // when the agent is not moving, get a route coordinate from MRA
                        if (!outDelivering) {
                            System.out.println("Got delivery route!");
                            try {
                                Message message = (Message) aclMessage.getContentObject();
                                route = message.getRoute();

                                System.out.print("Route Coord: [" + route.get(0).getX() + " , " + route.get(0).getY() + "].");

                            } catch (UnreadableException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        // otherwise request MRA for parcels
                    } else if (aclMessage.getOntology().equals(MasterRoutingAgent.ONTOLOGY_CAPACITY_REQUEST)){
                        ACLMessage response = new ACLMessage(ACLMessage.INFORM);
                        response.setOntology(MasterRoutingAgent.ONTOLOGY_CAPACITY_RESPONSE);
                        response.setContent(aclMessage.getContent()+":"+maxCapacity);
                        response.addReceiver(aclMessage.getSender());

                        System.out.println("RESPONSE: " + response);

                        send(response);
                    }
                }
            }
        });
        //draw the agent on screen, and set the agent capacity when initialised.
        Object[] args = getArguments();
        agentBody = (Circle) args[0];
        maxCapacity = (int) args[1];
    }
}
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;


public class DeliveryAgent extends Agent {
    private int maxCapacity;
    private List<Node> route = new ArrayList<Node>();
    private Circle agentBody;

    protected void setup() {
        System.out.println("Hello! Delivery Agent " + getAID().getName() + " is Ready");
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage message = receive();
                if (message != null) {
                    System.out.println(getLocalName() + " Received message " + message.getContent() + " from " + message.getSender().getLocalName());
                    ACLMessage reply = message.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent("Agent " + getLocalName() + " responding!");
                    System.out.println(getLocalName() + ": Sending response " + reply.getContent() + " to " + message.getAllReceiver().next());
                    send(reply);
                }
            }
        });

        Object[] args = getArguments();
        agentBody = (Circle) args[0];
        maxCapacity = (int) args[1];
    }
}
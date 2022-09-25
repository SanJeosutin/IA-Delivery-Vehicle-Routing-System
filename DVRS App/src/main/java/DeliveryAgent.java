import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.List;


public class DeliveryAgent extends Agent {
    private int maxCapacity;


    protected void setup() {
        System.out.println("Hello! Agent " + getAID().getName() + " is Ready");
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage message = receive();

                if (message != null) {
                    System.out.println("Received message from " + message.getSender().getName());
                } else {
                    block();
                }

            }
        });
    }

}

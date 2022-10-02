import jade.core.AID;
import jade.core.Agent;
<<<<<<< HEAD
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;

import java.util.Iterator;
=======
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
>>>>>>> origin/basic-agent-implementation

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MasterRoutingAgent extends Agent {

    private Position position = new Position(0, 0);
    private List<Parcel> parcels = new ArrayList<Parcel>();


    protected void setup() {
<<<<<<< HEAD
        System.out.println("Hello! Agent " + getAID().getName() + " is Ready");
        //First set-up message receiving behaviour
        CyclicBehaviour messageListeningBehaviour = new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    System.out.println(getLocalName() + ": Received response" + msg.getContent() + " from " + msg.getSender().getLocalName());
                }
                block();
            }
        };
        addBehaviour(messageListeningBehaviour);

        //Send messages to two agents (hard-coded)
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent("Ping");
        for (int i = 1; i <= 2; i++) {
            msg.addReceiver(new AID("DA"+i, AID.ISLOCALNAME));
        }

        //Send message (only once)
        System.out.println(getLocalName() + ": Sending message " + msg.getContent() + " to ");
        Iterator receivers = msg.getAllIntendedReceiver();
        while(receivers.hasNext()) {
            System.out.println(((AID)receivers.next()).getLocalName());
        }
        send(msg);
    }

=======
        {
            CyclicBehaviour msgListenBehaviour = new CyclicBehaviour(this) {
                public void action() {
                    ACLMessage message = receive();
                    if (message != null) {
                        System.out.println(getLocalName() + ": Received response " + message.getContent() + " from " + message.getSender().getLocalName());
                    }
                    block();
                }
            };
            addBehaviour(msgListenBehaviour);

            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("Contacting agents...");
            for (int i = 1; i <= 3; i++) {
                msg.addReceiver(new AID("d" + i, AID.ISLOCALNAME));
            }

            System.out.println(getLocalName() + ": Sending message " + msg.getContent() + " to ");
            Iterator receivers = msg.getAllIntendedReceiver();
            while (receivers.hasNext()) {
                System.out.println(((AID) receivers.next()).getLocalName());
            }
            send(msg);
        }

    }
>>>>>>> origin/basic-agent-implementation
}

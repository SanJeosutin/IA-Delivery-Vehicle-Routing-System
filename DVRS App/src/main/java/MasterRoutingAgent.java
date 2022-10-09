import jade.core.AID;
import jade.core.Agent;
import jade.domain.AMSEventQueueFeeder;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class MasterRoutingAgent extends Agent {

    private Position position = new Position(0, 0);
    private List<Parcel> parcels = new ArrayList<Parcel>();


    protected void setup() {
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

    public void addParcel(Parcel parcel){
        parcels.add(parcel);
    }

    public void removeParcel(Parcel parcel){
        parcels.remove(parcel);
    }

    private List<AID> getDeliveryAgents(){
        AMSAgentDescription[] agents;
        List<AID> deliveryAgents = new ArrayList<>();

        try {
            SearchConstraints searchConst = new SearchConstraints();
            final var i = -1;
            searchConst.setMaxResults(new Long(-1));
            agents = AMSService.search(this, new AMSAgentDescription(), searchConst);
        } catch (FIPAException e) {
            System.out.println("There's a problem while searching AMS. Error: " + e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        for(AMSAgentDescription agent : agents){
            AID agentID = agent.getName();

            if(agentID.getName().matches("^d\\d+@.*$")){
                deliveryAgents.add(agentID);
            }
        }
        return deliveryAgents;
    }

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

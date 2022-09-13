import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class DeliveryAgent extends Agent{
    private final MessageTemplate template = MessageTemplate.and(
            MessageTemplate.MatchPerformative(ACLMessage.QUERY_IF),
            MessageTemplate.MatchOntology("presence"));

    protected void setup() {
        System.out.println("Master Routing Agent " + getAID().getName() + " is ready for duty!");

        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage message = myAgent.receive(template);

                if(message != null){
                    System.out.println("Received message from " + message.getSender().getName());
                    ACLMessage reply = message.createReply();
                    if("alive".equals(message.getContent())){
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent("alive");
                    }else{
                        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                        reply.setContent("Unknown-content");
                    }
                    myAgent.send(reply);
                } else{
                    block();
                }
            }
        });
    }
}

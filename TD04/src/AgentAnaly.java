import java.util.List;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgentAnaly extends Agent{
	ObjectMapper mapper = new ObjectMapper();
	Message msg;
	List<Cellule> lcc = new ArrayList<Cellule>();
	
	protected void setup() {
		System.out.println(getName() + "--> Installed");

		ACLMessage sbcAnaly = new ACLMessage(ACLMessage.SUBSCRIBE);
		sbcAnaly.addReceiver(new AID("SIMULATION", AID.ISLOCALNAME));
		sbcAnaly.setContent(this.getLocalName());
		
		send(sbcAnaly);
		
		addBehaviour(new BehaviourAnalyse());
	}
	
	private class BehaviourAnalyse extends CyclicBehaviour{		
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage message = receive(mt);
			
			if(message != null){
				try {
					msg = mapper.readValue(message.getContent(), Message.class);
					  
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

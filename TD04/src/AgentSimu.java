import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgentSimu extends Agent {
	boolean fin = false;
	long time = 1000;
	List<String> agentsAnalyse = new ArrayList<String>();
	ObjectMapper mapper = new ObjectMapper();

	protected void setup() {
		System.out.println(getName() + "--> Installed");

		addBehaviour(new BehaviourSubscription());
		addBehaviour(new BehaviourSimu(this, time));
	}

	public class BehaviourSubscription extends CyclicBehaviour {
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE);
			ACLMessage message = receive(mt);
			
			if(message != null) {
				agentsAnalyse.add(message.getContent());
				System.out.println(getName() + " : " + message.getContent() + " added.");
			}
			
			mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			message = receive(mt);
			
			if(message != null) {
				System.out.println(getName() + " : Sudoku reussi avec compteur = " + message.getContent() + ".");
				
				fin = true;
			}
		}
	}

	public class BehaviourSimu extends TickerBehaviour {
		public BehaviourSimu(Agent a, long timeout) {
			super(a, timeout);
		}

		public void onTick() {
			if(!fin){
				ACLMessage rqtSimu = new ACLMessage(ACLMessage.REQUEST);
				Message msg = new Message("reply-to", "");
				
				for (String nom : agentsAnalyse) {
					rqtSimu.addReceiver(new AID("ENVIRONEMENT", AID.ISLOCALNAME));
	
					msg.setObjet(nom);
					try {
						rqtSimu.setContent(mapper.writeValueAsString(msg));
						//System.out.println(getName() + " : reply-to : " + nom);
						send(rqtSimu);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else
				done();
		}
	}
}

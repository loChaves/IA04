import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgentSimu extends Agent {
	private static final long serialVersionUID = 1L;
	boolean fin = false;
	long time = 500;
	List<String> agentsAnalyse = new ArrayList<String>();
	ObjectMapper mapper = new ObjectMapper();

	protected void setup() {
		System.out.println(getName() + "--> Installed");

		SequentialBehaviour mainBehaviour = new SequentialBehaviour();
		
		mainBehaviour.addSubBehaviour(new BehaviourSubscription(this, 1000));
		
		ParallelBehaviour parBehaviour = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
		
		parBehaviour.addSubBehaviour(new BehaviourSimu(this, time));
		parBehaviour.addSubBehaviour(new BehaviourFin());
		
		mainBehaviour.addSubBehaviour(parBehaviour);
		
		addBehaviour(mainBehaviour);
	}
	
	public class BehaviourSubscription extends WakerBehaviour {
		private static final long serialVersionUID = 1L;
		public BehaviourSubscription(Agent a, long timeout) {
			super(a, timeout);
		}
		public void onWake() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE);
			ACLMessage message = receive(mt);
			
			if(message != null) {
				agentsAnalyse.add(message.getContent());
				System.out.println(getName() + " : " + message.getContent() + " added.");
			}
		}
	}
	
	public class BehaviourFin extends SimpleBehaviour {
		private static final long serialVersionUID = 1L;
		public void action(){
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage message = receive(mt);
			
			if(message != null) {
				System.out.println(getName() + " : Sudoku reussi.");
				System.out.println(message.getContent());
				fin = true;
			}
		}
		
		public boolean done(){
			return fin;
		}
	}

	public class BehaviourSimu extends TickerBehaviour {
		private static final long serialVersionUID = 1L;
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
				stop();
		}
	}
}

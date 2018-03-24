import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgentEnvi extends Agent{
	int compteur = 0;
	ObjectMapper mapper = new ObjectMapper();
	Message msg;
	Sudoku s = new Sudoku();
	
	protected void setup() {
		System.out.println(getName() + "--> Installed");

		addBehaviour(new BehaviourCompte());
	}
	
	private class BehaviourCompte extends CyclicBehaviour{		
		public void action(){
			
			if(!s.isDone()) {
				MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
				ACLMessage message = receive(mt);
				
				ACLMessage rqtEnvi = new ACLMessage(ACLMessage.REQUEST);
				
				if(message != null){
					try {
						msg = mapper.readValue(message.getContent(), Message.class);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
					rqtEnvi.addReceiver(new AID(msg.getObjet(), AID.ISLOCALNAME));
							
					int c = compteur%26;
							
					if(c >= 0 && c <= 8){
						msg = new Message("analyse", s.getLigne(c));
					}else if(c >= 8 && c <= 16){
						msg = new Message("analyse", s.getColonne(c-8));
					}else if(c >= 17 && c <= 25){
						msg = new Message("analyse", s.getCarre(c-17));
					}
							
					//System.out.println("Compteur : " + compteur);
					//System.out.println(message.getContent() + " : " + msg.getObjet());
					try {
						rqtEnvi.setContent(mapper.writeValueAsString(msg));
						send(rqtEnvi);
						compteur++;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("Sudoku is done.");
				ACLMessage infEnvi = new ACLMessage(ACLMessage.INFORM);
				infEnvi.addReceiver(new AID("SIMULATION", AID.ISLOCALNAME));
				infEnvi.setContent(new Integer(compteur).toString());
				send(infEnvi);
			}
		}
	}
}

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgentAnaly extends Agent{
	private static final long serialVersionUID = 1L;
	ObjectMapper mapper = new ObjectMapper();
	Message msg;
	List<Cellule> cells = new ArrayList<Cellule>();
	
	protected void setup() {
		System.out.println(getName() + "--> Installed");

		ACLMessage sbcAnaly = new ACLMessage(ACLMessage.SUBSCRIBE);
		sbcAnaly.addReceiver(new AID("SIMULATION", AID.ISLOCALNAME));
		sbcAnaly.setContent(this.getLocalName());
		
		send(sbcAnaly);
		
		addBehaviour(new BehaviourAnalyse());
	}
	
	private class BehaviourAnalyse extends CyclicBehaviour{
		private static final long serialVersionUID = 1L;
		
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage message = receive(mt);
			
			if(message != null){
				ACLMessage reply = message.createReply();
				reply.setPerformative(ACLMessage.INFORM);
				
				try {
					msg = mapper.readValue(message.getContent(), Message.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				cells = msg.getList();
				Collections.sort(cells);
				for(Cellule c : cells) {
					if(c.getValeur() != 0) {
						Integer v = c.getValeur();
						for(Cellule d : cells)
							d.rmPossible(v);
					}else if(c.getPossibles().size() == 1) {
						Integer v = c.getPossibles().get(0);
						try {
							c.setValeur(v);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						for(Cellule d : cells)
							d.rmPossible(v);
					}else if(c.getPossibles().size() > 1) {
						boolean uneSeule = false;
						Integer n = -1;
						for(Integer i : c.getPossibles()) {
							uneSeule = true;
							n = i;
							for(Cellule d : cells) {
								if(c.getPosition() != d.getPosition()) {
									if(d.getPossibles().contains(i))
										uneSeule = false;
								}
							}
							if(uneSeule)
								break;
						}
						
						if(uneSeule) {
							try {
								c.setValeur(n);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						
						if(c.getPossibles().size() == 2) {
							boolean deuxSeule = false;
							Cellule e = new Cellule();
							for(Cellule d : cells) {
								if(c.getPosition() != d.getPosition() && d.getPossibles().size() == 2) {
									e = d;
									if(d.getPossibles().contains(c.getPossibles().get(0)) && d.getPossibles().contains(c.getPossibles().get(1)))
										deuxSeule = true;
								}
								if(deuxSeule)
									break;
							}
							if(deuxSeule){
								for(Cellule d : cells) {
									if(c.getPosition() != d.getPosition() && e.getPosition() != d.getPosition()) {
										d.rmPossible(c.getPossibles().get(0));
										d.rmPossible(c.getPossibles().get(1));
									}
								}
							}
						}
					}
				}
				
				msg = new Message("response", cells);
				try {
					reply.setContent(mapper.writeValueAsString(msg));
					send(reply);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

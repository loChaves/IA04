import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class NodeAgent extends Agent {
	boolean init = false;
	int valeur;
	String gauche = null, droite = null;
	
	protected void setup() {		
		addBehaviour(new InsertBehaviour());
		addBehaviour(new ExisteBehaviour());
		addBehaviour(new AfficheBehaviour());
		
		System.out.println(getName() + "--> Installed");
	}
	
	public class InsertBehaviour extends CyclicBehaviour {
		
		public void action(){			
			MessageTemplate mt = MessageTemplate.MatchConversationId("insert");
			ACLMessage message = receive(mt);
				
			if(message != null){
				ACLMessage reply = message.createReply();
				
				if(message.getPerformative() == ACLMessage.REQUEST){
					int cont = Integer.parseInt(message.getContent());
					
					if(!init){
						init = true;
						valeur = cont;
						
						reply.setPerformative(ACLMessage.INFORM);
						reply.setContent(String.valueOf(cont) + " est insere.");
						send(reply);
					} else if(cont < valeur){
						if(gauche == null){
							// Créer un nouveau fils
							try {
								AgentController ga = this.myAgent.getContainerController().createNewAgent(String.valueOf(cont), "NodeAgent", null);
								
								ga.start();
								
								gauche = ga.getName();
								
								ACLMessage rqtNode = new ACLMessage(ACLMessage.REQUEST);
								rqtNode.addReceiver(new AID(gauche, AID.ISLOCALNAME));
								
								rqtNode.setContent(String.valueOf(cont));
								rqtNode.setConversationId("insert");
								
								send(rqtNode);
								
								reply.setPerformative(ACLMessage.INFORM);
								reply.setContent(String.valueOf(cont) + " est insere.");
								send(reply);
							} catch (StaleProxyException e) {
								// TODO Auto-generated catch block
								
								e.printStackTrace();
							}
						} else{
							ACLMessage rqtNode = new ACLMessage(ACLMessage.REQUEST);
							rqtNode.addReceiver(new AID(gauche, AID.ISLOCALNAME));
							
							rqtNode.setContent(String.valueOf(cont));
							rqtNode.setConversationId("insert");
							
							send(rqtNode);
						}
					} else if(cont > valeur){
						if(droite == null){
							// Créer un nouveau fils
							try {
								AgentController ga = this.myAgent.getContainerController().createNewAgent(String.valueOf(cont), "NodeAgent", null);
								
								ga.start();
								
								droite = ga.getName();
								
								ACLMessage rqtNode = new ACLMessage(ACLMessage.REQUEST);
								rqtNode.addReceiver(new AID(droite, AID.ISLOCALNAME));
								
								rqtNode.setContent(String.valueOf(cont));
								rqtNode.setConversationId("insert");
								
								send(rqtNode);
								
								reply.setPerformative(ACLMessage.INFORM);
								reply.setContent(String.valueOf(cont) + " est insere.");
								send(reply);
							} catch (StaleProxyException e) {
								// TODO Auto-generated catch block
								
								e.printStackTrace();
							}
						} else{
							ACLMessage rqtNode = new ACLMessage(ACLMessage.REQUEST);
							rqtNode.addReceiver(new AID(droite, AID.ISLOCALNAME));
							
							rqtNode.setContent(String.valueOf(cont));
							rqtNode.setConversationId("insert");
							
							send(rqtNode);
						}
					} else if(cont == valeur){
						// Retourne un message, déjà existant
						reply.setPerformative(ACLMessage.INFORM);
						reply.setContent(String.valueOf(cont) + " deja insere.");
						send(reply);
					} else{
						// Message inconnu
						reply.setPerformative(ACLMessage.FAILURE);
						reply.setContent("Erreur.");
						send(reply);
					}
				} else if(message.getPerformative() == ACLMessage.INFORM){
					reply.setPerformative(ACLMessage.INFORM);
					reply.setContent(message.getContent());
					send(reply);
				}
			}
		}
	}
	
	public class ExisteBehaviour extends CyclicBehaviour {
		
		public void action(){
			MessageTemplate mt = MessageTemplate.MatchConversationId("existe");
			ACLMessage message = receive(mt);
			
		}
	}
	
	public class AfficheBehaviour extends CyclicBehaviour {
		
		public void action(){
			MessageTemplate mt = MessageTemplate.MatchConversationId("affiche");
			ACLMessage message = receive(mt);
			
		}
	}
}

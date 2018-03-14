import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class DispatchAgent extends Agent {
	protected void setup() {
		System.out.println(getName() + "--> Installed");

		addBehaviour(new DemandeBehaviour());
	}
	
	public class DemandeBehaviour extends CyclicBehaviour {
		ObjectMapper mapper = new ObjectMapper();
		
		public void action(){
			ACLMessage rqtNode = new ACLMessage(ACLMessage.REQUEST);
			rqtNode.addReceiver(new AID("ROOT", AID.ISLOCALNAME));
			
			ACLMessage message = receive();
			if(message != null){
				if(message.getPerformative() == 10){

					Scanner sc = new Scanner(message.getContent());
					
					while(sc.hasNextLine()){
						String line = sc.nextLine();
						
						Scanner scline = new Scanner(line);
						String op = scline.next("\\w+");
						
						int value = 0;
						try{
							value = scline.nextInt();
						} catch(Exception ex){
							System.out.println(getLocalName() + " -> " + ex.getMessage());
						}
						
						if(op.equals("insert")){
							// Insère un agent s'il n'est pas encore présent						
							
							rqtNode.setContent(String.valueOf(value));
							rqtNode.setConversationId("insert");
							
							send(rqtNode);
						} else if(op.equals("existe")){
							// Vérifie si un agent existe dans l'arbre

							rqtNode.setContent(String.valueOf(value));
							rqtNode.setConversationId("existe");
							
							send(rqtNode);
						} else if(op.equals("affiche")){
							// Affiche l'arbre
							
							rqtNode.setConversationId("affiche");
							
							send(rqtNode);
						} else{
							// Erreur, mauvaise requête
							
						}
					}
				} else if(message.getPerformative() == ACLMessage.INFORM){
					System.out.println(message.getContent());
				} else{
					System.out.println(message.getContent());
				}
			} else
				block();
		}
	}
}
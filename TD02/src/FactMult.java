import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

//Agent multiplicateur
public class FactMult extends Agent {
	protected void setup(){
		System.out.println(getName() + "--> Installed");

		//Behaviour de multiplication
		addBehaviour(new MultBehaviour());
	}

	//Behaviour pour effectuer un produit
	public class MultBehaviour extends CyclicBehaviour{
		//Mapper pour sérialiser et désérialiser les messages
		ObjectMapper mapper = new ObjectMapper();
		
		public void action(){
			ACLMessage message = receive();
			
			if (message != null) {
				String par = message.getContent();
				ACLMessage reply = message.createReply();
				
				Operation op;
				//Le message reçu est désérialisé et enregistré dans l'objet op de la classe Operation
				try {
					op = mapper.readValue(message.getContent(), Operation.class);
					
					//Si la demande d'operation correspond à une multipliciation, les arguments sont utilisés
					if (op.getOperation().equals("Multiplication")) {
						double n = op.getArg1() * op.getArg2();
						
						//La réponse du produit est passée dans une classe Operation sérialisée...
						op = new Operation("Answer", n, 0);
						
						//... dans un message de type Inform
						reply.setPerformative(ACLMessage.INFORM);
						reply.setContent(mapper.writeValueAsString(op));
					} else {
						//Des autres operations ne sont pas réalisées par l'agent Mult
						//Si un message est reçu en demandant une autre operation, un message d'erreur est transmis
						reply.setPerformative(ACLMessage.FAILURE);
						System.out.println(getName() + " : Error! " + op.getOperation() + " is not a valid operation.");
						reply.setContent("Unknown operator!");
					}
					
					send(reply);
				} catch (IOException e1) {
					//Des erreurs pendant la désérialisation du message sont traitées ici
					//Un message d'erreur est transmis
					e1.printStackTrace();
					System.out.println(getName() + " : Error! Wrong message received.");
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent("Error in the reception!");
					send(reply);
				}
			} else{
				//Si le message reçu est vide, on passe au prochain thread
				block();
			}
		}
	}
}

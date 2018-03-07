import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

//Agent factorielle
public class FactAgent extends Agent {
	protected void setup() {
		System.out.println(getName() + "--> Installed");

		//Behaviour de fatorielle
		addBehaviour(new FactBehaviour());
	}
	
	//Behaviour pour contrôler le calcul d'une factorielle
	public class FactBehaviour extends CyclicBehaviour {
		int n, i;
		double ans;
		//Mapper pour sérialiser et désérialiser les messages
		ObjectMapper mapper = new ObjectMapper();
		Operation op;
		
		public void action(){
			//Initialisation du message pour demander le calcul de la factorielle
			ACLMessage rqtFact = new ACLMessage(ACLMessage.REQUEST);
			rqtFact.addReceiver(new AID("MULT", AID.ISLOCALNAME));
			
			ACLMessage message = receive();
			if(message != null){
				//Si il y avait une erreur en calculant le produit, un message est affiché
				if(message.getPerformative() == ACLMessage.FAILURE){
					System.out.println(getName() + " : Error performing " + i + "!");
				} else if(message.getPerformative() == ACLMessage.INFORM){
					//Le message reçu est désérialisé et enregistré dans l'objet op de la classe Operation
					try {
						op = mapper.readValue(message.getContent(), Operation.class);
						
						ans = op.getArg1();
						
					} catch (IOException e1) {
						//Des erreurs pendant la désérialisation du message sont traitées ici
						//Un message d'erreur est transmis
						e1.printStackTrace();
						System.out.println(getName() + " : Error! Wrong message received.");
					}
					
					if(op.getOperation().equals("Answer")){
						//Condition d'arrête pour calculer la factorielle
						if(n == 1){
							//Affichage du resultat
							System.out.println(getName() + " : " + i + "! = " + ans);
						} else{
							//Continuation des calculs pour achever la factorielle
							op = new Operation("Multiplication", n, ans);
							
							//Le message à envoyer est sérialisé
							try {
								rqtFact.setContent(mapper.writeValueAsString(op));
							} catch (JsonProcessingException e) {
								//Traitement d'erreur dans la sérialisation
								e.printStackTrace();
							}
							send(rqtFact);
							
							//Rapprocher de la condition d'arrête
							n--;
						}
					} else{
						//Si l'operation demandé n'est pas du type attendu, un message d'erreur est affiché
						System.out.println(getName() + " : Error! " + op.getOperation() + " is not a valid operation.");
					}
				} else{
					String par = message.getContent();
					
					//Traitement de la demande d'un factorielle par l'utilisateur
					if (par.contains("!")) {
						String[] parameters = par.split("!");
						n = Integer.parseInt(parameters[0].trim());
						i = n;
						ans = 1;
						
						//Cas où l'utilisateur demmande un factorielle inexistant, un message d'erreur est affiché
						if(n < 0){
							System.out.println(getName() + " : Error! Please enter a positive value.");
						} else if(n > 1){
							//Début du calcul de la factorielle
							Operation op = new Operation("Multiplication", n, n-1);
							
							//Sérialisation du mensage pour demander une multiplication
							try {
								rqtFact.setContent(mapper.writeValueAsString(op));
							} catch (JsonProcessingException e) {
								//Traitement d'erreur dans la sérialisation
								e.printStackTrace();
							}
							n = n-2;
							send(rqtFact);
						} else
							//Cas où l'utilisateur demande la factorielle de 0
							System.out.println(getName() + " : " + i + "! = " + ans);
					} else {
						//Cas où l'utilisateur n'utilise pas la syntaxe correcte
						System.out.println(getName() + " : Error! Unkwon request.");
						block();
					}
				}
			} else{
				//Si le message reçu est vide, on passe au prochain thread
				block();
			}
		}
	}
}
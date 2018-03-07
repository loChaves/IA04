import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class HelloWorld extends Agent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 633440426709747928L;

	protected void setup(){
		System.out.println(getLocalName() + "--> Installed");
		addBehaviour(new Comportement());
	}
	
	public class Comportement extends CyclicBehaviour{

		private static final long serialVersionUID = 6766549685876386954L;

		@Override
		public void action() {
			ACLMessage message = receive();
			if(message != null){
				System.out.println("Contact " + message.getContent());
			}else{
				block();
			}
		}
		
	}
}

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class HelloMain {
	public static String SECOND_PROPERTIES_FILE = "SecondProperties";
	public static void main(String[ ] args){
		Runtime rt = Runtime.instance();
		Profile p = null;
		try{
			p = new ProfileImpl(SECOND_PROPERTIES_FILE);
			ContainerController cc = rt.createAgentContainer(p);
			AgentController ac2 = cc.createNewAgent("HelloWorld1", "HelloWorld", null);
			ac2.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

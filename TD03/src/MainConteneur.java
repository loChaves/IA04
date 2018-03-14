import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class MainConteneur {
	public static String MAIN_PROPERTIES_FILE = "./properties-princ";
	
	public static void main(String[] args) {
		Runtime rt_princ = Runtime.instance();
		Profile p = null;
		
		try{
			p = new ProfileImpl(MAIN_PROPERTIES_FILE);
			AgentContainer mc = rt_princ.createMainContainer(p);
			
			AgentController dispa = mc.createNewAgent("DISPATCH", "DispatchAgent", null);
			dispa.start();
			
			AgentController root = mc.createNewAgent("ROOT", "NodeAgent", null);
			root.start();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
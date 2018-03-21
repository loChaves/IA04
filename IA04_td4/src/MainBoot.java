import jade.core.Profile;
import jade.core.ProfileException;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;

public class MainBoot {
	public static String MAIN_PROPERTIES_FILE = "MainProperties";
	
	public static void main(String[] args) {
		Runtime rt = Runtime.instance();
		Profile p = null;
		try {
			p = new ProfileImpl(MAIN_PROPERTIES_FILE);
			AgentContainer mc = rt.createMainContainer(p);
			
		} catch (ProfileException e) {
			e.printStackTrace();
		}
		

	}

}

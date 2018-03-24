import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class MainBoot {
	public static String MAIN_PROPERTIES_FILE = "MainProperties";
	
	public static void main(String[] args) {
		Runtime rt = Runtime.instance();
		Profile p = null;
		try {
			p = new ProfileImpl(MAIN_PROPERTIES_FILE);
			AgentContainer mc = rt.createMainContainer(p);
			
			AgentController simu = mc.createNewAgent("SIMULATION", "AgentSimu", null);
			simu.start();
			
			AgentController envi = mc.createNewAgent("ENVIRONEMENT", "AgentEnvi", null);
			envi.start();
			
			AgentController analy;
			for(int i = 0; i < 3; i++) {
				analy = mc.createNewAgent("ANALYSE"+i, "AgentAnaly", null);
				analy.start();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

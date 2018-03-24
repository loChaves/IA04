import java.util.ArrayList;
import java.util.List;

public class Message {
	private String command = "";
	private String objet = "";
	private List<Cellule> lc = new ArrayList<Cellule>();
	
	public Message() {
		
	}
	
	public Message(String cmd, String obj){
		command = cmd;
		objet = obj;
	}
	
	public Message(String cmd, List<Cellule> l){
		command = cmd;
		lc = l;
	}
	
	public String getCommand(){
		return command;
	}
	
	public String getObjet(){
		return objet;
	}
	
	public List<Cellule> getList(){
		return lc;
	}
	
	public void setCommand(String cmd){
		command = cmd;
	}
	
	public void setObjet(String obj){
		objet = obj;
	}
}

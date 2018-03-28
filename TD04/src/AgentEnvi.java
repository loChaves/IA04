import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import static java.util.stream.Collectors.toList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgentEnvi extends Agent{
	private static final long serialVersionUID = 1L;
	int compteur = 0;
	ObjectMapper mapper = new ObjectMapper();
	Message msg;
	boolean fin = false;
	Sudoku s = new Sudoku();
	
	protected void setup() {
		System.out.println(getName() + "--> Installed");

		addBehaviour(new BehaviourCompte());
		
		try(BufferedReader br = new BufferedReader(new FileReader("sudoku5"))) {
		    String line = br.readLine();
		    List<Integer> sud = new ArrayList<Integer>();
		    
		    while (line != null) {
		    	String[] str = line.split(" ");
		    	for(String num : str)
		    		sud.add(Integer.parseInt(num));
		    	
		        line = br.readLine();
		    }
		    
		    s.setSudoku(sud);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class BehaviourCompte extends SimpleBehaviour{
		private static final long serialVersionUID = 1L;
		public void action(){
			if(!s.isDone()) {
				MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
				ACLMessage message = receive(mt);
				
				ACLMessage rqtEnvi = new ACLMessage(ACLMessage.REQUEST);
				
				if(message != null){
//					if(s.howDone() == 74)
//						System.out.println(s.printSudoku() + System.lineSeparator());
					//System.out.println(s.getCellule(40).getPossibles());
					System.out.println(s.howDone() + "%");
					try {
						msg = mapper.readValue(message.getContent(), Message.class);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
					rqtEnvi.addReceiver(new AID(msg.getObjet(), AID.ISLOCALNAME));
					
					int c = compteur%27;
					if(c >= 0 && c <= 8){
						msg = new Message("analyse", s.getLigne(c));
					}else if(c >= 9 && c <= 17){
						msg = new Message("analyse", s.getColonne(c-9));
					}else if(c >= 18 && c <= 26){
						msg = new Message("analyse", s.getCarre(c-18));
					}else
						msg = new Message();
				    
					try {
						rqtEnvi.setContent(mapper.writeValueAsString(msg));
						//System.out.println(rqtEnvi.getContent());
						send(rqtEnvi);
						compteur++;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
				message = receive(mt);
				
				if(message != null){
					try {
						msg = mapper.readValue(message.getContent(), Message.class);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					for(Cellule c : msg.getList()) {
						if(s.getCellule(c.getPosition()).getValeur() == 0){
							Cellule newCell = new Cellule(c.getPosition());
							if(c.getValeur() == 0){
//								for(Integer i : c.getPossibles()){
//									if(s.getCellule(c.getPosition()).getPossibles().contains(i))
//										pos.add(i);
//								}
								List<Integer> pos = s.getCellule(c.getPosition()).getPossibles().stream().filter(s->c.getPossibles().contains(s)).collect(toList());
								newCell.setPossibles(pos);
							} else{
								try {
									newCell.setValeur(c.getValeur());
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							s.setCellule(newCell.getPosition(), newCell);
						}
					}
				}
			} else {
				System.out.println(compteur);
				ACLMessage infEnvi = new ACLMessage(ACLMessage.INFORM);
				infEnvi.addReceiver(new AID("SIMULATION", AID.ISLOCALNAME));
				infEnvi.setContent(s.printSudoku());
				send(infEnvi);
				
				fin = true;
			}
		}
		
		public boolean done(){
			return fin;
		}
	}
}

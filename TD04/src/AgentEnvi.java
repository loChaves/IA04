import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class AgentEnvi extends Agent{
	int compteur = 0;
	ObjectMapper mapper = new ObjectMapper();
	Message msg;
	Sudoku s = new Sudoku();
	
	protected void setup() {
		System.out.println(getName() + "--> Installed");

		addBehaviour(new BehaviourCompte());
		
		try(BufferedReader br = new BufferedReader(new FileReader("sudoku.txt"))) {
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
	
	private class BehaviourCompte extends CyclicBehaviour{		
		public void action(){
			
			if(!s.isDone()) {
				MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
				ACLMessage message = receive(mt);
				
				ACLMessage rqtEnvi = new ACLMessage(ACLMessage.REQUEST);
				
				if(message != null){
					if(s.howDone() >= 95)
						System.out.println(s.printSudoku() + System.lineSeparator());
					//System.out.println(s.getCellule(40).getPossibles());
					System.out.println(s.howDone() + "%");
					try {
						msg = mapper.readValue(message.getContent(), Message.class);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
					rqtEnvi.addReceiver(new AID(msg.getObjet(), AID.ISLOCALNAME));
					
					int c = compteur%26;
					if(c >= 0 && c <= 8){
						msg = new Message("analyse", s.getLigne(c));
					}else if(c >= 8 && c <= 16){
						msg = new Message("analyse", s.getColonne(c-8));
					}else if(c >= 17 && c <= 25){
						msg = new Message("analyse", s.getCarre(c-17));
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
					
					boolean changed = false;
					for(Cellule c : msg.getList()) {
						if(c.getPossibles().size() < s.getCellule(c.getPosition()).getPossibles().size())
							changed = true;
					}
					
					if(changed) {
						for(Cellule c : msg.getList())
							s.setCellule(c.getPosition(), c);
					}
				}
			} else {
				System.out.println(compteur);
				ACLMessage infEnvi = new ACLMessage(ACLMessage.INFORM);
				infEnvi.addReceiver(new AID("SIMULATION", AID.ISLOCALNAME));
				infEnvi.setContent(s.printSudoku());
				send(infEnvi);
				
				done();
			}
		}
	}
}

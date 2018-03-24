import java.util.ArrayList;
import java.util.List;

public class Cellule{
	private int valeur = 0;
	private List<Integer> possibles = new ArrayList<Integer>();
	
	public Cellule(){
		for(int i = 1; i <= 9; i++)
			possibles.add(i);
	}
	
	public void setValeur(int val) throws Exception{
		if(val >= 0 && val <= 9){
			valeur = val;
			possibles.clear();
			if(val == 0) {
				for(int i = 1; i <= 9; i++)
					possibles.add(i);
			}
		}else{
			throw new Exception("Erreur, valeur illegal pour une cellule.");
		}
	}
	
	public void rmPossible(int val){
		possibles.remove(val);
	}
	
	public int getValeur(){
		return valeur;
	}
	
	public List<Integer> getPossibles(){
		return possibles;
	}
}
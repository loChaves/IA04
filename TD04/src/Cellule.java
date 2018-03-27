import java.util.ArrayList;
import java.util.List;

public class Cellule implements Comparable<Cellule>{
	private int position = 81;
	private int valeur = 0;
	private List<Integer> possibles = new ArrayList<Integer>();
	
	public Cellule() {
		for(Integer i = 1; i <= 9; i++)
			possibles.add(i);
	}
	
	public Cellule(int p){
		position = p;
		for(Integer i = 1; i <= 9; i++)
			possibles.add(i);
	}
	
	public void setValeur(int val) throws Exception{
		if(val >= 0 && val <= 9){
			valeur = val;
			possibles.clear();
			if(val == 0) {
				for(Integer i = 1; i <= 9; i++)
					possibles.add(i);
			}
		}else{
			throw new Exception("Erreur, valeur illegal pour une cellule.");
		}
	}
	
	public void rmPossible(Integer val){
		possibles.remove(val);
	}
	
	public int getValeur(){
		return valeur;
	}
	
	public List<Integer> getPossibles(){
		return possibles;
	}
	
	public void setPossibles(List<Integer> l) {
		possibles = l;
	}
	
	public int getPosition() {
		return position;
	}
	
	@Override
    public int compareTo(Cellule cell2) {
        return  Integer.compare(this.getPossibles().size(), cell2.getPossibles().size());
    }
}
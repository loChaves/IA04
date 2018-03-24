import java.util.ArrayList;
import java.util.List;

public class Sudoku {
	List<Cellule> cells = new ArrayList<Cellule>();
	
	public Sudoku(){
		for(int i = 0; i < 81; i++){
			cells.add(new Cellule());
		}
	}
	
	public Cellule getCellule(int i){
		return cells.get(i);
	}
	
	public List<Cellule> getLigne(int i){
		List<Cellule> ligne = new ArrayList<Cellule>();
		
		for(int j = 0; j < 9; j++){
			ligne.add(cells.get(i*9 + j));
		}
		
		return ligne;
	}

	public List<Cellule> getColonne(int i){
		List<Cellule> col = new ArrayList<Cellule>();
		
		for(int j = 0; j < 9; j++){
			col.add(cells.get(i + j*9));
		}
		
		return col;
	}
	
	public List<Cellule> getCarre(int i){
		List<Cellule> car = new ArrayList<Cellule>();
		
		if(i < 3 && i >= 0){
			for(int j = 0; j < 3; j++){
				car.add(cells.get(i*3 + j));
				car.add(cells.get(i*3 + j+9));
				car.add(cells.get(i*3 + j+18));
			}
		}else if(i < 6 && i >= 3){
			for(int j = 0; j < 3; j++){
				car.add(cells.get((i%3)*3 + 27+j));
				car.add(cells.get((i%3)*3 + 27+j+9));
				car.add(cells.get((i%3)*3 + 27+j+18));
			}
		}else if(i < 9 && i >= 6){
			for(int j = 0; j < 3; j++){
				car.add(cells.get((i%3)*3 + 54+j));
				car.add(cells.get((i%3)*3 + 54+j+9));
				car.add(cells.get((i%3)*3 + 54+j+18));
			}
		}
		return car;
	}
	
	public void setSudoku(List<Integer> sud){
		try{
			for(int i = 0; i < 81; i++){
				try {
					cells.get(i).setValeur(sud.get(i));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
			}
		}catch(IndexOutOfBoundsException e){
			System.out.println("Erreur de setSudoku(), List<Integer> sud n'a pas une bonne taille.");
		}
	}
}

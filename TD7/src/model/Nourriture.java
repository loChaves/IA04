package model;

public class Nourriture{
	private int quantite;
	
	public int x, y;
	
	public Nourriture(){
		quantite = Constants.MAX_FOOD;
	}

	public int getQuantite(){
		return quantite;
	}

	public void takeFood(){
		if(quantite > 0)
			quantite--;
	}
}

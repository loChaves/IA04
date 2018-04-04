package model;

import java.util.concurrent.ThreadLocalRandom;

public class Nourriture {
	private int quantite;

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

	public boolean isLast(){
		return quantite == 1;
	}
}

package model;

import java.util.concurrent.ThreadLocalRandom;

import sim.engine.SimState;
import sim.engine.Steppable;

public class Nourriture implements Steppable {
	private int quantite;
	
	public int x, y;
	
	@Override
	public void step(SimState arg0) {
		// TODO Auto-generated method stub
		
	}
	
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

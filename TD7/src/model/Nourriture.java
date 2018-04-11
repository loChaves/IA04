package model;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;

public class Nourriture implements Steppable{
	public Stoppable stoppable;
	private int quantite;
	
	public int x, y;
	
	public Nourriture(){
		quantite = Constants.MAX_FOOD;
	}

	@Override
	public void step(SimState state) {
		Beings beings = (Beings) state;
		
		if(quantite == 0){
			beings.yard.remove(this);
			stoppable.stop();
			
			beings.addNourriture();
		}
	}
	
	public int getQuantite(){
		return quantite;
	}

	public void takeFood(){
		if(quantite > 0)
			quantite--;
	}
}

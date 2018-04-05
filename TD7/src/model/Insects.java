package model;

import java.util.Random;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Int2D;

public class Insects implements Steppable {
	
	public Stoppable stoppable;
	public int x, y;
	public int charge;
	public int energie = Constants.MAX_ENERGY;
	public int DISTANCE_DEPLACEMENT;
	public int DISTANCE_PERCEPTION;
	public int CHARGE_MAX;
	
	public Insects() {
		Random random = new Random();  
		/* 10 = (1 + deplacement) + (1 + percevoir) + (1 + charge)*/
		DISTANCE_DEPLACEMENT = random.nextInt(Constants.CAPACITY - 3);
		DISTANCE_PERCEPTION =  random.nextInt(Constants.CAPACITY - DISTANCE_DEPLACEMENT - 3);
		CHARGE_MAX = Constants.CAPACITY - DISTANCE_DEPLACEMENT - DISTANCE_PERCEPTION - 3;
	}
	
	@Override
	public void step(SimState state) {
		Beings beings = (Beings) state;
		kill(beings);
		move(beings);
	}

	public boolean kill(Beings beings) {
		if(energie == 0){
			beings.yard.remove(this);
			stoppable.stop();
			return true;
		}
		else
			return false;
	}
	
	public boolean see(Beings beings) {
		//Object champVue[][] = new Object[2*perception+1][2*perception+1];
		return true;
	}
	
	public boolean move(Beings beings) {
		
		energie--;
		return true;
	}
}

package model;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Int2D;

public class AgentType implements Steppable {
	public Stoppable stoppable;
	public int x, y;
	public int energie = Constants.MAX_ENERGY;
	public int perception = 1;
	
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
		Object champVue[][] = new Object[2*perception+1][2*perception+1];
		return true;
	}
	
	public boolean move(Beings beings) {
		
		energie--;
		return true;
	}
}

package model;

import java.util.ArrayList;
import java.util.List;
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
		List<Nourriture> listNour;
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
	
	public List<Nourriture> see(Beings beings) {
		List<Nourriture> listNour = new ArrayList<>();
		for(int i = x - DISTANCE_DEPLACEMENT; i <= x + DISTANCE_DEPLACEMENT; i++) {
			for(int j = y - DISTANCE_DEPLACEMENT; j <= y + DISTANCE_DEPLACEMENT; j++) {
				Bag bag = beings.yard.getObjectsAtLocation(i, j);
				if(!bag.isEmpty()) {
					for(Object ob : bag) {
						if(ob.getClass() == Nourriture.class)
							listNour.add((Nourriture) ob);
					}
				}
			}
		}
		
		return listNour;
	}
	
	private boolean eat(List<Nourriture> listNour) {
		if(this.energie + Constants.FOOD_ENERGY < Constants.MAX_ENERGY) {
			// La charge de nourriture non null et les cases adjacentes sont nulles
			if(charge > 0 && listNour.isEmpty()) {// Manger sa propre charge
				charge --;
				energie += Constants.FOOD_ENERGY;
				return true;
			}else if(!listNour.isEmpty()) {// Manger sur la case adjacente de nourriture
				for(Nourriture nour : listNour) {
					if(distance(nour.x, nour.y) == 1 && nour.isExist()) {
						nour.takeFood();
						energie += Constants.FOOD_ENERGY;
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean charger() {
		//if()
		return false;
	}
	
	public boolean move(Beings beings) {
		
		energie--;
		return true;
	}
	
	public int distance(int x_a_calcul, int y_a_calcul) {
		int dis_x = Math.abs(x_a_calcul - this.x);
		int dis_y = Math.abs(y_a_calcul - this.y);
		int dis = Math.max(dis_x, dis_y);
		return dis;
	}
}

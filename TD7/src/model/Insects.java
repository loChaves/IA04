package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Int2D;

public class Insects implements Steppable {
	
	public Stoppable stoppable;
	public int x, y;
	public int charge = 0;
	public int energie = Constants.MAX_ENERGY;
	public int DISTANCE_DEPLACEMENT;
	public int DISTANCE_PERCEPTION;
	public int CHARGE_MAX = Constants.MAX_LOAD;
	
	public Insects() {
		Random random = new Random();  
		/* 10 = (1 + deplacement) + (1 + percevoir) + (1 + charge)*/
		//CHARGE_MAX = random.nextInt(Constants.MAX_LOAD);
		DISTANCE_DEPLACEMENT = random.nextInt(Constants.CAPACITY - CHARGE_MAX - 3);
		DISTANCE_PERCEPTION = Constants.CAPACITY - DISTANCE_DEPLACEMENT - CHARGE_MAX ;
		
	}
	
	@Override
	public void step(SimState state) {
		Beings beings = (Beings) state;
		updateNourriture(beings);
		kill(beings);
		List<Nourriture> nour = see(beings);
		charger(nour);
		eat(nour);
		move(beings);
	}

	public boolean updateNourriture(Beings beings) {
		return beings.listNour.removeAll(beings.listNour.stream().filter(n -> n.getQuantite() == 0).collect(Collectors.toList()));
//		for(Nourriture n : beings.listNour) {
//			if(n.getQuantite() == 0)
//				beings.listNour.remove(n);
//		}
	}
	
	public boolean kill(Beings beings) {
		if(energie == 0){
			beings.yard.remove(this);
			stoppable.stop();
			System.out.println(this + " vient de mourrir.");
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
				if(bag != null && !bag.isEmpty()) {
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
		if(energie + Constants.FOOD_ENERGY < Constants.MAX_ENERGY) {
			// La charge de nourriture non null et les cases adjacentes sont nulles
			if(charge > 0 && listNour.isEmpty()) {// Manger sa propre charge
				charge --;
				energie += Constants.FOOD_ENERGY;
				System.out.println(this + " vient de manger de sa propre nourriture.");
				return true;
			}else if(!listNour.isEmpty()) {// Manger sur la case adjacente de nourriture
				for(Nourriture nour : listNour) {
					if(distance(nour.x, nour.y) == 1) {
						nour.takeFood();
						energie += Constants.FOOD_ENERGY;
						System.out.println(this + " vient de manger d'un tas de nourriture.");
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean charger(List<Nourriture> listNour) {
		if(charge < CHARGE_MAX) {
			if(!listNour.isEmpty()) {
				for(Nourriture nour : listNour) {
					if(distance(nour.x, nour.y) == 0) {// Insecte est dans la case ou nourriture se situe
						charge++;
						nour.takeFood();
						System.out.println(this + " vient de se charger de nourriture.");
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean move(Beings beings) {
		x += beings.random.nextInt(DISTANCE_DEPLACEMENT*2 + 1) - DISTANCE_DEPLACEMENT;
		if(x > Constants.GRID_SIZE)
			x = Constants.GRID_SIZE;
		else if(x < 0)
			x = 0;
		y += beings.random.nextInt(DISTANCE_DEPLACEMENT*2 + 1) - DISTANCE_DEPLACEMENT;
		if(y > Constants.GRID_SIZE)
			y = Constants.GRID_SIZE;
		else if(y < 0)
			y = 0;
		
		beings.yard.setObjectLocation(this, x, y);
		
		energie--;
		return true;
	}
	
	public int distance(int x_a_calcul, int y_a_calcul) {
		int dis_x = Math.abs(x_a_calcul - x);
		int dis_y = Math.abs(y_a_calcul - y);
		int dis = Math.max(dis_x, dis_y);
		return dis;
	}
}

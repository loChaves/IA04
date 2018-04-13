package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;

public class Insects implements Steppable {
	
	public Stoppable stoppable;
	public int x, y;
	public int charge = 0;
	public int energie = Constants.MAX_ENERGY;
	public int DISTANCE_DEPLACEMENT;
	public int DISTANCE_PERCEPTION;
	public int CHARGE_MAX;
	
	public Insects() {
		Random random = new Random();  
		/* 10 = (1 + deplacement) + (1 + percevoir) + (1 + charge)*/
		CHARGE_MAX = Constants.MAX_LOAD - 4;
		DISTANCE_DEPLACEMENT = random.nextInt(Constants.CAPACITY - CHARGE_MAX - 3);
		DISTANCE_PERCEPTION = Constants.CAPACITY - DISTANCE_DEPLACEMENT - CHARGE_MAX ;
		
	}
	
	@Override
	public void step(SimState state) {
		Beings beings = (Beings) state;
		//updateNourriture(beings);
		if(energie == 0) {
			beings.yard.remove(this);
			stoppable.stop();
			beings.killInsecte();
			System.out.println(this + " vient de mourrir.");
		}else if(energie > 0){
			List<Nourriture> nour = see(beings);
			if(!charger(nour)){
				if(!eat(nour)){
					if(!nour.isEmpty()) {
						for(Nourriture n : nour) {
							if(distance(n.x, n.y) == 0) {
								move2();
							} else
								move(beings, nour);
						}
					} else
						move(beings, nour);
				}
			}
		}
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
				charge--;
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
					if(distance(nour.x, nour.y) <= 1) {// Insecte est dans la case ou nourriture se situe
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
	
	public boolean move(Beings beings, List<Nourriture> listNour) {
		boolean isMove = false;
		int dis = 1000;
		Nourriture nour_proche = new Nourriture();

		if(!listNour.isEmpty()) { // se deplacer a la nourriture la plus proche
			for(Nourriture nour : listNour) {
				if(distance(nour.x, nour.y) < dis) {
					dis = distance(nour.x, nour.y);
					nour_proche = nour;
				}
			}
			
			if(dis <= DISTANCE_DEPLACEMENT && nour_proche.x + 1 < Constants.GRID_SIZE) {
				this.x = nour_proche.x + 1;
				this.y = nour_proche.y;
				beings.yard.setObjectLocation(this, x, y);
				isMove = true;
				energie--;
				return isMove;
			}else if(dis <= DISTANCE_DEPLACEMENT && nour_proche.x - 1 >= 0) {
				this.x = nour_proche.x - 1;
				this.y = nour_proche.y;
				beings.yard.setObjectLocation(this, x, y);
				isMove = true;
				energie--;
				return isMove;
			}
		}
		
		if(!isMove) {// se deplacer aleatoire
			// Supposon qu'il se deplace le plus loin qu'il peut
			int sign_x = (int)(Math.random()*3);
			int sign_y = (int)(Math.random()*3);
			int competence = DISTANCE_DEPLACEMENT*2 + 1;
			int size = (int)Math.pow(competence, 2);
			int dx = size%competence + (int)Math.pow(-1, sign_x)*DISTANCE_DEPLACEMENT;
			int dy = size%competence + (int)Math.pow(-1, sign_y)*DISTANCE_DEPLACEMENT;
			if(this.x + dx < Constants.GRID_SIZE && this.x + dx >= 0 
					&& this.y + dy < Constants.GRID_SIZE && this.y + dy >= 0) {
				this.x += dx;
				this.y += dy;
			}else if(this.x + dx < 0 && this.y + dy < Constants.GRID_SIZE && this.y + dy >= 0
					|| this.x + dx > Constants.GRID_SIZE && this.y + dy < Constants.GRID_SIZE && this.y + dy >= 0) {
				this.y += dy;
			}else if(this.y + dy < 0 && this.x + dx < Constants.GRID_SIZE && this.x + dx >= 0
					|| this.x + dy > Constants.GRID_SIZE && this.x + dx < Constants.GRID_SIZE && this.x + dx >= 0) {
				this.x += dx;
			}
			beings.yard.setObjectLocation(this, x, y);
			isMove = true;
			energie--;
			return isMove;
		}
		return false;
	}
	
	public boolean move2(){
		if(x == Constants.GRID_SIZE-1)
			x -= 1;
		else
			x += 1;
		
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

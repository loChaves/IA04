package model;

import java.util.ArrayList;
import java.util.List;

import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

public class Beings extends SimState {
	private int numInsects;
	public List<Nourriture> listNour = new ArrayList<>();

	public static int NB_DIRECTIONS = 8;
	public SparseGrid2D yard = new SparseGrid2D(Constants.GRID_SIZE, Constants.GRID_SIZE);
	
	public Beings(long seed) {
		super(seed);
		yard.removeEmptyBags = true;
		numInsects = Constants.NUM_INSECT;
	}
	
	public void start() {
		System.out.println("Simulation started");
		super.start();
		yard.clear();
		createAllNourriture();
		addAgentsA();
	}
	
	private void createAllNourriture() {
		for(int i = 0; i < Constants.NUM_FOOD_CELL; i++){
			Int2D location = getFreeLocation();
			Nourriture nour = new Nourriture();
			yard.setObjectLocation(nour, location);
			nour.x = location.x;
			nour.y = location.y;
			
			listNour.add(nour);
		}
	}
	
	private void addNourriture() {
		if(listNour.size() < Constants.NUM_FOOD_CELL) {
			Int2D location = getFreeLocation();
			Nourriture nour = new Nourriture();
			yard.setObjectLocation(nour, location);
			nour.x = location.x;
			nour.y = location.y;

			listNour.add(nour);
		}
	}
	
	private void addAgentsA() {
		for(int i = 0; i < Constants.NUM_INSECT; i++) {
			TypeA a =  new TypeA();
			Int2D location = getFreeLocation();
			yard.setObjectLocation(a, location);
			a.x = location.x;
			a.y = location.y;
			Stoppable stoppable = schedule.scheduleRepeating(a);
			a.stoppable = stoppable;
		}  
	}
	
	public void killInsecte() {
		if(numInsects > 0)
			numInsects --;
	}

	public boolean free(int x, int y) {
		//	 int xx = yard.stx(x);
		//	 int yy = yard.sty(y);
		//	 return yard.get(xx,yy) == null;
		return true;
	}
	
	private Int2D getFreeLocation() {
		Int2D location = new Int2D(random.nextInt(Constants.GRID_SIZE),
				random.nextInt(Constants.GRID_SIZE) );
		return location;
	}
	
	public int getNumInsects() { 
		return numInsects; 
	}

	public void setNumInsects(int num) {
		numInsects = num;
	}
}

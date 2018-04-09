package model;


import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.ObjectGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

public class Beings extends SimState {
	private int numInsects = Constants.NUM_INSECT;
	
	public static int GRID_SIZE = Constants.GRID_SIZE; 
	public static int NB_DIRECTIONS = 8;
	public SparseGrid2D yard = new SparseGrid2D(GRID_SIZE,GRID_SIZE);
	
	public Beings(long seed) {
		super(seed);
	}
	
	public void start() {
		System.out.println("Simulation started");
		super.start();
		yard.clear();
		addNourriture();
		addAgentsA();
	}

	private void addNourriture() {
		for(int i = 0; i < Constants.NUM_FOOD_CELL; i++){
			Int2D location = getFreeLocation();
			Nourriture nour = new Nourriture();
			yard.setObjectLocation(nour, location.x, location.y);
			nour.x = location.x;
			nour.y = location.y;
			schedule.scheduleRepeating(nour);
		}
	}
	
	private void addAgentsA() {
		for(int  i  =  0;  i  <  numInsects;  i++) {
			TypeA a =  new TypeA();
			Int2D location = getFreeLocation();
			yard.setObjectLocation(a,location.x,location.y);
			a.x = location.x;
			a.y = location.y;
			Stoppable stoppable = schedule.scheduleRepeating(a);
			a.stoppable = stoppable;
		}  
	}

	public boolean free(int x,int y) {
		//	 int xx = yard.stx(x);
		//	 int yy = yard.sty(y);
		//	 return yard.get(xx,yy) == null;
		return true;
	}
	
	private Int2D getFreeLocation() {
		Int2D location = new Int2D(random.nextInt(GRID_SIZE),
				random.nextInt(GRID_SIZE) );
		return location;
	}
	
	public int getNumStudents() { 
		return numInsects; 
	}

	public void setNumInsects(int numInsects) {
		this.numInsects = numInsects;
	}
}

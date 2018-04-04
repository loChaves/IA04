package model;


import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.ObjectGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

public class Beings extends SimState {
	public static int GRID_SIZE = 100; 
	public static int NUM_A = 100; 
	public static int NUM_B = 100; 
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
			
			yard.setObjectLocation(new Nourriture(), location);
		}
	}
	
	private void addAgentsA() {
		for(int  i  =  0;  i  <  NUM_A;  i++) {
			TypeA  a  =  new  TypeA();
			Int2D location = getFreeLocation();
			yard.setObjectLocation(a,location.x,location.y);
			a.x = location.x;
			a.y = location.y;
			Stoppable stoppable = schedule.scheduleRepeating(a);
			a.stoppable = stoppable;
		}  
	}
//	private void addAgentsB() {  
//		for(int  i  =  0;  i  <  NUM_B;  i++) {
//			TypeB  b  =  new  TypeB();
//			Int2D location = getFreeLocation();
//			yard.setObjectLocation(b,location.x,location.y);
//			b.x = location.x;
//			b.y = location.y;
//			schedule.scheduleRepeating(b);
//		}  
//	}
	public boolean free(int x,int y) {
		//	 int xx = yard.stx(x);
		//	 int yy = yard.sty(y);
		//	 return yard.get(xx,yy) == null;
		return true;
	}
	private Int2D getFreeLocation() {
		Int2D location = new Int2D(random.nextInt(yard.getWidth()),
				random.nextInt(yard.getHeight()) );
		//	  Object ag;
		//	  while ((ag = yard.get(location.x,location.y)) != null) {
		//	   	  location = new Int2D(random.nextInt(yard.getWidth()),
		//	   	           random.nextInt(yard.getHeight()) );
		//	  }
		return location;
	}
	public  int  getNumStudents()  {  return  NUM_A;  }
}

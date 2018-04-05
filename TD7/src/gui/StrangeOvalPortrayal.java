package gui;

import java.awt.Color;
import java.awt.Graphics2D;

import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.OvalPortrayal2D;
import model.Insects;

public class StrangeOvalPortrayal extends OvalPortrayal2D {

	public StrangeOvalPortrayal() {
		super();
	paint = Color.GRAY;
	filled = true;
	}

	@Override
	public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
		Insects agent = (Insects)object;
		if (agent.x % 5 == 0 && agent.y % 5 == 0)
           scale = 2; 
		else scale = 1;
		super.draw(object, graphics, info);
	}
	

}

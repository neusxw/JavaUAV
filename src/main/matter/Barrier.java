package main.matter;

import java.util.ArrayList;
import java.util.List;

public class Barrier extends Polygon{
	
	public Barrier(){
		Map.getInstance().addBarrier(this);
	}

}
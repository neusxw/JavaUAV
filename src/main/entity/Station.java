package main.entity;

import java.util.ArrayList;
import java.util.List;

public class Station extends Polygon {
	public List<TakeOffPoint> takeOffPoints = new ArrayList<TakeOffPoint>();
	public Station(double[] x, double[] y){
		super(x,y);
	}
	
	public void addTakeOffPoint(TakeOffPoint takeOffPoint) {
		takeOffPoints.add(takeOffPoint);
	}
}

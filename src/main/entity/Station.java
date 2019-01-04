package main.entity;

import java.util.ArrayList;
import java.util.List;

public class Station extends Polygon {
	public List<TakeOffPoint> takeOffPoints = new ArrayList<TakeOffPoint>();
	public Station(double[] x, double[] y){
		super(x,y);
	}
	public Station(double[][] coord){
		super(coord);
		Map.getInstance().addStation(this);
	}
	
	public void addTakeOffPoint(TakeOffPoint takeOffPoint) {
		takeOffPoints.add(takeOffPoint);
	}
	
	public void arrangeTakeOffPoint(int n){
		takeOffPoints.clear();
		Point core = getBarycenter();
		Line takeOffLine = new Line(core,Math.tan(getLongestEdge().directionAngle));
		LineSegment takeOffLineSegment = takeOffLine.intersectionLineSegmentOfLineAndPolygon(this);
		double interval = takeOffLineSegment.length/n;
		for(int i = 0;i < n;i++) {
			double x =takeOffLineSegment.endPoint1.x + (i+1.0/2)*interval*Math.cos(takeOffLineSegment.directionAngle);
			double y =takeOffLineSegment.endPoint1.y + (i+1.0/2)*interval*Math.sin(takeOffLineSegment.directionAngle);
			takeOffPoints.add(new TakeOffPoint(this,x,y));
		}
	}
}

package main.entity;

import java.util.ArrayList;
import java.util.List;

import main.entity.geometry.Line;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;
import main.entity.geometry.Polygon;

public class Station extends Polygon {
	public int ID;
	private static int IDcount = 0;
	public List<TakeOffPoint> takeOffPoints = new ArrayList<TakeOffPoint>();
	public Station(double[] x, double[] y){
		super(x,y);
		Map.getInstance().stations.add(this);
		ID=IDcount++;
	}
	public Station(double[][] coord){
		this(coord[0],coord[1]);
	}
	
	public void addTakeOffPoint(TakeOffPoint takeOffPoint) {
		takeOffPoints.add(takeOffPoint);
	}
	
	public void arrangeTakeOffPoint(int n){
		takeOffPoints.clear();
		Point core = getBarycenter();
		Line takeOffLine = new Line(core,Math.tan(getLongestEdge().directionAngle));
		LineSegment takeOffLineSegment = takeOffLine.intersectionLineSegmentOfLineAndPolygon(this).get(0);
		double interval = takeOffLineSegment.length/n;
		for(int i = 0;i < n;i++) {
			double x =takeOffLineSegment.endPoint1.x + (i+1.0/2)*interval*Math.cos(takeOffLineSegment.directionAngle);
			double y =takeOffLineSegment.endPoint1.y + (i+1.0/2)*interval*Math.sin(takeOffLineSegment.directionAngle);
			//System.out.println(x + "," + y);
			takeOffPoints.add(new TakeOffPoint(this,x,y));
		}
	}
	
	public String toString(boolean highPrecision) {
		String str="Station: ";
		for(Point point:vertexes) {
			str+=point.toString(highPrecision)+" | ";
		}
		return str;
	}
}

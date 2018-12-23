package main.matter;

import main.arithmetic.SimUtils;

public class Point {
	public double x;
	public double y;
	
	public Point(){
		this.x = 0;
		this.y = 0;
	}
	
	public Point(double x,double y){
		this.x = x;
		this.y = y;
	}
	
	public double distanceToPoint(Point p) {
		return Math.sqrt((x - p.x)*(x - p.x)+(y - p.y)*(y - p.y));
	}
	
	public double distanceToStraightLine(Line line) {
		return line.distanceToPoint(this);
	}
	
	public boolean equals(Point point) {
		if (SimUtils.doubleEqual(x,point.x)&&SimUtils.doubleEqual(y,point.y)) {
			return true;
		}
		return false;
	}

}

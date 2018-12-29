package main.matter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import main.arithmetic.SimUtils;

public class Point {
	public double x;
	public double y;
	
	public Point(){
		this.x = Double.NaN;
		this.y = Double.NaN;
	}
	
	public Point(double x,double y){
		this.x = x;
		this.y = y;
	}
	
	public double distanceToPoint(Point p) {
		return Math.sqrt((x - p.x)*(x - p.x)+(y - p.y)*(y - p.y));
	}
	
	public double directionToPoint(Point p) {
		return Math.atan2(p.y-y, p.x-x);
	}
	
	public double distanceToLine(Line line) {
		return line.distanceToPoint(this);
	}
	
	public double distanceToLineSegment(LineSegment lineSegment) {
		double dis1 = distanceToPoint(lineSegment.endPoint1);
		double dis2 = distanceToPoint(lineSegment.endPoint2);
		if (dis1>=dis2) {
			return dis2;
		}else {
			return dis1;
		}
	}
	/*
	 * 判断一个点是在有向直线的左边、右边还是在该直线上
	 */
	public int leftOrRightToLine(Line line){
		if(SimUtils.doubleEqual(line.A*x+line.B*y+line.C, 0)) {
			return SimUtils.IN;
		}else {
			Point foot = line.getFootOfPerpendicular(this);
			Line perpendicularLine = new Line(this,foot);
			if(SimUtils.doubleEqual(perpendicularLine.directionAngle+Math.PI/2, line.directionAngle)) {
				return SimUtils.LEFT;
			}else {
				return SimUtils.RIGHT;
			}
		}
	}
	/*
	 * 
	 */
	public boolean isInPolygon(Polygon polygon) {
		Line verticalLine = new Line(this, 0);
		List<Point> crossPoints = new ArrayList<Point>();
		for(LineSegment polygonEdge:polygon.edges) {
			if (polygonEdge.intersectionLineSegmentAndLine(verticalLine)!=null) {
				crossPoints.add(polygonEdge.intersectionLineSegmentAndLine(verticalLine));
			}
		}
		if (crossPoints.size()==0) {
			return false;
		}else{
			return true;
		}
	}
	
	public boolean equals(Point point) {
		if (SimUtils.doubleEqual(x,point.x)&&SimUtils.doubleEqual(y,point.y)) {
			return true;
		}
		return false;
	}
	
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.00");
		return "Point:[" + df.format(x) + "," +df.format(y) +"]";
	}
	
	public void print() {
		System.out.println(toString());
	}
}

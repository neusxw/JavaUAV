package main.matter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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
	
	public double distanceToLine(Line line) {
		return line.distanceToPoint(this);
	}
	/*
	 * �ж�һ������������ֱ�ߵ���ߡ��ұ߻����ڸ�ֱ����
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
}

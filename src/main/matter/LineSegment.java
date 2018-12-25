package main.matter;

import main.arithmetic.SimUtils;

public class LineSegment extends Line{
	public Point endPoint1;
	public Point endPoint2;
	public double length;
	public LineSegment(Point pointFrom,Point pointTo){
		super(pointFrom,pointTo);
		this.endPoint1 = pointFrom;
		this.endPoint2 = pointTo;
		length = pointFrom.distanceToPoint(pointTo);
	}

	public LineSegment minDistanceToLineSegment(LineSegment line) {
		Point p1 = line.endPoint1;
		Point p2 = line.endPoint2;
		double d11= endPoint1.distanceToPoint(p1);
		double d12= endPoint1.distanceToPoint(p2);
		double d21= endPoint2.distanceToPoint(p1);
		double d22= endPoint2.distanceToPoint(p2);
		double[] d = {d11,d12,d21,d22};
		int index = SimUtils.findIndexOfMin(d);
		if(index==0){
			return new LineSegment(endPoint1,line.endPoint1);
		}else if(index==1){
			return new LineSegment(endPoint1,line.endPoint2);
		}else if(index==2){
			return new LineSegment(endPoint2,line.endPoint1);
		}else{
			return new LineSegment(endPoint2,line.endPoint2);
		}
	}
}

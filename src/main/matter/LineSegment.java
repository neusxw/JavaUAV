package main.matter;

import main.arithmetic.SimUtils;

public class LineSegment extends DirectionalLine{
	public Point endPoint1;
	public Point endPoint2;
	public double length;
	public LineSegment(Point point1,Point point2){
		super(point1,point2);
		this.endPoint1 = point1;
		this.endPoint2 = point2;
		length = point1.distanceToPoint(point2);
	}

	public LineSegment distanceToLinesegment(LineSegment line) {
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

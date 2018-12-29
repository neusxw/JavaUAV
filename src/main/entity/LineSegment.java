package main.entity;

import java.text.DecimalFormat;

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

	/* (non-Javadoc)
	 * 
	 */
	public Point intersectionLineSegmentAndLine(Line line) {
		Point point = super.intersectionLineAndLine(line);
		if(SimUtils.doubleEqual(point.distanceToPoint(endPoint1)+point.distanceToPoint(endPoint2), length)) {
			return point;
		}
		return null;
	}
	
	public Point intersectionLineSegmentAndLineSegment(LineSegment lineSegment) {
		Point point = super.intersectionLineAndLine(lineSegment);
		if(SimUtils.doubleEqual(point.distanceToPoint(lineSegment.endPoint1)+point.distanceToPoint(lineSegment.endPoint2),lineSegment.length)
				&&SimUtils.doubleEqual(point.distanceToPoint(endPoint1)+point.distanceToPoint(endPoint2),length)) {
			return point;
		}
		return null;
	}
	
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.00");
		return "Line: [A=" + df.format(A) + ",B=" + df.format(B) + ",C=" + df.format(C) +
				"]" +",截距=" + df.format(-C/B) +
				",方向角=" + df.format(directionAngle/Math.PI) +
				",长度=" + df.format(length) + 
				",起点=" + endPoint1.toString() + ",末点=" + endPoint2.toString(); 
	}

}

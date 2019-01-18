package main.entity.geometry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import main.arithmetic.SimUtils;

public class LineSegment extends Line{
	public Point endPoint1;
	public Point endPoint2;
	public double length;
	
	public LineSegment() {
		super();
	}
	
	public LineSegment(Point pointFrom,Point pointTo){
		super(pointFrom,pointTo);
		this.endPoint1 = pointFrom;
		this.endPoint2 = pointTo;
		this.endPoint1.motherLine=this;
		this.endPoint2.motherLine=this;
		length = pointFrom.distanceToPoint(pointTo);
	} 
	
	public LineSegment minPathToLineSegment(LineSegment line) {
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

	/**
	 * 获取直线和线段的交点，两者重合时返回NaN;
	 * @param line
	 * @return
	 */
	public Point intersectionPointOfLineSegmentAndLine(Line line) {
		Point point = super.intersectionPointOfTwoLines(line);
		//如果线段是直线的一部分，则返回NAN；
		if (point.isNaN()) {
			return point;
		}
		if(point.isInLineSegment(this)) {
			return point;
		}
		return null;
	}

	/**
	 * 获取两条线段的交点，线段共线且有重合部分返回NaN;
	 * @param lineSegment
	 * @return
	 */
	public Point intersectionPointOfTwoLineSegments(LineSegment lineSegment) { 
		Point point = super.intersectionPointOfTwoLines(lineSegment);
		if (point.isNaN()) {
			if(this.endPoint1.isInLineSegment(lineSegment)||
					this.endPoint2.isInLineSegment(lineSegment)) {
				return point;
			}else {
				return null;
			}
		}
		if(point.isInLineSegment(this)&&point.isInLineSegment(lineSegment)) {
			return point;
		}
		return null;
	}

	/**
	 * 获得线段与一个多边形的交线，即线段位于多边形中的那部分构成的线段
	 */
	public LineSegment intersectionLineSegmentOfLineSegmentAndPolygon(Polygon polygon) {
		LineSegment anotherLineSegment = ((Line)this).intersectionLineSegmentOfLineAndPolygon(polygon);
		if(anotherLineSegment!=null) {
			return this.intersectionLineSegmentOfTwoLineSegments(anotherLineSegment);
		}
		return null;
	}

	/**
	 * 已测试
	 * @param ls
	 * @return
	 */
	public LineSegment intersectionLineSegmentOfTwoLineSegments(LineSegment ls){
		if(!((Line)this).equals(ls)) {
			return null;
		}
		
		Point pointA,pointB,pointC,pointD;
		if(SimUtils.doubleEqual(ls.B, 0)) {
			if (this.endPoint1.y<this.endPoint2.y) {
				pointA=this.endPoint1;
				pointB=this.endPoint2;
			}else {
				pointA=this.endPoint2;
				pointB=this.endPoint1;
			}
			if (ls.endPoint1.y<ls.endPoint2.y) {
				pointC=ls.endPoint1;
				pointD=ls.endPoint2;
			}else {
				pointC=ls.endPoint2;
				pointD=ls.endPoint1;
			}
			if(pointA.y>pointD.y||pointC.y>pointB.y) {
				return null;
			}else{
				Point second= pointA.y>pointC.y?pointA:pointC;
				Point third= pointB.y<pointD.y?pointB:pointD;
				return new LineSegment(second,third);
			}
		}else {
			if (this.endPoint1.x<this.endPoint2.x) {
				pointA=this.endPoint1;
				pointB=this.endPoint2;
			}else {
				pointA=this.endPoint2;
				pointB=this.endPoint1;
			}
			if (ls.endPoint1.x<ls.endPoint2.x) {
				pointC=ls.endPoint1;
				pointD=ls.endPoint2;
			}else {
				pointC=ls.endPoint2;
				pointD=ls.endPoint1;
			}
			if(pointA.x>pointD.x||pointC.x>pointB.x) {
				return null;
			}else{
				Point second= pointA.x>pointC.x?pointA:pointC;
				Point third= pointB.x<pointD.x?pointB:pointD;
				return new LineSegment(second,third);
			}
		}
	} 
	
	public Point getBrotherPoint(Point point) {
		if(endPoint1==point) {
			return endPoint2;
		}else {
			return endPoint1;
		}
	}
	
	public Point getMidPoint() {
		double x = (this.endPoint1.x+this.endPoint2.x)/2;
		double y = (this.endPoint1.y+this.endPoint2.y)/2;
		return new Point(x,y);
	}

	/**
	 * 判断两条线段是否相等
	 * @param lineSegment
	 * @return
	 */
	public boolean equals(LineSegment lineSegment) {
		if(((Line)this).equals(lineSegment)&&
				this.endPoint1.equals(lineSegment.endPoint1)&&
				this.endPoint2.equals(lineSegment.endPoint2)) {
			return true;
		}
		return false;
	}

	/**
	 * toString
	 */
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.00");
		return "Line: [A=" + df.format(A) + ",B=" + df.format(B) + ",C=" + df.format(C) +
				"]" +"	|	截距=" + df.format(-C/B) +
				",方向角=" + df.format(directionAngle/Math.PI) +
				",长度=" + df.format(length) + 
				"	|	起点=" + endPoint1.toString() + ",末点=" + endPoint2.toString(); 
	}

}

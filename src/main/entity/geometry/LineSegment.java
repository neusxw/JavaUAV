package main.entity.geometry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import main.arithmetic.data.SimUtils;

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

	/**
	 * ��ȡֱ�ߺ��߶εĽ��㣬�����غ�ʱ����NaN;
	 * @param line
	 * @return
	 */
	public Point intersectionOfLineSegmentAndLine(Line line) {
		Point point = super.intersectionPointOfTwoLines(line);
		//����߶���ֱ�ߵ�һ���֣��򷵻�NAN��
		if (point.isNaN()) {
			return point;
		}else if(point.isInLineSegment(this)) {
			return point;
		}
		return null;
	}

	/**
	 * ��ȡ�����߶εĽ��㣬�߶ι��������غϲ��ַ���NaN;
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
	 * ����߶���һ������εĽ��ߣ����߶�λ�ڶ�����е��ǲ��ֹ��ɵ��߶�
	 */
	public List<LineSegment> intersectionLineSegmentOfLineSegmentAndPolygon(Polygon polygon) {
		List<LineSegment> list = new ArrayList<LineSegment>();
		List<LineSegment> lineSegments =((Line)this).intersectionLineSegmentOfLineAndPolygon(polygon);
		
		for(LineSegment lineSegment:lineSegments) {
			LineSegment intersection = this.intersectionLineSegmentOfTwoLineSegments(lineSegment);
			if(intersection!=null) {
				list.add(intersection);
			}
		}	
		return list;
	}

	/**
	 * �Ѳ���
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
		}else if(endPoint2==point) {
			return endPoint1;
		}else {
			return null;
		}
	}
	
	public Point getMidPoint() {
		double x = (this.endPoint1.x+this.endPoint2.x)/2;
		double y = (this.endPoint1.y+this.endPoint2.y)/2;
		return new Point(x,y);
	}
	
	public double minDistanceToLineSegment(LineSegment lineSegment) {
		Point point11 = this.endPoint1;
		Point point12 = this.endPoint2;
		Point point21 = lineSegment.endPoint1;
		Point point22 = lineSegment.endPoint2;
		double len1,len2;
		if(point11.distanceToPoint(point21)>point11.distanceToPoint(point22)) {
			len1=point11.distanceToPoint(point22);
		}else {
			len1=point11.distanceToPoint(point21);
		}
		if(point12.distanceToPoint(point21)>point12.distanceToPoint(point22)) {
			len2=point12.distanceToPoint(point22);
		}else {
			len2=point12.distanceToPoint(point21);
		}
		if(len1>len2) {
			return len2;
		}else {
			return len1;
		}
	}

	/**
	 * �ж������߶��Ƿ����
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
				"]" +"	|	�ؾ�=" + df.format(-C/B) +
				",�����=" + df.format(directionAngle/Math.PI) +
				",����=" + df.format(length) + 
				"	|	���=" + endPoint1.toString() + ",ĩ��=" + endPoint2.toString(); 
	}

}

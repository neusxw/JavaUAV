package main.entity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import main.arithmetic.SimUtils;

/*
 * 有向直线；
 */
public class Line {
	public double A;
	public double B;
	public double C;
	public double directionAngle;
	/*
	 * 构造方法1：标准式
	 *直线的标准式,由A、B和C三个参数决定直线。
	 *Ax+By+C=0
	 */
	public Line(double A,double B,double C){
		if (SimUtils.doubleEqual(A, 0)&&SimUtils.doubleEqual(B, 0)) {
			System.out.println("输入非法，A,B不能同时为0");
			return;
		}
		this.A=A;
		this.B=B;
		this.C=C;
		directionAngle =Math.atan2(A, -B);
		normal();
	}

	/*
	 * 构造方法2：两点式
	 * 由两个点生成直线，第一个点到第二个点的方向即为直线的方向；
	 * pointFrom：起点y1
	 * pointTo：末点y2
	 *         (y2-y1)
	 * y-y1 = ---------(x-x1)
	 *         (x2-x1)
	 *           
	 */
	public Line(Point pointFrom,Point pointTo){
		if(pointFrom.equals(pointTo)) {
			//@@@@潜藏重要bug，需要处理！！！
			//System.out.println("warning:两点重合了！！！");
			A=1;
			B=0;
			C=-pointTo.x;
			return;
		}
		A= pointTo.y-pointFrom.y;
		B= pointFrom.x-pointTo.x;
		C= pointFrom.y*pointTo.x-pointFrom.x*pointTo.y;
		directionAngle =Math.atan2(A, -B);
		normal();
	}

	/*
	 * 构造方法3：点斜式
	 * 由斜率k和截距b构造直线
	 * y-y0=k(x-x0);
	 *           
	 */
	public Line(Point point,double slope){
		this.directionAngle = Math.atan(slope);
		if(slope>SimUtils.INFINITY) {
			A=1;
			B=0;
			C=-point.x;
		}else {
			A=slope;
			B=-1;
			C=point.y-slope*point.x;
		}
		normal();
	}

	public void normal() {
		if(SimUtils.doubleEqual(A, 0)) {
			C/=B;
			B=1;
		}else {
			B/=A;
			C/=A;
			A=1;
		}	
	}

	public double distanceToPoint(Point point) {
		return Math.abs(A*point.x+B*point.y+C)/Math.sqrt(A*A+B*B);
	}

	public double distanceToLine(Line line) {
		if (SimUtils.doubleEqual(line.A*B, A*line.B)) {
			if (SimUtils.doubleEqual(A,0)) {
				return Math.abs(C/B-line.C/line.B);
			}else {
				return Math.abs((C/A-line.C/line.A)
						/Math.sqrt(1+Math.pow(line.B/line.A, 2)));
			}
		}else {
			return 0;
		}
	}

	public Point intersectionPointOfTwoLines(Line line) {
		if(this.equals(line)) {
			return new Point(Double.NaN,Double.NaN);
		}
		double x=(B*line.C-line.B*C)/(A*line.B-line.A*B);
		double y=-(A*line.C-line.A*C)/(A*line.B-line.A*B);
		return new Point(x,y);
	}

	public double angleToLine(Line line) {
		return line.directionAngle-this.directionAngle;
	}

	public Line getPerpendicularLine(Point point){
		return new Line(B,-A,-(B*point.x-A*point.y));
	}

	public Line getPerpendicularLine(){
		return new Line(B,-A,0);
	}

	public Point getFootOfPerpendicular(){
		Line perpendicularLine = this.getPerpendicularLine();
		return this.intersectionPointOfTwoLines(perpendicularLine);
	}

	public Point getFootOfPerpendicular(Point point){
		Line perpendicularLine = this.getPerpendicularLine(point);
		return this.intersectionPointOfTwoLines(perpendicularLine);
	}
	/**
	 * 获取直线和线段的交点，两者重合时返回NaN;
	 * @param lineSegment
	 * @return
	 */
	public Point intersectionPointOfLineAndLineSegment(LineSegment lineSegment) {
		Point point = intersectionPointOfTwoLines(lineSegment);
		//如果线段是直线的一部分，则返回NAN；
		if (point.isNaN()) {
			return point;
		}
		if(point.isInLineSegment(lineSegment)) {
			return point;
		}
		return null;
	}

	/**
	 * 获得直线与一个多边形的交线，即直线位于多边形中的那部分构成的线段
	 * @param polygon
	 * @return
	 */
	public LineSegment intersectionLineSegmentOfLineAndPolygon(Polygon polygon) {
		List<Point> crossPoints = new ArrayList<Point>();
		for(LineSegment ls:polygon.edges) {
			Point cross = ls.intersectionPointOfLineSegmentAndLine(this);
			//如果多边形的某条边属于线段所在直线，则返回该边；
			if(cross!=null) {
				if (cross.isNaN()) {
					return ls;
				}
				crossPoints.add(cross);
			}
		}
		if(crossPoints.size()==0) {
			return null;
		}
		//去除重复顶点
		List<Point> tempCrossPoints = new ArrayList<Point>();
		for(int i=0; i<crossPoints.size()-1;i++) {
			int flag=0;
			for(int j=i+1; j<crossPoints.size();j++) {
				if(crossPoints.get(i).equals(crossPoints.get(j))) {
					flag=1;
				}
			}
			if(flag==0) {
				tempCrossPoints.add(crossPoints.get(i));
			}
		}
		tempCrossPoints.add(crossPoints.get(crossPoints.size()-1));
		
		crossPoints=tempCrossPoints;
		if(crossPoints.size()==2) {
			return new LineSegment(crossPoints.get(0),crossPoints.get(1));
		}else if(crossPoints.size()==1){
			return new LineSegment(crossPoints.get(0),crossPoints.get(0));
		}else {
			return null;
		}
	}

	public void move(double leftORright, double distance) {
		double moveDirection;
		if(leftORright==SimUtils.RIGHT) {
			moveDirection=directionAngle-Math.PI/2;
		}else {
			moveDirection=directionAngle+Math.PI/2;
		}
		C-=distance*(A*Math.cos(moveDirection)+B*Math.sin(moveDirection));
	}


	public boolean equals(Line line) { 
		if (SimUtils.doubleEqual(A, line.A)&&
				SimUtils.doubleEqual(B, line.B)&& 
					SimUtils.doubleEqual(C, line.C)){ 
			return true; 
		} 
		return false; 
	}


	public String toString() {
		DecimalFormat df = new DecimalFormat("0.00");
		return "Line: [A=" + df.format(A) + ",B=" + df.format(B) + ",C=" + df.format(C) +
				"] &&&&& 截距=" + df.format(-C/B) +
				"		方向角=" + df.format(directionAngle/Math.PI);
	}

	public void print() {
		System.out.println(this.toString());
	}
}

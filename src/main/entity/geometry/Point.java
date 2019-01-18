package main.entity.geometry;

import java.text.DecimalFormat;

import main.arithmetic.SimUtils;

public class Point {
	public double x;
	public double y;
	public LineSegment motherLine = null;
	public final double z=0;

	public Point(){
		this.x = Double.NaN;
		this.y = Double.NaN;
	}

	public Point(double x,double y){
		this.x = x;
		this.y = y;
	}
	
	public Point(double[] coord){
		this.x = coord[0];
		this.y = coord[1];
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

	/**
	 * ���㵽�߶εľ��붨��Ϊ�㵽�߶����˵�����е���Сֵ��
	 * @param lineSegment
	 * @return
	 */
//	public double distanceToLineSegment(LineSegment lineSegment) {
//		double dis1 = distanceToPoint(lineSegment.endPoint1);
//		double dis2 = distanceToPoint(lineSegment.endPoint2);
//		if (dis1>=dis2) {
//			return dis2;
//		}else {
//			return dis1;
//		}
//	}
	
	public double distanceToLineSegment(LineSegment lineSegment) {
		double x = (lineSegment.endPoint1.x+lineSegment.endPoint2.x)/2;
		double y = (lineSegment.endPoint1.y+lineSegment.endPoint2.y)/2;
		return distanceToPoint(new Point(x,y));
	}
	
	/*
	 * �ж�һ������������ֱ�ߵ���ߡ��ұ߻����ڸ�ֱ����
	 */
	public int positionToLine(Line line){
		if(isInLine(line)) {
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

	public int positionToPolygon(Polygon polygon) {
		for(LineSegment lineSegment:polygon.edges) {
			if(isInLineSegment(lineSegment)) {
				return SimUtils.INBORDER;
			}
		}
		int verticesCount = polygon.vertexes.size();
		int nCross = 0;
		//��ֱ��y=y0,���������θ����ߣ��߶Σ����ཻ������ɵ��߽�����Ŀ�жϵ���Զ���ε�λ�ã�
		for (int i = 0; i < verticesCount; ++ i) {
			Point p1 = polygon.vertexes.get(i);
			Point p2 = polygon.vertexes.get((i + 1) % verticesCount);
			//ƽ�л����ӳ����ϣ����ཻ��
			if ( p1.y == p2.y ||y < Math.min(p1.y, p2.y)||y >= Math.max(p1.y, p2.y)) {
				continue;
			}
			double crossX = (y - p1.y) * (p2.x - p1.x) / (p2.y - p1.y) + p1.x; // �󽻵�� X ����
			if ( crossX > x ) { // ֻͳ�Ƶ��߽���
				nCross++;
			}
		}
		if(nCross%2==1) {
			return SimUtils.INNER;
		}else {
			return SimUtils.OUTTER;
		}
	}

	public boolean isInLine(Line line) {
		if(SimUtils.doubleEqual(line.A*x+line.B*y+line.C, 0)) {
			return true;
		}
		return false;
	}

	public boolean isInLineSegment(LineSegment line) {
		if(isInLine(line)) {
			if(SimUtils.doubleEqual(line.endPoint1.distanceToPoint(line.endPoint2),
					this.distanceToPoint(line.endPoint1)+this.distanceToPoint(line.endPoint2))) {
				return true;
			}
		}
		return false;
	}

	public boolean isNaN() {
		if(Double.isNaN(x)&&Double.isNaN(y)) {
			return true;
		}
		return false;
	}

	public boolean equals(Point point) {
		if (SimUtils.doubleEqual(x,point.x)&&
				SimUtils.doubleEqual(y,point.y)) {
			return true;
		}
		return false;
	}

	public String toString() {
		DecimalFormat df = new DecimalFormat("0.00");
		return "(" + df.format(x) + "," +df.format(y) +")";
	}

	public void print() {
		System.out.println(toString());
	}
}

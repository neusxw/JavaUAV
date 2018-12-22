package main.matter;

import main.arithmetic.SimUtils;

public class Line {
	private Point endPoint1;
	private Point endPoint2;
	private double A;
	private double B;
	private double C;

	public Line(Point endPoint1,Point endPoint2){
		this.endPoint1=endPoint1;
		this.endPoint2=endPoint2;
		toNormalFormula();
	}
	public void toNormalFormula() {
		A=-(endPoint1.getY()-endPoint2.getY());
		B= endPoint1.getX()-endPoint2.getX();
		C= endPoint1.getY()*endPoint2.getX()-endPoint1.getX()*endPoint2.getY();
	}
	
	public double distanceToStraightLine(Line line) {
		if (SimUtils.doubleEqual(line.A*B, A*line.getB())) {
			if (SimUtils.doubleEqual(A,0)) {
				return Math.abs(C/B-line.getC()/line.getB());
			}else {
				return Math.abs((C/A-line.getC()/line.getA())
						/Math.sqrt(1+Math.pow(line.getB()/line.getA(), 2)));
			}
		}else {
			return 0;
		}
	}
	
	public double distanceToLinesegment(Line line) {
		Point p1 = line.getEndPoint1();
		Point p2 = line.getEndPoint2();
		double d11= endPoint1.distanceToPoint(p1);
		double d12= endPoint1.distanceToPoint(p2);
		double d21= endPoint2.distanceToPoint(p1);
		double d22= endPoint2.distanceToPoint(p2);
		double[] d = {d11,d12,d21,d22};
		int index = SimUtils.findIndexOfMax(d);
		if(index==0){
			return d11;
		}else if(index==1){
			return d12;
		}else if(index==2){
			return d21;
		}else{
			return d22;
		}
	}
	
	public Point getEndPoint1() {
		return endPoint1;
	}
	public void setEndPoint1(Point endPoint1) {
		this.endPoint1 = endPoint1;
	}
	public Point getEndPoint2() {
		return endPoint2;
	}
	public void setEndPoint2(Point endPoint2) {
		this.endPoint2 = endPoint2;
	}
	public double getA() {
		return A;
	}
	public double getB() {
		return B;
	}
	public double getC() {
		return C;
	}
}

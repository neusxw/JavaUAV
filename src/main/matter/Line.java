package main.matter;

import main.arithmetic.SimUtils;

public class Line {
	public double slope;
	public double intercept;
	public double A;
	public double B;
	public double C;
	
	Line(Point point1,Point point2){
		if(point1.equals(point2)) {
			System.out.println("输入非法，两个点不能重合");
			return;
		}
		toNormalFormula(point1,point2);
	}

	public Line(double A,double B,double C){
		if (SimUtils.doubleEqual(A, 0)&&SimUtils.doubleEqual(B, 0)) {
			System.out.println("输入非法，A,B不能同时为0");
			return;
		}
		this.A=A;
		this.B=B;
		this.C=C;
	}

	public void toNormalFormula(Point point1,Point point2) {
		A=-(point1.y-point2.y);
		B= point1.x-point2.x;
		C= point1.y*point2.x-point1.x*point2.y;
	}

	public double distanceToPoint(Point point) {
		return Math.abs(A*point.x+B*point.y+C)/Math.sqrt(A*A+B*B);
	}
	
	public double distanceToStraightLine(Line line) {
		if (SimUtils.doubleEqual(line.A*B, A*line.B)) {
			if (SimUtils.doubleEqual(A,0)) {
				return Math.abs(C/B-line.C/line.B);
			}else {
				return Math.abs((C/A-line.C/line.A)
						/Math.sqrt(1+Math.pow(line.B/line.B, 2)));
			}
		}else {
			return 0;
		}
	}
}

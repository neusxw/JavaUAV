package main.matter;

import java.text.DecimalFormat;

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
			System.out.println("输入非法，两个点不能重合");
			return;
		}
		toNormalFormula(pointFrom,pointTo);
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
			A=0;
			B=1;
			C=-point.x;
		}else {
			A=slope;
			B=-1;
			C=point.y-slope*point.x;
		}
	}

	public void toNormalFormula(Point pointFrom,Point pointTo) {
		A= pointTo.y-pointFrom.y;
		B= pointFrom.x-pointTo.x;
		C= pointFrom.y*pointTo.x-pointFrom.x*pointTo.y;
		directionAngle =Math.atan2(A, -B);
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

	public Point Intersection(Line line) {
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
		return this.Intersection(perpendicularLine);
	}

	public Point getFootOfPerpendicular(Point point){
		Line perpendicularLine = this.getPerpendicularLine(point);
		return this.Intersection(perpendicularLine);
	}

	public void move(double leftORright, double distance) {
		double moveDirection=0;
		if(leftORright==SimUtils.RIGHT) {
			moveDirection=directionAngle-Math.PI/2;
		}else {
			moveDirection=directionAngle+Math.PI/2;
		}
		C-=distance*(A*Math.cos(moveDirection)+B*Math.sin(moveDirection));
	}

	public boolean equals(Line line) {
		if (A==line.A && B==line.B && C==line.C){
			return true;
		}	
		return false;
	}

	public String toString() {
		DecimalFormat df = new DecimalFormat("0.00");
		return "Line: [A=" + df.format(A) + ",B=" + df.format(B) + ",C=" + df.format(C) +
				"]		截距=" + df.format(-C/B) +
				"		方向角=" + df.format(directionAngle/Math.PI);
	}
}

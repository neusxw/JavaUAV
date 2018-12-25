package main.matter;

import main.arithmetic.SimUtils;

/*
 * ����ֱ�ߣ�
 */
public class Line {
	public double slope;
	public double intercept;
	public double A;
	public double B;
	public double C;
	public double direction;
	
	/*
	 * ���췽��1��
	 *ֱ�ߵı�׼ʽ��
	 */
	public Line(double A,double B,double C){
		if (SimUtils.doubleEqual(A, 0)&&SimUtils.doubleEqual(B, 0)) {
			System.out.println("����Ƿ���A,B����ͬʱΪ0");
			return;
		}
		this.A=A;
		this.B=B;
		this.C=C;
		direction =Math.atan2(A, -B);
	}
	
	/*
	 * ���췽��2��
	 * ������������ֱ�ߣ���һ���㵽�ڶ�����ķ���Ϊֱ�ߵķ���
	 * pointFrom�����
	 * pointTo��ĩ��
	 */
	public Line(Point pointFrom,Point pointTo){
		if(pointFrom.equals(pointTo)) {
			System.out.println("����Ƿ��������㲻���غ�");
			return;
		}
		toNormalFormula(pointFrom,pointTo);
	}

	public void toNormalFormula(Point pointFrom,Point pointTo) {
		A= pointTo.y-pointFrom.y;
		B= pointFrom.x-pointTo.x;
		C= pointFrom.y*pointTo.x-pointFrom.x*pointTo.y;
		direction =Math.atan2(A, -B);
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
	
	public double angleToDirectionalLine(Line line) {
		return line.direction-this.direction;
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
}

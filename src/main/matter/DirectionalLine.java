package main.matter;

public class DirectionalLine extends Line{
	public double direction;
	
	public DirectionalLine(Point point1,Point point2){
		super(point1,point2);
		direction =Math.atan2(point2.y-point1.y, point2.x-point1.x);
	}

	public DirectionalLine(double A,double B,double C){
		super(A,B,C);
		direction =Math.atan2(-A, B);
	}
	
	public double angleToStraightLine(DirectionalLine line) {
		return line.direction-this.direction;
	}
	
	public DirectionalLine getVerticalLine(Point point){
		return new DirectionalLine(B,-A,-(B*point.x-A*point.y));
	}
	
	public DirectionalLine getVerticalLine(){
		return new DirectionalLine(B,-A,0);
	}
	
	public Point Intersection(DirectionalLine line) {
		double x=(B*line.C-line.B*C)/(A*line.B-line.A*B);
		double y=-(A*line.C-line.A*C)/(A*line.B-line.A*B);
		return new Point(x,y);
	}
}

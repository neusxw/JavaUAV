package main.matter;

public class Point {
	private double x;
	private double y;
	
	public Point(){
		this.x = 0;
		this.y = 0;
	}
	
	public Point(double x,double y){
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	public double distanceToPoint(Point p) {
		return Math.sqrt(Math.pow(x - p.x, 2)+Math.pow(y - p.y, 2));
	}
	
	public double distanceToStraightLine(Line l) {
		Point p1 = l.getEndPoint1();
		Point p2 = l.getEndPoint2();
		//double k = (p1.getY()-p2.getY())/(p1.getX()-p2.getX());
		return Math.abs((p1.getY()-p2.getY())*(p1.getX()-this.getX())
				-(p1.getY()-this.getX())*(p1.getX()-p2.getX()))
				/Math.sqrt(Math.pow(p1.getX()-p2.getX(), 2)+Math.pow(p1.getY()-p2.getY(), 2));
	}

}

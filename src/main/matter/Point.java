package main.matter;

public class Point {
	private double x;
	private double y;
	
	Point(){
		this.x = 0;
		this.y = 0;
	}
	
	Point(double x,double y){
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

}

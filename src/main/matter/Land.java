package main.matter;

public class Land extends Polygon{
	private double ridgeWideth = 0.5;
	private double ridgeDirection = Math.PI / 2;
	public Land(){
		Map.getInstance().addland(this);
	}
	
	public void createLines(){
		
	}
	public double getRidgeWideth() {
		return ridgeWideth;
	}
	public void setRidgeWideth(double ridgeWideth) {
		this.ridgeWideth = ridgeWideth;
	}
	public double getRidgeDirection() {
		return ridgeDirection;
	}
	public void setRidgeDirection(double ridgeDirection) {
		this.ridgeDirection = ridgeDirection;
	}
	
	public void setRidgeDirection(Point p1,Point p2) {
		double deltaX=p1.getX()-p2.getX();
		double deltaY=p1.getY()-p2.getY();
		
		// @@@，存在除0情况，需要完善
		this.ridgeDirection = Math.atan2(deltaY, deltaX);
		if (this.ridgeDirection < 0) {
			this.ridgeDirection +=Math.PI;
		}
	}
}

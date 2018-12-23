package main.matter;

public class Land extends Polygon{
	public double ridgeWideth = 0.5;
	public double ridgeDirection = Math.PI / 2;
	public Land(){
		Map.getInstance().addland(this);
	}
	
	public void createLines(){
		
	}
	
	public void setRidgeDirection(Point p1,Point p2) {
		double deltaX=p1.x-p2.x;
		double deltaY=p1.y-p2.y;
		this.ridgeDirection = Math.atan2(deltaY, deltaX);
		if (this.ridgeDirection < 0) {
			this.ridgeDirection +=Math.PI;
		}
	}
}

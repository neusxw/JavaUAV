package main.entity;

public class FlightPoint extends Point{
	private FlightPoint next;
	private double direction = Double.NaN;
	public FlightPoint() {
		super();
	}
	
	public FlightPoint(double x,double y) {
		super(x,y);
	}
	
	public FlightPoint(Point point) {
		this.x = point.x;
		this.y = point.y;
	}

	public FlightPoint getNext() {
		return next;
	}

	public void setNext(FlightPoint next) {
		this.next = next;
		direction = this.directionToPoint(next);
	}

	public double getDirection() {
		return direction;
	} 
}

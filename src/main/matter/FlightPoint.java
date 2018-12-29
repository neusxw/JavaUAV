package main.matter;

public class FlightPoint extends Point{
	private FlightPoint previous;
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

	public FlightPoint getPrevious() {
		return previous;
	}

	public void setPrevious(FlightPoint previous) {
		this.previous = previous;
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

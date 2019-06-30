package entity;

import entity.geometry.LineSegment;
import entity.geometry.Point;

public class GridLine extends LineSegment{
	private Land motherLand;
	
	public GridLine(Point pointFrom,Point pointTo, Land motherLand) {
		super(pointFrom,pointTo);
		this.motherLand=motherLand;
	}
	
	public GridLine(LineSegment lineSegment, Land motherLand) {
		super(lineSegment.endPoint1,lineSegment.endPoint2);
		this.motherLand=motherLand;
	}
	
	public Land getMotherLand() {
		return motherLand;
	}

	public void setMotherLand(Land motherLand) {
		this.motherLand = motherLand;
	}
	
	public String toString() {
		return super.toString() + "	motherLand:" + this.motherLand.getID();
	}
}

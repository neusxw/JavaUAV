package main.arithmetic.GA;

import main.entity.geometry.LineSegment;

public class City {
	LineSegment lineSegment;

	public City(LineSegment ls) {
		this.lineSegment=ls;
	}

	public LineSegment getLineSegment() {
		return lineSegment;
	}

	public void setLineSegment(LineSegment lineSegment) {
		this.lineSegment = lineSegment;
	}
	
}

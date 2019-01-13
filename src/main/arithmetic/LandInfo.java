package main.arithmetic;

import java.util.List;

public class LandInfo extends MapInfo{
	private double ridgeWideth;
	private double ridgeDirection;
	public LandInfo() {}
	
	public LandInfo(String type, List<double[]> coords, double ridgeWideth,double ridgeDirection) {
		super(type, coords);
		this.ridgeWideth=ridgeWideth;
		this.ridgeDirection=ridgeDirection;
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
}

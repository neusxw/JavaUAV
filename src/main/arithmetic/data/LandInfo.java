package main.arithmetic.data;

import java.util.List;

public class LandInfo extends MapInfo{
	private double ridgeWideth;
	private List<double[]> ridgeDirection;
	public LandInfo() {}
	
	public LandInfo(String type, List<double[]> coords, double ridgeWideth, List<double[]> ridgeDirection) {
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

	public List<double[]> getRidgeDirection() {
		return ridgeDirection;
	}

	public void setRidgeDirection(List<double[]> ridgeDirection) {
		this.ridgeDirection = ridgeDirection;
	}
}

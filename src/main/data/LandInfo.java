package main.data;

import java.util.List;

public class LandInfo extends MapInfo{
	private double ridgeWideth;
	private List<double[]> ridgeDirection;
	private double height;
	public LandInfo() {}
	
	public LandInfo(String type, List<double[]> coords, double ridgeWideth, List<double[]> ridgeDirection,double height) {
		super(type, coords);
		this.ridgeWideth=ridgeWideth;
		this.ridgeDirection=ridgeDirection;
		this.height=height;
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

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}
}

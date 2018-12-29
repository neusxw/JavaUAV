package main.entity;


import java.util.ArrayList;
import java.util.List;

import main.arithmetic.SimUtils;

public class UAV {
	private double maxLiquid = 1;
	private double maxBattery = 1;
	private double idleSpeed = 0.05;
	private double operationSpeed = 0.02;
	private double liquidPerStep = 0.02; 
	private double batteryPerStep = 0.03;
	private FlightPoint currentPosition;
	private FlightPoint currentDestination = null;
	private FlightPoint nextDestination = null;
	private double direction;
	private Map map = Map.getInstance();
	public List<FlightPoint> trajectory = new ArrayList<FlightPoint>();

	public UAV() {
		this.currentPosition= new FlightPoint(SimUtils.Origin);
		trajectory.add(currentPosition);
	}
	public UAV(FlightPoint start) {
		this.currentPosition=start;
		trajectory.add(currentPosition);
	}

	public void creatTrajectory() {
		while (map.gridLines.size()>0) {
			chooseNextLine();
		}
		trajectory.add(new FlightPoint(start));
	}
	
	public void chooseNextLine() {
		 
		double minDistanceToGridLines = Double.MAX_VALUE;
		LineSegment candidateLine = null;
		for (LineSegment gridLine:map.gridLines) {
			if (currentPosition.distanceToLineSegment(gridLine) < minDistanceToGridLines) {
				minDistanceToGridLines=currentPosition.distanceToLineSegment(gridLine);
				candidateLine=gridLine;
			}
		}
		if(currentPosition.distanceToPoint(candidateLine.endPoint1)>=currentPosition.distanceToPoint(candidateLine.endPoint2)) {
			
			currentDestination = new FlightPoint(candidateLine.endPoint2);
			nextDestination = new FlightPoint(candidateLine.endPoint1);
		}else {
			currentDestination = new FlightPoint(candidateLine.endPoint1);
			nextDestination = new FlightPoint(candidateLine.endPoint2);
		}
		currentPosition.setNext(currentDestination);
		currentDestination.setPrevious(currentPosition);
		currentDestination.setNext(nextDestination);
		nextDestination.setPrevious(currentDestination);
		trajectory.add(currentDestination);
		trajectory.add(nextDestination);
		currentPosition = nextDestination;
		map.gridLines.remove(candidateLine);
	}
	/*
	 * @@@@@@未完成，暂时不使用
	 */
	public void moveToNextPosition() {
		double deltaLength = Math.sqrt(Math.pow(currentDestination.x-currentPosition.x, 2)+Math.pow(currentDestination.y-currentPosition.y, 2));
		if(deltaLength<operationSpeed) {
			trajectory.add(currentDestination);
		}else {
			
		}
		double deltaX=operationSpeed*(currentDestination.x-currentPosition.x)/deltaLength;
		double deltaY=operationSpeed*(currentDestination.y-currentPosition.y)/deltaLength;
	}


	public double getMaxLiquid() {
		return maxLiquid;
	}
	public void setMaxLiquid(double maxLiquid) {
		if(maxLiquid<0) {
			System.out.println("最大载液量不能小于0");
			return;
		}
		this.maxLiquid = maxLiquid;
	}
	public double getMaxBattery() {
		return maxBattery;
	}
	public void setMaxBattery(double maxBattery) {
		if(maxBattery<0) {
			System.out.println("最大电池量不能小于0");
			return;
		}
		this.maxBattery = maxBattery;
	}
}

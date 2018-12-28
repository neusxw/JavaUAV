package main.matter;


import java.util.ArrayList;
import java.util.List;

public class UAV {
	private double maxLiquid = 1;
	private double maxBattery = 1;
	private double idleSpeed = 0.05;
	private double operationSpeed = 0.02;
	private double liquidPerStep = 0.02; 
	private double batteryPerStep = 0.03;
	private Point currentPosition;
	private Point previousDestination = null;
	private Point currentDestination = null;
	private Point nextDestination = null;
	private double direction;
	private Map map;
	public List<Point> trajectory = new ArrayList<Point>();

	public UAV(Point start,Map map) {
		this.currentPosition=start;
		trajectory.add(currentPosition);
		this.map = map;
	}

	public void ChooseNextLine() {
		List<LineSegment>  gridLines = map.gridLines;
		double minDistanceToGridLines = Double.MAX_VALUE;
		LineSegment candidateLine = null;
		for (LineSegment gridLine:gridLines) {
			if (currentPosition.distanceToLineSegment(gridLine)<minDistanceToGridLines) {
				minDistanceToGridLines=currentPosition.distanceToLineSegment(gridLine);
				candidateLine=gridLine;
			}
		}
		previousDestination=currentDestination;
		if(currentPosition.distanceToPoint(candidateLine.endPoint1)>=currentPosition.distanceToPoint(candidateLine.endPoint2)) {
			currentDestination = candidateLine.endPoint2;
			nextDestination = candidateLine.endPoint1;
		}else {
			currentDestination = candidateLine.endPoint1;
			nextDestination = candidateLine.endPoint2;
		}
	}

	public void moveToNextPosition() {
		double deltaLength = Math.sqrt(Math.pow(currentDestination.x-currentPosition.x, 2)+Math.pow(currentDestination.y-currentPosition.y, 2));
		double deltaX=operationSpeed*(currentDestination.x-currentPosition.x)/
				(Math.sqrt(Math.pow(currentDestination.x-currentPosition.x, 2)+Math.pow(currentDestination.y-currentPosition.y, 2)));
		double deltaY=operationSpeed*(currentDestination.y-currentPosition.y)/
				(Math.sqrt(Math.pow(currentDestination.x-currentPosition.x, 2)+Math.pow(currentDestination.y-currentPosition.y, 2)));
		
		
	}



	/**
	 * @return the maxLiquid
	 */
	public double getMaxLiquid() {
		return maxLiquid;
	}
	/**
	 * @param maxLiquid the maxLiquid to set
	 */
	public void setMaxLiquid(double maxLiquid) {
		if(maxLiquid<0) {
			System.out.println("最大载液量不能小于0");
			return;
		}
		this.maxLiquid = maxLiquid;
	}
	/**
	 * @return the maxBattery
	 */
	public double getMaxBattery() {
		return maxBattery;
	}
	/**
	 * @param maxBattery the maxBattery to set
	 */
	public void setMaxBattery(double maxBattery) {
		if(maxBattery<0) {
			System.out.println("最大电池量不能小于0");
			return;
		}
		this.maxBattery = maxBattery;
	}



}

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
	public List<FlightPoint> trajectory = new ArrayList<FlightPoint>();
	private TakeOffPoint takeOffPoint;

	public UAV(TakeOffPoint takeOffPoint) {
		this.setTakeOffPoint(takeOffPoint);
		this.currentPosition=takeOffPoint;
		trajectory.add(currentPosition);
	}
	public UAV(Station station) {
		for(TakeOffPoint takeOffPoint:station.takeOffPoints) {
			if(takeOffPoint.isOccupied==false) {
				this.setTakeOffPoint(takeOffPoint);
				break;
			}
		}
		this.currentPosition=this.takeOffPoint;
		trajectory.add(currentPosition);
	}

	public void creatTrajectory() {
		while (Map.getInstance().gridLines.size()>0) {
			chooseNextPoint();
		}
		trajectory.add(new FlightPoint(takeOffPoint));
	}

	public void chooseNextLine() {
		double minDistanceToGridLines = Double.MAX_VALUE;
		LineSegment candidateLine = null;
		for (LineSegment gridLine:Map.getInstance().gridLines) {
			if(currentPosition.distanceToLineSegment(gridLine) < minDistanceToGridLines) {
				minDistanceToGridLines=currentPosition.distanceToLineSegment(gridLine);
				candidateLine=gridLine;
			}
		}
		if(currentPosition.distanceToPoint(candidateLine.endPoint1)>=
				currentPosition.distanceToPoint(candidateLine.endPoint2)) {
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
		Map.getInstance().gridLines.remove(candidateLine);
	}
	
	public void chooseNextPoint() {
		
		double minDistanceToGridPoints = 2*Double.MAX_VALUE;
		Point candidatePoint = null;
		for (Point gridPoint:Map.getInstance().gridPoints) {
			double distance = Map.getInstance().distanceOfTwoPoints(currentPosition, gridPoint);
			//System.out.println(currentPosition.toString() + "和" + gridPoint+"之间的距离是：" + distance);
			if(Map.getInstance().distanceOfTwoPoints(currentPosition, gridPoint) < minDistanceToGridPoints) {
				minDistanceToGridPoints=Map.getInstance().distanceOfTwoPoints(currentPosition, gridPoint);
				candidatePoint=gridPoint;
			}
		}
		
		//candidatePoint.print();
		Point candidateBrother = Map.getInstance().getBrotherPoint(candidatePoint);
		//System.out.println("当前位置：" + Map.getInstance().getMotherLine(candidatePoint));
		//生成将要到达的两个FlightPoint（两点在同一条线段上）；
		currentDestination = new FlightPoint(candidatePoint);
		nextDestination=new FlightPoint(candidateBrother);
		//更新FlightPoint的双向链表关系；
		currentPosition.setNext(currentDestination);
		currentDestination.setPrevious(currentPosition);
		currentDestination.setNext(nextDestination);
		nextDestination.setPrevious(currentDestination);
		//更新轨迹线和UAV位置；
		trajectory.add(currentDestination);
		trajectory.add(nextDestination);
		currentPosition = nextDestination;
		//更新grid
		Map.getInstance().gridPoints.remove(candidateBrother);
		Map.getInstance().gridPoints.remove(candidatePoint);
		Map.getInstance().gridLines.remove(Map.getInstance().getMotherLine(candidatePoint));
	}
	
	/*
	 * @@@@@@未完成，暂时不使用
	 */
	public void moveByStep() {
		double deltaLength = Math.sqrt(Math.pow(currentDestination.x-currentPosition.x, 2)+
										Math.pow(currentDestination.y-currentPosition.y, 2));
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

	public TakeOffPoint getTakeOffPoint() {
		return takeOffPoint;
	}
	public void setTakeOffPoint(TakeOffPoint takeOffPoint) {
		this.takeOffPoint = takeOffPoint;
		takeOffPoint.setaUAV(this);;
	}
}

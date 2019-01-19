package main.entity;


import java.util.ArrayList;
import java.util.List;

import main.arithmetic.Dijkstra;
import main.arithmetic.SimUtils;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;

public class UAV {
	private Point position;
	private Point destination = null;
	private Point nextDestination = null;
	public List<Point> trajectory = new ArrayList<Point>();
	private TakeOffPoint takeOffPoint;
	private List<LineSegment> gridLines= Map.getInstance().gridLines;
	private List<Point> gridPoints=  Map.getInstance().gridPoints;
	

	public UAV(TakeOffPoint takeOffPoint) {
		this.setTakeOffPoint(takeOffPoint);
		this.position=takeOffPoint;
		trajectory.add(position);
		Map.getInstance().UAVs.add(this);
	}
	public UAV(Station station) {
		for(TakeOffPoint takeOffPoint:station.takeOffPoints) {
			if(takeOffPoint.isOccupied==false) {
				this.setTakeOffPoint(takeOffPoint);
				break;
			}
		}
		this.position=this.takeOffPoint;
		trajectory.add(position);
		Map.getInstance().UAVs.add(this);
	}

	public void creatTrajectory() {
		//takeOffFromSide();
		while (gridLines.size()>0) {
			//System.out.println(grid.size());
			chooseNextPoint();
			//chooseNextLine();
		}
		homewardVoyage();
	}

	public void chooseNextLine() {
		double minDistanceToGridLines = Double.MAX_VALUE;
		LineSegment candidateLine = null;
		for (LineSegment gridLine:gridLines) {
			if(position.distanceToLineSegment(gridLine,"midpoint") < minDistanceToGridLines) {
				minDistanceToGridLines=position.distanceToLineSegment(gridLine,"midpoint");
				candidateLine=gridLine;
			}
		}
		if(position.distanceToPoint(candidateLine.endPoint1)>=
				position.distanceToPoint(candidateLine.endPoint2)) {
			destination = candidateLine.endPoint2;
			nextDestination = candidateLine.endPoint1;
		}else {
			destination = candidateLine.endPoint1;
			nextDestination = candidateLine.endPoint2;
		}

		trajectory.add(destination);
		trajectory.add(nextDestination);
		position = nextDestination;
		gridLines.remove(candidateLine);
	}

	public void chooseNextPoint() {
		double minDistance = Double.MAX_VALUE;
		Point candidate = null;
		for (Point gridPoint:gridPoints) {
			if(Grid.distanceOfTwoPoints(position, gridPoint) < minDistance) {
				minDistance=Grid.distanceOfTwoPoints(position, gridPoint);
				candidate=gridPoint;
			}
		}
		destination = candidate;
		if (!Grid.isConnected(position, candidate)) {
			candidate.print();
			//Map.getInstance().getBrotherPoint(candidate).print();
			obstacleAvoidance(position,candidate);
		}
		//生成与destination在同一条线段上的FlightPoint，记为nextDestination；
		Point brother = grid.getBrotherPoint(candidate);
		nextDestination=brother;
		
		//更新轨迹线和UAV位置；
		trajectory.add(destination);
		trajectory.add(nextDestination);
		position = nextDestination;
		//更新grid
		grid.remove(grid.getMotherLine(candidate));
	}

	public void takeOffFromSide(){
		Land land = Map.getInstance().lands.get(0);
		Point leftPoint = land.getSidePoint(SimUtils.LEFT);
		leftPoint.print();
		double minDis = SimUtils.INFINITY;
		Point candidatePoint=null;
		for(Point point:grid.getGridPoints()) {
			if(point.distanceToPoint(leftPoint)<minDis){
				minDis=point.distanceToPoint(leftPoint);
				candidatePoint=point;
			}
		}
		if(position.distanceToPoint(grid.getBrotherPoint(candidatePoint))<
				position.distanceToPoint(candidatePoint)) {
			candidatePoint=grid.getBrotherPoint(candidatePoint);
		}
		Dijkstra dj = new Dijkstra(position,candidatePoint,Map.getInstance().obstacles);
		List<Point> path = dj.getShortestPath();
		for(int i = 1;i<path.size();i++) {
			Point fp = path.get(i);
			trajectory.add(fp);
		}
		Point brother = grid.getBrotherPoint(candidatePoint);
		trajectory.add(brother);
		position=brother;

		grid.remove(grid.getMotherLine(candidatePoint));
	}
	
	public void takeOffFromMinDistance(){
		double minDis = SimUtils.INFINITY;
		Point candidatePoint=null;
		for(Point point:grid.getGridPoints()) {
			if(point.distanceToPoint(position)<minDis){
				minDis=point.distanceToPoint(position);
				candidatePoint=point;
			}
		}
		if(position.distanceToPoint(grid.getBrotherPoint(candidatePoint))<
				position.distanceToPoint(candidatePoint)) {
			candidatePoint=grid.getBrotherPoint(candidatePoint);
		}
		Dijkstra dj = new Dijkstra(position,candidatePoint,Map.getInstance().obstacles);
		List<Point> path = dj.getShortestPath();
		for(int i = 1;i<path.size();i++) {
			Point fp = path.get(i);
			trajectory.add(fp);
		}
		Point brother = grid.getBrotherPoint(candidatePoint);
		trajectory.add(brother);
		position=brother;

		grid.getGridLines().remove(grid.getMotherLine(candidatePoint));
	}
	
	public void obstacleAvoidance(Point from,Point to) {
		Dijkstra dj = new Dijkstra(from,to,Map.getInstance().obstacles);
		List<Point> path = dj.getShortestPath();
		for(int i = 1;i<path.size();i++) {
			Point ft = path.get(i);
			trajectory.add(ft);
		}
	}
	
	public void homewardVoyage() {
		obstacleAvoidance(position,this.takeOffPoint);
	}

	public TakeOffPoint getTakeOffPoint() {
		return takeOffPoint;
	}
	public void setTakeOffPoint(TakeOffPoint takeOffPoint) {
		this.takeOffPoint = takeOffPoint;
		takeOffPoint.setUAV(this);
	}
}
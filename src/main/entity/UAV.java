package main.entity;


import java.util.ArrayList;
import java.util.List;

import main.arithmetic.Dijkstra;
import main.arithmetic.SimUtils;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;

public class UAV {
	public int ID;
	private static int IDcount = 0;
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
		gridPoints.remove(takeOffPoint);
		ID = IDcount++;
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
		ID = IDcount++;
	}

	public void creatTrajectory() {
		//takeOffFromSide();
		//System.out.println(gridLines.size());
		while (gridLines.size()>0) {
			//System.out.println(grid.size());
			chooseNextPoint();
			//chooseNextLine();
		}
		homewardVoyage();
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
		if (!Grid.getConnectedRelation(position, candidate)) {
			obstacleAvoidance(position,candidate);
		}
		//������destination��ͬһ���߶��ϵ�FlightPoint����ΪnextDestination��
		Point brother = Grid.getBrotherPoint(candidate);
		nextDestination=brother;
		
		//���¹켣�ߺ�UAVλ�ã�
		trajectory.add(destination);
		trajectory.add(nextDestination);
		position = nextDestination;
		//����grid
		gridLines.remove(Grid.getMotherLine(candidate));
		gridPoints.remove(candidate);
		gridPoints.remove(Grid.getBrotherPoint(candidate));
	}

	public void takeOffFromSide(){
		Land land = Map.getInstance().lands.get(0);
		Point leftPoint = land.getSidePoint(SimUtils.LEFT);
		leftPoint.print();
		double minDis = SimUtils.INFINITY;
		Point candidatePoint=null;
		for(Point point:gridPoints) {
			if(point.distanceToPoint(leftPoint)<minDis){
				minDis=point.distanceToPoint(leftPoint);
				candidatePoint=point;
			}
		}
		if(position.distanceToPoint(Grid.getBrotherPoint(candidatePoint))<
				position.distanceToPoint(candidatePoint)) {
			candidatePoint=Grid.getBrotherPoint(candidatePoint);
		}
		Dijkstra dj = new Dijkstra(position,candidatePoint,Map.getInstance().obstacles);
		List<Point> path = dj.getShortestPath();
		for(int i = 1;i<path.size();i++) {
			Point fp = path.get(i);
			trajectory.add(fp);
		}
		Point brother = Grid.getBrotherPoint(candidatePoint);
		trajectory.add(brother);
		position=brother;

		gridLines.remove(Grid.getMotherLine(candidatePoint));
	}
	
	public void obstacleAvoidance(Point from,Point to) {
		List<Point> path = Grid.getPath(from,to);
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
	
	public List<LineSegment> getGridLines() {
		return gridLines;
	}
	public void setGridLines(List<LineSegment> gridLines) {
		this.gridLines = gridLines;
	}
	public List<Point> getGridPoints() {
		return gridPoints;
	}
	public void setGridPoints(List<Point> gridPoints) {
		this.gridPoints = gridPoints;
	}
}
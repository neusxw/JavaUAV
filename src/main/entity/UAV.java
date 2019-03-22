package main.entity;


import java.util.ArrayList;
import java.util.List;

import main.arithmetic.Dijkstra;
import main.arithmetic.TwoOpt4TSP;
import main.arithmetic.data.SimUtils;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;

public class UAV {
	static int UVAnum=0;
	private Point position;
	private Point destination = null;
	private Point nextDestination = null;
	public List<Point> trajectory = new ArrayList<Point>();
	private TakeOffPoint takeOffPoint;
	private List<LineSegment> gridLines = new ArrayList<LineSegment>();;
	private List<Point> gridPoints = new ArrayList<Point>();
	

	public UAV(TakeOffPoint takeOffPoint) {
		this.setTakeOffPoint(takeOffPoint);
		this.position=takeOffPoint;
		//trajectory.add(position);
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
		System.out.println("——————————第"+ UVAnum++ +"架无人机生成轨迹——————————");
		//System.out.println(gridLines.size());
		trajectory.add(position);
		while (gridLines.size()>0) {
			chooseNextPoint();
			//System.out.println(gridLines.size());
			//chooseNextLine();
		}
		//homewardVoyage();
	}
	
	public void creatTrajectory2Opt() {
		System.out.println("——————————第"+ UVAnum++ +"架无人机生成轨迹——————————");
		trajectory.addAll(new TwoOpt4TSP().run((ArrayList<LineSegment>) gridLines, this.takeOffPoint));
	}

	public void chooseNextPoint() {
		double minDistance = Double.MAX_VALUE;
		Point candidate = null;
		//System.out.println(gridPoints.size());
		for (Point gridPoint:gridPoints) {
			if(SimpleGrid.distanceOfTwoPoints(position, gridPoint) < minDistance) {
				minDistance=SimpleGrid.distanceOfTwoPoints(position, gridPoint);
				candidate=gridPoint;
			}
		}

		if (!SimpleGrid.isConnected(position, candidate)) {
			//obstacleAvoidance(position,candidate);
		}
		destination = candidate;
		//生成与destination在同一条线段上的FlightPoint，记为nextDestination；
		Point brother = candidate.getBrotherPoint();
		nextDestination=brother;
		
		//System.out.println(position);
		//System.out.println(candidate);
		
		//更新轨迹线和UAV位置；
		trajectory.add(destination);
		trajectory.add(nextDestination);
		position = nextDestination;
		//更新grid
		gridLines.remove(candidate.getMotherLine());
		gridPoints.remove(candidate);
		gridPoints.remove(candidate.getBrotherPoint());
	}
	
	public void obstacleAvoidance(Point from,Point to) {
		List<Point> path = SimpleGrid.getPath(from,to);
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
		createGridPoints();
	}
	public List<Point> getGridPoints() {
		return gridPoints;
	}
	
	private void createGridPoints() {
		for(LineSegment line:gridLines) {
			gridPoints.add(line.endPoint1);
			gridPoints.add(line.endPoint2);
		}
	}
}
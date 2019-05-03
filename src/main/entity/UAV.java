package main.entity;


import java.util.ArrayList;
import java.util.List;

import main.arithmetic.ConvexHull;
import main.arithmetic.Dijkstra;
import main.arithmetic.TwoOpt4TSP;
import main.arithmetic.data.SimUtils;
import main.entity.geometry.LineSegment;
import main.entity.geometry.MultiLineSegment;
import main.entity.geometry.Point;
import main.entity.geometry.Polygon;

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
		System.out.println("����������������������"+ UVAnum++ +"�����˻����ɹ켣��������������������");
		//System.out.println(gridLines.size());
		trajectory.add(position);
		takeoff();
		while (gridLines.size()>0) {
			chooseNextPoint();
		}
		
		List<Point> tempTrajectory = new ArrayList<Point>();
		for(int i=0;i<trajectory.size()-1;i++) {
			tempTrajectory.add(trajectory.get(i));
			if(!SimpleGrid.isConnected(trajectory.get(i), trajectory.get(i+1))) {
				List<Point> path = SimpleGrid.getPath(trajectory.get(i), trajectory.get(i+1));
				List<Point> subPath = path.subList(1, path.size()-1);
				tempTrajectory.addAll(subPath);
			}
		}
		tempTrajectory.add(trajectory.get(trajectory.size()-1));
		trajectory=tempTrajectory;
	}
	
	public void creatTrajectory2Opt() {
		System.out.println("����������������������"+ UVAnum++ +"�����˻����ɹ켣��������������������");
		trajectory.addAll(new TwoOpt4TSP().run((ArrayList<LineSegment>) gridLines, this.takeOffPoint));
	}

	public void takeoff() {
		Polygon hull =  new Polygon(new ConvexHull<Point>(gridPoints).getHull());
		if(this.position.positionToPolygon(hull)==SimUtils.INNER) {
			return;
		}
		
		double maxDistance = 0.0;
		LineSegment side1 = null;
		LineSegment side2 = null;
		Point center = MultiLineSegment.barycenter(gridLines);
		//System.out.println(center);
		for(LineSegment line:gridLines) {
			if(center.distanceToLine(line)>maxDistance) {
				side1=line;
				maxDistance=center.distanceToLine(line);
			}
		}
		maxDistance = 0.0;
		for(LineSegment line:gridLines) {
			if(line.distanceToLine(side1)>maxDistance) {
				side2=line;
				maxDistance=side1.distanceToLine(line);
			}
		}
		Point point1;
		Point point2;
		Point candi;
		if(position.distanceToPoint(side1.endPoint1)<position.distanceToPoint(side1.endPoint2)) {
			point1=side1.endPoint1;
		}else {
			point1=side1.endPoint2;
		}
		if(position.distanceToPoint(side2.endPoint1)<position.distanceToPoint(side2.endPoint2)) {
			point2=side2.endPoint1;
		}else {
			point2=side2.endPoint2;
		}
		double dis1=SimpleGrid.distanceOfTwoPoints(point1,position);
		double dis2=SimpleGrid.distanceOfTwoPoints(point2,position);
		if(dis1<dis2) {
			candi=point1;
		}else {
			candi=point2;
		}
		
		//������destination��ͬһ���߶��ϵ�FlightPoint����ΪnextDestination��
		Point brother = candi.getBrotherPoint();
		
		//���¹켣�ߺ�UAVλ�ã�
		trajectory.add(candi);
		trajectory.add(brother);
		position = brother;
		//����grid
		gridLines.remove(candi.getMotherLine());
		gridPoints.remove(candi);
		gridPoints.remove(brother);
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
		destination = candidate;
		//������destination��ͬһ���߶��ϵ�FlightPoint����ΪnextDestination��
		Point brother = candidate.getBrotherPoint();
		nextDestination=brother;
		
		//���¹켣�ߺ�UAVλ�ã�
		trajectory.add(destination);
		trajectory.add(nextDestination);
		position = nextDestination;
		//����grid
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
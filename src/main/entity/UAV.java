package main.entity;


import java.util.ArrayList;
import java.util.List;

import main.arithmetic.Dijkstra;
import main.arithmetic.SimUtils;

public class UAV {
	private double maxLiquid = 1;
	private double maxBattery = 1;
	private double idleSpeed = 0.05;
	private double operationSpeed = 0.02;
	private double liquidPerStep = 0.02; 
	private double batteryPerStep = 0.03;
	private FlightPoint position;
	private FlightPoint destination = null;
	private FlightPoint nextDestination = null;
	private double direction;
	public List<FlightPoint> trajectory = new ArrayList<FlightPoint>();
	private TakeOffPoint takeOffPoint;

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
		while (Map.getInstance().gridLines.size()>0) {
			chooseNextPoint();
			//chooseNextLine();

		}
		homewardVoyage();
	}

	public void renewByLineSegment(){
		LineSegment candidateLine = localOptimization(5);
		if(position.distanceToPoint(candidateLine.endPoint1)>=
				position.distanceToPoint(candidateLine.endPoint2)) {
			destination = new FlightPoint(candidateLine.endPoint2);
			nextDestination = new FlightPoint(candidateLine.endPoint1);
		}else {
			destination = new FlightPoint(candidateLine.endPoint1);
			nextDestination = new FlightPoint(candidateLine.endPoint2);
		}
		position.setNext(destination);
		destination.setNext(nextDestination);
		trajectory.add(destination);
		trajectory.add(nextDestination);
		position = nextDestination;
		Map.getInstance().gridLines.remove(candidateLine);
		Map.getInstance().gridPoints.remove(destination);
		Map.getInstance().gridPoints.remove(nextDestination);
	}

	public LineSegment localOptimization(int n) {
		if(Map.getInstance().gridLines.size()<n) {
			if(Map.getInstance().gridLines.size()==1) {
				return Map.getInstance().gridLines.get(0);
			}else {
				n=Map.getInstance().gridLines.size();
			}
		}
		List<LineSegment> candidateLines = Map.getInstance().ranking(position).subList(0, n);

		int[] rank= new int[n];
		for(int i=0;i<rank.length;i++) {rank[i]=i;}
		double localLength=SimUtils.INFINITY;
		int mark=0;
		for(int i=0;i<rank.length-1;i++) {
			for(int j=i+1;j<rank.length;j++) {
				int temp=rank[i];
				rank[i]=rank[j];
				rank[j]=temp;
				if(sumLength(changeSequence(candidateLines,rank),position)<localLength){
					localLength=sumLength(changeSequence(candidateLines,rank),position);
					mark=rank[0];
				}
			} 
		}
		return candidateLines.get(mark);
	}

	public List<LineSegment> changeSequence(List<LineSegment> lines,int[] rank) {
		List<LineSegment> tempLines=new ArrayList<LineSegment>(rank.length);
		for(int i=0;i<rank.length;i++) {
			tempLines.add(null);
		}
		for(int i=0;i<rank.length;i++) {
			tempLines.set(i, lines.get(rank[i]));
		}
		return tempLines;
	}

	public double sumLength(List<LineSegment> lines,Point point) {
		double sum = 0;
		Point now = position;
		for(LineSegment line:lines) {
			sum+=now.distanceToLineSegment(line);
			if(now.distanceToLineSegment(line)==now.directionToPoint(line.endPoint1)) {
				now = line.endPoint2;
			}else {
				now = line.endPoint1;
			}
		}
		return sum;
	}

	public void chooseNextLine() {
		double minDistanceToGridLines = Double.MAX_VALUE;
		LineSegment candidateLine = null;
		for (LineSegment gridLine:Map.getInstance().gridLines) {
			if(position.distanceToLineSegment(gridLine) < minDistanceToGridLines) {
				minDistanceToGridLines=position.distanceToLineSegment(gridLine);
				candidateLine=gridLine;
			}
		}
		if(position.distanceToPoint(candidateLine.endPoint1)>=
				position.distanceToPoint(candidateLine.endPoint2)) {
			destination = new FlightPoint(candidateLine.endPoint2);
			nextDestination = new FlightPoint(candidateLine.endPoint1);
		}else {
			destination = new FlightPoint(candidateLine.endPoint1);
			nextDestination = new FlightPoint(candidateLine.endPoint2);
		}
		position.setNext(destination);
		destination.setNext(nextDestination);
		trajectory.add(destination);
		trajectory.add(nextDestination);
		position = nextDestination;
		Map.getInstance().gridLines.remove(candidateLine);
	}

	public void chooseNextPoint() {
		double minDistance = Double.MAX_VALUE;
		Point candidate = null;
		for (Point gridPoint:Map.getInstance().gridPoints) {
			if(Map.getInstance().DistanceOfTwoPoints(position, gridPoint) < minDistance) {
				minDistance=Map.getInstance().DistanceOfTwoPoints(position, gridPoint);
				candidate=gridPoint;
			}
		}
		destination = new FlightPoint(candidate);
		if (minDistance>SimUtils.INFINITY) {
			candidate.print();
			 Map.getInstance().getBrotherPoint(candidate).print();
			obstacleAvoidance(position,candidate);
		}else {
			position.setNext(destination);
		}
		
		//生成与destination在同一条线段上的FlightPoint，记为nextDestination；
		Point brother = Map.getInstance().getBrotherPoint(candidate);
		nextDestination=new FlightPoint(brother);
		destination.setNext(nextDestination);
		
		//更新轨迹线和UAV位置；
		trajectory.add(destination);
		trajectory.add(nextDestination);
		position = nextDestination;
		//更新grid
		Map.getInstance().gridPoints.remove(brother);
		Map.getInstance().gridPoints.remove(candidate);
		Map.getInstance().gridLines.remove(Map.getInstance().getMotherLine(candidate));
	}

	public void takeOffFromSide(){
		Land land = Map.getInstance().lands.get(0);
		Point leftPoint = land.getSidePoint(SimUtils.LEFT);
		leftPoint.print();
		double minDis = SimUtils.INFINITY;
		Point candidatePoint=null;
		for(Point point:Map.getInstance().gridPoints) {
			if(point.distanceToPoint(leftPoint)<minDis){
				minDis=point.distanceToPoint(leftPoint);
				candidatePoint=point;
			}
		}
		if(position.distanceToPoint(Map.getInstance().getBrotherPoint(candidatePoint))<
				position.distanceToPoint(candidatePoint)) {
			candidatePoint=Map.getInstance().getBrotherPoint(candidatePoint);
		}
		Dijkstra dj = new Dijkstra(position,candidatePoint,Map.getInstance().obstacles);
		List<Point> path = dj.getShortestPath();
		for(int i = 1;i<path.size();i++) {
			FlightPoint fp = new FlightPoint(path.get(i));
			trajectory.get(trajectory.size()-1).setNext(fp);
			trajectory.add(fp);
		}
		FlightPoint brother = new FlightPoint(Map.getInstance().getBrotherPoint(candidatePoint));
		trajectory.get(trajectory.size()-1).setNext(brother);
		trajectory.add(brother);
		position=brother;

		Map.getInstance().gridPoints.remove(candidatePoint);
		Map.getInstance().gridPoints.remove(Map.getInstance().getBrotherPoint(candidatePoint));
		Map.getInstance().gridLines.remove(Map.getInstance().getMotherLine(candidatePoint));
	}
	
	public void takeOffFromMinDistance(){
		double minDis = SimUtils.INFINITY;
		Point candidatePoint=null;
		for(Point point:Map.getInstance().gridPoints) {
			if(point.distanceToPoint(position)<minDis){
				minDis=point.distanceToPoint(position);
				candidatePoint=point;
			}
		}
		if(position.distanceToPoint(Map.getInstance().getBrotherPoint(candidatePoint))<
				position.distanceToPoint(candidatePoint)) {
			candidatePoint=Map.getInstance().getBrotherPoint(candidatePoint);
		}
		Dijkstra dj = new Dijkstra(position,candidatePoint,Map.getInstance().obstacles);
		List<Point> path = dj.getShortestPath();
		for(int i = 1;i<path.size();i++) {
			FlightPoint fp = new FlightPoint(path.get(i));
			trajectory.get(trajectory.size()-1).setNext(fp);
			trajectory.add(fp);
		}
		FlightPoint brother = new FlightPoint(Map.getInstance().getBrotherPoint(candidatePoint));
		trajectory.get(trajectory.size()-1).setNext(brother);
		trajectory.add(brother);
		position=brother;

		Map.getInstance().gridPoints.remove(candidatePoint);
		Map.getInstance().gridPoints.remove(Map.getInstance().getBrotherPoint(candidatePoint));
		Map.getInstance().gridLines.remove(Map.getInstance().getMotherLine(candidatePoint));
	}
	
	public void obstacleAvoidance(Point from,Point to) {
		Dijkstra dj = new Dijkstra(from,to,Map.getInstance().obstacles);
		List<Point> path = dj.getShortestPath();
		for(int i = 1;i<path.size();i++) {
			FlightPoint ft = new FlightPoint(path.get(i));
			trajectory.get(trajectory.size()-1).setNext(ft);
			trajectory.add(ft);
		}
	}
	
	public void homewardVoyage() {
		obstacleAvoidance(position,this.takeOffPoint);
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
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
		Map.getInstance().UAVs.add(this);
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
		Map.getInstance().UAVs.add(this);
	}

	public void creatTrajectory() {
		takeOff();
		while (Map.getInstance().gridLines.size()>0) {
			chooseNextPoint();
			//chooseNextLine();

		}
		homewardVoyage();
	}

	public void renewByLineSegment(){
		LineSegment candidateLine = localOptimization(5);
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
		Map.getInstance().gridPoints.remove(currentDestination);
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
		List<LineSegment> candidateLines = Map.getInstance().ranking(currentPosition).subList(0, n);

		int[] rank= new int[n];
		for(int i=0;i<rank.length;i++) {rank[i]=i;}
		double localLength=SimUtils.INFINITY;
		int mark=0;
		for(int i=0;i<rank.length-1;i++) {
			for(int j=i+1;j<rank.length;j++) {
				int temp=rank[i];
				rank[i]=rank[j];
				rank[j]=temp;
				if(sumLength(changeSequence(candidateLines,rank),currentPosition)<localLength){
					localLength=sumLength(changeSequence(candidateLines,rank),currentPosition);
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
		Point now = currentPosition;
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
		double minDistanceToGridPoints = Double.MAX_VALUE;
		Point candidatePoint = null;
		for (Point gridPoint:Map.getInstance().gridPoints) {
			//if (currentPosition.distanceToPoint(new Point(18.84,165.4))<10&&gridPoint.distanceToPoint(new Point(295.3,489.3))<10) {
				//System.out.println("{{{{{{{{{{{{{{{{{{{{{{{{{{{");
				//double distance = Map.getInstance().straightDistanceOfTwoPoints(currentPosition, gridPoint);
				//System.out.println(currentPosition.toString() + "和" + gridPoint+"之间的距离是：" + distance);}
			if(Map.getInstance().straightDistanceOfTwoPoints(currentPosition, gridPoint) < minDistanceToGridPoints) {
				minDistanceToGridPoints=Map.getInstance().straightDistanceOfTwoPoints(currentPosition, gridPoint);
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

	public void takeOff(){
		Land land = Map.getInstance().lands.get(0);
		Point leftPoint = land.getSidePoint(SimUtils.RIGHT);
		leftPoint.print();
		double minDis = SimUtils.INFINITY;
		Point candidatePoint=null;
		for(Point point:Map.getInstance().gridPoints) {
			if(point.distanceToPoint(leftPoint)<minDis){
				minDis=point.distanceToPoint(leftPoint);
				candidatePoint=point;
			}
		}
		if(currentPosition.distanceToPoint(Map.getInstance().getBrotherPoint(candidatePoint))<
				currentPosition.distanceToPoint(candidatePoint)) {
			candidatePoint=Map.getInstance().getBrotherPoint(candidatePoint);
		}
		Dijkstra dj = new Dijkstra(currentPosition,candidatePoint,Map.getInstance().obstacles);
		List<Point> path = dj.getShortestPath();
		for(int i = 1;i<path.size();i++) {
			FlightPoint fp = new FlightPoint(path.get(i));
			trajectory.get(trajectory.size()-1).setNext(fp);
			fp.setPrevious(trajectory.get(trajectory.size()-1));
			trajectory.add(fp);
		}
		FlightPoint brother = new FlightPoint(Map.getInstance().getBrotherPoint(candidatePoint));
		brother.setPrevious(trajectory.get(trajectory.size()-1));
		trajectory.get(trajectory.size()-1).setNext(brother);
		trajectory.add(brother);
		currentPosition=brother;

		Map.getInstance().gridPoints.remove(candidatePoint);
		Map.getInstance().gridPoints.remove(Map.getInstance().getBrotherPoint(candidatePoint));
		Map.getInstance().gridLines.remove(Map.getInstance().getMotherLine(candidatePoint));
	}
	public void homewardVoyage() {
		Dijkstra dj = new Dijkstra(currentPosition,this.takeOffPoint,Map.getInstance().obstacles);
		List<Point> path = dj.getShortestPath();
		for(int i = 1;i<path.size();i++) {
			FlightPoint fp = new FlightPoint(path.get(i));
			trajectory.get(trajectory.size()-1).setNext(fp);
			fp.setPrevious(trajectory.get(trajectory.size()-1));
			trajectory.add(fp);
		}
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

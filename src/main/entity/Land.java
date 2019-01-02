package main.entity;

import java.util.ArrayList;
import java.util.List;

import main.arithmetic.SimUtils;

public class Land extends Polygon{
	public double ridgeWideth = 0.05;
	public double ridgeDirection = Math.PI / 2;
	public List<Point> gridPoints = new ArrayList<Point>();
	public List<LineSegment> gridLines = new ArrayList<LineSegment>();
	
	public Land(){
		Map.getInstance().addland(this);
	}
	public Land(double[] x, double[] y) {
		super(x,y);
		Map.getInstance().addland(this);
	}
	public Land(double[] x, double[] y, double ridgeDirection) {
		super(x,y);
		this.ridgeDirection=ridgeDirection;
		Map.getInstance().addland(this);
	}

	public void setRidgeDirection(double ridgeDirection) {
		this.ridgeDirection = ridgeDirection;
	}

	public void setRidgeDirection(Point p1,Point p2) {
		double deltaX=p2.x-p1.x;
		double deltaY=p2.y-p1.y;
		this.ridgeDirection = Math.atan2(deltaY, deltaX);
		if (this.ridgeDirection < 0) {
			this.ridgeDirection +=Math.PI;
		}
	}

	public void createGridLines(){
		Point start = getSidePoint(SimUtils.LEFT);
		Point end = getSidePoint(SimUtils.RIGHT);
		Line line = new Line(start,Math.tan(ridgeDirection));
		line.move(SimUtils.RIGHT, ridgeWideth/2);
		while(end.positionToLine(line)==SimUtils.RIGHT){
			gridLines.add(line.intersectionLineSegmentOfLineAndPolygon(this));
			line.move(SimUtils.RIGHT, ridgeWideth);
		}
	}
	/*
	 * 避障，如果gridLines中的某条线跨过障碍物，则将其打断
	 */
	public void avoidObstacle(List<Obstacle> obstacles) {
		List<LineSegment> tempListAdd = new ArrayList<LineSegment>();
		List<LineSegment> tempListRemove = new ArrayList<LineSegment>();
		for(Obstacle obstacle:obstacles) {
			if (!isContainsPolygon(obstacle)) {
				continue;
			}
			for(LineSegment lineSegment:gridLines){
				LineSegment lineSegmentWithinObstacle = 
						lineSegment.intersectionLineSegmentOfLineSegmentAndPolygon(obstacle);
				if (lineSegmentWithinObstacle!=null) {
					tempListRemove.add(lineSegment);
					if(lineSegment.endPoint1.distanceToPoint(lineSegmentWithinObstacle.endPoint1) < 
							lineSegment.endPoint1.distanceToPoint(lineSegmentWithinObstacle.endPoint2)) {
						tempListAdd.add(new LineSegment(lineSegment.endPoint1,lineSegmentWithinObstacle.endPoint1));
						tempListAdd.add(new LineSegment(lineSegment.endPoint2,lineSegmentWithinObstacle.endPoint2));
					}else{
						tempListAdd.add(new LineSegment(lineSegment.endPoint1,lineSegmentWithinObstacle.endPoint2));
						tempListAdd.add(new LineSegment(lineSegment.endPoint2,lineSegmentWithinObstacle.endPoint1));
					}
				}
			}
		}
		gridLines.removeAll(tempListRemove);
		gridLines.addAll(tempListAdd);
		
	}

	/*
	 * 获取边界上最左或最右的点
	 */
	public Point getSidePoint(int leftOrRight){
		Point sidePoint = vertexes.get(0);
		Line rightLine = new Line(sidePoint,Math.tan(ridgeDirection));
		for(int i = 1;i<vertexes.size();i++) {
			Point temp = vertexes.get(i);
			if (leftOrRight==SimUtils.RIGHT&&temp.positionToLine(rightLine)==SimUtils.RIGHT){
				sidePoint = vertexes.get(i);
				rightLine = new Line(sidePoint,Math.tan(ridgeDirection));
			}else if(leftOrRight==SimUtils.LEFT&&temp.positionToLine(rightLine)==SimUtils.LEFT){
				sidePoint = vertexes.get(i);
				rightLine = new Line(sidePoint,Math.tan(ridgeDirection));
			}
		}
		return sidePoint;
	}

	public String toString() {
		String str="Land: ";
		for(Point point:vertexes) {
			str+=point.toString()+" | ";
		}
		return str;
	}
	
	public void print() {
		System.out.println(this.toString());
	}

}

package main.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.arithmetic.data.SimUtils;
import main.entity.geometry.Line;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;
import main.entity.geometry.Polygon;

public class Land extends Polygon{
	private double ridgeWideth = 4.0;
	private double ridgeDirection = Math.PI / 2;
	//private List<Point> gridPoints = new ArrayList<Point>();
	private List<LineSegment> gridLines = new ArrayList<LineSegment>();
	
	public Land(double[][] coord){
		super(coord);
		Map.getInstance().lands.add(this);
	}
	
	public Land(double[][] coord, double ridgeDirection){
		super(coord);
		this.setRidgeDirection(ridgeDirection);
		Map.getInstance().lands.add(this);
	}
	
	public Land(double[] x, double[] y) {
		super(x,y);
		Map.getInstance().lands.add(this);
	}
	public Land(double[] x, double[] y, double ridgeDirection) {
		super(x,y);
		this.setRidgeDirection(ridgeDirection);
		Map.getInstance().lands.add(this);
	}

	public void createGridLines(){
		Point start = getSidePoint(SimUtils.LEFT);
		//System.out.println(start);
		Point end = getSidePoint(SimUtils.RIGHT);
		Line line = new Line(start,Math.tan(ridgeDirection));
		line.move(SimUtils.RIGHT, ridgeWideth/2);
		while(end.positionToLine(line)==SimUtils.RIGHT){
			for(LineSegment lineSegment:line.intersectionLineSegmentOfLineAndPolygon(this)) {
				if(lineSegment.length>2*this.ridgeWideth) {
					gridLines.add(lineSegment);
				}
			}
			line.move(SimUtils.RIGHT, ridgeWideth);
		}
	}
	/*
	 * 避障，如果gridLines中的某条线跨过障碍物，则将其打断
	 */
	public void devideGridLinesByObstacle(List<Obstacle> obstacles) {
		for(Obstacle obstacle:obstacles) {
			List<LineSegment> tempListAdd = new ArrayList<LineSegment>();
			List<LineSegment> tempListRemove = new ArrayList<LineSegment>();
			for(LineSegment lineSegment:gridLines){
				List<LineSegment> lineSegments = lineSegment.intersectionLineSegmentOfLineSegmentAndPolygon(obstacle);
				for(LineSegment lineSegmentWithinObstacle:lineSegments) {
						tempListRemove.add(lineSegment);
						LineSegment ls1;
						LineSegment ls2;
						if(lineSegment.endPoint1.distanceToPoint(lineSegmentWithinObstacle.endPoint1) < 
								lineSegment.endPoint1.distanceToPoint(lineSegmentWithinObstacle.endPoint2)) {
							ls1=new LineSegment(lineSegment.endPoint1,lineSegmentWithinObstacle.endPoint1);
							ls2=new LineSegment(lineSegment.endPoint2,lineSegmentWithinObstacle.endPoint2);
						}else{
							ls1=new LineSegment(lineSegment.endPoint1,lineSegmentWithinObstacle.endPoint2);
							ls2=new LineSegment(lineSegment.endPoint2,lineSegmentWithinObstacle.endPoint1);
						}
						if(ls1.length>this.ridgeWideth*2) {
							tempListAdd.add(ls1);
						}
						if(ls2.length>this.ridgeWideth*2) {
							tempListAdd.add(ls2);
						}
				}
			}
			gridLines.removeAll(tempListRemove);
			gridLines.addAll(tempListAdd);
		}
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

	public void setRidgeDirection(double ridgeDirection) {
		this.ridgeDirection = ridgeDirection/180.0*Math.PI;
		if (this.ridgeDirection < 0) {
			this.ridgeDirection +=Math.PI;
		}
	}

	public void setRidgeDirection(Point p1,Point p2) {
		double deltaX=p2.x-p1.x;
		double deltaY=p2.y-p1.y;
		this.ridgeDirection = Math.atan2(deltaY, deltaX);
		if (this.ridgeDirection < 0) {
			this.ridgeDirection +=Math.PI;
		}
	}
	
	public double getRidgeWideth() {
		return ridgeWideth;
	}

	public void setRidgeWideth(double ridgeWideth) {
		this.ridgeWideth = ridgeWideth;
	}
	
	public List<LineSegment> getGridLines() {
		return gridLines;
	}

	//public void setGridLines(List<LineSegment> gridLines) {
	//	this.gridLines = gridLines;
	//}

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

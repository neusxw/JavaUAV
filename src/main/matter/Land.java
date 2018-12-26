package main.matter;

import java.util.ArrayList;
import java.util.List;

import main.arithmetic.SimUtils;

public class Land extends Polygon{
	public double ridgeWideth = 0.1;
	public double ridgeDirection = Math.PI / 2;
	public List<Point> gridPoint = new ArrayList<Point>();
	public List<LineSegment> gridLine = new ArrayList<LineSegment>();
	public Land(){
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

	public void createGrid(){
		Point start = getSidePoint(SimUtils.LEFT);
		Point end = getSidePoint(SimUtils.RIGHT);
		Line line = new Line(start,Math.tan(ridgeDirection));
		System.out.println(end.toString());
		while(end.leftOrRightToLine(line)==SimUtils.RIGHT){
			line.move(SimUtils.RIGHT, ridgeWideth/2);
			System.out.println(getLineSegmentWithinMap(line).toString());
			gridLine.add(getLineSegmentWithinMap(line));
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
			if (leftOrRight==SimUtils.RIGHT&&temp.leftOrRightToLine(rightLine)==SimUtils.RIGHT){
				sidePoint = vertexes.get(i);
				rightLine = new Line(sidePoint,Math.tan(ridgeDirection));
			}else if(leftOrRight==SimUtils.LEFT&&temp.leftOrRightToLine(rightLine)==SimUtils.LEFT){
				sidePoint = vertexes.get(i);
				rightLine = new Line(sidePoint,Math.tan(ridgeDirection));
			}
		}
		return sidePoint;
	}
	public LineSegment getLineSegmentWithinMap(Line line) {
		List<Point> crossPoints = new ArrayList<Point>();
		for(LineSegment ls:edges) {
			Point cross = ls.IntersectionWithLine(line);
			if(cross!=null) {
				crossPoints.add(cross);
			}
		}
		if(crossPoints.size()!=2) {
			return null;
		}else {
			return new LineSegment(crossPoints.get(0),crossPoints.get(1));
		}
	}
	
}

package main.entity;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
	public List<Point> vertexes= new ArrayList<Point>();
	public List<LineSegment> edges = new ArrayList<LineSegment>();

	public Polygon() {
		
	}
	public Polygon(double[] x, double[] y) {
		createPolygonFromArray(x, y);
	}
	
	public void createPolygonFromArray(double[] x, double[] y) {
		if(x.length!=y.length) {
			System.out.println("横纵坐标长度不相等！");
			return;
		}
		for(int i=0; i< x.length; i++) {
			vertexes.add(new Point(x[i],y[i]));
		}
		for(int i=0;i<x.length -1;i++) {
			edges.add(new LineSegment(vertexes.get(i),vertexes.get(i+1)));
		}
		edges.add(new LineSegment(vertexes.get(x.length-1),vertexes.get(0)));
	}
	
	/*
	 * 判断一个点是否在多边形中
	 */
	public boolean isContainsPoint(Point point) {
	    int verticesCount = vertexes.size();
	    int nCross = 0;
	    for (int i = 0; i < verticesCount; ++ i) {
	        Point p1 = vertexes.get(i);
	        Point p2 = vertexes.get((i + 1) % verticesCount);
	        if ( p1.y == p2.y ) {   // p1p2 与 y=p0.y平行
	            continue;
	        }
	        if ( point.y < Math.min(p1.y, p2.y) ) { // 交点在p1p2延长线上 
	            continue;
	        }
	        if ( point.y >= Math.max(p1.y, p2.y) ) { // 交点在p1p2延长线上 
	            continue;
	        }
	        double x = (point.y - p1.y) * (p2.x - p1.x) / (p2.y - p1.y) + p1.x; // 求交点的 X 坐标
	        if ( x > point.x ) { // 只统计单边交点
	            nCross++;
	        }
	    }
	    return (nCross%2==1); // 单边交点为偶数，点在多边形之外
	}
	
	public boolean isContainsPolygon(Polygon polygon) {
		for (Point point:polygon.vertexes) {
			if (!isContainsPoint(point)) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * 获得直线与一个多边形的交线，即直线位于多边形中的那部分构成的线段
	 */
	public LineSegment getLineSegmentWithinPolygon(Line line) {
		List<Point> crossPoints = new ArrayList<Point>();
		for(LineSegment ls:edges) {
			Point cross = ls.intersectionLineSegmentAndLine(line);
			
			if(cross!=null) {
				crossPoints.add(cross);
			}
		}
		if(crossPoints.size()!=2) {
			return null;
		}else {
			//System.out.println(crossPoints.get(0).toString()+crossPoints.get(1).toString());
			return new LineSegment(crossPoints.get(0),crossPoints.get(1));
		}
	}
	
	/*
	 * 获得线段与一个多边形的交线，即线段位于多边形中的那部分构成的线段
	 */

	public LineSegment getLineSegmentWithinPolygon(LineSegment lineSegment) {
		List<Point> crossPoints = new ArrayList<Point>();
		for(LineSegment ls:edges) {
			Point cross = ls.intersectionLineSegmentAndLine(lineSegment);
			if(cross!=null) {
				crossPoints.add(cross);
			}
		}
		if(crossPoints.size()==1) {
			if(lineSegment.endPoint1.isInPolygon(this)) {
				return new LineSegment(crossPoints.get(0),lineSegment.endPoint1);
			}else {
				return new LineSegment(crossPoints.get(0),lineSegment.endPoint2);
			}
		}else if((crossPoints.size()==2)){
			return new LineSegment(crossPoints.get(0),crossPoints.get(1));
		}else {
			return null;
		}
		
	}
}

package main.arithmetic.trajectory.takeoff.how;

import java.util.ArrayList;
import java.util.List;

import main.arithmetic.hull.ConcaveHull;
import main.data.SimUtils;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;
import main.entity.geometry.Polygon;

public class FromNearestHullSide implements TakeoffStrategy {

	@Override
	public Point takeOff(Point position, List<LineSegment> gridLines) {
		List<Point> gridPoints = new ArrayList<Point>();
		for(LineSegment ls:gridLines) {
			gridPoints.add(ls.endPoint1);
			gridPoints.add(ls.endPoint2);
		}
		Polygon hull =  new ConcaveHull().createHull(gridPoints);
		if(position.positionToPolygon(hull)==SimUtils.INNER) {
			return null;
		}
		
		double minDis = Double.POSITIVE_INFINITY;
		Point minPoint = null;
		for(Point point:hull.vertexes) {
			double dis = point.distanceToPoint(position);
			if(dis<minDis) {
				minDis = dis;
				minPoint = point;
			}
		}
		//����Hull������Polygon��Polygon�еĵ��������ɵģ�����point==minPoint����Ϊtrue��
		for(Point point:gridPoints) {
			if(point.equals(minPoint)) {
				minPoint=point;
			}
		}
		return minPoint;
	}

}

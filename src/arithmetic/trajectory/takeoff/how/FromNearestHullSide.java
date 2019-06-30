package arithmetic.trajectory.takeoff.how;

import java.util.ArrayList;
import java.util.List;

import arithmetic.hull.ConcaveHull;
import data.SimUtils;
import entity.geometry.LineSegment;
import entity.geometry.Point;
import entity.geometry.Polygon;

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
		//由于Hull会生成Polygon，Polygon中的点是新生成的，所以point==minPoint不会为true；
		for(Point point:gridPoints) {
			if(point.equals(minPoint)) {
				minPoint=point;
			}
		}
		return minPoint;
	}

}

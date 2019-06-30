package arithmetic.trajectory.takeoff.how;

import java.util.ArrayList;
import java.util.List;

import arithmetic.hull.ConcaveHull;
import data.SimUtils;
import entity.SimpleGrid;
import entity.geometry.LineSegment;
import entity.geometry.MultiLineSegment;
import entity.geometry.Point;
import entity.geometry.Polygon;

public class FromNearestSide implements TakeoffStrategy {

	/**
	 * 计算离重心最远的两条线，从其中离起飞点更近的那个起飞。
	 */
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
		//寻找两条边界线
		double maxDistance = 0.0;
		LineSegment side1 = null;
		LineSegment side2 = null;
		Point center = MultiLineSegment.barycenter(gridLines);
		//第一条边界线为离重心最远的那条
		for(LineSegment line:gridLines) {
			if(center.distanceToLine(line)>maxDistance) {
				side1=line;
				maxDistance=center.distanceToLine(line);
			}
		}
		//第一条边界线为离第一条边界线最远的那条
		maxDistance = 0.0;
		for(LineSegment line:gridLines) {
			if(line.distanceToLine(side1)>maxDistance) {
				side2=line;
				maxDistance=side1.distanceToLine(line);
			}
		}
		//计算从起飞点到边界线的距离（最短距离）
		Point point1;
		Point point2;
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
		//确定下一点为边界线上离起飞点最近的那个点（距离采用迪杰斯特拉距离）
		double dis1=SimpleGrid.distanceOfTwoPoints(point1,position);
		double dis2=SimpleGrid.distanceOfTwoPoints(point2,position);
		if(dis1<dis2) {
			return point1;
		}else {
			return point2;
		}
	}
}

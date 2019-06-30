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
	 * ������������Զ�������ߣ�����������ɵ�������Ǹ���ɡ�
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
		//Ѱ�������߽���
		double maxDistance = 0.0;
		LineSegment side1 = null;
		LineSegment side2 = null;
		Point center = MultiLineSegment.barycenter(gridLines);
		//��һ���߽���Ϊ��������Զ������
		for(LineSegment line:gridLines) {
			if(center.distanceToLine(line)>maxDistance) {
				side1=line;
				maxDistance=center.distanceToLine(line);
			}
		}
		//��һ���߽���Ϊ���һ���߽�����Զ������
		maxDistance = 0.0;
		for(LineSegment line:gridLines) {
			if(line.distanceToLine(side1)>maxDistance) {
				side2=line;
				maxDistance=side1.distanceToLine(line);
			}
		}
		//�������ɵ㵽�߽��ߵľ��루��̾��룩
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
		//ȷ����һ��Ϊ�߽���������ɵ�������Ǹ��㣨������õϽ�˹�������룩
		double dis1=SimpleGrid.distanceOfTwoPoints(point1,position);
		double dis2=SimpleGrid.distanceOfTwoPoints(point2,position);
		if(dis1<dis2) {
			return point1;
		}else {
			return point2;
		}
	}
}

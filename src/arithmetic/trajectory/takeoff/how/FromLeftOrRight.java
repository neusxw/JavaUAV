package arithmetic.trajectory.takeoff.how;

import java.util.List;

import data.SimUtils;
import entity.SimpleGrid;
import entity.geometry.LineSegment;
import entity.geometry.MultiLineSegment;
import entity.geometry.Point;

public class FromLeftOrRight implements TakeoffStrategy {

	private int LR;
	public FromLeftOrRight(int lr) {
		LR = lr;
	}
	
	@Override
	public Point takeOff(Point position, List<LineSegment> gridLines) {
		LineSegment ls = getSideLine(gridLines);
		Point p = getNearestPoint(position, ls);
		return p;
	}
	
	private LineSegment getSideLine(List<LineSegment> gridLines){
		Point center = MultiLineSegment.barycenter(gridLines);
		System.out.println("中心点：" + center);
		double maxDistance = 0.0;
		LineSegment side = null;

		for(LineSegment ls:gridLines) {
			System.out.println("线段方向：" + ls.directionAngle);
			if((LR==SimUtils.RIGHT && center.positionToLine(ls)==SimUtils.RIGHT)
					|| (LR==SimUtils.LEFT && center.positionToLine(ls)==SimUtils.LEFT)) {
				continue;
			}
			if (center.distanceToLine(ls) > maxDistance) {
				maxDistance = center.distanceToLine(ls);
				side = ls;
			}
		}
		return side;
	}

	private Point getNearestPoint(Point position, LineSegment line) {
		double dis1=SimpleGrid.distanceOfTwoPoints(line.endPoint1,position);
		double dis2=SimpleGrid.distanceOfTwoPoints(line.endPoint2,position);
		if(dis1<dis2) {
			return line.endPoint1;
		}else {
			return line.endPoint2;
		}
		
	}
	
	public int getLR() {
		return LR;
	}
	public void setLR(int lR) {
		LR = lR;
	}

}

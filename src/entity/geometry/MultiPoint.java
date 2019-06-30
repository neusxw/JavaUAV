package entity.geometry;

import java.util.List;

public class MultiPoint {
	
	public static Point barycenter(List<Point> points) {
		double coordX = 0;
		double coordY = 0;
		for(Point point:points) {
			coordX+=point.x;
			coordY+=point.y;
		}
		coordX/=points.size();
		coordY/=points.size();
		return new Point(coordX,coordY);
	}
	
	public static double distanceMidToBarycenter(Point point,List<Point> points) {
		return barycenter(points).distanceToPoint(point);
	}

}

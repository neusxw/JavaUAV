package main.arithmetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import main.arithmetic.data.SimUtils;
import main.entity.geometry.MultiPoint;
import main.entity.geometry.Point;
import main.entity.geometry.Polygon;

public class ConcaveHull {
	
	public static Polygon createConcaveHull(List<Point> points) {
		rankingPoints(points);
		redundantRemove(points);
		return new Polygon(points);
	}
	
	private static List<Point> redundantRemove(List<Point> points) {
		for(int i=0;i<points.size();i++) {
			if(SimUtils.doubleEqual(getPseudoAngle(points.get(i),points.get((i+1)%points.size())),
					getPseudoAngle(points.get((i+1)%points.size()),points.get((i+2)%points.size())))) {
				points.remove((i+1)%points.size());
				redundantRemove(points);
			}
		}
		return points;
	}
	
	public static List<Point> rankingPoints(List<Point> points) {
		Point ref = MultiPoint.barycenter(points);
		Collections.sort(points, new Comparator<Point>() {
			@Override
			public int compare(Point point1, Point point2) {
				double angle1 = getPseudoAngle(ref,point1);
				double angle2 = getPseudoAngle(ref,point2);
				if (SimUtils.doubleEqual(angle1, angle2)) {
					return 0;
				}else if(angle1>angle2){
					return 1;
				}else {
					return -1;
				}
			}
		});
		return points;
	}

	private static double getPseudoAngle(Point point1, Point point2) {
		return getPseudoAngle(point2.x-point1.x, point2.y-point1.y);
	}
	
	private static double getPseudoAngle(double dx, double dy) {
		if (dx > 0 && dy >= 0)
			return dy / (dx + dy);
		if (dx <= 0 && dy > 0)
			return 1 + (Math.abs(dx) / (Math.abs(dx) + dy));
		if (dx < 0 && dy <= 0)
			return 2 + (dy / (dx + dy));
		if (dx >= 0 && dy < 0)
			return 3 + (dx / (dx + Math.abs(dy)));
		throw new Error("Impossible");
	}
}

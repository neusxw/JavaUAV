package main.arithmetic.hull;

import java.util.List;

import main.entity.geometry.Point;
import main.entity.geometry.Polygon;

public interface Hull {
	
	Polygon createHull(List<Point> points);
	
}

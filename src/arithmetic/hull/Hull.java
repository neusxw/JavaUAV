package arithmetic.hull;

import java.util.List;

import entity.geometry.Point;
import entity.geometry.Polygon;

public interface Hull {
	
	Polygon createHull(List<Point> points);
	
}

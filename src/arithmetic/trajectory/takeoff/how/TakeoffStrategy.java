package arithmetic.trajectory.takeoff.how;

import java.util.List;

import entity.geometry.LineSegment;
import entity.geometry.Point;

public interface TakeoffStrategy {
	Point takeOff(Point position,List<LineSegment> gridLines);
}

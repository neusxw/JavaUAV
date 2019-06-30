package main.arithmetic.trajectory.takeoff.how;

import java.util.List;

import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;

public interface TakeoffStrategy {
	Point takeOff(Point position,List<LineSegment> gridLines);
}

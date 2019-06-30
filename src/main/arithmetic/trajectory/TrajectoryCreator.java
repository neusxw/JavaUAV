package main.arithmetic.trajectory;

import java.util.List;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;

public interface TrajectoryCreator {
	//List<Point> trajectory = new ArrayList<Point>();
	
	List<Point> createTrajectory(Point position,List<LineSegment> gridLines);
}

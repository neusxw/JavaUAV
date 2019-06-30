package arithmetic.trajectory;

import java.util.List;

import entity.geometry.LineSegment;
import entity.geometry.Point;

public interface TrajectoryCreator {
	//List<Point> trajectory = new ArrayList<Point>();
	
	List<Point> createTrajectory(Point position,List<LineSegment> gridLines);
}

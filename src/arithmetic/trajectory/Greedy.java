package arithmetic.trajectory;

import java.util.ArrayList;
import java.util.List;

import arithmetic.trajectory.takeoff.how.FromLeftOrRight;
import arithmetic.trajectory.takeoff.how.TakeoffStrategy;
import data.SimUtils;
import entity.SimpleGrid;
import entity.geometry.LineSegment;
import entity.geometry.Point;


public class Greedy implements TrajectoryCreator{
	
	TakeoffStrategy takeoffStrategy;
	List<Point> trajectory;
	Point position = null;
	
	public Greedy(TakeoffStrategy takeoffStrategy) {
		this.takeoffStrategy =takeoffStrategy;
	}
	
	public List<Point> createTrajectory(Point p,List<LineSegment> gridLines) {
		trajectory = new ArrayList<Point>();
		position = p;
		trajectory.add(position);
		Point nextPoint = takeoffStrategy.takeOff(position, gridLines);
		if(nextPoint!=null) {
			step(nextPoint, gridLines);
		}
		
		while (gridLines.size()>0) {
			chooseNextPoint(gridLines);
		}
		return obstacleAvoidance(); 
	}
	
	private void step(Point nextPoint,List<LineSegment> gridLines) {
		//更新轨迹线和UAV位置；
		trajectory.add(nextPoint);
		trajectory.add(nextPoint.getBrotherPoint());
		position = nextPoint.getBrotherPoint();
		gridLines.remove(nextPoint.getMotherLine());
	}
	
	public void chooseNextPoint(List<LineSegment> gridLines) {
		double minDistance = Double.MAX_VALUE;
		Point candidate = null;
		List<Point> gridPoints = new ArrayList<Point>();
		for(LineSegment line:gridLines) {
			gridPoints.add(line.endPoint1);
			gridPoints.add(line.endPoint2);
		}
		for (Point gridPoint:gridPoints) {
			if(SimpleGrid.distanceOfTwoPoints(position, gridPoint) < minDistance) {
				minDistance=SimpleGrid.distanceOfTwoPoints(position, gridPoint);
				candidate=gridPoint;
			}
		}
		step(candidate,gridLines);
	}
	
	public List<Point> obstacleAvoidance() {
		List<Point> tempTrajectory = new ArrayList<Point>();
		for(int i=0;i<trajectory.size()-1;i++) {
			Point start = trajectory.get(i);
			Point end = trajectory.get((i+1)%trajectory.size());
			tempTrajectory.add(trajectory.get(i));
			if(!SimpleGrid.isConnected(start,end)) {
				List<Point> path = SimpleGrid.getPath(start,end);
				List<Point> subPath = path.subList(1, path.size()-1);
				tempTrajectory.addAll(subPath);
			}
		}
		tempTrajectory.add(trajectory.get(trajectory.size()-1));
		return tempTrajectory;
	}
}

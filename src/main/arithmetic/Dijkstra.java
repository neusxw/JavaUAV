package main.arithmetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.entity.Point;
import main.entity.Obstacle;

public class Dijkstra {
	Map<Point,Point> previous = new HashMap<Point,Point>();
	List<Point> mediumPoints = new ArrayList<Point>();
	Point startPoint;
	Point endPoint;
	public Dijkstra(Point startPoint,Point endPoint,List<Obstacle> obstacles) {
		this.startPoint=startPoint;
		this.endPoint=endPoint;
		for (Obstacle obstacle:obstacles) {
			mediumPoints.addAll(obstacle.vertexes);
		}
	}
	
	private double[][] generateAdjacentMatrix() {
		
	}
}
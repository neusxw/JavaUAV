package main.arithmetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.entity.Point;
import main.entity.Polygon;
import main.entity.LineSegment;
import main.entity.Obstacle;

public class Dijkstra {
	Map<Point,Point> previous = new HashMap<Point,Point>();
	List<Point> vertexes = new ArrayList<Point>();
	List<? extends Polygon> obstacles;
	Point startPoint;
	Point endPoint;
	double[][] adjacentMatrix;
	int size;

	public Dijkstra(Point startPoint,Point endPoint,List<? extends Polygon> obstacles) {
		this.startPoint=startPoint;
		vertexes.add(this.startPoint);
		this.obstacles=obstacles;
		for (Polygon obstacle:obstacles) {
			vertexes.addAll(obstacle.vertexes);
		}
		this.endPoint=endPoint;
		vertexes.add(this.endPoint);
		size = vertexes.size();
	}

	private double[][] getAdjacentMatrix() {
		adjacentMatrix = new double[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(i==j) {
					adjacentMatrix[i][j]=0;
				}else {
					LineSegment ls = new LineSegment(vertexes.get(i),vertexes.get(j));
					for(Polygon obstacle:obstacles) {
						for(LineSegment edge:obstacle.edges) {
							if(ls.intersectionLineSegmentOfTwoLineSegments(edge)!=null&&
									SimUtils.doubleEqual(ls.length,ls.intersectionLineSegmentOfTwoLineSegments(edge).length)) {
								adjacentMatrix[i][j]=ls.length;
								break;
							}
						}
						LineSegment intersection= ls.intersectionLineSegmentOfLineSegmentAndPolygon(obstacle);
						if(intersection!=null) {
							adjacentMatrix[i][j]=SimUtils.INFINITY;
						}else {
							adjacentMatrix[i][j]=intersection.length;
						}
					}
				}
			}
		}

		return adjacentMatrix;
	}
	public Map<Point,Point> getShortestPath(){
		getAdjacentMatrix();
		Map<Point,Double> exploited = new HashMap<Point,Double>();
		exploited.put(vertexes.get(0),adjacentMatrix[0][0]);
		Map<Point,Double> unexploited = new HashMap<Point,Double>();
		for(int i=1;i<vertexes.size();i++) {
			unexploited.put(vertexes.get(i), adjacentMatrix[0][i]);
		}
		Point tempPoint = null;
		for (int i = 1; i < size; i++) {
			double min = SimUtils.INFINITY;
			for (Point point:unexploited.keySet()) {
				if (unexploited.get(point) < min) {
					min = unexploited.get(point);
					tempPoint=point;
				}
			}
			exploited.put(tempPoint, unexploited.get(tempPoint));
			unexploited.remove(tempPoint);

			for (Point point:unexploited.keySet()) {
				double tmp = (SimUtils.doubleEqual(adjacentMatrix[vertexes.indexOf(tempPoint)][vertexes.indexOf(point)], SimUtils.INFINITY)? 
						SimUtils.INFINITY : (min + adjacentMatrix[vertexes.indexOf(tempPoint)][vertexes.indexOf(point)]));
				if (tmp < unexploited.get(point)) {
					unexploited.replace(point, tmp);
					previous.put(point, tempPoint);
				}
			}
		}
		return previous;
	}
}
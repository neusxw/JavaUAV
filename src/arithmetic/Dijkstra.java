package arithmetic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.SimUtils;
import entity.geometry.Line;
import entity.geometry.LineSegment;
import entity.geometry.Point;
import entity.geometry.Polygon;

public class Dijkstra {
	Map<Point,Point> previous = new HashMap<Point,Point>();
	List<Point> vertexes = new ArrayList<Point>();
	List<? extends Polygon> obstacles;
	Point startPoint;
	Point endPoint;
	double[][] adjacentMatrix;
	List<Point> path = new ArrayList<Point>();

	public Dijkstra(Point startPoint,Point endPoint,List<? extends Polygon> obstacles) {
		this.startPoint=startPoint;
		vertexes.add(this.startPoint);
		this.obstacles=obstacles;
		for (Polygon obstacle:obstacles) {
			vertexes.addAll(obstacle.vertexes);
		}
		this.endPoint=endPoint;
		vertexes.add(this.endPoint);
	}

	private double[][] getAdjacentMatrix() {
		adjacentMatrix = new double[vertexes.size()][vertexes.size()];
		for(int i = 0; i < vertexes.size(); i++) {
			for(int j = 0; j < vertexes.size(); j++) {
				if(i==j) {
					adjacentMatrix[i][j]=0;	//到自身的距离为0；
				}else {
					//System.out.println(vertexes.get(i));
					//System.out.println(vertexes.get(j));
					if(isIntersectionWithObstacles(vertexes.get(i),vertexes.get(j))) {
						adjacentMatrix[i][j]=SimUtils.INFINITY;
					}else {
						adjacentMatrix[i][j]=vertexes.get(i).distanceToPoint(vertexes.get(j));
					}
				}		
			}
		}
		return adjacentMatrix;
	}

	public boolean isIntersectionWithObstacles(Point p1,Point p2){
		LineSegment ls = new LineSegment(p1,p2);
		for(Polygon obstacle:obstacles) {
			List<LineSegment> intersection= ls.intersectionLineSegmentOfLineSegmentAndPolygon(obstacle);
			if(intersection.size()>0) {  //如果从i到j的线段与障碍物相交
				for(LineSegment edge:obstacle.edges) {
					//如果从i到j的线段与障碍物边界重合
					if(((Line)ls).equals(edge)&&
							SimUtils.doubleEqual(ls.length,ls.intersectionLineSegmentOfTwoLineSegments(edge).length)) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	public List<Point> getShortestPath(){
		getAdjacentMatrix();

		boolean[] isExploited = new boolean[vertexes.size()];
		double[] distanceFromStartToUnexploited = new double[vertexes.size()];
		Point[] previous = new Point[vertexes.size()];
		Point[] exploitedPoints = new Point[vertexes.size()];
		for (int i = 0; i < vertexes.size(); i++) {
			isExploited[i] = false; // 顶点i的最短路径还没获取到。
			distanceFromStartToUnexploited[i] = adjacentMatrix[0][i]; 
			previous[i] = vertexes.get(0); //顶点i的前驱顶点为0
		}
		isExploited[0] = true;
		distanceFromStartToUnexploited[0]=0;
		exploitedPoints[0] = vertexes.get(0);

		int tempIndex = 0;
		for (int i = 1; i < vertexes.size(); i++) {
			double min = SimUtils.INFINITY;
			for (int j = 0; j < vertexes.size(); j++) {
				if (isExploited[j] == false && distanceFromStartToUnexploited[j] < min) {
					min = distanceFromStartToUnexploited[j];
					tempIndex = j;
				}
			}           
			exploitedPoints[i] = vertexes.get(tempIndex);
			isExploited[tempIndex] = true;

			if(vertexes.get(tempIndex)==endPoint) {
				break;
			} 

			for (int j = 0; j < vertexes.size(); j++) {
				double temp = adjacentMatrix[tempIndex][j] == SimUtils.INFINITY ? 
						SimUtils.INFINITY : (min + adjacentMatrix[tempIndex][j]);
				if (isExploited[j] == false && (temp < distanceFromStartToUnexploited[j])) {
					distanceFromStartToUnexploited[j] = temp;
					previous[j] = vertexes.get(tempIndex);
				}
			}
		}
		Point to = endPoint;
		List<Point> reversePath = new ArrayList<Point>();
		reversePath.add(endPoint);
		while(to!=startPoint) {
			to=previous[vertexes.indexOf(to)];
			reversePath.add(to);
		}
		for(int i=reversePath.size()-1; i>=0;i--) {
			path.add(reversePath.get(i));
		}
		return path;
	}

	public void printAdjacentMatrix() {
		DecimalFormat df = new DecimalFormat("0.00");
		for(int i = 0; i < vertexes.size(); i++) {
			System.out.print("("+vertexes.get(i).x +"," + vertexes.get(i).y+ ")	");
			for(int j = 0; j < vertexes.size(); j++) {
				if(SimUtils.doubleEqual(adjacentMatrix[i][j], SimUtils.INFINITY)) {
					System.out.print("xx.x ");
				}else {
					System.out.print(df.format(adjacentMatrix[i][j]) + " ");
				}
			}
			System.out.println();
		}
	}

	public void printPath() {
		for(Point point:path) {
			System.out.print(point.toString()+"-->");
		}
	}
}
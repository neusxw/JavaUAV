package main.entity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.arithmetic.Dijkstra;
import main.data.SimUtils;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;
import main.entity.geometry.Polygon;

public class FastGrid {
	private static List<Point> gridPoints = new ArrayList<Point>();
	private static List<LineSegment> gridLines = new ArrayList<LineSegment>();
	private static java.util.Map<Point,LineSegment> point2MotherLine = new HashMap<Point,LineSegment>();
	private static double[][] adjacentMatrix;
	private static boolean[][] isConnected;

	private FastGrid() {}

	/**
	 * 复制gridLines并返回。
	 * @return the gridLines
	 */
	public static List<LineSegment> getGridLines() {
		List<LineSegment> temp = new ArrayList<LineSegment>();
		temp.addAll(gridLines);	
		return temp;
	}

	public static List<Point> getGridPoints() {
		List<Point> temp = new ArrayList<Point>();
		temp.addAll(gridPoints);	
		return temp;
	}

	public static LineSegment getGridLine(int i) {
		return gridLines.get(i);
	}

	public static void add(Point point) {
		gridPoints.add(point);
		renewAdjacentRelation();
	}

	public static void add(LineSegment lineSegment) {
		gridLines.add(lineSegment);
		gridPoints.add(lineSegment.endPoint1);
		gridPoints.add(lineSegment.endPoint2);
		point2MotherLine.put(lineSegment.endPoint1, lineSegment);
		point2MotherLine.put(lineSegment.endPoint2, lineSegment);
		renewAdjacentRelation();
	}

	public static void add(List<LineSegment> lineSegments) {
		for(LineSegment lineSegment:lineSegments) {
			gridLines.add(lineSegment);
			gridPoints.add(lineSegment.endPoint1);
			gridPoints.add(lineSegment.endPoint2);
			point2MotherLine.put(lineSegment.endPoint1, lineSegment);
			point2MotherLine.put(lineSegment.endPoint2, lineSegment);
		}
		renewAdjacentRelation();
	}

	/*
	 * public static void remove(LineSegment lineSegment) {
	 * gridLines.remove(lineSegment); gridPoints.remove(lineSegment.endPoint1);
	 * gridPoints.remove(lineSegment.endPoint2); renewAdjacentRelation(); }
	 * 
	 * public static void remove(List<LineSegment> lineSegments) { for(LineSegment
	 * lineSegment:lineSegments) { gridLines.remove(lineSegment);
	 * gridPoints.remove(lineSegment.endPoint1);
	 * gridPoints.remove(lineSegment.endPoint2); } renewAdjacentRelation(); }
	 */

	public static int size() {
		return gridLines.size();
	}

	/**
	 * 获取线段上某个点的母线段。
	 * @param point
	 * @return
	 * 母线
	 */
	public static LineSegment getMotherLine(Point point) {
		return point2MotherLine.get(point);
	}

	/**
	 * 获取线段上某个点的兄弟节点，即与它处在同一条母线上的点。
	 * @param point
	 * @return
	 * 兄弟节点
	 */
	public static Point getBrotherPoint(Point point) {
		return point2MotherLine.get(point).getBrotherPoint(point);
	}

	public static double[][] getAdjacentMatrix(){
		return adjacentMatrix;
	}

	private static void renewAdjacentRelation(){
		if(SimUtils.isGridSpeeding==false){
			return;
		}
		int numPoint = gridPoints.size();
		adjacentMatrix = new double[numPoint][numPoint];
		isConnected = new boolean[numPoint][numPoint];
		for(int i = 0; i < numPoint; i++) {
			for(int j = 0; j <numPoint; j++) {
				Point pi = gridPoints.get(i);
				Point pj = gridPoints.get(j);
				if(i==j) {
					adjacentMatrix[i][j]=SimUtils.INFINITY;
					isConnected[i][j] = false;
				}else {
					if(!isConnected(pi,pj)) {
						List<Point> path = getPath(pi, pj);
						double len = 0;
						for(int index =0;index<path.size()-1;index++) {
							len+=path.get(index).distanceToPoint(path.get(index+1));
						}
						adjacentMatrix[i][j]=len;
						isConnected[i][j] = false;
					}else {
						adjacentMatrix[i][j]=pi.distanceToPoint(pj);
						isConnected[i][j] = true;
					}
				}		
			}
		}
	}

	/**
	 * 判断连接两点的线段是否与障碍物相交。
	 * 此处的相交定义为线段的长度大于0的某一部分位于多边形内部。
	 * 若线段与多边形的交集只有一个顶点，
	 * 或线段与多边形的交集恰好是多边形的边界，则认为两者不相交。
	 * @param point1 起点
	 * @param point2 终点
	 * @param obstacles 障碍物
	 * @return 逻辑变量，若相交返回True。
	 */
	public static boolean isConnected(Point point1,Point point2,List<? extends Polygon> obstacles){
		LineSegment ls = new LineSegment(point1,point2);
		for(Polygon obstacle:obstacles) {
			List<LineSegment> intersection= ls.intersectionLineSegmentOfLineSegmentAndPolygon(obstacle);
			//如果从i到j的线段与障碍物相交
			if(intersection.size()>0) {
				if(intersection.get(0).getMidPoint().positionToPolygon(obstacle)==SimUtils.INNER) { 
					return false;
				}
			}	
		}
		return true;
	}
	
	/**
	 * 判断连接两点的线段是否与障碍物相交。
	 * 此处的相交定义为线段的长度大于0的某一部分位于多边形内部。
	 * 若线段与多边形的交集只有一个顶点，
	 * 或线段与多边形的交集恰好是多边形的边界，则认为两者不相交。
	 * 障碍物默认为Map中存在的所有obstacles。
	 * @param p1 起点
	 * @param p2 终点
	 * @return 逻辑变量，若相交返回True
	 */
	public static boolean isConnected(Point point1, Point point2) {
		return isConnected(point1,point2,Map.getInstance().obstacles);
	}
	
	/**
	 * 返回isConnected矩阵的值。
	 * <p><b>NOTE</b>:存在加速模式 </p> 
	 * @param point1
	 * @param point2
	 * @return
	 */
	public static boolean getConnectedRelation(Point point1, Point point2) {
		if(SimUtils.isGridSpeeding) {
			return isConnected[gridPoints.indexOf(point1)][gridPoints.indexOf(point2)];
		}else {
			return isConnected(point1,point2);
		}
	}
	
	/**
	 * 计算地图（Map）上两点的距离，如果两点之间没有障碍物，则它们的距离就是几何距离；
	 * 如果两点之间存在障碍物，则它们的距离要考虑到跨越障碍物的代价；
	 * @param point1 起点
	 * @param point2 终点
	 * @param obstacles 障碍物
	 * @return
	 */
	public double distanceOfTwoPoints(Point point1,Point point2, List<? extends Polygon> obstacles) {
		if(!isConnected(point1,point2,obstacles)) {
			List<Point> path = getPath(point1, point2,obstacles);
			double len = 0;
			for(int i =0; i < path.size()-1; i++) {
				len+=path.get(i).distanceToPoint(path.get(i+1));
			}
			return len;
		}else {
			return point1.distanceToPoint(point2);
		}
	}
	
	/**
	 * 计算地图（Map）上两点的距离，如果两点之间没有障碍物，则它们的距离就是几何距离；
	 * 如果两点之间存在障碍物，则它们的距离要考虑到跨越障碍物的代价；
	 * 障碍物默认为Map中存在的所有obstacles。
	 * <p><b>NOTE</b>:存在加速模式 </p> 
	 * @param point1 起点
	 * @param point2  终点
	 * @return 两点在Map上的距离
	 */
	public static double  distanceOfTwoPoints(Point point1,Point point2) {
		if(SimUtils.isGridSpeeding) {
			return adjacentMatrix[gridPoints.indexOf(point1)][gridPoints.indexOf(point2)];
		}else {
			if(!isConnected(point1,point2,Map.getInstance().obstacles)) {
				List<Point> path = getPath(point1, point2,Map.getInstance().obstacles);
				double len = 0;
				for(int i =0; i < path.size()-1; i++) {
					len+=path.get(i).distanceToPoint(path.get(i+1));
				}
				return len;
			}else {
				return point1.distanceToPoint(point2);
			}
		}
			
	}
	
	/**
	 * 获取两点之间跨越障碍的最短路径；
	 * @param point1 起点
	 * @param point2  终点
	 * @param obstacles 障碍物
	 * @return
	 */
	public static List<Point> getPath(Point point1,Point point2, List<? extends Polygon> obstacles){
		Dijkstra dj = new Dijkstra(point1,point2,obstacles);
		return dj.getShortestPath();
	}
	
	/**
	 * 获取两点之间跨越障碍的最短路径；
	 * 障碍物默认为Map中存在的所有obstacles。
	 * @param p1 起点
	 * @param p2 终点
	 * @return 跨越障碍的最短路径。
	 */
	public static List<Point> getPath(Point point1,Point point2){
		return getPath(point1,point2,Map.getInstance().obstacles);
	}

	/**
	 * 按gridLines到某个点的距离大小对gridLines进行排序。
	 * @param point
	 * 参考点
	 * @return
	 * gridLines排序后的结果。
	 */
	public static List<LineSegment> ranking(Point point){
		List<LineSegment> tempLines = new ArrayList<LineSegment>();
		for(int i = 0; i<gridLines.size();i++) {
			tempLines.add(gridLines.get(i));
		}
		for(int i = 0; i<tempLines.size()-1;i++) {
			for(int j = 0; j<tempLines.size()-1-i;i++) {
				if(point.distanceToLineSegment(tempLines.get(j),"midpoint")>
				point.distanceToLineSegment(tempLines.get(j+1),"midpoint")) {
					LineSegment temp = tempLines.get(j);
					tempLines.set(j, tempLines.get(j+1));
					tempLines.set(j+1, temp);
				}
			}
		}
		return tempLines;
	}
	
	public static List<Point> getGridPoints(List<LineSegment> lines) {
		List<Point> points = new ArrayList<Point>();
		for(LineSegment line:lines) {
			points.add(line.endPoint1);
			points.add(line.endPoint2);
		}
		return points;
	}
	
	public static List<Point> getMidPoints(List<LineSegment> lines) {
		List<Point> points = new ArrayList<Point>();
		for(LineSegment line:lines) {
			points.add(line.getMidPoint());
		}
		return points;
	}
	
	public static void printAdjacentMatrix() {
		System.out.println("-------------------AdjacentMatrix----------------------");
		System.out.println("共有" + gridPoints.size() + "个节点");
		DecimalFormat df = new DecimalFormat("0.00");
		for(int i = 0; i < gridPoints.size(); i++) {
			System.out.print("("+df.format(gridPoints.get(i).x) +"," + df.format(gridPoints.get(i).y)+ ")	");
			for(int j = 0; j < gridPoints.size(); j++) {
				if(SimUtils.doubleEqual(adjacentMatrix[i][j], SimUtils.INFINITY)) {
					System.out.print("xx.x |");
				}else {
					System.out.print(df.format(adjacentMatrix[i][j]) + " |");
				}
			}
			System.out.println();
		}
		System.out.println("--------------------END---------------------");
	}
	
	public static void printIsConnected() {
		System.out.println("-------------------IsConnected----------------------");
		DecimalFormat df = new DecimalFormat("0.00");
		for(int i = 0; i < isConnected.length; i++) {
			System.out.print("("+df.format(gridPoints.get(i).x) +"," + df.format(gridPoints.get(i).y)+ ")	");
			for(int j = 0; j < isConnected.length; j++) {
				if(isConnected[i][j]==true) {
					System.out.print("T ");
				}else {
					System.out.print("F ");
				}
			}
			System.out.println();
		}
		System.out.println("--------------------END---------------------");
	}
}

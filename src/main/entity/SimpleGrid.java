package main.entity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.arithmetic.Dijkstra;
import main.arithmetic.data.SimUtils;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;
import main.entity.geometry.Polygon;

public class SimpleGrid {

	private static SimpleGrid simpleGrid = new SimpleGrid();
	private SimpleGrid() {	}
	public static SimpleGrid getInstance(){
		return simpleGrid;
	}
	
	public static LineSegment createGridLines(Point point1,Point point2) {
		LineSegment lineSegment = new LineSegment(point1,point2);
		point1.setMotherLine(lineSegment);
		point2.setMotherLine(lineSegment);
		point1.setBrotherPoint(point2);
		point2.setBrotherPoint(point1);
		return lineSegment;
	}
	
	public static LineSegment createGridLines(LineSegment lineSegment) {
		Point point1 = lineSegment.endPoint1;
		Point point2 = lineSegment.endPoint2;
		point1.setMotherLine(lineSegment);
		point2.setMotherLine(lineSegment);
		point1.setBrotherPoint(point2);
		point2.setBrotherPoint(point1);
		return lineSegment;
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
	 * 计算地图（Map）上两点的距离，如果两点之间没有障碍物，则它们的距离就是几何距离；
	 * 如果两点之间存在障碍物(不相通，即isConnected返回为假)，则它们的距离要考虑到跨越障碍物的代价；
	 * @param point1 起点
	 * @param point2 终点
	 * @param obstacles 障碍物
	 * @return
	 */
	public static double  distanceOfTwoPoints(Point point1,Point point2, List<? extends Polygon> obstacles) {
		if(!isConnected(point1,point2,obstacles)) {
			List<Point> path = getPath(point1, point2,obstacles);
			double len = 0;
			for(int i =0; i < path.size()-1; i++) {
				len+=path.get(i).distanceToPoint(path.get(i+1))+SimUtils.TURNING$PAYOFF;
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
	 * @param point1 起点
	 * @param point2  终点
	 * @return 两点在Map上的距离
	 */
	public static double  distanceOfTwoPoints(Point point1,Point point2) {
		return distanceOfTwoPoints(point1,point2, Map.getInstance().obstacles);
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
	public static List<LineSegment> ranking(Point point, List<LineSegment> gridLines){
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
}

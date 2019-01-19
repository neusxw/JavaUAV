package main.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.arithmetic.Dijkstra;
import main.arithmetic.SimUtils;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;
import main.entity.geometry.Polygon;

public class Grid {
	private static List<Point> gridPoints = new ArrayList<Point>();
	private static List<LineSegment> gridLines = new ArrayList<LineSegment>();
	private static java.util.Map<Point,LineSegment> point2MotherLine = new HashMap<Point,LineSegment>();
	private static double[][] adjacentMatrix;
	private static boolean[][] isConnected;

	private Grid() {}

	/**
	 * @return the gridLines
	 */
	public static List<LineSegment> getGridLines() {
		return gridLines;
	}

	public static List<Point> getGridPoints() {
		return gridPoints;
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

	public static void remove(LineSegment lineSegment) {
		gridLines.remove(lineSegment);
		gridPoints.remove(lineSegment.endPoint1);
		gridPoints.remove(lineSegment.endPoint2);
		renewAdjacentRelation();
	}

	public static void remove(List<LineSegment> lineSegments) {
		for(LineSegment lineSegment:lineSegments) {
			gridLines.remove(lineSegment);
			gridPoints.remove(lineSegment.endPoint1);
			gridPoints.remove(lineSegment.endPoint2);
		}
		renewAdjacentRelation();
	}

	public static int size() {
		return gridLines.size();
	}

	/**
	 * ��ȡ�߶���ĳ�����ĸ�߶Ρ�
	 * @param point
	 * @return
	 * ĸ��
	 */
	public static LineSegment getMotherLine(Point point) {
		return point2MotherLine.get(point);
	}

	/**
	 * ��ȡ�߶���ĳ������ֵܽڵ㣬����������ͬһ��ĸ���ϵĵ㡣
	 * @param point
	 * @return
	 * �ֵܽڵ�
	 */
	public static Point getBrotherPoint(Point point) {
		return point2MotherLine.get(point).getBrotherPoint(point);
	}

	public static double[][] getAdjacentMatrix(){
		return adjacentMatrix;
	}

	private static void renewAdjacentRelation(){
		int numPoint = gridPoints.size();
		adjacentMatrix = new double[numPoint][numPoint];
		isConnected = new boolean[numPoint][numPoint];
		for(int i = 0; i < numPoint; i++) {
			for(int j = 0; j <numPoint; j++) {
				Point pi = gridPoints.get(i);
				Point pj = gridPoints.get(j);
				if(i==j) {
					adjacentMatrix[i][j]=Double.POSITIVE_INFINITY;
					isConnected[i][j] = false;
				}else {
					if(isConnected(pi,pj)) {
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
	 * �ж�����������߶��Ƿ����ϰ����ཻ��
	 * �˴����ཻ����Ϊ�߶εĳ��ȴ���0��ĳһ����λ�ڶ�����ڲ���
	 * ���߶������εĽ���ֻ��һ�����㣬
	 * ���߶������εĽ���ǡ���Ƕ���εı߽磬����Ϊ���߲��ཻ��
	 * @param point1 ���
	 * @param point2 �յ�
	 * @param obstacles �ϰ���
	 * @return �߼����������ཻ����True
	 */
	private static boolean isConnected(Point point1,Point point2,List<? extends Polygon> obstacles){
		LineSegment ls = new LineSegment(point1,point2);
		for(Polygon obstacle:obstacles) {
			LineSegment intersection= ls.intersectionLineSegmentOfLineSegmentAndPolygon(obstacle);
			//�����i��j���߶����ϰ����ཻ
			if(intersection!=null&&intersection.getMidPoint().positionToPolygon(obstacle)==SimUtils.INNER) { 
				return false;
			}
		}
		return true;
	}
	
	/**
	 * �ж�����������߶��Ƿ����ϰ����ཻ��
	 * �˴����ཻ����Ϊ�߶εĳ��ȴ���0��ĳһ����λ�ڶ�����ڲ���
	 * ���߶������εĽ���ֻ��һ�����㣬
	 * ���߶������εĽ���ǡ���Ƕ���εı߽磬����Ϊ���߲��ཻ��
	 * �ϰ���Ĭ��ΪMap�д��ڵ�����obstacles��
	 * @param p1 ���
	 * @param p2 �յ�
	 * @return �߼����������ཻ����True
	 */
	private static boolean isConnected(Point point, Point point2) {
		return isConnected(point,point2,Map.getInstance().obstacles);
	}
	
	/**
	 * �����ͼ��Map��������ľ��룬�������֮��û���ϰ�������ǵľ�����Ǽ��ξ��룻
	 * �������֮������ϰ�������ǵľ���Ҫ���ǵ���Խ�ϰ���Ĵ��ۣ�
	 * @param point1 ���
	 * @param point2 �յ�
	 * @param obstacles �ϰ���
	 * @return
	 */
	public double distanceOfTwoPoints(Point point1,Point point2, List<? extends Polygon> obstacles) {
		if(isConnected(point1,point2,obstacles)) {
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
	 * �����ͼ��Map��������ľ��룬�������֮��û���ϰ�������ǵľ�����Ǽ��ξ��룻
	 * �������֮������ϰ�������ǵľ���Ҫ���ǵ���Խ�ϰ���Ĵ��ۣ�
	 * �ϰ���Ĭ��ΪMap�д��ڵ�����obstacles��
	 * @param point1 ���
	 * @param point2  �յ�
	 * @return ������Map�ϵľ���
	 */
	public static double  distanceOfTwoPoints(Point point1,Point point2) {
			return adjacentMatrix[gridPoints.indexOf(point1)][gridPoints.indexOf(point1)];
	}
	
	/**
	 * ��ȡ����֮���Խ�ϰ������·����
	 * @param point1 ���
	 * @param point2  �յ�
	 * @param obstacles �ϰ���
	 * @return
	 */
	public static List<Point> getPath(Point point1,Point point2, List<? extends Polygon> obstacles){
		Dijkstra dj = new Dijkstra(point1,point2,obstacles);
		return dj.getShortestPath();
	}
	
	/**
	 * ��ȡ����֮���Խ�ϰ������·����
	 * �ϰ���Ĭ��ΪMap�д��ڵ�����obstacles��
	 * @param p1 ���
	 * @param p2 �յ�
	 * @return ��Խ�ϰ������·����
	 */
	public static List<Point> getPath(Point point1,Point point2){
		return getPath(point1,point2,Map.getInstance().obstacles);
	}

	/**
	 * ��gridLines��ĳ����ľ����С��gridLines��������
	 * @param point
	 * �ο���
	 * @return
	 * gridLines�����Ľ����
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
}

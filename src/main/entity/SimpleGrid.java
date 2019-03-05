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
	private static List<LineSegment> gridLines = new ArrayList<LineSegment>();
	private static List<Point> gridPoints = new ArrayList<Point>();
	private static double[][] adjacentMatrix = null;
	
	private static SimpleGrid simpleGrid = new SimpleGrid();
	private SimpleGrid() {	}
	public static SimpleGrid getInstance(){
		return simpleGrid;
	}
	
	public static LineSegment createGridLines(Point point1,Point point2,Land land) {
		LineSegment lineSegment = new LineSegment(point1,point2);
		gridLines.add(lineSegment);
		gridPoints.add(point1);
		gridPoints.add(point2);
		point1.setMotherLine(lineSegment);
		point2.setMotherLine(lineSegment);
		point1.setBrotherPoint(point2);
		point2.setBrotherPoint(point1);
		lineSegment.setMotherLand(land);
		//System.out.println(point1 + " " + point2);
		return lineSegment;
	}
	
	public static LineSegment createGridLines(LineSegment lineSegment,Land land) {
		return createGridLines(lineSegment.endPoint1,lineSegment.endPoint2,land);
	}

	/**
	 * �ж�����������߶��Ƿ����ϰ����ཻ��
	 * �˴����ཻ����Ϊ�߶εĳ��ȴ���0��ĳһ����λ�ڶ�����ڲ���
	 * ���߶������εĽ���ֻ��һ�����㣬
	 * ���߶������εĽ���ǡ���Ƕ���εı߽磬����Ϊ���߲��ཻ��
	 * @param point1 ���
	 * @param point2 �յ�
	 * @param obstacles �ϰ���
	 * @return �߼����������ཻ����True��
	 */
	public static boolean isConnected(Point point1,Point point2,List<? extends Polygon> obstacles){
		LineSegment ls = new LineSegment(point1,point2);
		for(Polygon obstacle:obstacles) {
			List<LineSegment> intersection= ls.intersectionLineSegmentOfLineSegmentAndPolygon(obstacle);
			//System.out.println("00");
			//�����i��j���߶����ϰ����ཻ
			if(intersection.size()>0) {
				//System.out.println(intersection.get(0).length);
				if(intersection.get(0).getMidPoint().positionToPolygon(obstacle)==SimUtils.INNER) { 
					return false;
				}
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
	public static boolean isConnected(Point point1, Point point2) {
		return isConnected(point1,point2,Map.getInstance().obstacles);
	}
	
	
	/**
	 * �����ͼ��Map��������ľ��룬�������֮��û���ϰ�������ǵľ�����Ǽ��ξ��룻
	 * �������֮������ϰ���(����ͨ����isConnected����Ϊ��)�������ǵľ���Ҫ���ǵ���Խ�ϰ���Ĵ��ۣ�
	 * @param point1 ���
	 * @param point2 �յ�
	 * @param obstacles �ϰ���
	 * @return
	 */
	public static double  distanceOfTwoPoints(Point point1,Point point2, List<? extends Polygon> obstacles) {
		try {
			if(point1.equals(point2)) {
				return 0;
			}
			if(adjacentMatrix==null) {
				int numPoint = gridPoints.size();
				adjacentMatrix = new double[numPoint][numPoint];
			}
			int i = gridPoints.indexOf(point1);
			int j = gridPoints.indexOf(point2);
			if(adjacentMatrix[i][j]>0) {
				return adjacentMatrix[i][j];
			}else {
				adjacentMatrix[i][j] = distanceCompute(point1,point2,obstacles);
				return adjacentMatrix[i][j];
			}
		}catch(Exception e){
			return distanceCompute(point1,point2,obstacles);
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
		return distanceOfTwoPoints(point1,point2, Map.getInstance().obstacles);
	}
	
	private static double distanceCompute(Point point1,Point point2, List<? extends Polygon> obstacles) {
		if(!isConnected(point1,point2,obstacles)) {
			List<Point> path = getPath(point1, point2,obstacles);
			double len = 0;
			for(int i =0; i < path.size()-1; i++) {
				len+=path.get(i).distanceToPoint(path.get(i+1))+SimUtils.velocity*SimUtils.turningTime;
			}
			return len;
		}else {
			return point1.distanceToPoint(point2);
		}
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
	
	public static List<LineSegment> getGridLines() {
		return gridLines;
	}
	public static List<Point> getMidPoints(List<LineSegment> lines) {
		List<Point> points = new ArrayList<Point>();
		for(LineSegment line:lines) {
			points.add(line.getMidPoint());
		}
		return points;
	}
}

package main.entity;

import java.util.ArrayList;
import java.util.List;

import main.arithmetic.SimUtils;
/*
 * ��ͼ��
 * ���õ���ģʽ��
 * ��ͼ��land��obstacle��ɣ������ǳ�ʼ��ʱ�Զ����뵽map
 * @@@@ ���裺land��obstacle�ı߽粻�غ�  @@@
 */
public class Map {
	/*
	 * 
	 */
	private static Map map = new Map();
	
	public List<Land> lands = new ArrayList<Land>();
	public List<Obstacle> obstacles = new ArrayList<Obstacle>();
	public List<Point> gridPoints = new ArrayList<Point>();
	public List<LineSegment> gridLines = new ArrayList<LineSegment>();

	private Map() {}
	public static Map getInstance(){
		return map;
	}
	public void addland(Land e) {
		this.lands.add(e);
	}

	public void addObstacle(Obstacle e) {
		this.obstacles.add(e);
	}
	
	public void createGridLines() {
		clearGrid();
		for(Land land:lands) {
			System.out.println("-----------��ʼ���ֵ�" + Integer.toString(lands.indexOf(land)+1) +"���-----------");
			System.out.println(land.toString());
			land.createGridLines();
			land.devideGridLinesByObstacle(obstacles);
			land.generateGridPointsFromGridLines();
			gridLines.addAll(land.gridLines);
			gridPoints.addAll(land.gridPoints);
			System.out.println("-------------END---------------");
		}
		for(LineSegment line:gridLines) {
			System.out.println(line);
		}
	}
	
	public double distanceOfTwoPoints(Point point1,Point point2) {
		LineSegment ls = new LineSegment(point1,point2);
		for(Obstacle obstacle:obstacles) {
			LineSegment intersection=ls.intersectionLineSegmentOfLineSegmentAndPolygon(obstacle);
			//System.out.println(ls);
			if(intersection!=null && intersection.length>10*SimUtils.EPS) {
				return SimUtils.INFINITY;
			}
		}
		return point1.distanceToPoint(point2);
	}
	
	public void clearGrid(){
		for(Land land:lands) {
			land.gridLines.clear();
			land.gridPoints.clear();
		}
	}
	
	public LineSegment getMotherLine(Point point) {
		for(Land land:lands) {
			if(land.getMotherLine(point)!=null) {
				return land.getMotherLine(point);
			}
		}
		return null;
	}
	
	public Point getBrotherPoint(Point point) {
		int i=1;
		for(Land land:lands) {
			Point brother=land.getBrotherPoint(point);
			if(brother!=null) {
				return brother;
			}
		}
		return null;
	}
	
	public void print() {
		System.out.println(this.toString());
	}
	public String toString() {
		String str="=======================Map=======================\t\n";
		str+= ">>>>>>����" + lands.size() + "��Land:\t\n";
		for(Land land:lands) {
			str+=land.toString()+" \t\n";
		}
		str+= ">>>>>>����" + obstacles.size() + "��Obstacle:\t\n";
		for(Obstacle obstacle:obstacles) {
			str+=obstacle.toString()+" \t\n";
		}
		return str;
	}
}

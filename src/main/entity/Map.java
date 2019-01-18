package main.entity;

import java.util.ArrayList;
import java.util.List;

import main.arithmetic.Dijkstra;
import main.arithmetic.SimUtils;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;
import main.entity.geometry.Polygon;
/**
 * ��ͼ��
 * ���õ���ģʽ��
 * ��ͼ��land��obstacle����ɣ������ǳ�ʼ��ʱ�Զ����뵽map
 */
public class Map {
	/**
	 * ����ʵ��ֱ�洢��lands,obstacles,stations,UAVs
	 */
	private static Map map = new Map();
	public final List<Land> lands = new ArrayList<Land>();
	public final List<Obstacle> obstacles = new ArrayList<Obstacle>();
	public final List<Station> stations = new ArrayList<Station>();
	public final List<UAV> UAVs = new ArrayList<UAV>();
	public final List<Point> gridPoints = new ArrayList<Point>();
	public final List<LineSegment> gridLines = new ArrayList<LineSegment>();
	
	private CreateTrajectory createTrajectory;
	/**
	 * ��ȫ����
	 */
	public static double safetyDistance = SimUtils.SAFETYDISTANCE;

	private Map() {}
	public static Map getInstance(){
		return map;
	}

	/**
	 * ���������߼����Ӧ�������
	 */
	public void createGrid() {
		clearGrid();
		for(Land land:lands) {
			System.out.println("-----------��ʼ���ֵ�" + Integer.toString(lands.indexOf(land)+1) +"���-----------");
			System.out.println(land.toString());
			land.createGridLines();
			//for(LineSegment line:gridLines) {System.out.println(line);}
			land.devideGridLinesByObstacle(obstacles);
			land.generateGridPointsFromGridLines();
			gridLines.addAll(land.gridLines);
			gridPoints.addAll(land.gridPoints);
			System.out.println("----------- --END---------------");
		}
	}

	/**
	 * �����ͼ��Map��������ľ��룬�������֮��û���ϰ�������ǵľ�����Ǽ��ξ��룻
	 * �������֮������ϰ�������ǵľ���Ҫ���ǵ���Խ�ϰ���Ĵ��ۣ�
	 * @param point1
	 * ���
	 * @param point2
	 * �յ�
	 * @return
	 * ������Map�ϵľ���
	 */
	public double DistanceOfTwoPoints(Point point1,Point point2) {
		if(isIntersectionWithObstacles(point1,point2,obstacles)) {
			Dijkstra dj = new Dijkstra(point1,point2,obstacles);
			List<Point> path=dj.getShortestPath();
			double pathLength=0;
			for(int i =0;i<path.size()-1;i++) {
				pathLength+=path.get(i).distanceToPoint(path.get(i+1))+SimUtils.INFINITY;
			}
			return pathLength;
		}else {
			return point1.distanceToPoint(point2);
		}
	}
	
	/**
	 * �����ͼ�������߶εľ��룬��������߶�֮��û���ϰ�������ǵľ�����Ǽ��ξ��룻
	 * ��������߶�֮������ϰ�������ǵľ���Ҫ���ǵ���Խ�ϰ���Ĵ��ۣ�
	 * @param point1
	 * @param point2
	 * @return
	 */
	public double DistanceOfTwoLineSegments(LineSegment ls1,LineSegment ls2) {
		return 0;
	}

	/**
	 * �ж�����������߶��Ƿ����ϰ����ཻ��
	 * �˴����ཻ����Ϊ�߶εĳ��ȴ���0��ĳһ����λ�ڶ�����ڲ���
	 * ���߶������εĽ���ֻ��һ�����㣬
	 * ���߶������εĽ���ǡ���Ƕ���εı߽磬����Ϊ���߲��ཻ��
	 * @param p1
	 * ���
	 * @param p2
	 * �յ�
	 * @param obstacles
	 * �ϰ���
	 * @return
	 * �߼����������ཻ����True
	 */
	public boolean isIntersectionWithObstacles(Point p1,Point p2,List<Obstacle> obstacles){
		LineSegment ls = new LineSegment(p1,p2);
		for(Polygon obstacle:obstacles) {
			LineSegment intersection= ls.intersectionLineSegmentOfLineSegmentAndPolygon(obstacle);
			//�����i��j���߶����ϰ����ཻ
			if(intersection!=null&&intersection.getMidPoint().positionToPolygon(obstacle)==SimUtils.INNER) { 
				return true;
			}
		}
		return false;
	}

	/**
	 * ��������ߺ�����㣻
	 */
	public void clearGrid(){
		for(Land land:lands) {
			land.gridLines.clear();
			land.gridPoints.clear();
		}
	}

	/**
	 * ��ȡ�߶���ĳ�����ĸ�߶Ρ�
	 * @param point
	 * @return
	 */
	public LineSegment getMotherLine(Point point) {
		return point.motherLine;
	}

	/**
	 * ��ȡ�߶���ĳ������ֵܽڵ㣬����������ͬһ��ĸ���ϵĵ㡣
	 * @param point
	 * @return
	 */
	public Point getBrotherPoint(Point point) {
		return point.motherLine.getBrotherPoint(point);
	}

	/**
	 * ��gridLines��ĳ����ľ����С��gridLines��������
	 * @param point
	 * �ο���
	 * @return
	 * gridLines�����Ľ����
	 */
	public List<LineSegment> ranking(Point point){
		List<LineSegment> tempLines = gridLines;
		for(int i = 0; i<tempLines.size()-1;i++) {
			for(int j = 0; j<tempLines.size()-1-i;i++) {
				if(point.distanceToLineSegment(tempLines.get(j))>point.distanceToLineSegment(tempLines.get(j+1))) {
					LineSegment temp = tempLines.get(j);
					tempLines.set(j, tempLines.get(j+1));
					tempLines.set(j+1, temp);
				}
			}
		}
		return tempLines;
	}
	
	public CreateTrajectory getCreateTrajectory() {
		return createTrajectory;
	}
	public void setCreateTrajectory(CreateTrajectory createTrajectory) {
		this.createTrajectory = createTrajectory;
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

		str+= ">>>>>>����" + stations.size() + "��Station:\t\n";
		for(Station station:stations) {
			str+=station.toString()+" \t\n";
		}
		str+="=======================END=======================\t\n";
		return str;
	}
	
	/**
	 * ���ĳ��ʵ�壨land��obstacle��station����
	 * @param polygon
	 */
	public void removePolygon(Polygon polygon) {
		if(polygon instanceof Land) {
			this.lands.remove(polygon);
		}
		if(polygon instanceof Obstacle) {
			this.obstacles.remove(polygon);
		}
		if(polygon instanceof Station) {
			this.stations.remove(polygon);
		}
	}
	
	/**
	 * ����ĳ��ʵ�壨land��obstacle��station����
	 * @param polygon
	 */
	public void addPolygon(Polygon polygon) {
		if(polygon instanceof Land) {
			this.lands.add((Land) polygon);
		}
		if(polygon instanceof Obstacle) {
			this.obstacles.add((Obstacle) polygon);
		}
		if(polygon instanceof Station) {
			this.stations.add((Station) polygon);
		}
	}
}

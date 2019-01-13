package main.entity;

import java.util.ArrayList;
import java.util.List;

import main.arithmetic.Dijkstra;
import main.arithmetic.SimUtils;
/**
 * ��ͼ��
 * ���õ���ģʽ��
 * ��ͼ��land��obstacle��ɣ������ǳ�ʼ��ʱ�Զ����뵽map
 * @@@@ ���裺land��obstacle�ı߽粻�غ�  @@@
 */
public class Map {
	/**
	 * ����ʵ��ֱ�Ϊlands,obstacles,stations,UAVs
	 */
	private static Map map = new Map();
	public List<Land> lands = new ArrayList<Land>();
	public List<Obstacle> obstacles = new ArrayList<Obstacle>();
	public List<Station> stations = new ArrayList<Station>();
	public List<UAV> UAVs = new ArrayList<UAV>();
	public List<Point> gridPoints = new ArrayList<Point>();
	public List<LineSegment> gridLines = new ArrayList<LineSegment>();
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
			System.out.println("-------------END---------------");
		}
	}

	public double DistanceOfTwoPoints(Point point1,Point point2) {
		if(SimUtils.doubleEqual(straightDistanceOfTwoPoints(point1,point2), SimUtils.INFINITY)) {
			return detourDistanceOfTwoPoints(point1,point2);
		}else {
			return straightDistanceOfTwoPoints(point1,point2);
		}
	}
	
	public double detourDistanceOfTwoPoints(Point point1,Point point2) {
		if(isIntersectionWithObstacles(point1,point2)) {
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
	
	public double straightDistanceOfTwoPoints(Point point1,Point point2) {
		if(isIntersectionWithObstacles(point1,point2)) {
			return SimUtils.INFINITY;
		}else {
			return point1.distanceToPoint(point2);
		}
	}

	public boolean isIntersectionWithObstacles(Point p1,Point p2){
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
		for(Land land:lands) {
			Point brother=land.getBrotherPoint(point);
			if(brother!=null) {
				return brother;
			}
		}
		return null;
	}

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
		return str;
	}
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

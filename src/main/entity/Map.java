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
	public final List<Land> lands = new ArrayList<Land>();
	public final List<Obstacle> obstacles = new ArrayList<Obstacle>();
	public final List<Station> stations = new ArrayList<Station>();
	public final List<UAV> UAVs = new ArrayList<UAV>();
	public final List<LineSegment> gridLines= new ArrayList<LineSegment>();
	public final List<Point> gridPoints= new ArrayList<Point>();
	
	private CreateTrajectory createTrajectory;
	public final static double TP = 10;
	/**
	 * ��ȫ����
	 */
	public static double safetyDistance = SimUtils.SAFETYDISTANCE;

	private static Map map = new Map();
	private Map() {}
	public static Map getInstance(){
		return map;
	}

	/**
	 * ���������߼����Ӧ�������
	 */
	public void createGrid() {
		for(Land land:lands) {
			System.out.println("-----------��ʼ���ֵ�" + Integer.toString(lands.indexOf(land)+1) +"���-----------");
			System.out.println(land.toString());
			land.createGridLines();
			//for(LineSegment line:gridLines) {System.out.println(line);}
			land.devideGridLinesByObstacle(obstacles);
			add(land.gridLines);
			System.out.println("----------- --END---------------");
		}
	}
	
	public void add(List<LineSegment> lines) {
		Grid.add(lines);
		for(LineSegment lineSegment:lines) {
			gridLines.add(lineSegment);
			gridPoints.add(lineSegment.endPoint1);
			gridPoints.add(lineSegment.endPoint2);
		}
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

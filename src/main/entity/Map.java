package main.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	public List<LineSegment> gridLines = new ArrayList<LineSegment>();
	public List<Point> gridPoints;

	private static Map map = new Map();
	private Map() {	}
	public static Map getInstance(){
		return map;
	}

	private void setGrid() {
		for(Land land:lands) {
			gridLines.addAll(land.getGridLines());
			//System.out.println("map.setgrid:" + land.getGridLines().size());
			//System.out.println("map.setgrid:" + gridLines.size());
		}
	}
	/**
	 * ���������߼����Ӧ�������
	 */
	public void createGrid() {
		System.out.println("===================���ֵؿ�===================");
		for(Land land:lands) {
		    //System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			System.out.println("-----------��ʼ���ֵ�" + Integer.toString(lands.indexOf(land)+1) +"���-----------");
			System.out.println(land.toString(false));
			land.createGridLines();
			//for(LineSegment line:land.getGridLines()) {System.out.println(line);}
			land.devideGridLinesByObstacle(obstacles);
		}
		System.out.println("===================  END  ===================");
		setGrid();
	}
	
	/**
	 * ���ĳ��ʵ�壨land��obstacle��station����
	 * @param polygon
	 */
	public void remove(Polygon polygon) {
		if(polygon instanceof Land) {
			lands.remove(polygon);
		}
		if(polygon instanceof Obstacle) {
			obstacles.remove(polygon);
		}
		if(polygon instanceof Station) {
			stations.remove(polygon);
		}
	}
	
	/**
	 * ����ĳ��ʵ�壨land��obstacle��station����
	 * @param polygon
	 */
	public void add(Polygon polygon) {
		if(polygon instanceof Land) {
			lands.add((Land) polygon);
		}
		if(polygon instanceof Obstacle) {
			obstacles.add((Obstacle) polygon);
		}
		if(polygon instanceof Station) {
			stations.add((Station) polygon);
		}
	}
	
	public void print() {
		System.out.println(this.toString());
	}

	public String toString() {
		String str="=======================Map=======================\r\n";
		str+= ">>>>>>����" + lands.size() + "��Land:\r\n";
		for(Land land:lands) {
			str+=land.toString(false)+" \r\n";
		}
		str+= ">>>>>>����" + obstacles.size() + "��Obstacle:\r\n";
		for(Obstacle obstacle:obstacles) {
			str+=obstacle.toString(false)+" \r\n";
		}

		str+= ">>>>>>����" + stations.size() + "��Station:\r\n";
		for(Station station:stations) {
			str+=station.toString(false)+" \r\n";
		}
		str+="=======================END=======================";
		return str;
	}
}

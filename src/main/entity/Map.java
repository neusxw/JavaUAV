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
	public List<LineSegment> gridLines;
	public List<Point> gridPoints;
	
	public final static double TURNINGPAYOFF = 10;

	private static Map map = new Map();
	private Map() {	}
	public static Map getInstance(){
		return map;
	}

	private void setGrid() {
		gridLines= Grid.getGridLines();
		gridPoints =  Grid.getGridPoints();
		//System.out.println(gridPoints.size());
		for(Station station:stations) {
			for(TakeOffPoint tp:station.takeOffPoints) {
				gridPoints.remove(tp);
			}
		}
	}
	/**
	 * ���������߼����Ӧ�������
	 */
	public void createGrid() {
		System.out.println("���������������������������������ֵؿ顪����������������������");
		for(Land land:lands) {
		    System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			System.out.println("-----------��ʼ���ֵ�" + Integer.toString(lands.indexOf(land)+1) +"���-----------");
			System.out.println(land.toString());
			land.createGridLines();
			//for(LineSegment line:land.getGridLines()) {System.out.println(line);}
			land.devideGridLinesByObstacle(obstacles);
			Grid.add(land.getGridLines());
		}
		System.out.println("������������������������������  END  ������������������������");
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
}

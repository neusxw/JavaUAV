package main.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;
import main.entity.geometry.Polygon;
/**
 * 地图：
 * 采用单例模式；
 * 地图由land和obstacle等组成，在它们初始化时自动加入到map
 */
public class Map {
	/**
	 * 四类实体分别存储在lands,obstacles,stations,UAVs
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
	 * 生成网格线及其对应的网格点
	 */
	public void createGrid() {
		System.out.println("===================划分地块===================");
		for(Land land:lands) {
		    //System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
			System.out.println("-----------开始划分第" + Integer.toString(lands.indexOf(land)+1) +"快地-----------");
			System.out.println(land.toString(false));
			land.createGridLines();
			//for(LineSegment line:land.getGridLines()) {System.out.println(line);}
			land.devideGridLinesByObstacle(obstacles);
		}
		System.out.println("===================  END  ===================");
		setGrid();
	}
	
	/**
	 * 清除某个实体（land、obstacle或station）。
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
	 * 增加某个实体（land、obstacle或station）。
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
		str+= ">>>>>>共有" + lands.size() + "快Land:\r\n";
		for(Land land:lands) {
			str+=land.toString(false)+" \r\n";
		}
		str+= ">>>>>>共有" + obstacles.size() + "快Obstacle:\r\n";
		for(Obstacle obstacle:obstacles) {
			str+=obstacle.toString(false)+" \r\n";
		}

		str+= ">>>>>>共有" + stations.size() + "个Station:\r\n";
		for(Station station:stations) {
			str+=station.toString(false)+" \r\n";
		}
		str+="=======================END=======================";
		return str;
	}
}

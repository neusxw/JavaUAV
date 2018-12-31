package main.entity;

import java.util.ArrayList;
import java.util.List;
/*
 * 地图：
 * 采用单例模式；
 * 地图由land和obstacle组成，在它们初始化时自动加入到map
 * @@@@ 假设：land和obstacle的边界不重合  @@@
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
		clearGridLines();
		for(Land land:lands) {
			System.out.println("{{{{{{{{{{{{{{{{{{{{");
			land.createGridLines();
			land.avoidObstacle(obstacles);
			gridLines.addAll(land.gridLines);
		}
	}
	
	public void clearGridLines(){
		for(Land land:lands) {
			land.gridLines.clear();
		}
	}
	
	public String toString() {
		String str="Map:\t\n";
		for(Land land:lands) {
			str+=land.toString()+" \t\n";
		}
		for(Obstacle obstacle:obstacles) {
			str+=obstacle.toString()+" \t\n";
		}
		return str;
	}
}

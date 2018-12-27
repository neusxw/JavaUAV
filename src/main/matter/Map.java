package main.matter;

import java.util.ArrayList;
import java.util.List;
/*
 * 地图：
 * 采用单例模式；
 * 地图由land和obstacle组成，在它们初始化时自动加入到map
 * @@@@ 假设：land和obstacle的边界不重合
 */
public class Map {
	/*
	 * 
	 */
	public List<Land> lands = new ArrayList<Land>();
	public List<Obstacle> Obstacles = new ArrayList<Obstacle>();
	private static Map map = new Map();

	private Map() {}
	public static Map getInstance(){
		return map;
	}
	public void addland(Land e) {
		this.lands.add(e);
	}

	public void addObstacle(Obstacle e) {
		this.Obstacles.add(e);
	}
	
	public String toString() {
		String str="Map:\t\n";
		for(Land land:lands) {
			str+=land.toString()+" \t\n";
		}
		for(Obstacle obstacle:Obstacles) {
			str+=obstacle.toString()+" \t\n";
		}
		return str;
	}

}

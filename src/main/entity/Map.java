package main.entity;

import java.util.ArrayList;
import java.util.List;

import main.arithmetic.Dijkstra;
import main.arithmetic.SimUtils;
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
	private static Map map = new Map();
	public final List<Land> lands = new ArrayList<Land>();
	public final List<Obstacle> obstacles = new ArrayList<Obstacle>();
	public final List<Station> stations = new ArrayList<Station>();
	public final List<UAV> UAVs = new ArrayList<UAV>();
	public final List<Point> gridPoints = new ArrayList<Point>();
	public final List<LineSegment> gridLines = new ArrayList<LineSegment>();
	
	private CreateTrajectory createTrajectory;
	/**
	 * 安全距离
	 */
	public static double safetyDistance = SimUtils.SAFETYDISTANCE;

	private Map() {}
	public static Map getInstance(){
		return map;
	}

	/**
	 * 生成网格线及其对应的网格点
	 */
	public void createGrid() {
		clearGrid();
		for(Land land:lands) {
			System.out.println("-----------开始划分第" + Integer.toString(lands.indexOf(land)+1) +"快地-----------");
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
	 * 计算地图（Map）上两点的距离，如果两点之间没有障碍物，则它们的距离就是几何距离；
	 * 如果两点之间存在障碍物，则它们的距离要考虑到跨越障碍物的代价；
	 * @param point1
	 * 起点
	 * @param point2
	 * 终点
	 * @return
	 * 两点在Map上的距离
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
	 * 计算地图上两条线段的距离，如果两条线段之间没有障碍物，则它们的距离就是几何距离；
	 * 如果两条线段之间存在障碍物，则它们的距离要考虑到跨越障碍物的代价；
	 * @param point1
	 * @param point2
	 * @return
	 */
	public double DistanceOfTwoLineSegments(LineSegment ls1,LineSegment ls2) {
		return 0;
	}

	/**
	 * 判断连接两点的线段是否与障碍物相交。
	 * 此处的相交定义为线段的长度大于0的某一部分位于多边形内部。
	 * 若线段与多边形的交集只有一个顶点，
	 * 或线段与多边形的交集恰好是多边形的边界，则认为两者不相交。
	 * @param p1
	 * 起点
	 * @param p2
	 * 终点
	 * @param obstacles
	 * 障碍物
	 * @return
	 * 逻辑变量，若相交返回True
	 */
	public boolean isIntersectionWithObstacles(Point p1,Point p2,List<Obstacle> obstacles){
		LineSegment ls = new LineSegment(p1,p2);
		for(Polygon obstacle:obstacles) {
			LineSegment intersection= ls.intersectionLineSegmentOfLineSegmentAndPolygon(obstacle);
			//如果从i到j的线段与障碍物相交
			if(intersection!=null&&intersection.getMidPoint().positionToPolygon(obstacle)==SimUtils.INNER) { 
				return true;
			}
		}
		return false;
	}

	/**
	 * 清除网格线和网格点；
	 */
	public void clearGrid(){
		for(Land land:lands) {
			land.gridLines.clear();
			land.gridPoints.clear();
		}
	}

	/**
	 * 获取线段上某个点的母线段。
	 * @param point
	 * @return
	 */
	public LineSegment getMotherLine(Point point) {
		return point.motherLine;
	}

	/**
	 * 获取线段上某个点的兄弟节点，即与它处在同一条母线上的点。
	 * @param point
	 * @return
	 */
	public Point getBrotherPoint(Point point) {
		return point.motherLine.getBrotherPoint(point);
	}

	/**
	 * 按gridLines到某个点的距离大小对gridLines进行排序。
	 * @param point
	 * 参考点
	 * @return
	 * gridLines排序后的结果。
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
		str+= ">>>>>>共有" + lands.size() + "快Land:\t\n";
		for(Land land:lands) {
			str+=land.toString()+" \t\n";
		}
		str+= ">>>>>>共有" + obstacles.size() + "快Obstacle:\t\n";
		for(Obstacle obstacle:obstacles) {
			str+=obstacle.toString()+" \t\n";
		}

		str+= ">>>>>>共有" + stations.size() + "个Station:\t\n";
		for(Station station:stations) {
			str+=station.toString()+" \t\n";
		}
		str+="=======================END=======================\t\n";
		return str;
	}
	
	/**
	 * 清除某个实体（land、obstacle或station）。
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
	 * 增加某个实体（land、obstacle或station）。
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

package main.arithmetic.hull;

import java.util.ArrayList;
import java.util.List;

import main.data.SimUtils;
import main.entity.geometry.Point;
import main.entity.geometry.Polygon;

/**
 * 计算给定点集的凸包
 * */
public class ConvexHull implements Hull{
	private List<Point> points;
	private List<Point> hull = new ArrayList<Point>();;
	private static int MAX_ANGLE = 4;   
	private double currentMinAngle = 0;
	
	public Polygon createHull(List<Point> points) {
		this.points = SimUtils.shallowPointsCopy(points);
		int firstIndex = getFirstPointIndex(this.points);
		this.hull.clear();
		this.hull.add(this.points.get(firstIndex));//向list(hull)中添加第一个点
		currentMinAngle = 0;
		for (int i = nextIndex(firstIndex, this.points); i != firstIndex; i = nextIndex(i, this.points)) {
			this.hull.add(this.points.get(i));
		}//向list(hull)中添加其他的点，这些点将构成一个convex hull
		return new Polygon(hull);
	}
 
	public void remove(Point item)  {
 
		if (!hull.contains(item)) {
			points.remove(item);
			return;
		}
		points.remove(item);
		// TODO
		createHull(points);
	}
	
	public void remove(List<Point> items){
		points.removeAll(items);
		createHull(points);
	}
	
 
	public void add(Point item) {
		points.add(item);
 
		List<Point> tmplist = new ArrayList<Point>();
 
		tmplist.addAll(hull);
		tmplist.add(item);
 
		List<Point> tmphull = new ArrayList<Point>();
		int firstIndex = getFirstPointIndex(tmplist);
		tmphull.add(tmplist.get(firstIndex));
		currentMinAngle = 0;
		for (int i = nextIndex(firstIndex, tmplist); i != firstIndex; i = nextIndex(
				i, tmplist)) {
			tmphull.add(tmplist.get(i));
		}
 
		this.hull = tmphull;
	}
 
	public void add(List<Point> items) {
		points.addAll(items);
		List<Point> tmplist = new ArrayList<Point>();
 
		tmplist.addAll(hull);
		tmplist.addAll(items);
 
		List<Point> tmphull = new ArrayList<Point>();
		int firstIndex = getFirstPointIndex(tmplist);
		tmphull.add(tmplist.get(firstIndex));
		currentMinAngle = 0;
		for (int i = nextIndex(firstIndex, tmplist); i != firstIndex; i = nextIndex(
				i, tmplist)) {
			tmphull.add(tmplist.get(i));
		}
 
		this.hull = tmphull;
	}
 
	public List<Point> getHull() {
		return this.hull;
	}
 
	private int nextIndex(int currentIndex, List<Point> points) {
		double minAngle = MAX_ANGLE;
		double pseudoAngle;
		int minIndex = 0;
		for (int i = 0; i < points.size(); i++) {
			if (i != currentIndex) {
				pseudoAngle = getPseudoAngle(
						points.get(i).x - points.get(currentIndex).x,
						points.get(i).y - points.get(currentIndex).y);
				if (SimUtils.doubleEqual(pseudoAngle,minAngle)) {
					if ((Math.abs(points.get(i).x
							- points.get(currentIndex).x) > Math.abs(points
							.get(minIndex).x - points.get(currentIndex).x))
							|| (Math.abs(points.get(i).y
									- points.get(currentIndex).y) > Math
										.abs(points.get(minIndex).y
												- points.get(currentIndex).y))) {
						minIndex = i;
					}
				}else if (pseudoAngle >= currentMinAngle && pseudoAngle < minAngle) {
					minAngle = pseudoAngle;
					minIndex = i;
				}
			}
		}
		currentMinAngle = minAngle;
		return minIndex;
	}
	
	//获得起始点
	private int getFirstPointIndex(List<Point> points) {
		int minIndex = 0;
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).y < points.get(minIndex).y) {
				minIndex = i;
			} else if ((points.get(i).y == points.get(minIndex).y)
					&& (points.get(i).x < points.get(minIndex).x)) {
				minIndex = i;
			}
		}
		return minIndex;
	}
 
	private double getPseudoAngle(double dx, double dy) {
		if (dx > 0 && dy >= 0)
			return dy / (dx + dy);
		if (dx <= 0 && dy > 0)
			return 1 + (Math.abs(dx) / (Math.abs(dx) + dy));
		if (dx < 0 && dy <= 0)
			return 2 + (dy / (dx + dy));
		if (dx >= 0 && dy < 0)
			return 3 + (dx / (dx + Math.abs(dy)));
		throw new Error("Impossible");
	}
	
	public static void main(String[] args){
        Point[] A = new Point[9];
        A[0] = new Point(1,0);
        A[1] = new Point(1,1);
        A[2] = new Point(0,1);
        A[3] = new Point(-1,1);
        A[4] = new Point(-1,0);
        A[5] = new Point(-1,-1);
        A[6] = new Point(0,-1);
        A[7] = new Point(1,-1);
        A[8] = new Point(0.5,0.5);

        List<Point> points = new ArrayList<Point>();
        for(int i = 0;i < A.length;i++)
            points.add(A[i]);
        
        System.out.println("集合A中满足凸包的点集为：");
        ConvexHull jm = new ConvexHull();
        for(Point point:jm.createHull(points).vertexes) {
        	System.out.println(point);
        }
        
        System.out.println("************");
        Polygon polygon = new ConcaveHull().createHull(points);
        //polygon.enlarge(0.01);
        for(int i = 0;i < polygon.vertexes.size();i++)
        	System.out.println(polygon.vertexes.get(i));
    }
}

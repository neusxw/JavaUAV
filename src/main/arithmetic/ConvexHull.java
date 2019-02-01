package main.arithmetic;

import java.util.ArrayList;
import java.util.List;

import main.arithmetic.data.SimUtils;
import main.entity.geometry.Point;

public class ConvexHull <T extends Point>{
	private List<T> points;
	private List<T> hull;
	private static int MAX_ANGLE = 4;   
	private double currentMinAngle = 0;
 
	public ConvexHull(List<T> points)  {
		this.points = points;
		this.hull = new ArrayList<T>();
 
		this.calculate();
	}
	
	public ConvexHull(T[] points)  {
		this.hull = new ArrayList<T>();
		this.points = new ArrayList<T>();
		for(int i=0;i<points.length;i++) {
			this.points.add(points[i]);
		}
		this.calculate();
	}
	
	private void calculate()  {
		int firstIndex = getFirstPointIndex(this.points);
		this.hull.clear();
		this.hull.add(this.points.get(firstIndex));//向list(hull)中添加第一个点
		currentMinAngle = 0;
		for (int i = nextIndex(firstIndex, this.points); i != firstIndex; i = nextIndex(
				i, this.points)) {
			this.hull.add(this.points.get(i));
		}//向list(hull)中添加其他的点，这些点将构成一个convex hull
	}
 
	public void remove(T item)  {
 
		if (!hull.contains(item)) {
			points.remove(item);
			return;
		}
		points.remove(item);
		// TODO
		calculate();
	}
	
	public void remove(List<T> items){
		points.removeAll(items);
		calculate();
	}
	
 
	public void add(T item) {
		points.add(item);
 
		List<T> tmplist = new ArrayList<T>();
 
		tmplist.addAll(hull);
		tmplist.add(item);
 
		List<T> tmphull = new ArrayList<T>();
		int firstIndex = getFirstPointIndex(tmplist);
		tmphull.add(tmplist.get(firstIndex));
		currentMinAngle = 0;
		for (int i = nextIndex(firstIndex, tmplist); i != firstIndex; i = nextIndex(
				i, tmplist)) {
			tmphull.add(tmplist.get(i));
		}
 
		this.hull = tmphull;
	}
 
	public void add(List<T> items) {
		points.addAll(items);
		List<T> tmplist = new ArrayList<T>();
 
		tmplist.addAll(hull);
		tmplist.addAll(items);
 
		List<T> tmphull = new ArrayList<T>();
		int firstIndex = getFirstPointIndex(tmplist);
		tmphull.add(tmplist.get(firstIndex));
		currentMinAngle = 0;
		for (int i = nextIndex(firstIndex, tmplist); i != firstIndex; i = nextIndex(
				i, tmplist)) {
			tmphull.add(tmplist.get(i));
		}
 
		this.hull = tmphull;
	}
 
	public List<T> getHull() {
		return this.hull;
	}
 
	private int nextIndex(int currentIndex, List<T> points) {
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
	private int getFirstPointIndex(List<T> points) {
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
}

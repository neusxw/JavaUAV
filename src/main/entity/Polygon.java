package main.entity;

import java.util.ArrayList;
import java.util.List;

import main.arithmetic.SimUtils;

public class Polygon {
	public List<Point> vertexes= new ArrayList<Point>();
	public List<LineSegment> edges = new ArrayList<LineSegment>();

	public Polygon() {
		
	}
	public Polygon(double[] x, double[] y) {
		createPolygonFromArray(x, y);
	}
	
	public void createPolygonFromArray(double[] x, double[] y) {
		if(x.length!=y.length) {
			System.out.println("横纵坐标长度不相等！");
			return;
		}
		for(int i=0; i< x.length; i++) {
			vertexes.add(new Point(x[i],y[i]));
		}
		for(int i=0;i<x.length;i++) {
			edges.add(new LineSegment(vertexes.get(i),vertexes.get((i+1)%x.length)));
		}
	}
	
	public boolean isContainsPolygon(Polygon polygon) {
		for (Point point:polygon.vertexes) {
			if (point.positionToPolygon(this)==SimUtils.OUTTER) {
				return false;
			}
		}
		return true;
	};
	
	public void print() {
		System.out.println(this.toString());
	}
	public String toString() {
		String str=this.getClass().toString()+":";
		for(Point point:vertexes) {
			str+=point.toString()+" | ";
		}
		return str;
	}
	
}

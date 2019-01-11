package main.entity;

import java.util.ArrayList;
import java.util.List;
import main.arithmetic.SimUtils;

public class Polygon {
	public List<Point> vertexes= new ArrayList<Point>();
	public List<LineSegment> edges = new ArrayList<LineSegment>();

	public Polygon(double[][] coord) {
		if(coord.length!=2) {
			System.out.println("维数应为2，维数错误！");
		} 
		createPolygonFromArray(coord[0], coord[1]);
	}
	public Polygon(double[] x, double[] y) {
		createPolygonFromArray(x, y);
	}
	
	public void createPolygonFromArray(double[] x, double[] y) {
		if(x.length!=y.length) {
			System.out.println("横纵坐标长度不相等！");
			return;
		}
		if(x.length==0) {
			System.out.println("横纵坐标长度不能为0！");
			return;
		}
		if(x.length==1) {
			double p = Map.safetyDistance;
			double[] coordX = new double[] {x[0]+p,x[0]-p,x[0]-p,x[0]+p};
			double[] coordY = new double[] {y[0]+p,y[0]+p,y[0]-p,y[0]-p};
			addVertexAndEdge(coordX,coordY);
			return;
		}
		if(x.length==2) {
			double p = Map.safetyDistance;
			double[] coordX;
			double[] coordY;
			if (x[0]>x[1]) {
				coordX=new double[] {x[0]+p,x[1]-p,x[1]-p,x[0]+p};
				coordY=new double[] {y[0]+p,y[1]+p,y[1]-p,y[0]-p};
			}else {
				coordX=new double[] {x[1]+p,x[0]-p,x[0]-p,x[1]+p};
				coordY=new double[] {y[1]+p,y[0]+p,y[0]-p,y[1]-p};
			}
			addVertexAndEdge(coordX,coordY);
			return;
		}
		addVertexAndEdge(x,y);
	}
	
	public void addVertexAndEdge(double[] x, double[] y){
		for(int i=0; i< x.length; i++) {
			vertexes.add(new Point(x[i],y[i]));
		}
		for(int i=0;i<x.length;i++) {
			edges.add(new LineSegment(vertexes.get(i),vertexes.get((i+1)%x.length)));
		}
	}
	
	public void removeVertex(Point point){
		int index = vertexes.indexOf(point);
		vertexes.remove(index);
		edges.clear();
		for(int i=0;i<vertexes.size();i++) {
			edges.add(new LineSegment(vertexes.get(i),vertexes.get((i+1)%vertexes.size())));
		}
	}
	
	public Point getBarycenter(){
		double x=0;
		double y=0;
		for(Point point:vertexes) {
			x+=point.x;
			y+=point.y;
		}
		x/=vertexes.size();
		y/=vertexes.size();
		return new Point(x,y);
	}
	
	public LineSegment getLongestEdge() {
		double max = 0;
		LineSegment ls = null;
		for(LineSegment edge:edges) {
			if(edge.length>max) {
				max=edge.length;
				ls=edge;
			}
		}
		return ls;
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
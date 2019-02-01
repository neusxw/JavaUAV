package main.entity.geometry;

import java.util.ArrayList;
import java.util.List;

import main.arithmetic.data.SimUtils;
import main.entity.Map;

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
	
	public Polygon(List<Point> points) {
		double[] x = new double[points.size()];
		double[] y = new double[points.size()];
		for(int i = 0; i<points.size();i++) {
			x[i]=points.get(i).x;
			y[i]=points.get(i).y;
		}
		createPolygonFromArray(x, y);
	}
	
	public Polygon(Point[] points) {
		double[] x = new double[points.length];
		double[] y = new double[points.length];
		for(int i = 0; i<points.length;i++) {
			x[i]=points[i].x;
			y[i]=points[i].y;
		}
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
	
	//暂时只实现了对只有一个凹点的剖分；
	public List<Triangle> triangularization(){
		List<Triangle> triList = new ArrayList<Triangle>();
		List<Point> concavePoints = findConcavePoints();
		Point first;
		Point second;
		Point third;
		int index;
		if(concavePoints.size()==0) {
			first = vertexes.get(0);
			second = vertexes.get(1);
			index = 1;
			
		}else {
			first=concavePoints.get(0);
			index = vertexes.indexOf(first);
			second = vertexes.get((++index)%vertexes.size());
		}
		for(int i = 0;i<vertexes.size()-2;i++) {
			third = vertexes.get((++index)%vertexes.size());
			Triangle tri = new Triangle(first,second,third);
			triList.add(tri);
			second=third;
		}
		return triList;
	}
	
	public List<Point> findConcavePoints(){
		List<Point> concavePoints = new ArrayList<Point>();
		for(int i = 0; i<edges.size();i++) {
			LineSegment ls1 = edges.get(i%edges.size());
			LineSegment ls2 = edges.get((i+1)%edges.size());
			double deltaA;
			if(ls1.directionAngle<0&&ls2.directionAngle>0) {
				deltaA = ls1.directionAngle-ls2.directionAngle+Math.PI*2;
			}else {
				deltaA = ls1.directionAngle-ls2.directionAngle;
			}
			if (deltaA<0) {
				concavePoints.add(vertexes.get((i+1)%edges.size()));
			}
		}
		return concavePoints;
	}
	
	public boolean isContainsPolygon(Polygon polygon) {
		for (Point point:polygon.vertexes) {
			if (point.positionToPolygon(this)==SimUtils.OUTTER) {
				return false;
			}
		}
		return true;
	};
	
	public Polygon enlarge(double d) {
		Point center = MultiPoint.barycenter(this.vertexes);
		for(int i=0;i<vertexes.size();i++) {
			double angle=Math.atan2(vertexes.get(i).y-center.y,vertexes.get(i).x-center.x);
			vertexes.get(i).x+=d*Math.cos(angle);
			vertexes.get(i).y+=d*Math.sin(angle);
		}
		return this;
	}
	
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
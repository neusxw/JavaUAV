package main.matter;

import java.util.ArrayList;
import java.util.List;

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
			System.out.println("�������곤�Ȳ���ȣ�");
			return;
		}
		for(int i=0; i< x.length; i++) {
			vertexes.add(new Point(x[i],y[i]));
		}
		for(int i=0;i<x.length -1;i++) {
			edges.add(new LineSegment(vertexes.get(i),vertexes.get(i+1)));
		}
		edges.add(new LineSegment(vertexes.get(x.length-1),vertexes.get(0)));
	}
	
	/*
	 * �ж�һ�����Ƿ��ڶ������
	 */
	public boolean containsPoint(Point point) {
	    int verticesCount = vertexes.size();
	    int nCross = 0;
	    for (int i = 0; i < verticesCount; ++ i) {
	        Point p1 = vertexes.get(i);
	        Point p2 = vertexes.get((i + 1) % verticesCount);
	        // ��� y=p.y �� p1 p2 �Ľ���
	        if ( p1.y == p2.y ) {   // p1p2 �� y=p0.yƽ��
	            continue;
	        }
	        if ( point.y < Math.min(p1.y, p2.y) ) { // ������p1p2�ӳ����� 
	            continue;
	        }
	        if ( point.y >= Math.max(p1.y, p2.y) ) { // ������p1p2�ӳ����� 
	            continue;
	        }
	        // �󽻵�� X ����
	        double x = (point.y - p1.y) * (p2.x - p1.x) / (p2.y - p1.y) + p1.x;
	        if ( x > point.x ) { // ֻͳ�Ƶ��߽���
	            nCross++;
	        }
	    }
	    // ���߽���Ϊż�������ڶ����֮��
	    return (nCross%2==1);
	}
	
	/*
	 * ���ֱ����һ������εĽ��ߣ���ֱ��λ�ڶ�����е��ǲ��ֹ��ɵ��߶�
	 */
	public LineSegment getLineSegmentWithinPolygon(Line line) {
		List<Point> crossPoints = new ArrayList<Point>();
		for(LineSegment ls:edges) {
			Point cross = ls.intersectionLineSegmentAndLine(line);
			
			if(cross!=null) {
				crossPoints.add(cross);
			}
		}
		if(crossPoints.size()!=2) {
			return null;
		}else {
			System.out.println(crossPoints.get(0).toString()+crossPoints.get(1));
			return new LineSegment(crossPoints.get(0),crossPoints.get(1));
		}
	}
	
	/*
	 * ����߶���һ������εĽ��ߣ����߶�λ�ڶ�����е��ǲ��ֹ��ɵ��߶�
	 */

	public LineSegment getLineSegmentWithinPolygon(LineSegment lineSegment) {
		List<Point> crossPoints = new ArrayList<Point>();
		for(LineSegment ls:edges) {
			Point cross = ls.intersectionLineSegmentAndLine(lineSegment);
			if(cross!=null) {
				crossPoints.add(cross);
			}
		}
		if(crossPoints.size()!=2) {
			return null;
		}else {
			System.out.println(crossPoints.get(0).toString()+crossPoints.get(1));
			return new LineSegment(crossPoints.get(0),crossPoints.get(1));
		}
	}
}

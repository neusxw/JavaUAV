package main.matter;

import java.util.ArrayList;
import java.util.List;

public class Boundary {
public List<Point> BoundaryPoints= new ArrayList<Point>();
	
	public void addPoint(Point p) {
		BoundaryPoints.add(p);
	}
	
	public void createBoundaryFromArray(double[] x, double[] y) {
		if(x.length!=y.length) {
			System.out.println("横纵坐标长度不相等！");
			return;
		}
		for(int i=0; i< x.length; i++) {
			BoundaryPoints.add(new Point(x[i],y[i]));
		}
	}
}

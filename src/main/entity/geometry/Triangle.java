package main.entity.geometry;

public class Triangle extends Polygon {

	public Triangle(double[] x, double[] y) {
		super(x, y);
		if(x.length!=y.length||x.length!=3) {
			System.out.println("三角形必须有且仅有三个点！");
			return;
		}
	}

	public Triangle(Point A,Point B,Point C) {
		super(new Point[] {A,B,C});
	}
}

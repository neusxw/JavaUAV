package entity;

import java.util.List;

import entity.geometry.Point;
import entity.geometry.Polygon;
import entity.geometry.Triangle;

public class Obstacle extends Polygon{
	
	public Obstacle(Polygon polygon){
		super(polygon.vertexes);
		this.enlarge(2);
		Map.getInstance().obstacles.add(this);
	}
	
	public Obstacle(double[][] coord){
		super(coord);
		Map.getInstance().obstacles.add(this);
	}
	
	public Obstacle(double x[],double y[]){
		super(x,y);
		Map.getInstance().obstacles.add(this);
	}

	public void triDecompose() {
		List<Triangle> tris = triangularization();
		for(Triangle tri:tris) {
			new Obstacle(tri);
		}
		Map.getInstance().remove(this);
	}
	
	public String toString(boolean highPrecision) {
		String str="Obstacle: ";
		for(Point point:vertexes) {
			str+=point.toString(highPrecision)+" | ";
		}
		return str;
	}
}

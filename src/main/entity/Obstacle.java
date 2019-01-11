package main.entity;

public class Obstacle extends Polygon{
	
	public Obstacle(double[][] coord){
		super(coord);
		Map.getInstance().obstacles.add(this);
	}
	
	public Obstacle(double x[],double y[]){
		super(x,y);
		Map.getInstance().obstacles.add(this);
	}

	public String toString() {
		String str="Obstacle: ";
		for(Point point:vertexes) {
			str+=point.toString()+" | ";
		}
		return str;
	}
}

package main.entity;

public class Obstacle extends Polygon{
	
	public Obstacle(){
		Map.getInstance().addObstacle(this);
	}
	
	public Obstacle(double x[],double y[]){
		super(x,y);
		Map.getInstance().addObstacle(this);
	}

	public String toString() {
		String str="Obstacle: ";
		for(Point point:vertexes) {
			str+=point.toString()+" | ";
		}
		return str;
	}
}

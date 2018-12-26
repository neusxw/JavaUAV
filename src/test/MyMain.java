package test;

import main.matter.*;

public class MyMain {
	
	public static void main(String[] args) {
		Land land1 = new Land();
		double[] xv1 = {0.2,0.6,0.6,0.1};
		double[] yv1 = {0.7,0.75,0.3,0.4};
		land1.createPolygonFromArray(xv1,yv1);
		land1.setRidgeDirection(Math.PI/4);
		land1.createGrid();
		Land land2 = new Land();
		double[] xv2 = {0.2,0.6,0.6,0.1};
		double[] yv2 = {0.7,0.75,0.3,0.4};
		land2.createPolygonFromArray(xv2,yv2);
		
		Barrier barrier1 = new Barrier();
		double[] xv3 = {0.4,0.5,0.5,0.4};
		double[] yv3 = {0.5,0.5,0.4,0.4};
		barrier1.createPolygonFromArray(xv3,yv3);
		
	}
}

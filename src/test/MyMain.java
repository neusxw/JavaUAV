package test;

import java.util.List;

import main.arithmetic.DataExport;
import main.matter.*;

public class MyMain {
	
	public static void main(String[] args) {
		Land land1 = new Land(new double[] {0.2,0.6,0.7,0.1},new double[] {0.7,0.75,0.3,0.4});
		land1.setRidgeDirection(Math.PI/4);
		land1.createGrid();
		DataExport dataExport = new DataExport(true);
		dataExport.landOutput(land1);
		
		Land land2 = new Land(new double[] {-0.9,-0.7,-0.3,-0.4,-0.8},new double[] {0.9,0.9,0.7,0.5,0.6});
		land2.setRidgeDirection(-Math.PI/4);
		land2.createGrid();
		dataExport.landOutput(land2);
		
		Obstacle barrier1 = new Obstacle(new double[] {0.4,0.5,0.5,0.4},new double[] {0.5,0.5,0.4,0.4});
		Obstacle barrier2 = new Obstacle(new double[] {0.2,0.3,0.35,0.2},new double[] {0.6,0.6,0.5,0.5});
		Obstacle barrier3 = new Obstacle(new double[] {-0.6,-0.4,-0.4,-0.6},new double[] {0.3,0.3,0.1,0.1});
		Obstacle barrier4 = new Obstacle(new double[] {-0.7,-0.6,-0.6,-0.7},new double[] {0.8,0.8,0.6,0.6});
				
		System.out.println(Map.getInstance().toString());
	}
}

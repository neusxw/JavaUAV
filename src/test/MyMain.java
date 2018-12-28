package test;

import java.util.List;

import main.arithmetic.DataExport;
import main.arithmetic.SimUtils;
import main.matter.*;

public class MyMain {
	
	public static void main(String[] args) {
		DataExport dataExport = new DataExport(true);
		
		Land land1 = new Land(new double[] {0.2,0.6,0.7,0.1},new double[] {0.7,0.75,0.3,0.4});
		Land land2 = new Land(new double[] {-0.9,-0.7,-0.3,-0.4,-0.8},new double[] {0.9,0.9,0.7,0.5,0.6},-Math.PI/4);
		
		Obstacle barrier1 = new Obstacle(new double[] {0.4,0.5,0.5,0.4},new double[] {0.5,0.5,0.4,0.4});
		Obstacle barrier2 = new Obstacle(new double[] {0.2,0.3,0.35,0.2},new double[] {0.6,0.6,0.5,0.5});
		Obstacle barrier3 = new Obstacle(new double[] {-0.6,-0.4,-0.4,-0.6},new double[] {0.3,0.3,0.1,0.1});
		Obstacle barrier4 = new Obstacle(new double[] {-0.7,-0.6,-0.6,-0.7},new double[] {0.8,0.8,0.6,0.6});
		Map.getInstance().createGridLines();
		dataExport.gridLinesOutput(Map.getInstance().gridLines);
		
		UAV UAV1= new UAV(SimUtils.Origin,Map.getInstance());
		
		
	}
}

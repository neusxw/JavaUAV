package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import main.arithmetic.DataExport;
import main.arithmetic.Dijkstra;
import main.arithmetic.SimUtils;
import main.entity.*;

public class MyMain {
    
	public static void main(String[] args){
		
		DataExport dataExport = new DataExport();
		Land land1 = new Land(new double[] {0.2,0.6,0.7,0.1},new double[] {0.7,0.75,0.3,0.4});
		Land land2 = new Land(new double[] {-0.9,-0.7,-0.3,-0.4,-0.8},new double[] {0.9,0.9,0.7,0.5,0.6},-Math.PI/4);
		
		Obstacle barrier1 = new Obstacle(new double[] {0.4,0.5,0.5,0.4},new double[] {0.5,0.5,0.4,0.4});
		Obstacle barrier2 = new Obstacle(new double[] {0.2,0.3,0.35,0.2},new double[] {0.6,0.6,0.5,0.5});
		Obstacle barrier3 = new Obstacle(new double[] {-0.6,-0.4,-0.4,-0.6},new double[] {0.3,0.3,0.1,0.1});
		Obstacle barrier4 = new Obstacle(new double[] {-0.7,-0.6,-0.6,-0.7},new double[] {0.8,0.8,0.6,0.6});
		
		Map.getInstance().print();
		Map.getInstance().createGrid();
		dataExport.linesOutput(Map.getInstance().gridLines);
		
		Station station = new Station(new double[] {-0.1,0.1,0.1,-0.1},new double[] {0.05,0.05,0,0});
		station.addTakeOffPoint(new TakeOffPoint(station,-0.05,0.025));
		station.addTakeOffPoint(new TakeOffPoint(station,0.05,0.025));
		UAV UAV1= new UAV(station);
		UAV1.creatTrajectory();
		dataExport.pointsOutput(UAV1.trajectory);
		Dijkstra dj=new Dijkstra(new Point(-1,1),UAV1.getTakeOffPoint(),Map.getInstance().obstacles);
		dj.getShortestPath();
		dj.printPath();
		System.out.println(Map.getInstance().detourDistanceOfTwoPoints(new Point(-0.05,0.025),new Point(0.125,0.4)));
		System.out.println(Map.getInstance().detourDistanceOfTwoPoints(new Point(-0.05,0.025),new Point(0.375,0.354)));
		
	}
}

package main.arithmetic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import main.entity.Land;
import main.entity.Map;
import main.entity.Obstacle;
import main.entity.Station;
import main.entity.UAV;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;

public class DataExport {
	public File file;

	public void mapOutput() {
		file = new File("output\\map.txt");
		file.delete();
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file,true);
			String str;
			for(Land land : Map.getInstance().lands) {
				str= "land" +Integer.toString(Map.getInstance().lands.indexOf(land)+1)+":";
				writer.write(str + "\r\n");
				for(Point point:land.vertexes) {
					str = point.x +" "+ point.y;
					writer.write(str + "\r\n");
				}
			}
			for(Obstacle obstacle : Map.getInstance().obstacles) {
				str= "obstacle" +Integer.toString(Map.getInstance().obstacles.indexOf(obstacle)+1)+":";
				writer.write(str + "\r\n");
				for(Point point:obstacle.vertexes) {
					str = point.x +" "+ point.y;
					writer.write(str + "\r\n");
				}
			}

			for(Station station : Map.getInstance().stations) {
				str= "station" +Integer.toString(Map.getInstance().stations.indexOf(station)+1)+":";
				writer.write(str + "\r\n");
				for(Point point:station.vertexes) {
					str = point.x +" "+ point.y;
					writer.write(str + "\r\n");
				}
			}

			writer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void linesOutput(List<LineSegment> gridLines) {
		file = new File("output\\linesOut.txt");
		file.delete();
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file,true);
			String str;
			for(LineSegment line : gridLines) {
				str = line.endPoint1.x+" "+line.endPoint1.y+" "+line.endPoint2.x+" "+line.endPoint2.y;
				writer.write(str + "\r\n");
			}
			writer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public  void pointsOutput(List<? extends Point> points) {
		file = new File("output\\pointsOut.txt");
		pointsOutput(file,points);
	}

	public  void pointsOutput(File file,List<? extends Point> points) {
		file.delete();
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file,true);
			String str;
			for(Point point : points) {
				str = point.x + " " + point.y;
				writer.write(str + "\r\n");
			}
			writer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public  void takeOffPointsOutput() {
		file = new File("output\\takeOffPointsOut.txt");
		List<Point> takeOffPoints = new ArrayList<Point>();
		for(Station station : Map.getInstance().stations) {
			takeOffPoints.addAll(station.takeOffPoints);
		}
		pointsOutput(file,takeOffPoints);
	}

	public  void trajectoryOutput() {
		file = new File("output\\trajectoryOut.txt");
		List<Point> trajectoryPoints = new ArrayList<Point>();
		for(UAV aUAV : Map.getInstance().UAVs) {
			trajectoryPoints.addAll(aUAV.trajectory);
		}
		pointsOutput(file,trajectoryPoints);
	}

	public  void trajectoryOutputForGeography() {
		CoordinateTransformation cf= new CoordinateTransformation();
		file = new File("output\\trajectoryOutForGeography.txt");
		List<Point> trajectoryPoints = new ArrayList<Point>();
		for(UAV aUAV : Map.getInstance().UAVs) {
			for(Point point:aUAV.trajectory) {
				trajectoryPoints.add(new Point(cf.coordinate2Geography(point)));
			}
		}
		pointsOutput(file,trajectoryPoints);
	}

	public static void changeOutPosition(){
		try {
			PrintStream ps = new PrintStream("d:\\out.txt");
			System.setOut(ps);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}

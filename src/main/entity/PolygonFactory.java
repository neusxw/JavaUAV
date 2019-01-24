package main.entity;

import java.util.Arrays;
import java.util.List;

import main.arithmetic.data.CoordinateTransformation;
import main.arithmetic.data.LandInfo;
import main.arithmetic.data.MapInfo;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;
import main.entity.geometry.Polygon;

public class PolygonFactory{

	public static Polygon createPolygon(MapInfo info,boolean isGeography) {
		String type = info.getType();
		List<double[]> coordList = info.getData();
		double[] x = new double[coordList.size()];
		double[] y = new double[coordList.size()];
		for(int i=0;i<coordList.size();i++) {
			x[i]=coordList.get(i)[0];
			y[i]=coordList.get(i)[1];
		}
		if(isGeography) {
			double[][] coords = CoordinateTransformation.geography2Coordinate(x,y);
			x=coords[0];
			y=coords[1];
		}
		switch(type) {
		case "land" :{
			Land land= new Land(x,y);
			try {
				land.setRidgeWideth(((LandInfo)info).getRidgeWideth());
				List<double[]> ridgeDirection= ((LandInfo)info).getRidgeDirection();
				//for(double[] xy:ridgeDirection) {for(double t:xy) {System.out.print(t+"	");}System.out.println();}
				if(ridgeDirection.size()==1) {
					land.setRidgeDirection(ridgeDirection.get(0)[0]);
				}else {
					Point point1 = new Point(CoordinateTransformation.geography2Coordinate(new Point(ridgeDirection.get(0))));
					Point point2 = new Point(CoordinateTransformation.geography2Coordinate(new Point(ridgeDirection.get(1))));
					land.setRidgeDirection(new LineSegment(point1,point2).directionAngle*CoordinateTransformation.RADIAN2ANGLE);
				}
				
			}catch(Exception e){
				//e.printStackTrace();
			}
			return land;}
		case "obstacle":{
			return new Obstacle(x,y);}
		case "station":{
			return new Station(x,y);}
		}
		return null;
	}
}

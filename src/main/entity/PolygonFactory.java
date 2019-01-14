package main.entity;

import java.util.Arrays;
import java.util.List;

import main.arithmetic.CoordinateTransformation;
import main.arithmetic.MapInfo;
import main.entity.geometry.Polygon;

public class PolygonFactory{
	
	public static Polygon createPolygon(MapInfo info,boolean isGeography) {
		String type = info.getType();
		List<double[]> coordList = info.getData();
		CoordinateTransformation ct = new CoordinateTransformation(coordList.get(0));
		double[] x = new double[coordList.size()];
		double[] y = new double[coordList.size()];
		for(int i=0;i<coordList.size();i++) {
			x[i]=coordList.get(i)[0];
			y[i]=coordList.get(i)[1];
		}
		if(isGeography) {
			double[][] coords = ct.geography2Coordinate(x,y);
			x=coords[0];
			y=coords[1];
		}
		switch(type) {
		case "land" :{
			return new Land(x,y);}
		case "obstacle":{
			return new Obstacle(x,y);}
		case "station":{
			return new Station(x,y);}
		}
		return null;
	}
}

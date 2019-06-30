package entity;

import java.util.Arrays;
import java.util.List;

import data.CoordTrans;
import data.LandInfo;
import data.MapInfo;
import entity.geometry.LineSegment;
import entity.geometry.Point;
import entity.geometry.Polygon;

public class PolygonFactory{

	static int landID = 1;
	public static Polygon createPolygon(MapInfo info,boolean isGeography) {
		String type = info.getType();
		List<double[]> coordList = info.getData();
		double[] x = new double[coordList.size()];
		double[] y = new double[coordList.size()];
		double[] z = new double[coordList.size()];
		for(int i=0;i<coordList.size();i++) {
			x[i]=coordList.get(i)[0];
			y[i]=coordList.get(i)[1];
		}
		if(isGeography) {
			double[][] coords = CoordTrans.geo2Coord(x,y,z);
			x=coords[0];
			y=coords[1];
		}
		switch(type) {
		case "land" :{
			Land land= new Land(x,y);
			land.setID(landID++);
			try {
				LandInfo landInfo = (LandInfo)info;
				land.setRidgeWideth(landInfo.getRidgeWideth());
				List<double[]> ridgeDirection= ((LandInfo)info).getRidgeDirection();
				//for(double[] xy:ridgeDirection) {for(double t:xy) {System.out.print(t+"	");}System.out.println();}
				if(ridgeDirection.size()==1) {
					land.setRidgeDirection(ridgeDirection.get(0)[0]);
				}else {
					Point point1 = new Point(CoordTrans.geo2Coord(new Point(ridgeDirection.get(0))));
					Point point2 = new Point(CoordTrans.geo2Coord(new Point(ridgeDirection.get(1))));
					land.setRidgeDirection(new LineSegment(point1,point2).directionAngle*CoordTrans.RADIAN2ANGLE);
				}
				land.setRidgeWideth(landInfo.getRidgeWideth());
				land.setHeight(landInfo.getHeight());
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

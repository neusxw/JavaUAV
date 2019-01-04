package main.arithmetic;

import main.entity.Point;

public class CoordinateTransformation {
	public static final double RADIAN2ANGLE = 180/Math.PI;
	public static final double ANGLE2RADIAN = Math.PI/180;
	public double[] geographyOrigin =new double[2];
	public double smallRadius;
	public CoordinateTransformation(double longitude,double latitude){
		geographyOrigin[0]=longitude;
		geographyOrigin[1]=latitude;
		smallRadius=SimUtils.RADIUSofEARTH*Math.cos(latitude*ANGLE2RADIAN);
	}
	public double[] geography2Coordinate(double longitude, double latitude) {
		double deltaLongitude=longitude-geographyOrigin[0];
		double deltaLatitude=latitude-geographyOrigin[1];
		
		double x = smallRadius*deltaLongitude*ANGLE2RADIAN;
		double y = SimUtils.RADIUSofEARTH*deltaLatitude*ANGLE2RADIAN;
		return new double[] {x,y};
	}
	
	public double[][] geography2Coordinate(double longitude[], double latitude[]) {
		if(longitude.length!=latitude.length) {
			System.out.println("经纬度坐标数组长度不同！");
			return null;
		}
		double[][] coord = new double[2][longitude.length];
		for(int i=0;i<longitude.length;i++) {
			double[] coordI = geography2Coordinate(longitude[i],latitude[i]);
			coord[0][i]=coordI[0];
			coord[1][i]=coordI[1];
		}
		return coord;
	}
	
	public double[] geography2Coordinate(Point point) {
		double longitude = point.x;
		double latitude = point.y;
		return geography2Coordinate(longitude,latitude);
	}
	
	public double[] coordinate2Geography(double x, double y) {
		double deltaLongitude= x/smallRadius*RADIAN2ANGLE;
		double deltaLatitude=y/SimUtils.RADIUSofEARTH*RADIAN2ANGLE;
		double Longitude = geographyOrigin[0] + deltaLongitude;
		double Latitude = geographyOrigin[1] + deltaLatitude;
		return new double[] {Longitude,Latitude};
	}
	
	public double[] coordinate2Geography(Point point) {
		double x = point.x;
		double y = point.y;
		return coordinate2Geography(x,y);
	}
	
	public double[][] coordinate2Geography(double x[], double y[]) {
		if(x.length!= y.length) {
			System.out.println("横纵坐标数组长度不同！");
			return null;
		}
		double[][] geography = new double[2][x.length];
		for(int i=0;i<x.length;i++) {
			double[] geographyI = coordinate2Geography(x[i],y[i]);
			geography[0][i]=geographyI[0];
			geography[1][i]=geographyI[1];
		}
		return geography;
	}
}

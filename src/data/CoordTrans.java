package data;

import entity.geometry.Point;

public class CoordTrans {
	public static final double RADIAN2ANGLE = 180/Math.PI;
	public static final double ANGLE2RADIAN = Math.PI/180;
	private static final double[] GeographyOrigin =new double[2];
	private static boolean isOriginSet = false;
	public static double smallRadius;
	
	public CoordTrans(){}
	
	public CoordTrans(double[] geographyOrigin){
		if(isOriginSet == false) {
			GeographyOrigin[0]=geographyOrigin[0];
			GeographyOrigin[1]=geographyOrigin[1];
			smallRadius=SimUtils.RADIUSofEARTH*Math.cos(geographyOrigin[1]*ANGLE2RADIAN);
			isOriginSet = true;
		}
	}
	
	public CoordTrans(double longitude,double latitude){
		this(new double[] {longitude,latitude});
	}
	
	public static double[] geo2Coord(double longitude, double latitude, double height) {
		double deltaLongitude=longitude-GeographyOrigin[0];
		double deltaLatitude=latitude-GeographyOrigin[1];
		
		double x = smallRadius*deltaLongitude*ANGLE2RADIAN;
		double y = SimUtils.RADIUSofEARTH*deltaLatitude*ANGLE2RADIAN;
		return new double[] {x,y,height};
	}
	
	public static double[][] geo2Coord(double longitude[], double latitude[],double height[]) {
		if(longitude.length!=latitude.length) {
			System.out.println("经纬度坐标数组长度不同！");
			return null;
		}
		double[][] coord = new double[2][longitude.length];
		for(int i=0;i<longitude.length;i++) {
			double[] coordI = geo2Coord(longitude[i],latitude[i],height[i]);
			coord[0][i]=coordI[0];
			coord[1][i]=coordI[1];
		}
		return coord;
	}
	
	public static double[] geo2Coord(Point point) {
		double longitude = point.x;
		double latitude = point.y;
		double height = point.z;
		return geo2Coord(longitude,latitude,height);
	}
	
	public static double[] coord2Geo(double x, double y,double z) {
		double deltaLongitude= x/smallRadius*RADIAN2ANGLE;
		double deltaLatitude=y/SimUtils.RADIUSofEARTH*RADIAN2ANGLE;
		double Longitude = GeographyOrigin[0] + deltaLongitude;
		double Latitude = GeographyOrigin[1] + deltaLatitude;
		return new double[] {Longitude,Latitude,z};
	}
	
	public static double[] coord2Geo(Point point) {
		double x = point.x;
		double y = point.y;
		double z = point.z;
		return coord2Geo(x,y,z);
	}
	
	public static double[][] coord2Geo(double x[], double y[], double z[]) {
		if(x.length!= y.length) {
			System.out.println("横纵坐标数组长度不同！");
			return null;
		}
		double[][] geography = new double[2][x.length];
		for(int i=0;i<x.length;i++) {
			double[] geographyI = coord2Geo(x[i],y[i],z[i]);
			geography[0][i]=geographyI[0];
			geography[1][i]=geographyI[1];
		}
		return geography;
	}
	
	public static boolean isOriginSet() {
		return isOriginSet;
	}

	public static void setOriginSet(boolean isOriginSet) {
		CoordTrans.isOriginSet = isOriginSet;
	}

	public static double[] getGeographyOrigin() {
		return GeographyOrigin;
	}
}

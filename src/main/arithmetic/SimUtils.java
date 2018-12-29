package main.arithmetic;

import main.entity.Point;

public class SimUtils {
	public static Point Origin = new Point(0,0);
	
	public final static int LEFT = -1;
	public final static int RIGHT = 1;
	public final static int IN = 0;
	
	public final static double EPS = Math.pow(10, -10);
	public final static double INFINITY = Math.pow(10, 10);
	
	public static boolean doubleEqual(double d1, double d2) {
		if(Math.abs(d1-d2)<EPS){
			return true;
		}
		return false;
	}

	public static int findIndexOfMin(double[] doubleArray) {
		int index = 0;
		double min = doubleArray[0];
		for(int i = 1;i<doubleArray.length;i++) {
			if(doubleArray[i]<min) {
				min=doubleArray[i];
				index=i;
			}
		}
		return index;
	}
}

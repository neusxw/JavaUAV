package main.arithmetic;

public class SimUtils {
	static double[] Origin = {0,0};
	final static double eps = Math.pow(10, -10);
	public static boolean doubleEqual(double d1, double d2) {
		if(Math.abs(d1-d2)<eps){
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
	
	public static double findMax(double[] doubleArray) {
		double max = doubleArray[0];
		for(int i = 1;i<doubleArray.length;i++) {
			if(doubleArray[i]>max) {
				max=doubleArray[i];
			}
		}
		return max;
	}
	
}

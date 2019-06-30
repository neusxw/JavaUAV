package data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import entity.geometry.Point;

public class SimUtils {
	public static int numUAV;
	public static double height;
	public static double defaultHeight;
	public static double velocity;
	public static double turningTime;
	public static double kmeansAlpha = 10;
	public static Point Origin = new Point(0,0);
	//点对直线的位置
	public final static int LEFT = -1;
	public final static int RIGHT = 1;
	public final static int IN = 0;
	//点对多边形的位置
	public final static int OUTTER = -1;
	public final static int INBORDER = 1;
	public final static int INNER = 0;
	//无穷小，无穷大
	public final static double EPS = Math.pow(10, -6);
	public final static double INFINITY = Math.pow(10, 10);
	public final static double RADIUSofEARTH = 6371393;
	public final static double SAFETY$DISTANCE = 4;
	
	public static boolean isGridSpeeding = false;
	
	public static boolean doubleEqual(double d1, double d2) {
		if(Math.abs(d1/Math.pow(10,magnitude(d1))-d2/Math.pow(10,magnitude(d1)))<EPS){
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
	
	public static void changeOutputDestination() throws IOException {
		File f=new File("D:\\out.txt");
	    f.createNewFile();
	    FileOutputStream fileOutputStream = new FileOutputStream(f);
	    PrintStream printStream = new PrintStream(fileOutputStream);
	    System.setOut(printStream);
	}
	
	public static int magnitude(double d) {
		int n = 0;
		while(Math.abs(d)>1) {
			d/=10;
			n++;
		}
		return n;
	}
	
	public static void printMatrix(double[][] adjacentMatrix){
		System.out.println("-------SimUtils.printMatrix--------");
		for(int i = 0;i < adjacentMatrix.length;i ++){
			for(int j = 0;j < adjacentMatrix.length;j ++)
				System.out.print(adjacentMatrix[i][j] + " ");
			System.out.println();
		}
	}
	
	public static void printMatrix(int[][] adjacentMatrix){
		System.out.println("-------SimUtils.printMatrix--------");
		for(int i = 0;i < adjacentMatrix.length;i ++){
			for(int j = 0;j < adjacentMatrix.length;j ++)
				System.out.print(adjacentMatrix[i][j] + " ");
			System.out.println();
		}
	}
	
	public static double variance(double[] array) {
		double average = 0;
		for(int i=0;i<array.length;i++) {
			average+=array[i];
		}
		average/=array.length;
		double var = 0;
		for(int i=0;i<array.length;i++) {
			var+=Math.pow((array[i]-average), 2);
		}
		var=Math.sqrt(var/(array.length-1));
		return var;
	}
	
	public static double variance(int[] array) {
		double average = 0;
		for(int i=0;i<array.length;i++) {
			average+=array[i];
		}
		average/=array.length;
		double var = 0;
		for(int i=0;i<array.length;i++) {
			var+=Math.pow((array[i]-average), 2);
		}
		var=Math.sqrt(var/(array.length-1));
		return var;
	}
	
	public static List<Point> shallowPointsCopy(List<Point> points){
		List<Point> tempPoints = new ArrayList<Point>();
		for(Point point:points) {
			tempPoints.add(point);
		}
		return tempPoints;
	}
}

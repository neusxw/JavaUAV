package main.arithmetic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import main.entity.Point;

public class SimUtils {
	public static Point Origin = new Point(0,0);
	//点对直线的位置
	public final static int LEFT = -1;
	public final static int RIGHT = 1;
	public final static int IN = 0;
	//点对多边形的位置
	public final static int OUTTER = -1;
	public final static int INBORDER = 1;
	public final static int INNER = 0;
	
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
	
	public static void changeOutputDestination() throws IOException {
		File f=new File("D:\\out.txt");
	    f.createNewFile();
	    FileOutputStream fileOutputStream = new FileOutputStream(f);
	    PrintStream printStream = new PrintStream(fileOutputStream);
	    System.setOut(printStream);
	}
}

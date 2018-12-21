package test;

import main.matter.*;

public class MyMain {
	
	public static void main(String[] args) {
		Boundary b1 = new Boundary();
		double[] xv1 = {0.2,0.6,0.6,0.1};
		double[] yv1 = {0.7,0.75,0.3,0.4};
		b1.createBoundaryFromArray(xv1,yv1);
		
		Boundary b2 = new Boundary();
		double[] xv2 = {0.2,0.6,0.6,0.1};
		double[] yv2 = {0.7,0.75,0.3,0.4};
		b2.createBoundaryFromArray(xv2,yv2);
	}
}

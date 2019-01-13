package test;

import java.util.ArrayList;
import java.util.List;

import main.arithmetic.CoordinateTransformation;
import main.arithmetic.SimUtils;
import main.entity.Point;

public class TempTest {

	public static void main(String[] args) {
		CoordinateTransformation ct = new CoordinateTransformation(1,2);
		//System.out.println(ct.getGeographyorigin()[0]);
		CoordinateTransformation ct2 = new CoordinateTransformation(10,2);
		//System.out.println(ct.getGeographyorigin()[0]);
		String str = new String("1.0 2.0;3.0;4.0");
		System.out.println(Double.parseDouble(str));
	}

}

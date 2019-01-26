package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import main.arithmetic.AllocationUAV;
import main.arithmetic.data.CoordTrans;
import main.arithmetic.data.SimUtils;
import main.entity.geometry.Point;

public class TempTest {

	public static void main(String[] args) {
		CoordTrans ct = new CoordTrans(1,2);
		//System.out.println(ct.getGeographyorigin()[0]);
		CoordTrans ct2 = new CoordTrans(10,2);
		//System.out.println(ct.getGeographyorigin()[0]);
		String str = new String("1.0 2.0 3.0 4.0");
		String[] strArray = str.split(";");
		System.out.println(strArray[0].split(" "));
		for(String s:strArray) {
			System.out.println(s);
		}
		System.out.println("abc".equals("abc"));
		
		AllocationUAV al = new AllocationUAV(8,3);
		//al.allocation();
	}

}

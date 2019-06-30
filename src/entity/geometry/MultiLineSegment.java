package entity.geometry;

import java.util.ArrayList;
import java.util.List;

public class MultiLineSegment {
	List<LineSegment> lineSegments = new ArrayList<LineSegment>();
	
	public MultiLineSegment(){	}
	
	public  MultiLineSegment(LineSegment lineSegment){	
		this.lineSegments.add(lineSegment);
	}
	
	public  MultiLineSegment(List<LineSegment> lineSegments){	
		this.lineSegments=lineSegments;
	}
	
	public  MultiLineSegment(LineSegment[] lineSegments){	
		for(LineSegment lineSegment:lineSegments) {
			this.lineSegments.add(lineSegment);
		}
	}
	
	public void add(LineSegment lineSegment) {
		this.lineSegments.add(lineSegment);
	}
	
	public  void add(List<LineSegment> lineSegments){	
		for(LineSegment lineSegment:lineSegments) {
			this.lineSegments.add(lineSegment);
		}
	}
	
	public  void add(LineSegment[] lineSegments){	
		for(LineSegment lineSegment:lineSegments) {
			this.lineSegments.add(lineSegment);
		}
	}
	
	public void remove(LineSegment lineSegment) {
		this.lineSegments.remove(lineSegment);
	}
	
	public static Point barycenter(List<LineSegment> lineSegments) {
		double coordX = 0;
		double coordY = 0;
		for(LineSegment line:lineSegments) {
			coordX+=line.getMidPoint().x;
			coordY+=line.getMidPoint().y;
		}
		coordX/=lineSegments.size();
		coordY/=lineSegments.size();
		return new Point(coordX,coordY);
	}
	
	public static double distanceMidToBarycenter(LineSegment lineSegment,List<LineSegment> lineSegments) {
		return barycenter(lineSegments).distanceToPoint(lineSegment.getMidPoint());
	}
	
	public static double minDistanceOfLineSegmentToLineSegments(LineSegment line0,List<LineSegment> lineSegments) {
		double len = Double.MAX_VALUE;
		for(LineSegment line1:lineSegments) {
			if(line0==line1) {
				continue;
			}
			if(distanceOfTwoLineSegment(line0,line1)<len) {
				len = distanceOfTwoLineSegment(line0,line1);
			}
		}
		return len;
	}
	
	public static double distanceOfTwoLineSegment(LineSegment line1,LineSegment line2) {
		return line1.getMidPoint().distanceToPoint(line2.getMidPoint());
	}
	
	public static double length(List<LineSegment> lineSegments) {
		double sum=0;
		for(LineSegment line:lineSegments) {
			sum+=line.length;
		}
		return sum;
	}
	
	public static void print(List<LineSegment> lineSegments) {
		for(LineSegment line:lineSegments) {
			line.print();
		}
		
	}
}

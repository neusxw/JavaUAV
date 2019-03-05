package main.arithmetic;

import java.util.ArrayList;
import java.util.List;

import main.entity.SimpleGrid;
import main.entity.geometry.LineSegment;
import main.entity.geometry.MultiLineSegment;
import main.entity.geometry.Point;

public class TwoOpt4TSP {

	public ArrayList<Point> run(ArrayList<LineSegment> lineSegments,Point point) {
		double bestScore = tourCost(creteTour(lineSegments,point)); 
		//System.out.println("INITIAL SCORE: " + bestScore);
		//MultiLineSegment.print(lineSegments);
		boolean noChange = false; 
		while(!noChange) {
			noChange=linear2OPT(lineSegments,point);
			//bestScore = tourCost(creteTour(lineSegments,point)); 
			//System.out.println("INITIAL SCORE: " + bestScore);
			//MultiLineSegment.print(lineSegments);
		}
		ArrayList<Point> tour = creteTour(lineSegments, point);

		if(point.distanceToPoint(tour.get(1))>point.distanceToPoint(tour.get(tour.size()-1))) {
			int i = 1;
			int j = tour.size()-1;
			while(i<j) {
				Point pi = tour.get(i);
				Point pj = tour.get(j);
				tour.set(i, pj);
				tour.set(j, pi);
				i++;
				j--;
			}
		}
		List<Point> newTour = new ArrayList<Point>();
		if(!SimpleGrid.isConnected(tour.get(0), tour.get(1))) {
			List<Point> path = SimpleGrid.getPath(tour.get(0),tour.get(1));
			List<Point> subTour = tour.subList(1, tour.size());
			List<Point> subPath = path.subList(1, path.size()-1);
			newTour.add(tour.get(0));
			newTour.addAll(subPath);
			newTour.addAll(subTour);
			System.out.println(subPath);
		}else {
			newTour=tour;
		}
		return (ArrayList<Point>) newTour;
	}

	public ArrayList<Point> run(Point point,ArrayList<LineSegment> lineSegments) {
		return run(lineSegments,point);
	}

	public ArrayList<LineSegment> initial(ArrayList<LineSegment> lineSegments,Point point) {
		ArrayList<LineSegment> newLineSegments = new ArrayList<LineSegment>();
		Point position = point;
		int index = -1;
		LineSegment candiLine = null;
		while(lineSegments.size()>0) {
			double minDistance = Double.MAX_VALUE;
			for (LineSegment line:lineSegments) {
				double dis1 = SimpleGrid.distanceOfTwoPoints(position, line.endPoint1);
				double dis2 = SimpleGrid.distanceOfTwoPoints(position, line.endPoint2);
				if(dis1< minDistance||dis2< minDistance) {	
					candiLine=line;
					if(dis1<dis2) {
						minDistance=dis1;
						index=2;
					}else {
						minDistance=dis2;
						index=1;
					}
				}
			}
			newLineSegments.add(candiLine);
			lineSegments.remove(candiLine);
			if(index==1) {
				position=candiLine.endPoint1;
			}else {
				position=candiLine.endPoint2;
			}
		}
		return newLineSegments;
	}

	//将从i到j之间的路径倒转
	public void swap(ArrayList<LineSegment> lineSegments,int i, int j){ 
		if(i>j) {
			int temp = i;
			i = j;
			j = temp;
		}
		while(i<j) {
			LineSegment li = lineSegments.get(i);
			LineSegment lj = lineSegments.get(j);
			lineSegments.set(i, lj);
			lineSegments.set(j, li);
			i++;
			j--;
		}
		//MultiLineSegment.print(lineSegments);
	} 

	public boolean linear2OPT(ArrayList<LineSegment> lineSegments,Point point){ 
		ArrayList<Point> points = creteTour(lineSegments,point);
		double bestScore = tourCost(points); 
		double curScore = 0; 

		for(int i=0;i<lineSegments.size()-1;i++){ 
			for(int j=i+1;j<lineSegments.size();j++){ 
				//System.out.println(i+", "+j); 
				swap(lineSegments, i, j); 
				ArrayList<Point> newPoints = creteTour(lineSegments,point);
				curScore = tourCost(newPoints); 
				//System.out.println("INITIAL SCORE: " + curScore);
				if(curScore<bestScore){ 
					//System.out.println("^^^^^^^^^^^^^^^^^");
					return false; 
				} 
				else{ 
					//Reverse changes 
					swap(lineSegments, j, i);  
				} 
			} 
		} 
		//System.out.println("xxxxxxxxxxxxxxxx");
		return true; 
	}  

	public ArrayList<Point> creteTour(ArrayList<LineSegment> lineSegments,Point point){
		ArrayList<Point> points = new ArrayList<Point>();
		points.add(point);
		for(LineSegment lineSegment:lineSegments) {
			Point last = points.get(points.size()-1);
			if(last.distanceToPoint(lineSegment.endPoint1)<
					last.distanceToPoint(lineSegment.endPoint2)) {
				points.add(lineSegment.endPoint1);
				points.add(lineSegment.endPoint2);
			}else {
				points.add(lineSegment.endPoint2);
				points.add(lineSegment.endPoint1);
			}
		}
		return points;
	}
	public double tourCost(ArrayList<Point> tour){ 
		double cost =0; 
		for(int i =0;i<tour.size()-1;i++){ 
			cost += EUC(tour.get(i), tour.get(i+1)); 
		} 
		cost+=EUC(tour.get(tour.size()-1), tour.get(0)); 
		return cost; 
	} 

	public double EUC(Point a, Point b){
		return SimpleGrid.distanceOfTwoPoints(a, b);
	} 
}
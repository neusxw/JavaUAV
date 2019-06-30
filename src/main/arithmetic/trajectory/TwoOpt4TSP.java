package main.arithmetic.trajectory;

import java.util.ArrayList;
import java.util.List;

import main.entity.SimpleGrid;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;

public class TwoOpt4TSP implements TrajectoryCreator{

	public List<Point> createTrajectory(Point point,List<LineSegment> lineSegments) {
		double bestScore = tourCost(creteTour(lineSegments,point)); 
		boolean noChange = false; 
		while(!noChange) {
			noChange=linear2OPT(lineSegments,point);
		}
		ArrayList<Point> tour = creteTour(lineSegments, point);

		//让离起始点最近的那个先访问
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
		//加入避障路径
		ArrayList<Point> newTour = new ArrayList<Point>();
		for(int i=0;i<tour.size()-1;i++) {
			newTour.add(tour.get(i));
			Point start = tour.get(i);
			Point end = tour.get((i+1)%tour.size());
			if(!SimpleGrid.isConnected(start,end)) {
				List<Point> path = SimpleGrid.getPath(start,end);
				//List<Point> head = tour.subList(0,i+1);
				//List<Point> rear = tour.subList(i+1,tour.size());
				List<Point> subPath = path.subList(1, path.size()-1);
				newTour.addAll(subPath);
				//System.out.println(subPath);
			}
		}
		newTour.add(tour.get(tour.size()-1));
		return (ArrayList<Point>) newTour;
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
	public void swap(List<LineSegment> lineSegments,int i, int j){ 
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
	} 

	public boolean linear2OPT(List<LineSegment> lineSegments,Point point){ 
		ArrayList<Point> points = creteTour(lineSegments,point);
		double bestScore = tourCost(points); 
		double curScore = 0; 

		for(int i=0;i<lineSegments.size()-1;i++){ 
			for(int j=i+1;j<lineSegments.size();j++){ 
				swap(lineSegments, i, j); 
				ArrayList<Point> newPoints = creteTour(lineSegments,point);
				curScore = tourCost(newPoints); 
				if(curScore<bestScore){ 
					return false; 
				} 
				else{ 
					//Reverse changes 
					swap(lineSegments, j, i);  
				} 
			} 
		} 
		return true; 
	}  

	public ArrayList<Point> creteTour(List<LineSegment> lineSegments,Point point){
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
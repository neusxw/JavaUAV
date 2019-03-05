package main.arithmetic;

import java.util.HashMap;
import java.util.List;

import main.arithmetic.data.SimUtils;
import main.entity.Map;
import main.entity.TakeOffPoint;
import main.entity.geometry.LineSegment;
import main.entity.geometry.MultiLineSegment;
import main.entity.geometry.Point;

public class AllocationUAV {
	private int numUAV;
	private int layer = Map.getInstance().stations.size();
	private int[] location;
	public java.util.Map<TakeOffPoint,List<LineSegment>> matches = new HashMap<TakeOffPoint,List<LineSegment>>();
	public AllocationUAV(int numUAV) {
		this.numUAV=numUAV;
		location= new int[layer];
	}
	
	public AllocationUAV(int numUAV,int layer) {
		this.numUAV=numUAV;
		this.layer=layer;
		location= new int[layer];
	}
	
	public java.util.Map<TakeOffPoint,List<LineSegment>> allocation(List<List<LineSegment>> groups) {
		System.out.println("================ 作业任务分配  ================");
		double fitness = Double.MAX_VALUE;
		Point[] barycenters = new Point[groups.size()];
		for(int i = 0;i<groups.size();i++) {
			barycenters[i]=MultiLineSegment.barycenter(groups.get(i));
		}
		TakeOffPoint[] takeOffPoints = new TakeOffPoint[numUAV];
		while(hasNext(location)) {
			//System.out.println("VVVVVVVVVVVVVVVVVVVVVV");
			for(int i = 0;i<layer;i++) {
				Map.getInstance().stations.get(i).takeOffPoints.clear();
			}
			location=next(location);
			//for(int i =0;i<allocation.length;i++) { System.out.print(allocation[i]+"	");} System.out.println();
			int index=0;
			for(int i = 0;i<layer;i++) {
				Map.getInstance().stations.get(i).arrangeTakeOffPoint(location[i]);
				for(TakeOffPoint point:Map.getInstance().stations.get(i).takeOffPoints) {
					takeOffPoints[index++]=point;
				}
			}
			
			int[][] adjacentMatrix = adjacentMatrix(takeOffPoints,barycenters);
			//SimUtils.printMatrix(adjacentMatrix);
			Hungary hungary = new Hungary();
			int[] UAV2land = hungary.appoint(adjacentMatrix);
			
			double tempFitness = fitness(takeOffPoints,barycenters,UAV2land);
			if(tempFitness<fitness) {
				fitness=tempFitness;
				matches.clear();
				for(int i = 0;i<UAV2land.length;i++) {
					matches.put(takeOffPoints[i], groups.get(UAV2land[i]));
				}
			}
		}
		for(int i = 0;i<layer;i++) {
			Map.getInstance().stations.get(i).takeOffPoints.clear();
		}
		System.out.println("无人机起飞点：");
		for(TakeOffPoint point:matches.keySet()) {
			point.getStation().takeOffPoints.add(point);
			System.out.println("起飞区"+point.getStation().ID+"："+point.toString());
		}
		System.out.println("================  END  ================");
		return matches;
	}
	
	private boolean hasNext(int[] array) {
		if (location[location.length-1]==numUAV) {
			return false;
		}else {
			return true;
		}
	}
	public int[] next(int[] array) {
		int num=0;
		int base=1;
		for(int i = 0;i<array.length;i++) {
			num+=array[i]*base;
			base*=(numUAV+1);
		}
		num++;
		while(true) {
			int[] temp = code(num);
			int amount=0;
			for(int i = 0;i<temp.length;i++) {
				amount+=temp[i];
			}
			if(amount==numUAV) {
				return temp;
			}
			num++;
			//System.out.println(num);
		}
	}
	
	private int[] code(int num) {
		int[] array = new int[layer];
		int i=0;
		while(num>0) {
			array[i]=num%(numUAV+1);
			num/=(numUAV+1);
			i++;
		}
		return array;
	}
	
	private int[][] adjacentMatrix(Point[] points1,Point[] points2) {
		int[][] adjacentMatrix = new int[points1.length][points2.length];
		for(int i = 0;i<points1.length;i++) {
			for(int j = 0;j<points2.length;j++) {
				adjacentMatrix[i][j]=(int)points1[i].distanceToPoint(points2[j]);
			}
		}
		return adjacentMatrix;
	}
	
	private double fitness(Point[] points1,Point[] points2,int[] appoint) {
		double fit = 0;
		for(int i=0;i<points1.length;i++) {
			fit+=points1[i].distanceToPoint(points2[appoint[i]]);
		}
		return fit;
	}
}

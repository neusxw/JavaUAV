package main.arithmetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import main.entity.Map;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;

public class DistributeGrid {
	private List<LineSegment> gridLines= Map.getInstance().gridLines;
	private List<Point> gridPoints=  Map.getInstance().gridPoints;
	List<List<LineSegment>> groups = new ArrayList<List<LineSegment>>();
	private int[] solution;
	private int numUAV;
	
	public DistributeGrid(int numUAV){
		this.numUAV=numUAV;
		solution=new int[gridLines.size()];
		Random rand = new Random();
		for(int i=0;i<solution.length;i++) {
			solution[i]=rand.nextInt(this.numUAV);
		}
	}
	
	public void distribute() {
		
	}
	
	public void groupingGridLines(){
		for(int i=0;i<numUAV;i++) {
			groups.add(new ArrayList<LineSegment>());
		}
		for(int i=0;i<solution.length;i++) {
			groups.get(solution[i]).add(gridLines.get(i));
		}
	}
	
	public double fitness() {
		double alpha = 0.5;
		double averageDistance = 0;
		double[] groupLength = new double[numUAV];
		double varianceOfGroupLength = 0;
		//��������
		for(List<LineSegment> group:groups) {
			if(group.size()<2) {continue;}
			double intragroupDistance = 0;
			for(int i=0;i<group.size();i++) {
				for(int j=i+1;j<group.size();j++) {
					intragroupDistance+=this.distanceOfTwoLineSegment(group.get(i), group.get(j));
				}
			}
			intragroupDistance/=group.size()*(group.size()-1);
			averageDistance+=intragroupDistance;
		}
		averageDistance/=groups.size();
		//�������
		for(int i=0;i<groups.size();i++) {
			for(int j=0;j<groups.get(i).size();j++) {
				groupLength[i]+=groups.get(i).get(j).length;
			}
		}
		varianceOfGroupLength=variance(groupLength);
		System.out.println("<fitness>");
		System.out.println(averageDistance);
		System.out.println(varianceOfGroupLength);
		System.out.println(alpha*averageDistance+(1-alpha)*varianceOfGroupLength);
		System.out.println("----------------------------------");
		return alpha*averageDistance+(1-alpha)*varianceOfGroupLength;
	}
	
	private double distanceOfTwoLineSegment(LineSegment line1,LineSegment line2) {
		return line1.getMidPoint().distanceToPoint(line2.getMidPoint());
	}
	
	private double variance(double[] array) {
		double average = 0;
		for(int i=0;i<array.length;i++) {
			average+=array[i];
		}
		average/=array.length;
		double var = 0;
		for(int i=0;i<array.length;i++) {
			var+=Math.pow((array[i]-average), 2);
		}
		var=Math.sqrt(var)/(array.length-1);
		return var;
	}
	
	public void printGrouped() {
		int i=0;
		System.out.println("=================Grouped=================");
		System.out.println("�߶���������"+this.gridLines.size());
		for(List<LineSegment> member:groups) {
			System.out.println("��"+i++ + "�飬��"+member.size()+"���߶�");
			for(LineSegment line:member) {
				line.print();
			}
		}
		System.out.println("===================END===================");
	}
	
}

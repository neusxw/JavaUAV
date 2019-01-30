package main.arithmetic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import main.entity.Grid;
import main.entity.Map;
import main.entity.geometry.LineSegment;
import main.entity.geometry.MultiLineSegment;
import main.entity.geometry.Point;

public class DecomposeGrid {
	private List<LineSegment> gridLines= Grid.getGridLines();
	private List<Point> gridPoints=  Grid.getGridPoints();
	public List<List<LineSegment>> groups = new ArrayList<List<LineSegment>>();
	private int[] solution;
	private int numUAV;
	public int TURN = 100;

	public DecomposeGrid(int numUAV){
		this.numUAV=numUAV;
		solution=new int[gridLines.size()];
		Random rand = new Random();
		for(int i=0;i<solution.length;i++) {
			solution[i]=rand.nextInt(this.numUAV);
		}
		groupingGridLines();
	}

	public void distribute() {
		System.out.println("————————————  作业任务分解  ————————————");
		for(int turn=0;turn<TURN;turn++) {
			for(int i = 0;i<gridLines.size();i++) {
				turning(gridLines.get(i),solution[i],i);
			}
			//System.out.println("----------"+turn+"-----------");
			//this.fitness(true);
		}
		//while(true) {if(!slightAdjustment()) break;}
	}

	public void groupingGridLines(){
		for(int i=0;i<numUAV;i++) {
			groups.add(new ArrayList<LineSegment>());
		}
		for(int i=0;i<solution.length;i++) {
			groups.get(solution[i]).add(gridLines.get(i));
		}
	}
	
	public void turning(LineSegment lineSegment,int groupNum,int positionInSolution) {
		double fittest=fitness(false);
		int index = groupNum;
		for(int i = 0;i<numUAV-1;i++) {
			int from = (groupNum+i)%numUAV;
			int to = (groupNum+i+1)%numUAV;
			changeGroup(lineSegment,from,to);
			solution[positionInSolution]=to;
			if(fittest>fitness(false)) {
				fittest = fitness(false);
				index = to;
			}
		}
		changeGroup(lineSegment,(groupNum+numUAV-1)%numUAV,index);
		solution[positionInSolution]=index;
	}

	private void changeGroup(LineSegment lineSegment,int from,int to) {
		//System.out.println(from+"-->"+to);
		groups.get(from).remove(lineSegment);
		groups.get(to).add(lineSegment);
	}

	public double fitness(boolean print) {
		double alpha = 0.5;
		Point[] clusteringCenter = new Point[numUAV];
		double[] groupLength = new double[numUAV];
		double score = 0;
		double variance = 0;
		//k-means
		for(int i = 0;i<groups.size();i++) {
			clusteringCenter[i]=MultiLineSegment.barycenter(groups.get(i));
			//System.out.print(clusteringCenter[i]+"	");
		}
		//System.out.println();
		for(int i = 0;i<groups.size();i++) {
			double groupScore = 0;
			if(clusteringCenter[i]==null) {
				score=Double.MAX_VALUE;
				break;
			}
			for(LineSegment line:groups.get(i)) {
				 groupScore+=line.getMidPoint().distanceToPoint(clusteringCenter[i]);
			}
			score+=groupScore;
		}
		//variance
		for(int i=0;i<groups.size();i++) {
			for(int j=0;j<groups.get(i).size();j++) {
				groupLength[i]+=groups.get(i).get(j).length;
			}
		}
		variance=variance(groupLength);
		if (print) {
			System.out.println("<fitness>");
			System.out.print("<groupLength>:");
			DecimalFormat df = new DecimalFormat("0");
			for(int i =0;i<groupLength.length;i++) {
				System.out.print("	"+df.format(groupLength[i]));
			}
			System.out.println();
			double fit = alpha*score+(1-alpha)*variance;
			double kmeans = alpha*score;
			double var = (1-alpha)*variance;
			System.out.println(fit+"="+kmeans + "+"+var);
			System.out.println("----------------------------------");
		}
		return alpha*score+(1-alpha)*variance;
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
		var=Math.sqrt(var/(array.length-1));
		return var;
	}

	private boolean slightAdjustment(){
		Point[] clusteringCenter = new Point[numUAV];
		double[] len = new double[numUAV];
		for(int i = 0;i<groups.size();i++) {
			clusteringCenter[i]=MultiLineSegment.barycenter(groups.get(i));
		}

		for(int i = 0;i<groups.size();i++) {
			if(clusteringCenter[i]==null) {
				len[i]=Double.MAX_VALUE;
				break;
			}
			for(LineSegment line:groups.get(i)) {
				len[i]+=line.getMidPoint().distanceToPoint(clusteringCenter[i]);
			}
		}
		for(int i = 0;i<groups.size()-1;i++) {
			for(int j = i+1;j<groups.size();j++) {
				List<LineSegment> groupi = groups.get(i);
				List<LineSegment> groupj = groups.get(j);
				for(int m = 0;m<groupi.size()-1;m++) {
					for(int n = 0;n<groupj.size();n++) {
						LineSegment li = groupi.get(m);
						LineSegment lj = groupj.get(n);
						Point ci = clusteringCenter[i];
						if(li.minDistanceToLineSegment(lj)<1.1*Map.getInstance().lands.get(0).getRidgeWideth()
								&&ci.distanceToLine(li)>li.distanceToLine(lj)&&ci.distanceToLine(li)>ci.distanceToLine(lj)) {
							groupj.remove(lj);
							groupi.add(lj);
							if(Math.abs(len[j]-len[i]-2*lj.length)>Math.abs(len[j]-len[i]-2*(lj.length-li.length))) {
								//交换
								//groupi.remove(li);
								//groupj.add(li);
							}
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public void printGrouped() {
		int i=0;
		System.out.println("=================Grouped=================");
		System.out.println("线段总条数："+this.gridLines.size());
		for(List<LineSegment> member:groups) {
			System.out.println("第"+i++ + "组，共"+member.size()+"条线段");
			for(LineSegment line:member) {
				//line.print();
			}
		}
		System.out.println("===================END===================");
	}
}

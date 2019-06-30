package arithmetic;

import java.util.ArrayList;
import java.util.List;

import entity.geometry.Point;

public class DepthFirstSearch {
	int[][] m;
	List<List<Point>> zerosList = new ArrayList<List<Point>>();
	Stack stack = new Stack();
	Point[] path;
	int maxSite;

	DepthFirstSearch(int[][] m){
		this.m = m;
		this.maxSite = m.length - 1;
		path = new Point[m.length];
		for(int i = 0;i < m.length;i ++){
			List<Point> zeros = new ArrayList<Point>();
			for(int j = 0;j < m.length;j ++) {
				if(m[i][j] == 0) {
					zeros.add(new Point(i,j));
				}
			}
			zerosList.add(zeros);
		}
		for(List<Point> points:zerosList) {for(Point point:points) {
		System.out.print("("+point.x+","+point.y+")"+"	");}System.out.println();}
	}

	public Point[] dfs(){
		for(Point point:zerosList.get(0)) {
			stack.push(point);
		}
		while (!stack.isEmpty()){
			//System.out.print(stack.list.size());
			Point peek = stack.pop();
			int site = layer(peek);
			//System.out.println(">>>" + site);
			path[site]=peek;
			if(!isCollide(path,site)) {
				if(site==maxSite) {
					return path;
				}else {
					for(Point point:zerosList.get(site+1)) {
						stack.push(point);
					}
				}
			}
			//for(int i=0;i<=site;i++) {System.out.print("("+path[i].x+","+path[i].y+")"+"	");}System.out.println();
		}
		return null;
	}

	private int layer(Point point) {
		return (int)point.x;
	}

	private boolean isCollide(Point[] points, int site) {
		if(site<1) {
			return false;
		}
		for(int i = 0;i <= site-1;i ++){
			for(int j = i+1;j <= site;j ++) {
				if(points[i].x==points[j].x||points[i].y==points[j].y) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		int[][] m = {{-2,-2,-2,-1,-2,-2,-2,-2},	
				{-2,-2,-2,2,-1,-2,-2,-2},
				{-2,-2,-1,12,8,-2,-2,-2},
				{-2,-2,-2,23,17,-2,-1,-2},	
				{-2,-2,-2,36,28,-2,-2,-1},	
				{-2,-2,9,48,39,-1,-2,-2},	
				{-1,-2,14,63,52,9,-2,-2},	
				{-2,-1,21,78,66,15,-2,-2}};

		for(int i = 0;i < m.length;i ++){
			for(int j = 0;j < m.length;j ++) {
				if(m[i][j]<0) {
					m[i][j]=0;
				}
			}
		}
		
		int[][] m2= {{0,0,0,0,32,33,0,0},
				{0,0,0,3,40,40,0,0},
				{0,0,0,8,49,48,0,0},
				{0,0,0,19,63,62,0,0},
				{0,0,0,29,76,75,0,0},
				{0,0,0,43,93,91,30,0},
				{0,0,0,57,109,107,43,0},
				{74,220,119,0,0,0,159,198}};
		DepthFirstSearch dfs = new DepthFirstSearch(m2);
		Point[] appoint = dfs.dfs();
		System.out.println("result:");
		for(int i = 0;i < m2.length;i ++) {
			System.out.println(appoint[i]);
		}
	}

	class Stack {
		List<Point> list = new ArrayList<Point>();
		public Stack(){
		}
		public Point peek(){
			return list.get(list.size()-1);
		}
		public Point pop(){
			Point peek = peek();
			list.remove(peek);
			return peek;
		}
		public void push(Point point){
			list.add(point);
		}
		public boolean isEmpty(){
			return list.size()==0;
		}
	}

}

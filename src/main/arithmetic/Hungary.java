package main.arithmetic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import main.entity.geometry.Point;

public class Hungary {
	public int[] appoint(int[][] m){
		int N = m.length;
		//行规约
		for(int i = 0;i < N;i ++){
			double min = Double.MAX_VALUE;
			for(int j = 0;j < N;j ++){
				if(m[i][j] < min)
					min = m[i][j];
			}
			for(int j = 0;j < N;j ++)
				m[i][j] -= min;
		}
		//列规约
		for(int j = 0;j < N;j ++){
			double min = Double.MAX_VALUE;
			for(int i = 0;i < N;i ++){
				if(m[i][j] < min)
					min = m[i][j];
			}
			if(min == 0)
				continue;
			for(int i = 0;i < N;i ++)
				m[i][j] -=min;
		}
		//System.out.println("规约后");printM(m);
		//进行试分配
		while(true){
			rcAppoint(m);
			 //System.out.println("指派"); printM(m);
			//判断是否达到最优分配
			if(isOptimal(m))
				break;
			
			//变换矩阵
			boolean shouldUpdata = updataM(m);
			if(!shouldUpdata) {
				for(int i = 0;i < N;i ++){
					for(int j = 0;j < N;j ++)
						if(m[i][j]<0)
							m[i][j] = 0;
				}
				System.out.println("DepthFirstSearch");
				DepthFirstSearch dfs = new DepthFirstSearch(m);
				Point[] points = dfs.dfs();
				int[] appoint = new int[points.length];
				for(Point point:points) {
					appoint[(int)point.x]=(int)point.y;
				}
				return appoint;
			}
			//System.out.println("更新");printM(m);
			//将0元素恢复
			for(int i = 0;i < N;i ++){
				for(int j = 0;j < N;j ++)
					if(m[i][j]<0)
						m[i][j] = 0;
			}
			//System.out.println("恢复");printM(m);
		}
		int[] appoint = new int[m.length];
		for(int i = 0;i < N;i ++){
			for(int j = 0;j < N;j ++) {
				if(m[i][j]==-1) {
					appoint[i]=j;
				}
			}
		}
		//printM(m);
		//printResult(m);
		return appoint;
	}
	
	public static boolean updataM(int[][] m){
		int N = m.length;
		//记录行、列是否打钩
		boolean[] rowIsChecked = new boolean[N];
		boolean[] colIsChecked = new boolean[N];
		//给没有被圈的行打钩
		for(int i = 0;i < N;i ++){
			for(int j = 0;j < N;j ++){
				if(m[i][j] == -1){
					rowIsChecked[i] = false;
					break;
				}else{
					rowIsChecked[i] = true;
				}
			}
		}
		
		boolean isChecked = true;
		while(isChecked){
			isChecked = false;
			//对所有打钩行的0元素所在列打钩
			for(int i = 0;i < N;i ++){
				if(rowIsChecked[i]){
					for(int j = 0;j < N;j ++){
						if(m[i][j]==-2 && !colIsChecked[j]){
							colIsChecked[j] = true;
							isChecked = true;
						}
					}
				}
			}
			//对打钩列上的独立零元素行打钩
			for(int j = 0;j < N;j ++){
				if(colIsChecked[j]){
					for(int i = 0;i < N;i ++){
						if(m[i][j] == -1 && !rowIsChecked[i]){
							rowIsChecked[i] = true;
							isChecked = true;
						}
					}
				}
			}
		}

		int numCol=0;
		for(int i = 0;i < colIsChecked.length;i ++){
			if(colIsChecked[i]==true) {
				numCol++;
			}
		}
		if(numCol==rowIsChecked.length) {
			return false;
		}
		
		//寻找盖零线以外最小的数
		double min = Double.MAX_VALUE;
		for(int i = 0;i < N;i ++){
			if(rowIsChecked[i]){
				for(int j = 0;j < N;j ++){
					if(!colIsChecked[j]){
						if(m[i][j] < min)
							min = m[i][j];
					}
				}			
			}
		}
		
		//打钩各行减去min
		for(int i=0;i < N;i ++){
			if(rowIsChecked[i]){
				for(int j = 0;j < N;j ++){
					if(m[i][j] > 0)
						m[i][j] -= min;
				}
			}
		}
		
		//打钩各列加上min
		for(int j=0;j < N;j ++){
			if(colIsChecked[j]){
				for(int i = 0;i < N;i ++){
					if(m[i][j] > 0)
						m[i][j] += min;
				}
			}
		}
		return true;
	}
	
	//统计被圈起来的0数量,判断是否找到最优解
	public static boolean isOptimal(int[][] m){
		int count = 0;
		for(int i = 0;i < m.length;i ++){
			for(int j = 0;j < m.length;j ++)
				if(m[i][j] == -1)
					count ++;
		}
		return count==m.length;
	}
	
	//寻找只有一个0元素的行，将其标记为独立0元素（-1），对其所在列的0元素画叉（-2）
	//若存在独立0元素返回true
	public static void rcAppoint(int[][] m){
		boolean zeroExist = true; 
		int N = m.length;
		while(zeroExist) {
			zeroExist = false; 
			int[] zeroCount = new int[N*2];
			//寻找只有一个0元素的行（列）
			for(int i = 0;i < N;i ++){
				for(int j = 0;j < N;j ++){
					if(m[i][j]==0){
						zeroCount[i]++;
						zeroExist = true;
					}
				}
			}
			for(int j = 0;j < N;j ++){
				for(int i = 0;i < N;i ++){
					if(m[i][j]==0){
						zeroCount[j+N]++;
						zeroExist = true;
					}
				}
			}
			//for(int i = 0;i < zeroCount.length;i ++){ System.out.print(zeroCount[i]); } System.out.println();printM(m);
			if(zeroExist){
				int index=-1;
				int leastZeroButNone=Integer.MAX_VALUE;
				for(int i = 0;i < zeroCount.length;i ++){
					if(zeroCount[i]>0&&zeroCount[i]<leastZeroButNone) {
						leastZeroButNone=zeroCount[i];
						index=i;
					}
				}
				int colIndex=-1;
				int rowIndex=-1;
				if(index<N) {
					for(int j = 0;j < N;j ++){
						if(m[index][j]==0) {
							rowIndex=index;
							colIndex=j;
							break;
						}
					}
				}else {
					for(int i = 0;i < N;i ++){
						if(m[i][index-N]==0) {
							rowIndex=i;
							colIndex=index-N;
							break;
						}
					}
				}
				
				//标记独立零元素
				m[rowIndex][colIndex] = -1;
				for(int j = 0;j < N;j ++){
					if(j == colIndex)
						continue;
					else if(m[rowIndex][j] == 0)
						m[rowIndex][j] = -2;
				}
				for(int i = 0;i < N;i ++){
					if(i == rowIndex)
						continue;
					else if(m[i][colIndex] == 0)
						m[i][colIndex] = -2;
				}
			}
		}
	}
	
	public static void main(String[] args) {
 
		int[][] m = new int[][]{{12,7,9,7,9},
									{8,9,6,6,6},
									{7,17,12,14,9},
									{15,14,6,6,10},
									{4,10,7,10,9}};
		new Hungary().appoint(m);
		printResult(m);
	}
	
	public static void printM(int[][] m){
		DecimalFormat df= new DecimalFormat("0");
		System.out.println("---------------");
		for(int i = 0;i < m.length;i ++){
			for(int j = 0;j < m.length;j ++)
				System.out.print(df.format(m[i][j]) + "	");
			System.out.println();
		}
	}
	
	public static void printResult(int[][] m){
		System.out.println("-----Result------");
		for(int i = 0;i < m.length;i ++){
			for(int j = 0;j < m.length;j ++)
				if(m[i][j] == -1)
					System.out.print(i+"--"+j+", ");
		}
		System.out.println();
	}
}

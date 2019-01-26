package main.arithmetic;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Arrays;

public class HungaryTest {
	public static void appoint(double[][] m){
		int N = m.length;
		//�й�Լ
		for(int i = 0;i < N;i ++){
			double min = Double.MAX_VALUE;
			for(int j = 0;j < N;j ++){
				if(m[i][j] < min)
					min = m[i][j];
			}
			for(int j = 0;j < N;j ++)
				m[i][j] -= min;
		}
		//�й�Լ
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
		//printM(m);
		//�����Է���
		while(true){
			rowAppoint(m);
			 //System.out.println("ָ��"); printM(m);
			//�ж��Ƿ�ﵽ���ŷ���
			if(isOptimal(m))
				break;
			
			//�任����
			updataM(m);
			//System.out.println("����");printM(m);
			//��0Ԫ�ػָ�
			for(int i = 0;i < N;i ++){
				for(int j = 0;j < N;j ++)
					if(m[i][j]<0)
						m[i][j] = 0;
			}
			//System.out.println("�ָ�");printM(m);
		}
	}
	
	public static void updataM(double[][] m){
		int N = m.length;
		//��¼�С����Ƿ��
		boolean[] rowIsChecked = new boolean[N];
		boolean[] colIsChecked = new boolean[N];
		//��û�б�Ȧ���д�
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
			
			//�����д��е�0Ԫ�������д�
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
			//�Դ����ϵĶ�����Ԫ���д�
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
 
		//Ѱ�Ҹ�����������С����
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
		
		//�򹳸��м�ȥmin
		for(int i=0;i < N;i ++){
			if(rowIsChecked[i]){
				for(int j = 0;j < N;j ++){
					if(m[i][j] > 0)
						m[i][j] -= min;
				}
			}
		}
		
		//�򹳸��м���min
		for(int j=0;j < N;j ++){
			if(colIsChecked[j]){
				for(int i = 0;i < N;i ++){
					if(m[i][j] > 0)
						m[i][j] += min;
				}
			}
		}				
	}
	
	//ͳ�Ʊ�Ȧ������0����,�ж��Ƿ��ҵ����Ž�
	public static boolean isOptimal(double[][] m){
		int count = 0;
		for(int i = 0;i < m.length;i ++){
			for(int j = 0;j < m.length;j ++)
				if(m[i][j] == -1)
					count ++;
		}
		return count==m.length;
	}
	
	//Ѱ��ֻ��һ��0Ԫ�ص��У�������Ϊ����0Ԫ�أ�-1�������������е�0Ԫ�ػ��棨-2��
	//�����ڶ���0Ԫ�ط���true
	public static void rowAppoint(double[][] m){
		boolean zeroExist = true; 
		int N = m.length;
		while(zeroExist) {
			zeroExist = false; 
			int[] zeroCount = new int[N];
			//Ѱ��ֻ��һ��0Ԫ�ص��У��У�
			for(int i = 0;i < N;i ++){
				for(int j = 0;j < N;j ++){
					if(m[i][j]==0){
						zeroCount[i]++;
						zeroExist = true;
					}
				}
			}
			//for(int i = 0;i < N;i ++){ System.out.print(zeroCount[i]); } System.out.println();printM(m);
			
			if(zeroExist){
				int rowIndex=-1;
				int leastZeroButNone=Integer.MAX_VALUE;
				for(int i = 0;i < N;i ++){
					if(zeroCount[i]>0&&zeroCount[i]<leastZeroButNone) {
						leastZeroButNone=zeroCount[i];
						rowIndex=i;
					}
				}
				int colIndex=-1;
				for(int j = 0;j < N;j ++){
					if(m[rowIndex][j]==0) {
						colIndex=j;
						break;
					}
				}
				
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
 
		double[][] m = new double[][]{{12,7,9,7,9},
									{8,9,6,6,6},
									{7,17,12,14,9},
									{15,14,6,6,10},
									{4,10,7,10,9}};
		appoint(m);
		printResult(m);
	}
	
	public static void printM(double[][] m){
		DecimalFormat df= new DecimalFormat("0");
		System.out.println("---------------");
		for(int i = 0;i < m.length;i ++){
			for(int j = 0;j < m.length;j ++)
				System.out.print(df.format(m[i][j]) + "	");
			System.out.println();
		}
	}
	
	public static void printResult(double[][] m){
		System.out.println("-----Result------");
		for(int i = 0;i < m.length;i ++){
			for(int j = 0;j < m.length;j ++)
				if(m[i][j] == -1)
					System.out.print(i+"--"+j+", ");
		}				
	}
}
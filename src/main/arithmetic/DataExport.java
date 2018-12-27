package main.arithmetic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import main.matter.Land;
import main.matter.LineSegment;

public class DataExport {
	boolean dataOutputState;
	public File file;

	public DataExport(boolean dataOutputState) {
		this.dataOutputState = dataOutputState;
		file = new File("output\\dataOut.txt");
		file.delete();
	}



	/*	MATLAB��ͼ��
 	clc;
	clear all;
	close all;
	a=load('D:\GitHub\Reast\HeteMobileCooperation\repastOutput.txt');
	figure;
	plot(a(:,1),a(:,2)/100,'-b');
	legend('������')
	figure;
	plot(a(:,1),a(:,3),'-g',a(:,1),a(:,4),'-r',a(:,1),a(:,5),'-y');
	legend('ƽ���ƶ�����','������ƽ���ƶ�����','������ƽ���ƶ�����')
	figure;
	plot(a(:,1),a(:,6),'-b',a(:,1),a(:,7),'-r');
	legend('������ƽ��֧��','������ƽ��֧��')
	 */

	public void landOutput(Land land) {
		try {
			if(!file.exists()) {
				file.createNewFile();
				}
			FileWriter writer = new FileWriter(file,true);
			String str;
			for(LineSegment line : land.gridLines) {
				str = line.endPoint1.x+" "+line.endPoint1.y+" "+line.endPoint2.x+" "+line.endPoint2.y;
				writer.write(str + "\r\n");
			}
			writer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}

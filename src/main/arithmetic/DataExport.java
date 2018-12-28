package main.arithmetic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import main.matter.Land;
import main.matter.LineSegment;
import main.matter.Map;

public class DataExport {
	boolean dataOutputState;
	public File file;

	public DataExport(boolean dataOutputState) {
		this.dataOutputState = dataOutputState;
		file = new File("output\\dataOut.txt");
		file.delete();
	}

	public void gridLinesOutput(List<LineSegment> gridLines) {
		try {
			if(!file.exists()) {
				file.createNewFile();
				}
			FileWriter writer = new FileWriter(file,true);
			String str;
			for(LineSegment line : gridLines) {
				str = line.endPoint1.x+" "+line.endPoint1.y+" "+line.endPoint2.x+" "+line.endPoint2.y;
				writer.write(str + "\r\n");
			}
			writer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/*	MATLAB绘图：
 	clc;
	clear all;
	close all;
	a=load('D:\GitHub\Reast\HeteMobileCooperation\repastOutput.txt');
	figure;
	plot(a(:,1),a(:,2)/100,'-b');
	legend('合作率')
	figure;
	plot(a(:,1),a(:,3),'-g',a(:,1),a(:,4),'-r',a(:,1),a(:,5),'-y');
	legend('平均移动概率','背叛者平均移动概率','合作者平均移动概率')
	figure;
	plot(a(:,1),a(:,6),'-b',a(:,1),a(:,7),'-r');
	legend('背叛者平均支付','合作者平均支付')
	 */

}

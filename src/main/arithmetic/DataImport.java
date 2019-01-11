package main.arithmetic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataImport {
	public List<MapInfo> GIS = new ArrayList<MapInfo>();
	public String filePath="D:\\";
	public String fileName=null;
	public DataImport(String filePath,String fileName) {
		this.filePath=filePath.toLowerCase();
		this.fileName=fileName.toLowerCase();
	}
	public DataImport(String fileName) {
		this.fileName=fileName.toLowerCase();
	}

	public List<MapInfo> readTxt() {
		try {
			File file = new File(filePath + fileName);
			if(file.isFile() && file.exists()) {
				//InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
				InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
				BufferedReader br = new BufferedReader(isr);       
				String lineTxt = null;   
				String type = null;
				List<double[]> data = new ArrayList<double[]>();
				while ((lineTxt = br.readLine())!=null) {
					switch(lineTxt.toLowerCase()){
					case "land" :{
						if(type!=null) {GIS.add(new MapInfo(type, data));}
						type="land";
						data.clear();
						break;}
					case "obstacle":{
						if(type!=null) {GIS.add(new MapInfo(type, data));}
						type="obstacle";
						data.clear();
						break;}
					case "takeoff":{
						if(type!=null) {GIS.add(new MapInfo(type, data));}
						type="takeoff";
						data.clear();
						break;}
					default:{
						String[] info = lineTxt.split(" ");
						data.add(new double[] {Double.valueOf(info[1]) , Double.valueOf(info[2])});}
					}
				}
				GIS.add(new MapInfo(type, data));
				br.close();     
			} else {       
				System.out.println("文件不存在!");     
			}   
		} catch (Exception e) {
			System.out.println("文件读取错误!");   
		}
		return GIS;
	} 


}

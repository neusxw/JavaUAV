package main.data;

import java.io.BufferedReader;
import java.io.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.entity.PolygonFactory;

public class DataImport {
	public List<MapInfo> GIS = new ArrayList<MapInfo>();
	public String[] polygonType= new String[] {"land", "obstacle","station"};
	public String fileName=null;
	public DataImport(String fileName) {
		this.fileName=fileName.toLowerCase();
	}

	public List<MapInfo> readTxt() {
		try {
			//File file = new File(fileName);
			InputStream is=this.getClass().getResourceAsStream(fileName);   
			if(is!=null) {//(file.isFile() && file.exists()) {
				//InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
				//InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);       
				String lineTxt = null;   
				String type = null;
				List<double[]> data = new ArrayList<double[]>();
				while ((lineTxt = br.readLine())!=null) {
					if(Arrays.asList(polygonType).contains(lineTxt.toLowerCase())) {
						//System.out.println(lineTxt);
						if(type!=null) {GIS.add(new MapInfo(type, data));}
						type=lineTxt;
						data= new ArrayList<double[]>();
					}else {
						//System.out.println(lineTxt);
						String[] info = lineTxt.split(" ");
						if(info.length==3) {
							data.add(new double[] {Double.valueOf(info[1]) , Double.valueOf(info[2])});
						}
					}
				}
				GIS.add(new MapInfo(type, data));
				br.close();     
			} else {       
				System.out.println("文件不存在!##sxw##");     
			}   
		} catch (Exception e) {
			System.out.println("文件读取错误!");   
		}
		return GIS;
	} 
	
	public void resultPrint() {
		System.out.println("^^^^^^^^^^^^^^^^^^^^^READ TXT^^^^^^^^^^^^^^^^^^^^^");
		for(MapInfo info:GIS) {
			System.out.println(info.getType());
			for(double[] d:info.getData()) {
				System.out.println(d[0] + "," +d[1]);
			}
		}
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^END^^^^^^^^^^^^^^^^^^^^^^^");
	}		
}

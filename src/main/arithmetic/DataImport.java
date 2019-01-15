package main.arithmetic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
			
			if(true) {//(file.isFile() && file.exists()) {
				//InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
				//InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
				InputStream is=this.getClass().getResourceAsStream(fileName);   
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
				System.out.println("�ļ�������!##sxw##");     
			}   
		} catch (Exception e) {
			System.out.println("�ļ���ȡ����!");   
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

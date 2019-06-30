package main.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.entity.GridLine;
import main.entity.Land;
import main.entity.Map;
import main.entity.Obstacle;
import main.entity.SimpleGrid;
import main.entity.Station;
import main.entity.TakeOffPoint;
import main.entity.UAV;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;

public class DataExport {
	static String userPath = System.getProperty("user.dir");
	static String output = userPath + "/output";
	
	public DataExport() {
		File file = new File(output);
		if (!file.exists()) {
			file.mkdir();
		}else if(!file.isDirectory()) {
			file.delete();
			file.mkdir();
		}else {
			delAllFile(file.toString());
		}
		new File(userPath + "/output/runInfo").mkdir();
	}

	public void mapOutput() {
		File file = new File("output/runInfo/map.txt");
		file.delete();
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file,true);
			String str;
			for(Land land : Map.getInstance().lands) {
				str= "land" +Integer.toString(Map.getInstance().lands.indexOf(land)+1)+":";
				writer.write(str + "\r\n");
				for(Point point:land.vertexes) {
					str = point.x +" "+ point.y;
					writer.write(str + "\r\n");
				}
			}
			for(Obstacle obstacle : Map.getInstance().obstacles) {
				str= "obstacle" +Integer.toString(Map.getInstance().obstacles.indexOf(obstacle)+1)+":";
				writer.write(str + "\r\n");
				for(Point point:obstacle.vertexes) {
					str = point.x +" "+ point.y;
					writer.write(str + "\r\n");
				}
			}

			for(Station station : Map.getInstance().stations) {
				str= "station" +Integer.toString(Map.getInstance().stations.indexOf(station)+1)+":";
				writer.write(str + "\r\n");
				for(Point point:station.vertexes) {
					str = point.x +" "+ point.y;
					writer.write(str + "\r\n");
				}
			}

			writer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void numUAVOutput() {
		File file = new File("output/runInfo/numUAV.txt");
		file.delete();
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file,true);
			writer.write(SimUtils.numUAV + "\r\n");
			writer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	public void linesOutput(List<LineSegment> gridLines) {
		File file = new File("output/runInfo/linesOut.txt");
		file.delete();
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

	public void linesOutput(List<LineSegment> gridLines,int i) {
		File file = new File("output/runInfo/linesOut"+i+".txt");
		file.delete();
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

	public  void pointsOutput(List<? extends Point> points) {
		File file = new File("output/runInfo/pointsOut.txt");
		pointsOutput(file,points);
	}

	public  void pointsOutput(File file,List<? extends Point> points) {
		//System.out.println(points.get(5));
		file.delete();
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file,true);
			String str;
			int i=1;
			for(Point point : points) {
				str = i++ + " " + point.x + " " + point.y + " " +point.z;
				writer.write(str + "\r\n");
			}
			writer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public  void takeOffPointsOutput() {
		File file = new File("output/runInfo/takeOffPointsOut.txt");
		List<TakeOffPoint> takeOffPoints = new ArrayList<TakeOffPoint>();
		for(Station station : Map.getInstance().stations) {
			takeOffPoints.addAll(station.takeOffPoints);
		}
		file.delete();
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file,true);
			String str;
			int i=1;
			for(TakeOffPoint point : takeOffPoints) {
				str = i++ +" " + point.ID + " " + point.x + " " + point.y + " " +point.z;
				writer.write(str + "\r\n");
			}
			writer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public  void trajectoryOutput(UAV uav,int i) {
		File file = new File("output/runInfo/trajectoryOut"+i+".txt");
		pointsOutput(file,uav.trajectory);
	}

	public  void trajectoryOutputForGeography(UAV uav,int number) {
		String userPath = System.getProperty("user.dir");
		//String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
		File file = new File(userPath + "/output/trajectory"+number+".txt");

		//判断节点是否为作业点
		int[] isOperPoint = new int[uav.trajectory.size()];
		//System.out.println(number +": " + uav.trajectory.size());
		for(int i=0;i<uav.trajectory.size()-1;i++) {
			Point now = uav.trajectory.get(i);
			Point next = uav.trajectory.get(i+1);
			//System.out.println(now + "[]" +next);
			for(LineSegment line:SimpleGrid.getGridLines()) {
				if((line.endPoint1.equals(now)&&line.endPoint2.equals(next))||
						(line.endPoint2.equals(now)&&line.endPoint1.equals(next))) {
					isOperPoint[i]=1;
					break;
				}
			}
		}

		//设置飞行高度
		List<Point> trajectoryPoints = new ArrayList<Point>();
		Land land = null;
		for(Point point:uav.trajectory) {
			try{
				land = ((GridLine)point.getMotherLine()).getMotherLand();
				point.z = land.getHeight();
				//System.out.println(SimUtils.defaultHeight);
			}catch(Exception e){
				point.z = SimUtils.defaultHeight;
			}
			//System.out.println(CoordTrans.coord2Geo(point)[2]);
			Point geoPoint = new Point(CoordTrans.coord2Geo(point));
			trajectoryPoints.add(geoPoint);
			//System.out.println(geoPoint);
		}

		//输出数据
		file.delete();
		DecimalFormat df = new DecimalFormat("0.00000000000000");
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			FileWriter writer = new FileWriter(file,true);
			String str;
			str = "地块ID：" + land.getID();
			writer.write(str + "\r\n");
			str = "无人机ID：" + uav.getID();
			writer.write(str + "\r\n");
			for(int i=0;i<trajectoryPoints.size();i++) {
				str = i+1 + " " + df.format(trajectoryPoints.get(i).x) + " " + 
						df.format(trajectoryPoints.get(i).y) + " " +
						trajectoryPoints.get(i).z + " " + SimUtils.velocity +" " + isOperPoint[i];
				//System.out.println(str);
				writer.write(str + "\r\n");
			}
			writer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	//删除指定文件夹下所有文件
	//param path 文件夹完整绝对路径
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);//再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); //删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); //删除空文件夹
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}

	public static void changeOutPosition(){
		try {
			PrintStream ps = new PrintStream(userPath + "/output/runInfo/runInfo.txt");
			System.setOut(ps);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}

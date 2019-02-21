package test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import main.arithmetic.AllocationUAV;
import main.arithmetic.KMeans;
import main.arithmetic.data.DataExport;
import main.arithmetic.data.MapGenerate;
import main.arithmetic.data.MapInfo;
import main.arithmetic.data.ReadXML;
import main.arithmetic.data.SimUtils;
import main.entity.Map;
import main.entity.TakeOffPoint;
import main.entity.UAV;
import main.entity.geometry.LineSegment;

public class Main {
	public static void main(String[] args){
		DataExport.changeOutPosition();
		System.out.println("############## START ###############");
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		
		//导入数据，生成地图
		DataExport dataExport = new DataExport();
		String userPath = System.getProperty("user.dir");
		File file = new File(userPath + "/rs/map.xml");
		ReadXML readXml = new ReadXML();
		List<MapInfo> mapInfoList = readXml.getMapInfo(file);
		new MapGenerate(mapInfoList).generateMap();
		Map.getInstance().print();
		dataExport.mapOutput();
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		
		//划分网格
		Map.getInstance().createGrid();
		dataExport.linesOutput(Map.getInstance().gridLines);
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		
		//任务分解
		List<List<LineSegment>> groups = KMeans.clusteringLines(Map.getInstance().gridLines, SimUtils.numUAV,1000);
		System.out.println("************");
		for(int i=0;i<SimUtils.numUAV;i++) {dataExport.linesOutput(groups.get(i),i);}
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		
		//任务分配
		AllocationUAV allocationUAV = new AllocationUAV(SimUtils.numUAV);
		java.util.Map<TakeOffPoint,List<LineSegment>> UAV2Land = allocationUAV.allocation(groups);
		dataExport.takeOffPointsOutput();
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		
		//无人机生成路径
		for(TakeOffPoint tp:UAV2Land.keySet()) {
			UAV uav = new UAV(tp);
			uav.setGridLines(UAV2Land.get(tp));
			dataExport.linesOutput(UAV2Land.get(tp),uav.ID);
			uav.creatTrajectory2Opt();
		}
		for(int i =0;i<Map.getInstance().UAVs.size();i++) {
			dataExport.trajectoryOutput(Map.getInstance().UAVs.get(i),i);
			dataExport.trajectoryOutputForGeography(Map.getInstance().UAVs.get(i),i);
		}
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		
		System.out.println("############## END ALL ###############");
	}
}

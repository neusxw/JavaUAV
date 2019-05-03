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
import main.entity.SimpleGrid;
import main.entity.TakeOffPoint;
import main.entity.UAV;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;

public class Main {
	public static void main(String[] args){
		//DataExport.changeOutPosition();

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

		//划分地块
		Map.getInstance().createGrid();
		dataExport.numUAVOutput();
		dataExport.linesOutput(Map.getInstance().gridLines);
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		//任务分解
		List<List<LineSegment>> groups = KMeans.clusteringLines(Map.getInstance().gridLines, SimUtils.numUAV,1000);
		for(int i=0;i<SimUtils.numUAV;i++) {
			dataExport.linesOutput(groups.get(i),i);
		}
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		//任务分配
		AllocationUAV allocationUAV = new AllocationUAV(SimUtils.numUAV);
		java.util.Map<TakeOffPoint,List<LineSegment>> UAV2Land = allocationUAV.allocation(groups);
		dataExport.takeOffPointsOutput();
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		//无人机生成路径
		System.out.println("=============== 无人机生成路径 ================");
		for(TakeOffPoint tp:UAV2Land.keySet()) {
			UAV uav = new UAV(tp);
			uav.setGridLines(UAV2Land.get(tp));
			dataExport.linesOutput(UAV2Land.get(tp),tp.ID);
			//uav.creatTrajectory();
			uav.creatTrajectory2Opt();
		}
		for(int i =0;i<Map.getInstance().UAVs.size();i++) {
			UAV uav = Map.getInstance().UAVs.get(i);
			//System.out.println(uav.getTakeOffPoint().ID);
			dataExport.trajectoryOutput(uav,uav.getTakeOffPoint().ID);
			dataExport.trajectoryOutputForGeography(uav,uav.getTakeOffPoint().ID);
		}
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		System.out.println("############## END ALL ###############");
	}
}

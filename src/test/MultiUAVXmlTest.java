package test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import main.arithmetic.AllocationUAV;
import main.arithmetic.DistributeGrid;
import main.arithmetic.data.CoordTrans;
import main.arithmetic.data.DataExport;
import main.arithmetic.data.GenerateMap;
import main.arithmetic.data.MapInfo;
import main.arithmetic.data.ReadXMLByDom4j;
import main.entity.Grid;
import main.entity.Land;
import main.entity.Map;
import main.entity.Obstacle;
import main.entity.PolygonFactory;
import main.entity.Station;
import main.entity.TakeOffPoint;
import main.entity.UAV;
import main.entity.geometry.Line;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;

public class MultiUAVXmlTest {
	public static void main(String[] args){
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		int numUAV = 8;
		DataExport dataExport = new DataExport();
		File file = new File("rs/multiUAV.xml");
		//String userPath = System.getProperty("user.dir");
		//File file = new File(userPath + "/map.xml");
		ReadXMLByDom4j readXml = new ReadXMLByDom4j();
		List<MapInfo> mapInfoList = readXml.getMapInfo(file);
		new GenerateMap(mapInfoList).generateMap();
		
		Map.getInstance().print();
		dataExport.mapOutput();
		
		Map.getInstance().createGrid();
		dataExport.linesOutput(Grid.getGridLines());
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		
		DistributeGrid dg = new DistributeGrid(numUAV);
		//dg.printGrouped();
		dg.distribute();
		for(int i=0;i<numUAV;i++) {
			dataExport.linesOutput(dg.groups.get(i),i);
		}
		
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		AllocationUAV allocationUAV = new AllocationUAV(numUAV);
		java.util.Map<TakeOffPoint,List<LineSegment>> UAV2land = allocationUAV.allocation(dg.groups);
		dataExport.takeOffPointsOutput();
		
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		int k = 0;
		for(TakeOffPoint tp:UAV2land.keySet()) {
			UAV uav = new UAV(tp);
			uav.setGridLines(UAV2land.get(tp));
			dataExport.linesOutput(UAV2land.get(tp),k++);
			
			uav.creatTrajectory();
		}
		for(int i =0;i<Map.getInstance().UAVs.size();i++) {
			dataExport.trajectoryOutput(Map.getInstance().UAVs.get(i),i);
			dataExport.trajectoryOutputForGeography(Map.getInstance().UAVs.get(i),i);
		}
		
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		System.out.println("############## END ALL ###############");
	}
}

package test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import arithmetic.AllocationUAV;
import arithmetic.DecomposeGrid;
import arithmetic.KMeans;
import arithmetic.hull.ConcaveHull;
import data.CoordTrans;
import data.DataExport;
import data.MapGenerate;
import data.MapInfo;
import data.ReadXML;
import data.SimUtils;
import entity.Land;
import entity.Map;
import entity.Obstacle;
import entity.PolygonFactory;
import entity.SimpleGrid;
import entity.Station;
import entity.TakeOffPoint;
import entity.UAV;
import entity.geometry.Line;
import entity.geometry.LineSegment;
import entity.geometry.Point;
import entity.geometry.Polygon;

public class MultiUAVXmlTest {
	public static void main(String[] args){
		//DataExport.changeOutPosition();
		System.out.println("############## START ###############");
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		DataExport dataExport = new DataExport();
		//File file = new File("rs/multiUAV.xml");
		String userPath = System.getProperty("user.dir");
		File file = new File(userPath + "/rs/multi.xml");
		ReadXML readXml = new ReadXML();
		List<MapInfo> mapInfoList = readXml.getMapInfo(file);
		new MapGenerate(mapInfoList).generateMap();
		
		Map.getInstance().print();
		dataExport.mapOutput();
		
		Map.getInstance().createGrid();
		dataExport.linesOutput(Map.getInstance().gridLines);
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		
		int numUAV = SimUtils.numUAV;
		
		//DecomposeGrid dg = new DecomposeGrid(numUAV);
		//dg.distribute();
		//dg.printGrouped();
		
		List<List<LineSegment>> groups = KMeans.clusteringLines(Map.getInstance().gridLines, numUAV,1000);
		ConcaveHull ch = new ConcaveHull();
		Polygon polygon = new ConcaveHull().createHull(SimpleGrid.getGridPoints(groups.get(0)));
		System.out.println("************");
       //polygon.enlarge(1);
        //for(int i = 0;i < polygon.vertexes.size();i++){System.out.println(polygon.vertexes.get(i));}
		for(int i=0;i<numUAV;i++) {
			dataExport.linesOutput(groups.get(i),i);
		}
		
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		AllocationUAV allocationUAV = new AllocationUAV(numUAV);
		java.util.Map<TakeOffPoint,List<LineSegment>> UAV2Land = allocationUAV.allocation(groups);
		dataExport.takeOffPointsOutput();
		
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		int k = 0;
		for(TakeOffPoint tp:UAV2Land.keySet()) {
			UAV uav = new UAV(tp);
			uav.setGridLines(UAV2Land.get(tp));
			dataExport.linesOutput(UAV2Land.get(tp),k++);
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

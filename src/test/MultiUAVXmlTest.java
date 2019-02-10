package test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import main.arithmetic.AllocationUAV;
import main.arithmetic.ConcaveHull;
import main.arithmetic.DecomposeGrid;
import main.arithmetic.KMeansPlusPlus;
import main.arithmetic.data.CoordTrans;
import main.arithmetic.data.DataExport;
import main.arithmetic.data.GenerateMap;
import main.arithmetic.data.MapInfo;
import main.arithmetic.data.ReadXML;
import main.arithmetic.data.SimUtils;
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
import main.entity.geometry.Polygon;

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
		new GenerateMap(mapInfoList).generateMap();
		
		Map.getInstance().print();
		dataExport.mapOutput();
		
		Map.getInstance().createGrid();
		dataExport.linesOutput(Grid.getGridLines());
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		
		int numUAV = SimUtils.numUAV;
		
		//DecomposeGrid dg = new DecomposeGrid(numUAV);
		//dg.distribute();
		//dg.printGrouped();
		
		List<List<LineSegment>> groups = KMeansPlusPlus.ClusteringLines(Map.getInstance().gridLines, numUAV,1000);
		ConcaveHull ch = new ConcaveHull();
		Polygon polygon = ConcaveHull.createConcaveHull(Grid.getGridPoints(groups.get(0)));
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

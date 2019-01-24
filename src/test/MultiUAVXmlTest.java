package test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import main.arithmetic.DistributeGrid;
import main.arithmetic.data.CoordinateTransformation;
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
import main.entity.geometry.Line;
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
		
		//Map.getInstance().stations.get(0).arrangeTakeOffPoint(1);
		//dataExport.takeOffPointsOutput();

		//UAV aUAV= new UAV(Map.getInstance().stations.get(0));
		//aUAV.creatTrajectory();
		
		//dataExport.trajectoryOutput();
		//dataExport.trajectoryOutputForGeography();
		
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		System.out.println("############## END ALL ###############");
	}
}

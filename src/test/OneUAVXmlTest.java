package test;

import java.io.File;
import java.util.List;

import main.arithmetic.CoordinateTransformation;
import main.arithmetic.DataExport;
import main.arithmetic.DistributeGrid;
import main.arithmetic.MapInfo;
import main.arithmetic.ReadXMLByDom4j;
import main.entity.CreateTrajectoryByGA;
import main.entity.Grid;
import main.entity.Map;
import main.entity.PolygonFactory;
import main.entity.UAV;

public class OneUAVXmlTest {

	public static void main(String[] args) {
		DataExport dataExport = new DataExport();
		
		File file = new File("rs/oneUAV.xml");
		List<MapInfo> mapInfoList = new ReadXMLByDom4j().getMapInfo(file);
		for(MapInfo info : mapInfoList){
			if (info.getType()=="origin") {
				new CoordinateTransformation(info.getData().get(0));
			}else {
				PolygonFactory.createPolygon(info, true);
			}
		}
   
		int obstacleNumber = Map.getInstance().obstacles.size();
		for(int i = 0;i<obstacleNumber;i++) {
			Map.getInstance().obstacles.get(i).triDecompose();
		}
		Map.getInstance().print();
		dataExport.mapOutput();

		Map.getInstance().createGrid();
		dataExport.linesOutput(Grid.getGridLines());
		Grid.printAdjacentMatrix();
		Grid.printIsConnected();
		
		DistributeGrid dg = new DistributeGrid(8);
		dg.groupingGridLines();
		dg.printGrouped();
		dg.fitness();
		
		Map.getInstance().stations.get(0).arrangeTakeOffPoint(1);
		dataExport.takeOffPointsOutput();
		
		UAV anUAV= new UAV(Map.getInstance().stations.get(0));
		
		//CreateTrajectoryByGA ct = new CreateTrajectoryByGA();
		//anUAV.trajectory = ct.createTrajectory();
		
		anUAV.creatTrajectory();
		
		dataExport.trajectoryOutput();
		dataExport.trajectoryOutputForGeography();
		
		
	}

}

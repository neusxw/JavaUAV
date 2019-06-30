package test;

import java.io.File;
import java.util.List;

import main.arithmetic.DecomposeGrid;
import main.data.CoordTrans;
import main.data.DataExport;
import main.data.MapInfo;
import main.data.ReadXML;
import main.entity.Map;
import main.entity.PolygonFactory;
import main.entity.UAV;

public class OneUAVXmlTest {

	public static void main(String[] args) {
		DataExport.changeOutPosition();
		DataExport dataExport = new DataExport();
		//File file = new File("rs/oneUAV.xml");
		String userPath = System.getProperty("user.dir");
		File file = new File(userPath + "/map.xml");
		ReadXML readXml = new ReadXML();
		List<MapInfo> mapInfoList = readXml.getMapInfo(file);
		for(MapInfo info : mapInfoList){
			if (info.getType()=="origin") {
				new CoordTrans(info.getData().get(0));
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
		dataExport.linesOutput(Map.getInstance().gridLines);
		
//		DistributeGrid dg = new DistributeGrid(8);
//		dg.printGrouped();
//		dg.distribute();
//		for(int i=0;i<8;i++) {
//			dataExport.linesOutput(dg.groups.get(i),i);
//		}
//		dg.test();
		
		Map.getInstance().stations.get(0).arrangeTakeOffPoint(1);
		dataExport.takeOffPointsOutput();
		
		UAV anUAV= new UAV(Map.getInstance().stations.get(0));
		
		//CreateTrajectoryByGA ct = new CreateTrajectoryByGA();
		//anUAV.trajectory = ct.createTrajectory();
		
		anUAV.creatTrajectory();
		
		dataExport.trajectoryOutput(anUAV,0);
		dataExport.trajectoryOutputForGeography(anUAV,0);
		
		
	}

}

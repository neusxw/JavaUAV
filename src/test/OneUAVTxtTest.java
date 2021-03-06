package test;

import java.util.List;

import arithmetic.*;
import data.DataExport;
import data.DataImport;
import data.MapInfo;
import entity.*;

/**
 * 
 * @author 沈小伟
 * 主程序，
 * 完成文档的读入，并据此完成地图中各个实体的创建；
 *
 */
public class OneUAVTxtTest {
	public static void main(String[] args) {
		DataExport dataExport = new DataExport();
		//dataExport.changeOutPosition();
		DataImport dataImport = new DataImport("/rs/map.txt");
		List<MapInfo> gis= dataImport.readTxt();
		//dataImport.resultPrint();
		
		for(MapInfo info:gis) {
			System.out.println(info.getType());
			PolygonFactory.createPolygon(info, true);
		}
		for(Land land:Map.getInstance().lands) {
			land.setRidgeDirection(90);
			land.setRidgeWideth(4);
		}
		Map.getInstance().obstacles.get(0).triDecompose();
		
		Map.getInstance().print();
		dataExport.mapOutput();

		Map.getInstance().createGrid();
		dataExport.linesOutput(Map.getInstance().gridLines);

		Map.getInstance().stations.get(0).arrangeTakeOffPoint(1);
		dataExport.takeOffPointsOutput();

		UAV aUAV= new UAV(Map.getInstance().stations.get(0));
		aUAV.creatTrajectory();
		
		dataExport.trajectoryOutput(aUAV,0);
		dataExport.trajectoryOutputForGeography(aUAV,0);
	}
}

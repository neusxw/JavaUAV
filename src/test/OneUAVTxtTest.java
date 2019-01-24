package test;

import java.util.List;

import main.arithmetic.*;
import main.arithmetic.data.DataExport;
import main.arithmetic.data.DataImport;
import main.arithmetic.data.MapInfo;
import main.entity.*;

/**
 * 
 * @author ��Сΰ
 * ������
 * ����ĵ��Ķ��룬���ݴ���ɵ�ͼ�и���ʵ��Ĵ�����
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
		dataExport.linesOutput(Grid.getGridLines());

		Map.getInstance().stations.get(0).arrangeTakeOffPoint(1);
		dataExport.takeOffPointsOutput();

		UAV aUAV= new UAV(Map.getInstance().stations.get(0));
		aUAV.creatTrajectory();
		
		dataExport.trajectoryOutput();
		dataExport.trajectoryOutputForGeography();
	}
}

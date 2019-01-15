package test;

import java.util.List;

import main.arithmetic.CoordinateTransformation;
import main.arithmetic.DataExport;
import main.arithmetic.DataImport;
import main.arithmetic.MapInfo;
import main.entity.Land;
import main.entity.Map;
import main.entity.Obstacle;
import main.entity.PolygonFactory;
import main.entity.Station;
import main.entity.TakeOffPoint;
import main.entity.UAV;
import main.entity.geometry.Line;
import main.entity.geometry.Point;
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
		dataExport.changeOutPosition();
		DataImport dataImport = new DataImport("rs/map.txt");
		List<MapInfo> gis= dataImport.readTxt();
		//dataImport.resultPrint();
		
		for(MapInfo info:gis) {
			System.out.println(info.getType());
			PolygonFactory.createPolygon(info, true);
		}
		Map.getInstance().print();
		Map.getInstance().obstacles.get(0).triDecompose();
		
		//dataExport.changeOutPosition();
		for(Land land:Map.getInstance().lands) {
			land.setRidgeDirection(90);
			land.setRidgeWideth(4);
		}
		Map.getInstance().print();
		dataExport.mapOutput();

		Map.getInstance().createGrid();
		dataExport.linesOutput(Map.getInstance().gridLines);

		Map.getInstance().stations.get(0).arrangeTakeOffPoint(1);
		dataExport.takeOffPointsOutput();

		UAV aUAV= new UAV(Map.getInstance().stations.get(0));
		aUAV.creatTrajectory();
		
		dataExport.trajectoryOutput();
		dataExport.trajectoryOutputForGeography();
	}
}

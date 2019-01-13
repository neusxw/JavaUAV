package test;

import java.util.List;

import main.arithmetic.CoordinateTransformation;
import main.arithmetic.DataExport;
import main.arithmetic.DataImport;
import main.arithmetic.Dijkstra;
import main.arithmetic.MapInfo;
import main.arithmetic.SimUtils;
import main.entity.Land;
import main.entity.Line;
import main.entity.Map;
import main.entity.Obstacle;
import main.entity.Point;
import main.entity.PolygonFactory;
import main.entity.Station;
import main.entity.TakeOffPoint;
import main.entity.Triangle;
import main.entity.UAV;
/**
 * 
 * @author 沈小伟
 * 主程序，
 * 完成文档的读入，并据此完成地图中各个实体的创建；
 *
 */
public class RealMapTest {
	public static void main(String[] args) {
		DataExport dataExport = new DataExport();
		DataImport dataImport = new DataImport("map.txt");
		List<MapInfo> gis= dataImport.readTxt();
		//dataImport.resultPrint();
		
		for(MapInfo info:gis) {
			PolygonFactory.createPolygon(info, true);
		}
		Map.getInstance().obstacles.get(0).triDecompose();
		
		//dataExport.changeOutPosition();
		for(Land land:Map.getInstance().lands) {
			land.setRidgeDirection(90);
			land.ridgeWideth=4;
		}
		System.out.println("######");
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
	
	public void generateMap() {
		CoordinateTransformation ct = new CoordinateTransformation(118.29588,39.694277);
		double ridgeDirection = new Line(new Point(ct.geography2Coordinate(118.296841,39.69767)),
				new Point(ct.geography2Coordinate(118.295759,39.699527))).directionAngle;
		//System.out.println(ridgeDirection);
		Land land1 = new Land(ct.geography2Coordinate(
				new double[] {118.295759,118.296841,118.299347,118.298449},
				new double[] {39.699527,39.69767,39.698684,39.700411}),ridgeDirection);
		Land land2 = new Land(ct.geography2Coordinate(
				new double[] {118.294919,118.294892,118.29588,118.296644,118.297623,118.29738,118.297748,118.296877},
				new double[] {39.696067,39.695859,39.694277,39.694443,39.695144,39.695616,39.695783,39.697289}),ridgeDirection);
		land2.removeVertex(land2.vertexes.get(5));
		Land land3 = new Land(ct.geography2Coordinate(
				new double[] {118.29703,118.298162,118.300749,118.299383},
				new double[] { 39.697337,39.695394,39.69606,39.698538}),ridgeDirection);

		Obstacle obstacle1 = new Obstacle(ct.geography2Coordinate(
				new double[] {118.297178,118.298243,118.29835,118.297811},
				new double[] { 39.697826,39.698926,39.698833,39.698076}));
		Obstacle obstacle2 = new Obstacle(ct.geography2Coordinate(
				new double[] {118.29597,118.296082,118.29619,118.29606},
				new double[] {39.695908,39.695758,39.695797,39.695935}));
		Obstacle obstacle3 = new Obstacle(ct.geography2Coordinate(
				new double[] {118.297295,118.297591},
				new double[] {39.696569,39.696119}));

		Station station1 = new Station(ct.geography2Coordinate(
				new double[] {118.297596,118.297623,118.298373,118.29831},
				new double[] { 39.697865,39.69783,39.698177,39.698212}));

		Station station2 = new Station(ct.geography2Coordinate(
				new double[] {118.29698,118.297039,118.297313,118.297263},
				new double[] {39.697185,39.697205,39.696685,39.696674}));
	}

	public static void allIn() {
		for(int i=0;i<8;i++) {
			for(Station station:Map.getInstance().stations) {
				for(TakeOffPoint point:station.takeOffPoints) {
					if(!point.isOccupied) {
						new UAV(station);
					}
				}
			}
		}
		int size = Map.getInstance().gridLines.size();
		for(UAV aUAV:Map.getInstance().UAVs) {
			//int index = Map.getInstance().UAVs.indexOf(aUAV);
			for(int i = 0;i<size/8+1;i++) {
				if(Map.getInstance().gridLines.size()==0) {
					continue;
				}
				aUAV.chooseNextPoint();
			}
		}
		for(UAV aUAV:Map.getInstance().UAVs) {
			aUAV.homewardVoyage();
		}
	}
}

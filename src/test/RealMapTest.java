package test;

import main.arithmetic.CoordinateTransformation;
import main.arithmetic.DataExport;
import main.arithmetic.Dijkstra;
import main.entity.Land;
import main.entity.Line;
import main.entity.Map;
import main.entity.Obstacle;
import main.entity.Point;
import main.entity.Station;
import main.entity.TakeOffPoint;
import main.entity.UAV;

public class RealMapTest {

	public static void main(String[] args) {
		DataExport dataExport = new DataExport(true);
		CoordinateTransformation ct = new CoordinateTransformation(118.29588,39.694277);
		double ridgeDirection = new Line(new Point(ct.geography2Coordinate(118.295759,39.699527)), 
									new Point(ct.geography2Coordinate(118.296841,39.69767))).directionAngle;
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
		
		Map.getInstance().print();
		dataExport.mapOutput();
		
		Map.getInstance().createGridLines();
		dataExport.linesOutput(Map.getInstance().gridLines);
		
		station1.arrangeTakeOffPoint(5);
		station2.arrangeTakeOffPoint(3);
		dataExport.takeOffPointsOutput();
		
		UAV UAV1= new UAV(station1);
		UAV1.creatTrajectory();
		dataExport.pointsOutput(UAV1.trajectory);
		Dijkstra dj=new Dijkstra(new Point(-1,1),UAV1.getTakeOffPoint(),Map.getInstance().obstacles);
		dj.getShortestPath();
		dj.printPath();

	}
}

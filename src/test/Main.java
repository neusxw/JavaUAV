package test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import arithmetic.AllocationUAV;
import arithmetic.DistributeUAV;
import arithmetic.KMeans;
import arithmetic.trajectory.Greedy;
import arithmetic.trajectory.takeoff.how.FromLeftOrRight;
import arithmetic.trajectory.takeoff.where.LeftOrRight;
import data.DataExport;
import data.MapGenerate;
import data.MapInfo;
import data.ReadXML;
import data.SimUtils;
import entity.GridLine;
import entity.Land;
import entity.Map;
import entity.TakeOffPoint;
import entity.UAV;
import entity.geometry.LineSegment;

public class Main {
	public static void main(String[] args){
		//DataExport.changeOutPosition();
		System.out.println("############## START ###############");
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		//�������ݣ����ɵ�ͼ
		DataExport dataExport = new DataExport();
		String userPath = System.getProperty("user.dir");
		File file = new File(userPath + "/rs/map.xml");
		ReadXML readXml = new ReadXML();
		List<MapInfo> mapInfoList = readXml.getMapInfo(file);
		new MapGenerate(mapInfoList).generateMap();
		Map.getInstance().print();
		dataExport.mapOutput();
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		//���ֵؿ�
		Map.getInstance().createGrid();
		dataExport.numUAVOutput();
		dataExport.linesOutput(Map.getInstance().gridLines);
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		//����ֽ�
		new DistributeUAV(Map.getInstance().lands,SimUtils.numUAV).distribute();//����ÿ��ص����˻��ܴ�
		List<List<LineSegment>> groups = new ArrayList<List<LineSegment>>();
		for(Land land:Map.getInstance().lands) {
			List<List<LineSegment>> aGroup = KMeans.clusteringLines(land.getGridLines(), land.amountOfUAVs,1000);
			groups.addAll(aGroup);
		}
		for(int i=0;i<SimUtils.numUAV;i++) {
			dataExport.linesOutput(groups.get(i),i);
		}
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		//�������
		AllocationUAV allocationUAV = new AllocationUAV(SimUtils.numUAV);
		java.util.Map<TakeOffPoint,List<LineSegment>> takeoffPoint2Land = allocationUAV.allocation(groups);
		dataExport.takeOffPointsOutput();
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		//���˻�����·��
		System.out.println("=============== ���˻�����·�� ================");
		for(TakeOffPoint tp:takeoffPoint2Land.keySet()) {
			UAV uav = new UAV(tp);
			uav.setGridLines(takeoffPoint2Land.get(tp));
			dataExport.linesOutput(takeoffPoint2Land.get(tp),tp.ID);
		}
		for(Land land:Map.getInstance().lands) {
			System.out .println("�ؿ���ɷ���");
			int where = new LeftOrRight().where(land);
			System.out .println("����Ϊ��" + where);
			for(UAV uav:land.UAVs) {
				uav.setTc(new Greedy(new FromLeftOrRight(where)));
				uav.creatTrajectory();
			}
			System.out .println("��������������������������");
		} 
		
		for(int i =0;i<Map.getInstance().UAVs.size();i++) {
			UAV uav = Map.getInstance().UAVs.get(i);
			//System.out.println(uav.getTakeOffPoint().ID);
			dataExport.trajectoryOutput(uav,uav.getTakeOffPoint().ID);
			dataExport.trajectoryOutputForGeography(uav,uav.getTakeOffPoint().ID);
		}
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		System.out.println("############## END ALL ###############");
	}
}

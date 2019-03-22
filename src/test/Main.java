package test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import main.arithmetic.AllocationUAV;
import main.arithmetic.KMeans;
import main.arithmetic.data.DataExport;
import main.arithmetic.data.MapGenerate;
import main.arithmetic.data.MapInfo;
import main.arithmetic.data.ReadXML;
import main.arithmetic.data.SimUtils;
import main.entity.Map;
import main.entity.SimpleGrid;
import main.entity.TakeOffPoint;
import main.entity.UAV;
import main.entity.geometry.LineSegment;
import main.entity.geometry.Point;

public class Main {
	public static void main(String[] args){
		boolean forJAR = true;
		if(forJAR==true) {DataExport.changeOutPosition();}
		
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
		if(forJAR==false) {
			//System.out.println(Map.getInstance().gridLines.size());
			dataExport.linesOutput(Map.getInstance().gridLines);
		}
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		//����ֽ�
		List<List<LineSegment>> groups = KMeans.clusteringLines(Map.getInstance().gridLines, SimUtils.numUAV,1000);
		if(forJAR==false) {
			for(int i=0;i<SimUtils.numUAV;i++) {
				dataExport.linesOutput(groups.get(i),i);
			}
		}
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		//�������
		AllocationUAV allocationUAV = new AllocationUAV(SimUtils.numUAV);
		java.util.Map<TakeOffPoint,List<LineSegment>> UAV2Land = allocationUAV.allocation(groups);
		if(forJAR==false) {dataExport.takeOffPointsOutput();}
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		//���˻�����·��
		System.out.println("=============== ���˻�����·�� ================");
		for(TakeOffPoint tp:UAV2Land.keySet()) {
			UAV uav = new UAV(tp);
			uav.setGridLines(UAV2Land.get(tp));
			if(forJAR==false) {dataExport.linesOutput(UAV2Land.get(tp),tp.ID);}
			uav.creatTrajectory2Opt();
		}
		for(int i =0;i<Map.getInstance().UAVs.size();i++) {
			UAV uav = Map.getInstance().UAVs.get(i);
			//System.out.println(uav.getTakeOffPoint().ID);
			if(forJAR==false) {dataExport.trajectoryOutput(uav,uav.getTakeOffPoint().ID);}
			dataExport.trajectoryOutputForGeography(uav,uav.getTakeOffPoint().ID);
		}
		System.out.println(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

		System.out.println("############## END ALL ###############");
	}
}

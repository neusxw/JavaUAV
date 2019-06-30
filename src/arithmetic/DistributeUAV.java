package arithmetic;

import java.util.List;

import data.SimUtils;
import entity.GridLine;
import entity.Land;
import entity.Map;
import entity.UAV;
import entity.geometry.LineSegment;

public class DistributeUAV {
	private List<Land> lands;
	private int numUAV;
	//private List<UAV> UAVs;
	public DistributeUAV(List<Land> lands,int numUAV){
		this.lands = lands;
		this.numUAV = numUAV;
	}
	
	public void distribute(){
		int[] amountOfUAVs = new int[lands.size()];
		double[] lengthArray = new double[lands.size()];
		double totalLength = 0;
		for(int i=0;i<lands.size();i++) {
			lengthArray[i] = gridLength(lands.get(i));
			totalLength += lengthArray[i];
			System.out.println("地块" + i + "长度:" + lengthArray[i]);
		}
		double lenghtPerUAV = totalLength / numUAV;
		
		int done = 0;
		for(int i=0;i<lands.size();i++) {
			amountOfUAVs[i] = (int) Math.ceil(gridLength(lands.get(i))/lenghtPerUAV);
			done+=amountOfUAVs[i];
			System.out.println("地块" + i + "初次分配:" + amountOfUAVs[i]);
		}
		
		double[] dec = new double[lands.size()];
		for(int i=0;i<lands.size();i++) {
			if(amountOfUAVs[i]==1) {
				dec[i] = 0.9999; 
			}else {
				dec[i] = lengthArray[i] - amountOfUAVs[i]*lenghtPerUAV;
			}
		}
		while(done>numUAV) {
			int index = SimUtils.findIndexOfMin(dec);
			amountOfUAVs[index]-=1;
			dec[index]=0.9999;
			done--;
		}
		for(int i=0;i<lands.size();i++) {
			lands.get(i).amountOfUAVs = amountOfUAVs[i];
			System.out.println("地块" + i + "共有:" + amountOfUAVs[i]);
		}
	}
	
	public double gridLength(Land land){
		double length =0;
		for(LineSegment gridLine:land.getGridLines()) {
			length+=gridLine.length;
		}
		return length;
	}
	
	
}

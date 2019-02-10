package main.arithmetic;

import java.util.List;

import main.entity.Land;
import main.entity.Map;

public class DistributeLand {
	private int numUAV;
	private List<Land> lands = Map.getInstance().lands;
	public DistributeLand(int numUAV){
		this.numUAV=numUAV;
	}
	
	public void distribute() {
		if(lands.size()<numUAV) {
			
		}else {
			
		}
	}
	
}

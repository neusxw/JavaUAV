package entity;

import data.SimUtils;
import entity.geometry.Point;

public class TakeOffPoint extends Point {
	public int ID;
	public static int IDcount = 1;
	public boolean isOccupied = false;
	private Station station;
	private UAV uav = null;

	public TakeOffPoint(Station station,Point point) {
		this(station,point.x,point.y);
	}
	
	public TakeOffPoint(Station station,double x,double y) {
		super(x,y);
		setStation(station);
		ID = IDcount++;
		//System.out.println(ID);
	}
	
	public UAV getUAV() {
		return uav;
	}
	public void setUAV(UAV uav) {
		this.uav = uav;
		this.isOccupied=true;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		if(this.positionToPolygon(station)!=SimUtils.OUTTER) {
			this.station = station;
		}else {
			System.out.println("此起飞点不可能位于该地面站中！");
		}
		
	}

}

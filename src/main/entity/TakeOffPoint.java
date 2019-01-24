package main.entity;

import main.arithmetic.data.SimUtils;
import main.entity.geometry.Point;

public class TakeOffPoint extends Point {
	public boolean isOccupied = false;
	private Station station;
	private UAV uav = null;

	public TakeOffPoint(Station station,Point point) {
		this(station,point.x,point.y);
	}
	
	public TakeOffPoint(Station station,double x,double y) {
		super(x,y);
		this.setStation(station);
		Grid.add(this);
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

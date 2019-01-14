package main.entity;

import main.arithmetic.SimUtils;
import main.entity.geometry.Point;

public class TakeOffPoint extends FlightPoint {
	public boolean isOccupied = false;
	private Station station;
	private UAV aUAV = null;

	public TakeOffPoint(Station station,Point point) {
		super(point);
		this.setStation(station);
	}
	
	public TakeOffPoint(Station station,double x,double y) {
		super(x,y);
		this.setStation(station);
	}
	
	public TakeOffPoint(Station station,UAV aUAV,Point point) {
		super(point);
		this.setStation(station);
		setaUAV(aUAV); 
	}
	
	public TakeOffPoint(Station station,UAV aUAV,double x,double y) {
		super(x,y);
		this.setStation(station);
		setaUAV(aUAV);
	}
	
	public UAV getaUAV() {
		return aUAV;
	}
	public void setaUAV(UAV aUAV) {
		this.aUAV = aUAV;
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

package main.data;

import java.util.List;

public class MapInfo {
	private String type;
	private List<double[]> data;
	
	public MapInfo() {}
	
	public MapInfo(String type,List<double[]> data) {
		this.type=type;
		this.data=data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<double[]> getData() {
		return data;
	}

	public void setData(List<double[]> data) {
		this.data = data;
	}
	
}

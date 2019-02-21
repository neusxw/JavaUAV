package main.arithmetic.data;

import java.util.List;

import main.entity.PolygonFactory;

public class MapGenerate {
	List<MapInfo> mapInfoList;
	public MapGenerate(List<MapInfo> mapInfoList){
		this.mapInfoList=mapInfoList;
	}

	public void generateMap(){
		addOrigin();
		addGeometry();
	}
	private void addOrigin() {
		boolean isContains = false;
		for(MapInfo info : mapInfoList){
			if (info.getType()=="origin") {
				new CoordTrans(info.getData().get(0));
				mapInfoList.remove(info);
				isContains=true;
				return;
			}
		}
		if(!isContains) {
			new CoordTrans(mapInfoList.get(0).getData().get(0));
		}
	}

	private void addGeometry() {
		for(MapInfo info : mapInfoList){
			PolygonFactory.createPolygon(info, true);
		}
	}
}

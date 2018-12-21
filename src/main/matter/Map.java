package main.matter;

import java.util.ArrayList;
import java.util.List;

public class Map {
	List<Land> boundaries = new ArrayList<Land>();
	List<Barrier> barriers = new ArrayList<Barrier>();
	private static Map map = new Map();

	private Map() {}
	public static Map getInstance(){
		return map;
	}
	public void addland(Land e) {
		this.boundaries.add(e);
	}

	public void addBarrier(Barrier e) {
		this.barriers.add(e);
	}

}

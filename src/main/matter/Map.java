package main.matter;

import java.util.ArrayList;
import java.util.List;
/*
 * ��ͼ��
 * ���õ���ģʽ��
 * ��ͼ��land��obstacle��ɣ������ǳ�ʼ��ʱ�Զ����뵽map
 * @@@@ ���裺land��obstacle�ı߽粻�غ�
 */
public class Map {
	/*
	 * 
	 */
	public List<Land> lands = new ArrayList<Land>();
	public List<Obstacle> Obstacles = new ArrayList<Obstacle>();
	private static Map map = new Map();

	private Map() {}
	public static Map getInstance(){
		return map;
	}
	public void addland(Land e) {
		this.lands.add(e);
	}

	public void addObstacle(Obstacle e) {
		this.Obstacles.add(e);
	}
	
	public String toString() {
		String str="Map:\t\n";
		for(Land land:lands) {
			str+=land.toString()+" \t\n";
		}
		for(Obstacle obstacle:Obstacles) {
			str+=obstacle.toString()+" \t\n";
		}
		return str;
	}

}

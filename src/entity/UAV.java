package entity;

import java.util.List;

import arithmetic.trajectory.TrajectoryCreator;
import entity.geometry.LineSegment;
import entity.geometry.Point;

/**
 * @author Administrator
 *
 */
public class UAV {
	private int ID = -1;
	private TakeOffPoint takeOffPoint;
	private Land landIn = null;
	
	private Point position;
	public List<Point> trajectory;
	private List<LineSegment> gridLines;
	
	private TrajectoryCreator tc;

	public UAV(TakeOffPoint takeOffPoint) {
		this.setTakeOffPoint(takeOffPoint);
		this.position=takeOffPoint;
		Map.getInstance().UAVs.add(this);
	}
	public UAV(Station station) {
		for(TakeOffPoint takeOffPoint:station.takeOffPoints) {
			if(takeOffPoint.isOccupied==false) {
				this.setTakeOffPoint(takeOffPoint);
				break;
			}
		}
		this.position=this.takeOffPoint;
		trajectory.add(position);
		Map.getInstance().UAVs.add(this);
	}

	public void creatTrajectory() {
		System.out.println("——————————第"+ ID +"架无人机生成轨迹——————————");
		trajectory = tc.createTrajectory(this.position,this.getGridLines());
	}

	public TakeOffPoint getTakeOffPoint() {
		return takeOffPoint;
	}
	public void setTakeOffPoint(TakeOffPoint takeOffPoint) {
		this.takeOffPoint = takeOffPoint;
		this.setID(takeOffPoint.ID);
		takeOffPoint.setUAV(this);
	}
	
	public List<LineSegment> getGridLines() {
		return gridLines;
	}
	public void setGridLines(List<LineSegment> gridLines) {
		this.gridLines = gridLines;
		Land land = ((GridLine)gridLines.get(0)).getMotherLand();
		this.landIn = land;
		land.UAVs.add(this);
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		if(this.ID==-1) {
			this.ID =iD;
		}else {
			System.out.println("无人机ID不能重复设置！");
		}
	}
	
	public Point getPosition() {
		return position;
	}
	
	public void setPosition(Point position) {
		this.position = position;
	}
	
	public List<Point> getTrajectory() {
		return trajectory;
	}
	
	public Land getLandIn() {
		return landIn;
	}

	public TrajectoryCreator getTc() {
		return tc;
	}
	public void setTc(TrajectoryCreator tc) {
		this.tc = tc;
	}
	
}
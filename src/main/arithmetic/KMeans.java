package main.arithmetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import main.arithmetic.data.SimUtils;
import main.entity.Grid;
import main.entity.Map;
import main.entity.geometry.LineSegment;
import main.entity.geometry.MultiLineSegment;
import main.entity.geometry.Point;
import main.entity.geometry.Polygon;

public class KMeans {
	int k; // 指定划分的簇数
	double mu; // 迭代终止条件，当各个新质心相对于老质心偏移量小于mu时终止迭代
	double[][] center; // 上一次各簇质心的位置
	int repeat; // 重复运行次数
	double[] crita; // 存放每次运行的满意度
	int[] result; //存放聚类的结果
	int[] bestResult;
	double[][] bestCenter;
	List<Point> points;
	List<List<Point>> groups;

	public KMeans(List<Point> points,int k, double mu, int repeat) {
		this.points=points;
		this.k = k;
		this.mu = mu;
		this.repeat = repeat;
		center = new double[k][2];
		crita = new double[repeat];
		groups = new ArrayList<List<Point>>();
		for(int i =0;i<k;i++) {
			groups.add(new ArrayList<Point>());
		}
	}

	public KMeans(List<Point> points,int k, double mu) {
		this(points,k, mu, 1);
	}

	public KMeans(List<Point> points,int k) {
		this(points,k, 10e-6, 1);
	}

	// 初始化k个质心，每个质心是len维的向量，每维均在left--right之间
	public void initRandomCenter(List<Point> points) {
		Random random = new Random(System.currentTimeMillis());
		int[] count = new int[k]; // 记录每个簇有多少个元素
		Iterator<Point> iter = points.iterator();
		while (iter.hasNext()) {
			Point point = iter.next();
			int id = random.nextInt(10000)%k;
			count[id]++;
			center[id][0] += point.x;
			center[id][1] += point.y;
		}
		for (int i = 0; i < k; i++) {
			center[i][0] /= count[i];
			center[i][1] /= count[i];
		}
	}

	// 初始化1个质心，逐渐增加质心到k个
	public void initCenterPlusPlus(List<Point> points) {
		Random random = new Random(System.currentTimeMillis());
		Point firstPoint = points.get(random.nextInt(points.size()));
		center[0][0] = firstPoint.x;
		center[0][1] = firstPoint.y;
		for(int i=1;i<k;i++) {
			double[] dis = new double[points.size()];
			for(Point point:points) {
				double minDis=Double.MAX_VALUE;
				for(int j=0;j<i;j++){
					double temp = new Point(center[j]).distanceToPoint(point);
					if(temp<minDis) {
						minDis=temp;
					}
				}
				dis[points.indexOf(point)]=minDis;
			}
			
			int n = roulette(dis);
			Point candi = points.get(n);
			center[i][0] = candi.x;
			center[i][1] = candi.y;
		}
	}

	public int roulette(double[] dis) {
		for(int i=0;i<dis.length-1;i++) {
			dis[i+1]+=dis[i];
			//System.out.println(dis[i]);
		}
		for(int i=0;i<dis.length;i++) {
			dis[i]/=dis[dis.length-1];
			//System.out.println(dis[i]);
		}
		double r = Math.random();
		for(int i=0;i<dis.length;i++) {
			if(r<dis[i]) {
				return i;
			}
		}
		return -1;
	}

	// 把数据集中的每个点归到离它最近的那个质心
	public void classify(List<Point> points) {
		result=new int[points.size()];
		for(int i=0;i<points.size();i++){
			Point point = points.get(i);
			int index = 0;
			double nearDist = Double.MAX_VALUE;
			for (int j = 0; j < k; j++) {
				//使用欧氏距离
				double dist = new Point(center[j]).distanceToPoint(point); // 使用编辑距离
				if (dist < nearDist) {
					nearDist = dist;
					index = j;
				}
			}
			result[i]=index;
		}
	}

	// 重新计算每个簇的质心，并判断终止条件是否满足，如果不满足更新各簇的质心,如果满足就返回true.len是数据的维数
	public boolean calNewCenter(List<Point> points) {
		boolean end = true;
		int[] count = new int[k]; // 记录每个簇有多少个元素
		double[][] sum = new double[k][2];
		for(int i=0;i<points.size();i++){
			Point point = points.get(i);
			int id = result[i];
			count[id]++;
			sum[id][0] += point.x;
			sum[id][1] += point.y;
		}
		for (int i = 0; i < k; i++) {
			if (count[i] != 0) {
				sum[i][0] /= count[i];
				sum[i][1] /= count[i];
			}else {
				initCenterPlusPlus(points);
				return false;
			}
		}
		for (int i = 0; i < k; i++) {
			// 只要有一个质心需要移动的距离超过了mu，就返回false
			if (new Point(sum[i]).distanceToPoint(new Point(center[i]))>=mu){
				end = false;
				break;
			}
		}
		if (!end) {
			for (int i = 0; i < k; i++) {
				center[i] = sum[i];
			}
		}
		return end;
	}

	// 计算各簇内数据和方差的加权平均，得出本次聚类的满意度.
	public double getSati(List<Point> points, double[] weight) {
		double satisfy = 0.0;
		double[] size = new double[k];
		double[] ss = new double[k];
		for(int i=0;i<points.size();i++){
			Point point = points.get(i);
			int id = result[i];
			size[id]+=weight[i]+SimUtils.TURNING$PAYOFF;//权值
			ss[id] += Math.pow(point.x - center[id][0], 2.0);
			ss[id] += Math.pow(point.y - center[id][1], 2.0);
		}
		for (int i = 0; i < k; i++) {
			satisfy += ss[i];
		}
		return satisfy*=Math.pow(SimUtils.variance(size), 1);//SimUtils.kmeansAlpha);
	}

	public double runOnce(int round, List<Point> points,double[] weight) {
		//System.out.println("第" + round + "次运行");
		initCenterPlusPlus(points);
		classify(points);
		while (!calNewCenter(points)) {
			classify(points);
		}
		double ss = getSati(points,weight);
		return ss;
	}

	public void run(double[] weight) {
		double minsa = Double.MAX_VALUE;
		for (int i = 0; i < repeat; i++) {
			double ss = runOnce(i, points,weight);
			if (ss < minsa) {
				minsa = ss;
				bestResult=result;
				bestCenter=center;
			}
		}
		System.out.println("满意度：" + minsa);
	}
	public void run() {
		double[] weight = new double[points.size()];
		Arrays.fill(weight, 1);
		run(weight);
	}

	public static List<List<LineSegment>> clusteringLines(List<LineSegment> lines,int k,int repeat) {
		List<Point> points = Grid.getMidPoints(lines);
		double[] weight = new double[points.size()];
		for(int i=0;i<weight.length;i++) {
			weight[i]=lines.get(i).length;
		}
		KMeans km = new KMeans(points,k,10e-6,repeat); 
		km.run(weight);

		List<List<LineSegment>> groups = new ArrayList<List<LineSegment>>();
		for(int i=0;i<k;i++) {
			groups.add(new ArrayList<LineSegment>());
		}
		for(int i=0;i<km.result.length;i++) {
			groups.get(km.bestResult[i]).add(lines.get(i));
		}

		for(List<LineSegment> lineList:groups) {
			System.out.println(MultiLineSegment.length(lineList));
		}
		while(slightAdjustment(groups)) {System.out.println("——————————————————————————————");}
		return groups;
	}
	
	private static boolean slightAdjustment(List<List<LineSegment>> groups){
		Point[] clusteringCenter = new Point[groups.size()];
		double[] len = new double[groups.size()];
		for(int i = 0;i<groups.size();i++) {
			clusteringCenter[i]=MultiLineSegment.barycenter(groups.get(i));
		}

		for(int i = 0;i<groups.size();i++) {
			if(groups.get(i).size()==0) {
				len[i]=Double.MAX_VALUE;
				break;
			}
			for(LineSegment line:groups.get(i)) {
				len[i]+=line.getMidPoint().distanceToPoint(clusteringCenter[i]);
			}
		}
		
		for(int i = 0;i<groups.size();i++) {
			List<Point> hull = new ConvexHull<Point>(Grid.getGridPoints(groups.get(i))).getHull();
			Polygon polygon = new Polygon(hull).enlarge(5);
			for(int j = 0;j<groups.size();j++) {
				if(i==j) {
					continue;
				}
				for(LineSegment line:groups.get(j)) {
					if(line.endPoint1.positionToPolygon(polygon)!=SimUtils.OUTTER &&
							line.endPoint2.positionToPolygon(polygon)!=SimUtils.OUTTER) {
						System.out.println(line);
						System.out.println(j+"-->"+i);
						groups.get(j).remove(line);
						groups.get(i).add(line);
						return true;
					}
				}
			}
		}
		return false;
	}
}

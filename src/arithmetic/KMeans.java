package arithmetic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import arithmetic.hull.ConvexHull;
import data.SimUtils;
import entity.SimpleGrid;
import entity.geometry.LineSegment;
import entity.geometry.MultiLineSegment;
import entity.geometry.Point;
import entity.geometry.Polygon;

public class KMeans {
	int k; // ָ�����ֵĴ���
	double mu; // ������ֹ�����������������������������ƫ����С��muʱ��ֹ����
	double[][] center; // ��һ�θ������ĵ�λ��
	int repeat; // �ظ����д���
	double[] crita; // ���ÿ�����е������
	int[] result; //��ž���Ľ��
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

	// ��ʼ��k�����ģ�ÿ��������lenά��������ÿά����left--right֮��
	public void initRandomCenter(List<Point> points) {
		Random random = new Random(System.currentTimeMillis());
		int[] count = new int[k]; // ��¼ÿ�����ж��ٸ�Ԫ��
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

	// ��ʼ��1�����ģ����������ĵ�k��
	public void initCenterPlusPlus(List<Point> points) {
		Random random = new Random(System.currentTimeMillis());
		//System.out.println(points.size());
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

	// �����ݼ��е�ÿ����鵽����������Ǹ�����
	public void classify(List<Point> points) {
		result=new int[points.size()];
		for(int i=0;i<points.size();i++){
			Point point = points.get(i);
			int index = 0;
			double nearDist = Double.MAX_VALUE;
			for (int j = 0; j < k; j++) {
				//ʹ��ŷ�Ͼ���
				double dist = new Point(center[j]).distanceToPoint(point); 
				if (dist < nearDist) {
					nearDist = dist;
					index = j;
				}
			}
			result[i]=index;
		}
	}

	// ���¼���ÿ���ص����ģ����ж���ֹ�����Ƿ����㣬�����������¸��ص�����,�������ͷ���true.len�����ݵ�ά��
	public boolean calNewCenter(List<Point> points) {
		boolean end = true;
		int[] count = new int[k]; // ��¼ÿ�����ж��ٸ�Ԫ��
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
			// ֻҪ��һ��������Ҫ�ƶ��ľ��볬����mu���ͷ���false
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

	// ������������ݺͷ���ļ�Ȩƽ�����ó����ξ���������.
	public double getSati(List<Point> points, double[] weight) {
		double satisfy = 0.0;
		double[] size = new double[k];
		double[] ss = new double[k];
		for(int i=0;i<points.size();i++){
			Point point = points.get(i);
			int id = result[i];
			size[id]+=weight[i]+SimUtils.velocity*SimUtils.turningTime;//Ȩֵ
			ss[id] += Math.pow(point.x - center[id][0], 2.0);
			ss[id] += Math.pow(point.y - center[id][1], 2.0);
		}
		for (int i = 0; i < k; i++) {
			satisfy += ss[i];
		}
		return satisfy*=Math.pow(SimUtils.variance(size), SimUtils.kmeansAlpha);
	}

	public double runOnce(int round, List<Point> points,double[] weight) {
		//System.out.println("��" + round + "������");
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
		System.out.println("����ȣ�" + minsa);
	}
	public void run() {
		double[] weight = new double[points.size()];
		Arrays.fill(weight, 1);
		run(weight);
	}

	public static List<List<LineSegment>> clusteringLines(List<LineSegment> lines,int k,int repeat) {
		System.out.println("===================��ҵ����ֽ�===================");

		List<List<LineSegment>> groups = new ArrayList<List<LineSegment>>();
		if(k==1) {
			groups.add(lines);
			return groups;
		}
		for(int i=0;i<k;i++) {
			groups.add(new ArrayList<LineSegment>());
		}

		List<Point> points = SimpleGrid.getMidPoints(lines);
		double[] weight = new double[points.size()];
		for(int i=0;i<weight.length;i++) {
			weight[i]=lines.get(i).length;
		}
		KMeans km = new KMeans(points,k,10e-6,repeat); 
		km.run(weight);
		for(int i=0;i<km.result.length;i++) {
			//System.out.println(km.bestResult[i]);
			groups.get(km.bestResult[i]).add(lines.get(i));
		}

		double[] len=new double[groups.size()];
		for(List<LineSegment> lineList:groups) {
			System.out.println(MultiLineSegment.length(lineList));
			len[groups.indexOf(lineList)]=MultiLineSegment.length(lineList);
		}
		System.out.println("��׼��Ϊ��"+SimUtils.variance(len));
		while(slightAdjustment(groups)) {System.out.println();}
		while(slightAdjustment2(groups)) {System.out.println();}
		System.out.println("===================  END  ===================");
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
			Polygon polygon = new ConvexHull().createHull(SimpleGrid.getGridPoints(groups.get(i))).enlarge(0.01);
			for(int j = 0;j<groups.size();j++) {
				if(i==j) {
					continue;
				}
				for(LineSegment line:groups.get(j)) {
					if(line.endPoint1.positionToPolygon(polygon)!=SimUtils.OUTTER &&
							line.endPoint2.positionToPolygon(polygon)!=SimUtils.OUTTER) {
						//System.out.println(line);
						//System.out.println(j+"-->"+i);
						groups.get(j).remove(line);
						groups.get(i).add(line);
						return true;
					}
				}
			}
		}
		return false;
	}

	private static boolean slightAdjustment2(List<List<LineSegment>> groups){
		for(int groupIndex=0;groupIndex<groups.size();groupIndex++){
			for(int lineIndex=0;lineIndex<groups.get(groupIndex).size();lineIndex++) {
				double lenInner = Double.MAX_VALUE;
				for(int i=0;i<groups.get(groupIndex).size();i++) {
					if(lineIndex==i) {
						continue;
					}else {
						double tempInner=groups.get(groupIndex).get(lineIndex).distanceToParallelLineSegment(groups.get(groupIndex).get(i));
						if(tempInner<lenInner){
							lenInner=tempInner;
						}
					}
				}
				double lenOutter = Double.MAX_VALUE;
				int groupNum=-1;
				for(int i=0;i<groups.size();i++) {
					if(groupIndex==i) {
						continue;
					}else {
						for(int j=0;j<groups.get(i).size();j++) {
							double tempOutter=groups.get(groupIndex).get(lineIndex).distanceToParallelLineSegment(groups.get(i).get(j));
							if(tempOutter<lenOutter){
								lenOutter=tempOutter;
								groupNum=i;
							}
						}
					}
				}
				
				//System.out.println(lenInner+","+lenOutter+":"+groupNum+","+groupIndex);
				if(lenInner-lenOutter>0.1) {
					groups.get(groupNum).add(groups.get(groupIndex).get(lineIndex));
					groups.get(groupIndex).remove(groups.get(groupIndex).get(lineIndex));
					return true;
				}
			}
		}
		return false;
	}
}
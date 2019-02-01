package main.arithmetic;

import java.util.List;

import main.arithmetic.data.SimUtils;
import main.entity.geometry.Point;
import main.entity.geometry.Polygon;

public class ConvexHullTest {
	public static Point[] getConvexHull(List<Point> pointsList){
		Point[] points = new Point[pointsList.size()];
		for(int i=0;i<pointsList.size();i++) {
			points[i]=pointsList.get(i);
		}
		return getConvexHull(points);
	}
	
	public static Point[]  getConvexHull(Point[] points){
        int len = 0;  //���ڼ������շ��ؽ������͹���е�ĸ���
        Point[] result = new Point[points.length];
        for(int i = 0;i < points.length;i++){
            for(int j = 0;j < points.length;j++){
                if(j == i)     //��ȥѡ����Ϊȷ��ֱ�ߵĵ�һ����
                    continue;
                
                int[] judge = new int[points.length];   //��ŵ㵽ֱ�߾�����ʹ���жϹ�ʽ�Ľ��
                
                for(int k = 0;k < points.length;k++){
                    int a = (int) (points[j].y - points[i].y);
                    int b = (int) (points[i].x - points[j].x);
                    int c = (int) ((points[i].x)*(points[j].y) - (points[i].y)*(points[j].x));

                    judge[k] = (int) (a*(points[k].x) + b*(points[k].y) - c);  //���ݹ�ʽ��������жϽ��
                }
                
                if(JudgeArray(judge)){  // ��������ֱ�ߵ�һ��,����Ӧ��A[i]��͹���еĵ�
                    result[len++] = points[i];
                    break;
                }    
            }
        }
        Point[] result1 = new Point[len];
        for(int m = 0;m < len;m++) {
        	 result1[m] = result[m];
        }
        
        Polygon convexHull=new Polygon(result1);
        
        return result1;
    }
    
    //�ж�������Ԫ���Ƿ�ȫ�����ڵ���0����С�ڵ���0��������򷵻�true�����򷵻�false
    public static boolean JudgeArray(int[] Array){
        boolean judge = false;
        int len1 = 0, len2 = 0;
        
        for(int i = 0;i < Array.length;i++){
            if(Array[i] >= 0)
                len1++;
        }
        for(int j = 0;j < Array.length;j++){
            if(Array[j] <= 0)
                len2++;
        }
        
        if(len1 == Array.length || len2 == Array.length)
            judge = true;
        return judge;
    }
    
    private Point getFirst(Point[] points) {
    	double minx=Double.MAX_VALUE;
    	int count=0;
    	Point candidate=null;
    	for(int i=0;i<points.length;i++) {
    		if (SimUtils.doubleEqual(points[i].x, minx)) {
    			candidate=points[i];
    			count++;
    		}else if(points[i].x<minx) {
    			candidate=points[i];
    			minx=points[i].x;
    			count=0;
    		}	
    	}
    	if(count>1) {
    		double miny=Double.MAX_VALUE;
        	count=0;
    		for(int i=0;i<points.length;i++) {
        		if (SimUtils.doubleEqual(points[i].y, miny)) {
        			candidate=points[i];
        			count++;
        		}else if(points[i].y<miny) {
        			candidate=points[i];
        			miny=points[i].y;
        			count=0;
        		}
    		}
    	}
    	return candidate;
    }
    
    public static void main(String[] args){
        Point[] A = new Point[8];
        A[0] = new Point(1,0);
        A[1] = new Point(0,1);
        A[2] = new Point(0,-1);
        A[3] = new Point(-1,0);
        A[4] = new Point(2,0);
        A[5] = new Point(0,2);
        A[6] = new Point(0,-2);
        A[7] = new Point(-2,0);
        
        Point[] result = getConvexHull(A);
        System.out.println("����A������͹���ĵ㼯Ϊ��");
        for(int i = 0;i < result.length;i++)
            System.out.println("("+result[i].x+","+result[i].y+")");
        
        System.out.println("����A������͹���ĵ㼯Ϊ��");
        ConvexHull<Point> jm = new ConvexHull<Point>(A);
        for(Point point:jm.getHull()) {
        	System.out.println(point);
        }
    }
}

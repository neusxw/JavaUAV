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
        int len = 0;  //用于计算最终返回结果中是凸包中点的个数
        Point[] result = new Point[points.length];
        for(int i = 0;i < points.length;i++){
            for(int j = 0;j < points.length;j++){
                if(j == i)     //除去选中作为确定直线的第一个点
                    continue;
                
                int[] judge = new int[points.length];   //存放点到直线距离所使用判断公式的结果
                
                for(int k = 0;k < points.length;k++){
                    int a = (int) (points[j].y - points[i].y);
                    int b = (int) (points[i].x - points[j].x);
                    int c = (int) ((points[i].x)*(points[j].y) - (points[i].y)*(points[j].x));

                    judge[k] = (int) (a*(points[k].x) + b*(points[k].y) - c);  //根据公式计算具体判断结果
                }
                
                if(JudgeArray(judge)){  // 如果点均在直线的一边,则相应的A[i]是凸包中的点
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
    
    //判断数组中元素是否全部大于等于0或者小于等于0，如果是则返回true，否则返回false
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
        System.out.println("集合A中满足凸包的点集为：");
        for(int i = 0;i < result.length;i++)
            System.out.println("("+result[i].x+","+result[i].y+")");
        
        System.out.println("集合A中满足凸包的点集为：");
        ConvexHull<Point> jm = new ConvexHull<Point>(A);
        for(Point point:jm.getHull()) {
        	System.out.println(point);
        }
    }
}

package main.arithmetic;

/**
 * Node 来表示图中的点的信息，其中value表示的是其到原点的距离，vex代表的是哪一个点。
 * 在我们建立小顶堆的时候，value的值与vex一同交换，因此value与vex总是成对出现的。
 * 首先我们用数组来保存有向图的信息，m代表着无穷大，自己与自己的距离为0。
 * 在松弛的时候，每当一个点加入到S集合（s集合代表着已经加入的到了最短路径点的集合，初始的
 * 时候只有原点一个点。松弛的时候，以新加入的点开始，到所有的点都进行松弛。当对自己松弛时，
 * 值是不会改变的，所以这也是没有问题的。
 * 每当有一个点加入S集合，d的长度就会减一，这样可以判断是否是所有的节点都已经加入到了S集合。
 * 我们用p集合来保存每个点的前驱节点，最后我们有求最短路径的时候，就可以从后向前，把前驱节点
 * 加入到一个栈（自己实现的)，然后在取出来打印。
 * @author Administrator
 *
 */
public class Dijkstra {
	private static final int m = 100;
	public static void main(String[] args) throws Exception {
		//图的存储,用数组来存储
		int[][] t = new int[][]{
				{0, 10, 3, m, m},
				{m, 0, 1, 2, m},
				{m, 4, 0, 8, 2},
				{m, m, m, 0, 7},
				{m, m, m, 9, 0}
		};
		//初始化
		Node[] d = new Node[]{new Node(), new Node(), new Node(), new Node(), new Node()};
		int[] p = new int[t.length];
		int[] s = new int[1];
		s[0] = d.length;
		d[0].setValue(0);
		d[0].setVex(0);
		
		for (int i = 1; i < d.length; i++) {
			d[i].setValue(m);
			d[i].setVex(i);
			p[i] = -1;
		}
		
		//建立一个小顶堆
		buildMinHeap(d, s);
		
		
		//dijkstra
		dijkstra(t, d, p, s);
		
		
		int y = 3;//3是我们想到的点
		print(p, y);
		
	}
	
	private static void print(int[] p, int y) throws Exception {
		Stack ss = new Stack();
		int k = y;
		ss.push(k);
		do {
			int u = p[k];
			ss.push(u);
			k = u;
		} while(k != 0);
		
		while(!ss.isEmpty()) {
			System.out.print("--->" + ss.pop());
		}
	}
	
	
	private static void dijkstra(int[][] t,Node[] d, int[] p, int[] s){
		while(s[0] != 0) {
			Node q = extract_min(d, s);
			//松弛
			int i = q.getVex();
			int value = q.getValue();
			for (int j = 0; j < s[0]; j ++) {
				int n = d[j].getVex();
				int m = d[j].getValue();
				if (m > value + t[i][n]) {
					d[j].setValue(value + t[i][n]);
					p[n] = i;
				}
			}
		}
	}
	
	private static Node extract_min(Node[] d, int[] s) {
		minHeafiy(d, 1, s[0]);
		//建立d的最小优先队列
		Node n = new Node();
		n.setValue(d[0].getValue());
		n.setVex(d[0].getVex());
		int i = --s[0];
		int temp = d[0].getValue();
		d[0].setValue(d[i].getValue());
		d[i].setValue(temp);
		int temp1 = d[0].getVex();
		d[0].setVex(d[i].getVex());
		d[i].setVex(temp1);
		
		return n;
	}
 
	private static void buildMinHeap(Node[] d, int[] s) {
		int l = s[0];
		int i = l/2;
		for (int j = i; j > 0; j--) {
			minHeafiy(d, j, l);
		}
	}
	
	private static void minHeafiy(Node[] d, int i, int j) {
		int l = leftChild(i);
		int r = rightChild(i);
		int min= i;
		if (l <= j && d[min - 1].getValue() > d[l - 1].getValue()) {
			min = l;
		}
		if (r <= j && d[min - 1].getValue() > d[r - 1].getValue()) {
			min = r;
		} 
		if (min != i) {
			int temp = d[min - 1].getValue();
			d[min - 1].setValue(d[i - 1].getValue());
			d[i - 1].setValue(temp);
			
			int temp1 = d[min - 1].getVex();
			d[min - 1].setVex(d[i - 1].getVex());
			d[i - 1].setVex(temp1);
			minHeafiy(d, min, j);
		}
			
	}
 
	//下面三个方法分别求出父节点，左孩子，右孩子
	public int parent(int i) {
		return i/2;
	}
	public static int leftChild(int i) {
		return 2*i;
	}
	public static int rightChild(int i) {
		return 2*i + 1;
	}
}
 
 
class Node {
	private int value;
	private int vex;
	public Node() {
	}
	public Node(int value, int vex) {
		this.value = value;
		this.vex  = vex;
	}
 
	public int getValue() {
		return value;
	}
 
	public void setValue(int value) {
		this.value = value;
	}
 
	public int getVex() {
		return vex;
	}
 
	public void setVex(int vex) {
		this.vex = vex;
	}
}
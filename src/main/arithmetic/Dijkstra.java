package main.arithmetic;

/**
 * Node ����ʾͼ�еĵ����Ϣ������value��ʾ�����䵽ԭ��ľ��룬vex���������һ���㡣
 * �����ǽ���С���ѵ�ʱ��value��ֵ��vexһͬ���������value��vex���ǳɶԳ��ֵġ�
 * ������������������������ͼ����Ϣ��m������������Լ����Լ��ľ���Ϊ0��
 * ���ɳڵ�ʱ��ÿ��һ������뵽S���ϣ�s���ϴ������Ѿ�����ĵ������·����ļ��ϣ���ʼ��
 * ʱ��ֻ��ԭ��һ���㡣�ɳڵ�ʱ�����¼���ĵ㿪ʼ�������еĵ㶼�����ɳڡ������Լ��ɳ�ʱ��
 * ֵ�ǲ���ı�ģ�������Ҳ��û������ġ�
 * ÿ����һ�������S���ϣ�d�ĳ��Ⱦͻ��һ�����������ж��Ƿ������еĽڵ㶼�Ѿ����뵽��S���ϡ�
 * ������p����������ÿ�����ǰ���ڵ㣬��������������·����ʱ�򣬾Ϳ��ԴӺ���ǰ����ǰ���ڵ�
 * ���뵽һ��ջ���Լ�ʵ�ֵ�)��Ȼ����ȡ������ӡ��
 * @author Administrator
 *
 */
public class Dijkstra {
	private static final int m = 100;
	public static void main(String[] args) throws Exception {
		//ͼ�Ĵ洢,���������洢
		int[][] t = new int[][]{
				{0, 10, 3, m, m},
				{m, 0, 1, 2, m},
				{m, 4, 0, 8, 2},
				{m, m, m, 0, 7},
				{m, m, m, 9, 0}
		};
		//��ʼ��
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
		
		//����һ��С����
		buildMinHeap(d, s);
		
		
		//dijkstra
		dijkstra(t, d, p, s);
		
		
		int y = 3;//3�������뵽�ĵ�
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
			//�ɳ�
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
		//����d����С���ȶ���
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
 
	//�������������ֱ�������ڵ㣬���ӣ��Һ���
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
package main.arithmetic;

import java.util.ArrayList;
import java.util.List;

public class ShortestPathDijkstra {
    /** �ڽӾ��� */
    private int[][] matrix;
    /** ��ʾ������ */
    private int MAX_WEIGHT = Integer.MAX_VALUE;
    /** ���㼯�� */
    private String[] vertexes;
 
    /**
     * ����ͼ2
     */
    private void createGraph2(int index) {
        matrix = new int[index][index];
        vertexes = new String[index];
        
        int[] v0 = { 0, 1, 5, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT };
        int[] v1 = { 1, 0, 3, 7, 5, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT };
        int[] v2 = { 5, 3, 0, MAX_WEIGHT, 1, 7, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT };
        int[] v3 = { MAX_WEIGHT, 7, MAX_WEIGHT, 0, 2, MAX_WEIGHT, 3, MAX_WEIGHT, MAX_WEIGHT };
        int[] v4 = { MAX_WEIGHT, 5, 1, 2, 0, 3, 6, 9, MAX_WEIGHT };
        int[] v5 = { MAX_WEIGHT, MAX_WEIGHT, 7, MAX_WEIGHT, 3, 0, MAX_WEIGHT, 5, MAX_WEIGHT };
        int[] v6 = { MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 3, 6, MAX_WEIGHT, 0, 2, 7 };
        int[] v7 = { MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 9, 5, 2, 0, 4 };
        int[] v8 = { MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 7, 4, 0 };
        matrix[0] = v0;
        matrix[1] = v1;
        matrix[2] = v2;
        matrix[3] = v3;
        matrix[4] = v4;
        matrix[5] = v5;
        matrix[6] = v6;
        matrix[7] = v7;
        matrix[8] = v8;
        
        vertexes[0] = "v0";
        vertexes[1] = "v1";
        vertexes[2] = "v2";
        vertexes[3] = "v3";
        vertexes[4] = "v4";
        vertexes[5] = "v5";
        vertexes[6] = "v6";
        vertexes[7] = "v7";
        vertexes[8] = "v8";
    }
    
    /**
     * ����ͼ1
     */
    private void createGraph1(int index) {
        matrix = new int[index][index];
        vertexes = new String[index];
 
        int[] v0 = { 0, 1, MAX_WEIGHT, MAX_WEIGHT, 2, MAX_WEIGHT };
        int[] v1 = { 1, 0, 1, MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT };
        int[] v2 = { MAX_WEIGHT, 1, 0, 1, MAX_WEIGHT, MAX_WEIGHT };
        int[] v3 = { MAX_WEIGHT, MAX_WEIGHT, 1, 0, 1, MAX_WEIGHT };
        int[] v4 = { MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 1, 0, 1 };
        int[] v5 = { MAX_WEIGHT, MAX_WEIGHT, MAX_WEIGHT, 1, 1, 0 };
 
        matrix[0] = v0;
        matrix[1] = v1;
        matrix[2] = v2;
        matrix[3] = v3;
        matrix[4] = v4;
        matrix[5] = v5;
 
        vertexes[0] = "A";
        vertexes[1] = "B";
        vertexes[2] = "C";
        vertexes[3] = "D";
        vertexes[4] = "E";
        vertexes[5] = "F";
    }
 
    /**
     * Dijkstra���·����
     * 
     * vs -- ��ʼ����(start vertex) ����ͳ��ͼ��"����vs"������������������·����
     */
    public void dijkstra(int vs) {
        // flag[i]=true��ʾ"����vs"��"����i"�����·���ѳɹ���ȡ
        boolean[] flag = new boolean[vertexes.length];
        // U���Ǽ�¼��δ������·���Ķ���(�Լ��ö��㵽���s�ľ���)���� flag���ʹ��,flag[i] == true ��ʾU��i�����ѱ��Ƴ�
        int[] U = new int[vertexes.length];
        // ǰ����������,����prev[i]��ֵ��"����vs"��"����i"�����·����������ȫ�������У�λ��"����i"֮ǰ���Ǹ����㡣
        int[] prev = new int[vertexes.length];
        // S�������Ǽ�¼��������·���Ķ���
        String[] S = new String[vertexes.length];
 
        // ����һ����ʼʱ��S��ֻ�����vs��U���ǳ�vs֮��Ķ��㣬����U�ж����·����"���vs���ö����·��"��
        for (int i = 0; i < vertexes.length; i++) {
            flag[i] = false; // ����i�����·����û��ȡ����
            U[i] = matrix[vs][i]; // ����i�붥��vs�ĳ�ʼ����Ϊ"����vs"��"����i"��Ȩ��Ҳ�����ڽӾ���vs�е����ݡ�
            
            prev[i] = 0; //����i��ǰ������Ϊ0
        }
 
        // ��vs��U�С��Ƴ�����U��flag���ʹ�ã�
        flag[vs] = true;
        U[vs] = 0;
        // ��vs�������S
        S[0] = vertexes[vs];
        // ����һ����
        
        //�����ģ��ظ����������ֱ�����������ж��㡣
        // ����vertexes.length-1�Σ�ÿ���ҳ�һ����������·����
        int k = 0;
        for (int i = 1; i < vertexes.length; i++) {
            // ���������U���ҳ�·����̵Ķ��㣬��������뵽S�У����vs���㵽x���㻹�и��̵�·���Ļ�����ô
            // ��Ȼ����һ��y���㵽vs�����·����ǰ�߸�����û�м���S��
            // ���ԣ�U��·����̶����·�����Ǹö�������·����
            // ������δ��ȡ���·���Ķ����У��ҵ���vs����Ķ���(k)��
            int min = MAX_WEIGHT;
            for (int j = 0; j < vertexes.length; j++) {
                if (flag[j] == false && U[j] < min) {
                    min = U[j];
                    k = j;
                }
            }
            
            //��k����S��
            S[i] = vertexes[k];
            
            //���������
            
            
            //������������U�еĶ���Ͷ����Ӧ��·��
            //���"����k"Ϊ�Ѿ���ȡ�����·��������U�еĶ��㣬����k�����Ӧ��flag���Ϊtrue��
            flag[k] = true;
            
            //������ǰ���·����ǰ�����㣨����U��ʣ�ඥ���Ӧ��·����
            //�������Ѿ�"����k�����·��"֮�󣬸���"δ��ȡ���·���Ķ�������·����ǰ������"��
            for (int j = 0; j < vertexes.length; j++) {
                //��k��������λ�������������㣬�ж��������㾭�����·������k����vs�����Ƿ�С��Ŀǰ�����·�����ǣ�������U�����ǣ���������
                int tmp = (matrix[k][j] == MAX_WEIGHT ? MAX_WEIGHT : (min + matrix[k][j]));
                if (flag[j] == false && (tmp < U[j])) {
                    U[j] = tmp;
                    //���� j��������·��ǰ������Ϊk
                    prev[j] = k;
                }
            }
            //����������
        }
        //�����Ľ���
 
        // ��ӡdijkstra���·���Ľ��
        System.out.println("��ʼ���㣺" + vertexes[vs]);
        for (int i = 0; i < vertexes.length; i++) {
            System.out.print("���·����" + vertexes[vs] + "," + vertexes[i] + "):" + U[i] + "  ");
            
            List<String> path = new ArrayList<>();
            int j = i;
            while (true) {
                path.add(vertexes[j]);
                
                if (j == 0)
                    break;
                
                j = prev[j];
            }
            
            for (int x = path.size()-1; x >= 0; x--) {
                if (x == 0) {
                    System.out.println(path.get(x));
                } else {
                    System.out.print(path.get(x) + "->");
                }
            }
            
        }
        
        System.out.println("�������S�е�˳��");
        for (int i = 0; i< vertexes.length; i++) {
            
            System.out.print(S[i]);
            
            if (i != vertexes.length-1) 
                System.out.print("-->");
        }
            
    }
 
    public static void main(String[] args) {
        ShortestPathDijkstra dij = new ShortestPathDijkstra();
        dij.createGraph1(6);
//        dij.createGraph2(9);
        dij.dijkstra(0);
    }
 
}

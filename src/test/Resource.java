package test;

import java.io.*;  
 
public class Resource {  
    public  void getResource() throws IOException{    
        
        //���ض�ȡָ����Դ��������  
        InputStream is=this.getClass().getResourceAsStream("/rs/map.txt");   
        BufferedReader br=new BufferedReader(new InputStreamReader(is));  
        String s="";  
        while((s=br.readLine())!=null)  
            System.out.println(s);  
    
    }  
    
    public static void main(String[] args) throws IOException {  
        Resource res=new Resource();  
        res.getResource();  
    }  
}    

package test;

import java.io.*;
import java.net.URL;  
 
public class Resource {  
    public  void getResource() throws IOException{    
    	 URL fileURL=this.getClass().getResource("/rs/map.txt");   
         System.out.println(fileURL.getFile());  
         
    }  
    
    public static void main(String[] args) throws IOException {  
        Resource res=new Resource();  
        res.getResource();  
    }  
}    

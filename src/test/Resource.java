package test;

import java.io.*;
import java.net.URL;

import main.arithmetic.data.DataExport;  
 
public class Resource {  
    public  void getResource() throws IOException{    
    	 //URL fileURL=this.getClass().getResource("/rs/map.txt");
    	 String userPath = System.getProperty("user.dir");
    	 DataExport.changeOutPosition();
         //System.out.println(fileURL.getFile());  
         System.out.println(userPath + "\\map.xml"); 
         System.out.println(new File(userPath + "\\map.xml")); 
    }  
    
    public static void main(String[] args) throws IOException {  
        Resource res=new Resource();  
        res.getResource();  
    }  
}    

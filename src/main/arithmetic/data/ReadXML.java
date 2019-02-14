package main.arithmetic.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ReadXML {
	private List<MapInfo> mapInfoList = new ArrayList<MapInfo>();

	public List<MapInfo> getMapInfo(File file){
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(file);
            Element map = document.getRootElement();
            //根节点迭代器
            Iterator mapit = map.elementIterator();
			while(mapit.hasNext()){
				Element mapElement = (Element) mapit.next();//当前节点
				//遍历mapElement的属性
				List<Attribute> attributes = mapElement.attributes();
				for(Attribute attribute : attributes){
					if(attribute.getName().equals("type")){
						String type = attribute.getValue().toLowerCase();
						switch (type){
						case "alpha":{
							Iterator geometryit = mapElement.elementIterator();
							List<double[]> data = new ArrayList<double[]>();
							while(geometryit.hasNext()){
								Element child = (Element) geometryit.next();
								String nodeName = child.getName();
								if(nodeName.equals("num")){
									//System.out.println(child.getStringValue());
									String num = child.getStringValue();
									SimUtils.kmeansAlpha=Double.parseDouble(num);
								}
							}
							break;
						}
						case "uav":{
							Iterator geometryit = mapElement.elementIterator();
							List<double[]> data = new ArrayList<double[]>();
							while(geometryit.hasNext()){
								Element child = (Element) geometryit.next();
								String nodeName = child.getName();
								if(nodeName.equals("num")){
									//System.out.println(child.getStringValue());
									String num = child.getStringValue();
									SimUtils.numUAV=Integer.parseInt(num);
								}
							}
							break;
						}
						case "origin":{
							Iterator geometryit = mapElement.elementIterator();
							List<double[]> data = new ArrayList<double[]>();
							while(geometryit.hasNext()){
								Element child = (Element) geometryit.next();
								String nodeName = child.getName();
								if(nodeName.equals("coords")){
									//System.out.println(child.getStringValue());
									String coords = child.getStringValue();
									data = getDataFromString(coords);
								}
							}
							mapInfoList.add(new MapInfo("origin",data));
							break;
						}
						case "land":{
							Iterator geometryit = mapElement.elementIterator();
							List<double[]> data = new ArrayList<double[]>();
							double ridgeWideth = 0;
							List<double[]> ridgeDirection = null;
							while(geometryit.hasNext()){
								Element child = (Element) geometryit.next();
								String nodeName = child.getName();
								String value = child.getStringValue();
								if(nodeName.equals("coords")){
									//System.out.println(child.getStringValue());
									data = getDataFromString(value);
								}else if(nodeName.equals("ridgeWideth")) {
									ridgeWideth=Double.parseDouble(value);
								}else if(nodeName.equals("ridgeDirection")) {
									ridgeDirection=getDataFromString(value);
								}
							}
							mapInfoList.add(new LandInfo("land",data,ridgeWideth,ridgeDirection));
							break;
						}
						case "obstacle":{
							Iterator geometryit = mapElement.elementIterator();
							List<double[]> data = new ArrayList<double[]>();
							while(geometryit.hasNext()){
								Element child = (Element) geometryit.next();
								String nodeName = child.getName();
								String value = child.getStringValue();
								if(nodeName.equals("coords")){
									//System.out.println(child.getStringValue());
									data = getDataFromString(value);
								}
							}
							mapInfoList.add(new MapInfo("obstacle",data));
							break;
						}				
						case "station":{
							Iterator geometryit = mapElement.elementIterator();
							List<double[]> data = new ArrayList<double[]>();
							while(geometryit.hasNext()){
								Element child = (Element) geometryit.next();
								String nodeName = child.getName();
								String value = child.getStringValue();
								if(nodeName.equals("coords")){
									//System.out.println(child.getStringValue());
									data = getDataFromString(value);
								}
							}
							mapInfoList.add(new MapInfo("station",data));
							break;
						}
						}
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return mapInfoList;
    }

    public List<double[]> getDataFromString(String coords) {
    	List<double[]> data = new ArrayList<double[]>();
    	String[] coordsString = coords.split(";");
    	//System.out.println(coordsString.length);
    	String[] firstOne = coordsString[0].split(" ");
    	//for(String str:firstOne) {System.out.println(str);}
    	if(firstOne.length<2) {
    		data.add(new double[] {Double.parseDouble(firstOne[0])});
    		return data;
    	}
    	for(String str:coordsString) {
    		String[] coordString =str.split(" ");
    		double x = Double.parseDouble(coordString[0]);
    		double y = Double.parseDouble(coordString[1]);
    		data.add(new double[] {x,y});
    	}
    	return data;
    }
}
package main.arithmetic;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ReadXMLByDom4j {
	private List<MapInfo> mapInfoList = null;
    private MapInfo info = null;

    public List<MapInfo> getMapInfo(File file){
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(file);
            Element map = document.getRootElement();
            Iterator mapit = map.elementIterator();
			
            mapInfoList = new ArrayList<MapInfo>();
			while(mapit.hasNext()){
				Element mapElement = (Element) mapit.next();
				//±È¿˙bookElementµƒ Ù–‘
				List<Attribute> attributes = mapElement.attributes();
				for(Attribute attribute : attributes){
					if(attribute.getName().equals("type")){
						String type = attribute.getValue();//System.out.println(id);
						Iterator geometryit = mapElement.elementIterator();
						switch (type){
						case "origin":{
							while(geometryit.hasNext()){
								Element child = (Element) geometryit.next();
								String nodeName = child.getName();
								if(nodeName.equals("coords")){
									//System.out.println(child.getStringValue());
									String coords = child.getStringValue();
									book.setName(name);
								}else if(nodeName.equals("author")){
									String author = child.getStringValue();
									book.setAuthor(author);
								}else if(nodeName.equals("year")){
									String year = child.getStringValue();
									book.setYear(Integer.parseInt(year));
								}else if(nodeName.equals("price")){
									String price = child.getStringValue();
									book.setPrice(Double.parseDouble(price));
								}
							}
						}
						}
					}
				}
				
				Iterator bookit = mapElement.elementIterator();
				
				bookList.add(book);
				book = null;
				
			}
		} catch (DocumentException e) {
		
			e.printStackTrace();
		}
		
		
		return bookList;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        File file = new File("src/res/books.xml");
        List<Book> bookList = new ReadXMLByDom4j().getBooks(file);
        for(Book book : bookList){
            System.out.println(book);
        }
    }
}
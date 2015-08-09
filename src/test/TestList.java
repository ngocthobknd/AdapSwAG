package test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.emf.common.util.EList;

public class TestList {
	public static void main(String arg[]) {
		List<String> myList = new CopyOnWriteArrayList<String>();
        
//        myList.add("1");
//        myList.add("2");
//        myList.add("3");
//        myList.add("4");
//        myList.add("5");
//        ArrayList<String> b = new ArrayList<String>();
//        b.add("6");
//        b.add("7");
//        
		for (int i = 0; i < myList.size(); i++) {
			
			System.out.println("123"+myList.get(i));
		}
//        myList.addAll(b);
//        Iterator<String> it = myList.iterator();
//        while(it.hasNext()){
//            String value = it.next();
//            System.out.println("List Value:"+value);
//            if(value.equals("3")){
//                myList.remove("4");
//                myList.add("6");
//                myList.add("7");
//            }
//        }
      //  System.out.println("List Size:"+myList.size());
//         
//        Map<String,String> myMap = new ConcurrentHashMap<String,String>();
//        myMap.put("1", "1");
//        myMap.put("2", "2");
//        myMap.put("3", "3");
//         
//        Iterator<String> it1 = myMap.keySet().iterator();
//        while(it1.hasNext()){
//            String key = it1.next();
//            System.out.println("Map Value:"+myMap.get(key));
//            if(key.equals("1")){
//                myMap.remove("3");
//                myMap.put("4", "4");
//                myMap.put("5", "5");
//            }
//        }
//        System.out.println("Map Size:"+myMap.size());
		  
       
	}
}

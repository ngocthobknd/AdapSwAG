package test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.emf.common.util.EList;

public class TestList {
	private ArrayList<String > a = new ArrayList<String>();
	public TestList(ArrayList<String > a) {
		this.a = a;
	}
	public void rem(ArrayList<String> a){
		ArrayList<String> b = new ArrayList<String>(a);
		for (int i = 0; i < b.size(); i++) {
			if (b.get(i).equals("3")) b.remove(i);
		}
	}
	public static void main(String arg[]) {
		ArrayList<String > a = new ArrayList<String>();
		a.add("1");
		a.add("2");
		a.add("3");
		TestList b = new TestList(a);
		b.rem(b.a);
		for (int i = 0; i < b.a.size(); i++) {
			System.out.println(b.a.get(i));
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

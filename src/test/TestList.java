package test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;

public class TestList {
	public static void main(String arg[]) {
		ArrayList a = new ArrayList<String>();
		a.add("abc");
		a.add("abc");
		a.add("abc");
		a.add("s");
		for (Iterator<String> iter = a.listIterator(); iter.hasNext(); ) {
		    String s = iter.next();
		    if (s.equals("abc")) {
		        iter.remove();
		    }
		}
		
		for (int i = 0; i < a.size(); i++) {
			System.out.println(a.get(i));
			
		}
		
	}
}

package test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

public class TestList {
	public static void main(String arg[]) {
		List a = new ArrayList<String>();
		a.add("abc");
		a.add("abc");
		a.add("abc");
		a.add("abc");
		
		for (int i = 0; i < a.size(); i++) {
			System.out.println(a.get(i)+":"+a.size());
		}
		
	}
}

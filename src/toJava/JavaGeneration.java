package toJava;

import toJava.acme2Java.ACME2Java;
import toJava.fractal2Java.Fractal2Java;

public class JavaGeneration {

	public JavaGeneration(String file) {
		// TODO Auto-generated constructor stub
		String fileName = file.replaceAll("/", "//");
		String ext = fileName.substring(fileName.lastIndexOf(".")+1);
		if (ext.equals("fractal")) {
			new Fractal2Java(fileName);
		}
		else if (ext.equals("acme")) {
			new ACME2Java(fileName);
		}
	}

}

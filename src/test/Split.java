package test;

public class Split {
 public static void main (String arg []){
	 String string = "0040.034556";
	 String[] parts = string.split("\\.");
	 String part1 = parts[0]; // 004
	 String part2 = parts[1]; // 034556
	 System.out.println(part2);
 }
}

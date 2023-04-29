package cse114;

public class Test {
	 public static void main(String[] args) {
		 try { method1();
		 System.out.println("A");
		 } catch(Exception e) { System.out.println("Method1");
		 }
			 
		 }
	 public static void method1() {
		 try { double a = 5.6;
		 Integer.parseInt("5.5");
		 System.out.println("B");
		 } catch (Exception e) {
			 System.out.println("Method2");
		 }
		 System.out.println();
	 }
}

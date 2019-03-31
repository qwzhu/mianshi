package mianshi;

public class Test {
	public static void main(String[] args) {
		String a = new String ("1");
		String b = "1";
		System.out.println(a ==b);
		String c = a.intern();
		System.out.println(c==a);
	}
}

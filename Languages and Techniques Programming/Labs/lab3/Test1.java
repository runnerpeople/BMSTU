import java.util.Arrays;
import java.lang.*;

class LowestCommonMultiple implements Comparable<LowestCommonMultiple> {
	private int a,b;
	
	public static int gcd(int a, int b) {
		while(b > 0) {
			int c = a % b;
			a = b;
			b = c;
		}
		return a;
	}
	
	public static int lcm(int a, int b) {
		if ((a ==  0) || (b == 0)) return 0;	
		else return Math.abs(a*b)/gcd(a,b);
	}
 
	public LowestCommonMultiple(int a, int b) { 
		this.a=a;
		this.b=b; 
	}
	
	public String toString() { 
		return a + " " + b; 
	}
	
	public int compareTo(LowestCommonMultiple obj) {
		if (lcm(this.a,this.b) == 0 && lcm(obj.a,obj.b) == 0) return 0;
		else if (lcm(this.a,this.b) == 0) return -1;
		else if (lcm(obj.a,obj.b)==0) return 1;
		else if (lcm(this.a,this.b)>lcm(obj.a,obj.b)) return 1;
		else return -1;
	}
}

public class Test1 {
	public static void main(String[] args) {
		LowestCommonMultiple[] a  = new LowestCommonMultiple[] { new LowestCommonMultiple(14,2),new LowestCommonMultiple(19,11), new LowestCommonMultiple(5,6), new LowestCommonMultiple(6,2), new LowestCommonMultiple(5,2) };
	Arrays.sort(a);
	for(LowestCommonMultiple s : a)
		System.out.println(s);
	}
}

public class Test2 {
	public class Line {
        	private int a,b,c; //a*x+b*y+c=0

        	public Line(int a,int b,int c) {
			this.a=a;
			this.b=b;
			this.c=c;
        	}
	
		public static int gcd(int a,int b){
			return b == 0 ? a : gcd(b,a % b);		
		}
	
		public static int lcm(int a,int b){
			return a / gcd(a,b) * b;
		}

        	public static Line intersection(Line l1, Line l2,Line l3) {
            		int z=Line.lcm(l1.a,l2.a);
            		int k = z/l1.a, y=z/l2.a;
            		l1.a*=k;
        	 	l1.b*=k;
            		l1.c*=k;
            		l2.b*=y;
            		l2.c*=y;
            		l2.b=l2.b-l1.b;
            		l2.c=l2.c-l1.c;
            		l3.b=l2.c/l2.b;
            		l3.a=(l1.c/k)-(l3.b*l1.b)/k;
            		return l3;
        	}

        	public String toString() {
            		String s = "Точка пересечения: " + "x = " + a + " " + "y = " + b;
	    		return s;
    		}
	}
	public static void main(String[] args) {
        	Line l1 = new Line(1,-9,-14);
        	Line l2 = new Line(5,-2,16);
        	Line l3 = new Line(0,0,0);
		l3 = Line.intersection(l1,l2,l3);
        	System.out.println(l3);
    	}
}

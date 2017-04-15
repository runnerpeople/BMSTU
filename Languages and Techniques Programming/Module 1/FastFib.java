import java.util.Scanner;
import java.math.BigInteger;

public class FastFib {

        public static int power2 (int n) {
                int s=1;
                if (n==0) return 1;
                for (;n>0;n--)
                        s*=2;
                return s;
        }

        static void multiply(BigInteger x[][], BigInteger y[][]) {
                BigInteger a,b,c,d,e,f,g,h;
                a=x[0][0];
                b=y[0][0];
                a=a.multiply(b);
                c=x[0][1];
                d=y[1][0];
                c=c.multiply(d);
                a=a.add(c);
                e=a;
                a=x[0][0];
                b=y[0][1];
                a=a.multiply(b);
                c=x[0][1];
                d=y[1][1];
                c=c.multiply(d);
                a=a.add(c);
                f=a;
                a=x[1][0];
                b=y[0][0];
                a=a.multiply(b);
                c=x[1][1];
                d=y[1][0];
                c=c.multiply(d);
                a=a.add(c);
                g=a;
                a=x[1][0];
                b=y[0][1];
                a=a.multiply(b);
                c=x[1][1];
                d=y[1][1];
                c=c.multiply(d);
                a=a.add(c);
                h=a;
                x[0][0]=e;
                x[0][1]=f;
                x[1][0]=g;
                x[1][1]=h;
        }

        public static void squareMatrix(BigInteger x[][],int n) {
                BigInteger a, f, c, d, e, g, h, r;
                        while (n > 0) {
                                a = x[0][0];
                                a = a.multiply(a);
                                c = x[0][1];
                                d = x[1][0];
                                c = c.multiply(d);
                                a = a.add(c);
                                e = a;
                                a = x[0][0];
                                r = x[0][1];
                                a = a.multiply(r);
                                c = x[0][1];
                                d = x[1][1];
                                c = c.multiply(d);
                                a = a.add(c);
                                f = a;
                                a = x[1][0];
                                r = x[0][0];
                                a = a.multiply(r);
                                c = x[1][1];
                                d = x[1][0];
                                c = c.multiply(d);
                                a = a.add(c);
                                g = a;
                                a = x[1][0];
                                r = x[0][1];
                                a = a.multiply(r);
                                c = x[1][1];
                                c = c.multiply(c);
                                a = a.add(c);
                                h = a;
                                x[0][0] = e;
                                x[0][1] = f;
                                x[1][0] = g;
                                x[1][1] = h;
                                n--;
                        }
        }

        public static BigInteger fibonacci(int n) {
                int i = n - 2,count=0,q;
                BigInteger b[][] = {{BigInteger.ONE, BigInteger.ONE}, {BigInteger.ONE, BigInteger.ZERO}};
                BigInteger w[][] = {{BigInteger.ONE, BigInteger.ONE}, {BigInteger.ONE, BigInteger.ZERO}};
        	BigInteger t[][] = {{BigInteger.ONE, BigInteger.ONE}, {BigInteger.ONE, BigInteger.ZERO}};
                BigInteger a, f, c, d, e, g, h, r;
                if (i>0)
                        i++;
                if (i / 2 > 0) {
                        q = i;
                        while (q / 2 > 0) {
                                count++;
                                q /= 2;
                        }
                        i = i - power2(count);
                }
                squareMatrix(b,count);
                if (i - power2(count-1)>0) {
                        squareMatrix(w,count-1);
                        i = i - power2(count-1);
                        multiply(b,w);
                }
		if (i - power2(count-2)>0) {
                        squareMatrix(t,count-2);
                        i = i - power2(count-2);
                        multiply(b,t);
                }
                for (; i > 0; i--) {
                        a = b[0][0];
                        c = b[0][1];
                        f = b[1][0];
                        d = b[1][1];
                        b[0][1] = b[0][0];
                        b[0][0] = a.add(c);
                        b[1][1] = b[1][0];
                        b[1][0] = f.add(d);
                }
                return b[0][0];
        }

        public static void main(String[] args) {
                Scanner in = new Scanner(System.in);
                int k = in.nextInt();
                System.out.println(fibonacci(k));
        }
}

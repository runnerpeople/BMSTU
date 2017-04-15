import java.util.Scanner;

public class Kth {

        static long power10(long i) {
                long j,u=10;
                if (i == 0) return 1;
                for (j=2;j<=i;j++)
                        u*=10;
                return u;
        }

        public static void main(String[] args) {
                Scanner in = new Scanner(System.in);
                long k = in.nextLong();
                long i,b;
                if (k<9) {
                        System.out.println(k+1);
                        System.exit(0);
                }
                k++;
                long y;
                y=k-9;
                for(i=1;y>0;i++) {
                        k=y;
                        y=k-9*power10(i)*(i+1);
                }
                if (k/i==0)
                        b=1;
                else b=k%i+k/i;
                long t=power10(i-1)+b-1;
                for(k=i-k%i;k>0 && k%i!=0;k--) {
                        t/=10;
                }
                System.out.println(t%10);
        }
}


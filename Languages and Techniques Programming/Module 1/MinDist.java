import java.util.Scanner;
import static java.lang.Character.compare;

public class MinDist {

        static int min(int n,int x) {
                if (n<x)
                        return n;
                else return x;
        }

        public static void main(String[] args) {
                Scanner in = new Scanner(System.in);
                String s = in.nextLine();
                char x = in.next().charAt(0), y = in.next().charAt(0);
                int sx=0,sy=0,min=1000001,a=0,b=0;
                int i;
                for(i=0;i<s.length();i++) {
                        if (compare(x,s.charAt(i))==0) {
                                if (b==1)
                                        min=min(sy,min);
                                a=1;
                                sx=0;
                        }
                        if (compare(y,s.charAt(i))==0) {
                                if (a==1)
                                        min=min(sx,min);
                                b=1;
                                sy=0;
                        }
                        if (a==1 && compare(x,s.charAt(i))!=0)
                                sx++;
                        if (b==1 && compare(y,s.charAt(i))!=0)
                                sy++;
                }
                System.out.println(min);
        }
}

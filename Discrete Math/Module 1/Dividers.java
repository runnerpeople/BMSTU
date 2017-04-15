import java.util.*;

public class Dividers {
        public static void dividers(ArrayList<Long> a,long b ,long c, long d) {
                a.add(c);
                if (c != d) {
                        c++;
                        for (; c <= (long) Math.sqrt(b) && b % c != 0; c++);
                        if (c <= (long) Math.sqrt(b))
                                dividers(a, b, c, b / c);
                        a.add(d);
                }
        }

        public static void main(String[] args) {
                Scanner in=new Scanner(System.in);
                long k=in.nextLong(),j,i,z,m=0;
                System.out.println("graph {");
                ArrayList<Long> all=new ArrayList<>();
                dividers(all,k,1,k);
                for(i=all.size()-1;i>=0;i--)
                        System.out.println("\t" + all.get((int)i));
                long[][] help = new long [(int)10000][2];
                for(i=all.size()-1;i>=0;i--) {
                        for(j=i-1;j>=0;j--) {
                                if ((all.get((int)i) % all.get((int)j)) == 0) {
                                        int status=1;
                                        for(z=i-1;z>j;z--) {
                                                if (((all.get((int)i) % all.get((int)z)) == 0) && (all.get((int)z) % all.get((int)j)) == 0) {
                                                        status=0;
                                                        break;
                                                }
                                        }
                                        if(status!=0) {
                                                help[(int)m][0]=all.get((int)i);
                                                help[(int)m++][1]=all.get((int)j);
                                        }
                                }
                        }
                }
                for (i = 0; i < m; i++) {
                        System.out.printf("\t" + help[(int)i][0] + " " + "--" + " " + help[(int)i][1] + "\n");
                }
                System.out.println("}");
        }
}

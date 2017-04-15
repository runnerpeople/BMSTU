import java.util.Scanner;

public class Econom {
        public static void main(String[] args) {
                Scanner in = new Scanner(System.in);
                String str = in.nextLine();
                int res,i,j;
                String help,help2,buf;
                for(res=0; str.length()>1;res++) {
                        i=str.indexOf(')');
                        help=str.substring(i-4,i+1);
                        buf = res + "";
                        for (j=0;j<str.length()-4;j++) {
                                help2=str.substring(j,j+5);
                                if (help2.compareTo(help)==0) {
                                        str=str.substring(0,j) + buf + str.substring(j+5,str.length());
                                        j=-1;
                                }
                        }
                }
                System.out.println(res);
        }
}

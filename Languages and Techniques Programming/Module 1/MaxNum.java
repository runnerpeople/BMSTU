import java.util.Scanner;

public class MaxNum {

        public static void main(String[] args) {
                Scanner in = new Scanner(System.in);
                int n = in.nextInt();
                int[] w = new int [n];
                int i,j;
                String s="";
                for(i=0;i<n;i++) {
                        w[i]=in.nextInt();
                }
                for (i=0;i<n-1;i++) {
                        String a = w[i] + "" + w[i+1];
                        String b = w[i+1] + "" + w[i];
                        if (b.compareTo(a)>0) {
        	                int buf=w[i];
		                w[i]=w[i+1];
		                w[i+1]=buf;
                        }
		        for (j=i;j>0;j--) {
			        String c = w[j] + "" + w[j-1];
            		        String d = w[j-1] + "" + w[j];
            		        if (c.compareTo(d)>0) {
				        int buf=w[j];
				        w[j]=w[j-1];
				        w[j-1]=buf;
            		        }
        	        }
	        }
	        for(i=0;i<n;i++)
		        s=s + "" + w[i] + "";
                System.out.println(s);
        }
}
